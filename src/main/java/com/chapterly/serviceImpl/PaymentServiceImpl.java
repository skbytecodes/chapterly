package com.chapterly.serviceImpl;

import com.chapterly.constants.PaymentMethod;
import com.chapterly.constants.PaymentStatus;
import com.chapterly.dto.PaymentDto;
import com.chapterly.dto.PaymentVerification;
import com.chapterly.entity.Payment;
import com.chapterly.mapper.PaymentMapper;
import com.chapterly.repository.OrderRepo;
import com.chapterly.repository.PaymentRepo;
import com.chapterly.service.OrderService;
import com.chapterly.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.id}")
    private String id;

    @Value("${payment.secret}")
    private String secret;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PaymentMapper paymentMapper;

    Logger logger = LoggerFactory.getLogger("PaymentServiceImpl");


    @Override
    public PaymentDto savePayment(PaymentDto paymentRequest) {
        PaymentDto paymentResponse = null;
        if(paymentRequest != null){
            Payment payment = new Payment();
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setAmount(paymentRequest.getAmount());
            payment.setOrder(paymentRequest.getOrder());
            payment.setTransactionId(paymentRequest.getTransactionId());
            payment.setPaymentStatus(PaymentStatus.SUCCESS.name());

            try {
                Payment savedPayment = paymentRepo.save(payment);
                paymentResponse = paymentMapper.toDto(savedPayment);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return paymentResponse;
    }

    @Override
    public PaymentDto getPaymentDetailsByPaymentId(Long paymentId) {
        if(paymentId != null){
            PaymentDto dto = paymentMapper.toDto(paymentRepo.findById(paymentId).get());
            return dto;
        }
        return null;
    }

    @Override
    public boolean deletePaymentDetails(Long paymentId) {
        if(paymentId != null){
            Payment payment = paymentRepo.findById(paymentId).get();
            if(payment != null){
                paymentRepo.deleteById(paymentId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean refundPayment(String transactionId, BigDecimal amount) {
        return false;
    }

    @Override
    public String sendPaymentConfirmation(String transactionId) {
        return null;
    }

    @Override
    public String generatePaymentReport(String reportType, Timestamp startDate, Timestamp endDate) {
        return null;
    }

    @Override
    public void reconcilePayments(Timestamp startDate, Timestamp endDate) {

    }

    @Override
    public List<PaymentDto> getAllPayments() {
        try {
            List<Payment> allPayments = paymentRepo.findAll();
            List<PaymentDto> payments = new ArrayList<>();
            for (Payment paymentById : allPayments) {
                PaymentDto paymentDto = new PaymentDto();
                paymentDto.setPaymentId(paymentById.getPaymentId());
                paymentDto.setOrder(paymentById.getOrder());
                paymentDto.setSignature(paymentById.getSignature());
                paymentDto.setPaymentDate(paymentById.getPaymentDate());
                payments.add(paymentDto);
            }
            return payments;
        }catch (Exception e){
            logger.error("ERROR",e);
            return null;
        }
    }

    @Transactional
    @Override
    public boolean getResult(PaymentVerification paymentVerification) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(id, secret);
        String secret = this.secret;
        Order order = razorpay.orders.fetch(paymentVerification.getOrderCreationId());
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", paymentVerification.getOrderCreationId());
        options.put("razorpay_payment_id", paymentVerification.getRazorpayPaymentId());
        options.put("razorpay_signature", paymentVerification.getRazorpaySignature());

        com.razorpay.Payment payment  = razorpay.payments.fetch(paymentVerification.getRazorpayPaymentId());

        Payment userPayment = new Payment();
        userPayment.setPaymentMethod(payment.get("method"));
        try {
            System.out.println("created at "+ payment.get("created_at").toString());
            LocalDateTime createdAt = dateFormatter(payment.get("created_at").toString());
            userPayment.setPaymentDate(createdAt);
            userPayment.setCreated_at(createdAt);
        }catch (DateTimeException e){
            userPayment.setCreated_at(LocalDateTime.now());
        }
        System.out.println("razorpay order "+paymentVerification.getOrderCreationId());
        com.chapterly.entity.Order orderByRazorPayOrderId = orderService.getOrderByRazorPayOrderId(paymentVerification.getOrderCreationId());
        orderByRazorPayOrderId.setStatus(payment.get("status"));
        orderByRazorPayOrderId.setPaymentMethod(payment.get("method"));
        orderRepo.save(orderByRazorPayOrderId);
        userPayment.setOrder(orderByRazorPayOrderId);
        userPayment.setAmount(Double.parseDouble(payment.get("amount").toString()));

        String jsonCard = payment.get("card").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> card = null;
        try {
            card = objectMapper.readValue(jsonCard, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("dataaaa "+card);
        userPayment.setTransactionId(card.get("id").toString());
        if(payment.get("status").equals("captured"))
            userPayment.setCaptured(true);
        else
            userPayment.setCaptured(false);
        userPayment.setCardLastFourDigits(card.get("last4").toString());
        userPayment.setCartType(card.get("type").toString());
        userPayment.setCurrency(payment.get("currency"));
        boolean international = (boolean) card.get("international");
        System.out.println("inta "+international);
        if(card.get("international").equals("true"))
            userPayment.setInternational(true);
        else
            userPayment.setInternational(false);
        userPayment.setNetwork(card.get("network").toString());
        userPayment.setNameOnCard(card.get("name").toString());
        userPayment.setPaymentStatus(payment.get("status"));
        userPayment.setSignature(paymentVerification.getRazorpaySignature());
        userPayment.setRazorpayPaymentId(paymentVerification.getRazorpayPaymentId());
        userPayment.setRazorpayFee(payment.get("fee").toString());
        paymentRepo.save(userPayment);
        System.out.println("payment details \n" + payment);
        return Utils.verifyPaymentSignature(options, secret);
    }

    private static LocalDateTime dateFormatter(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);

        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

        System.out.println("Formatted Date is "+localDateTime);
        return localDateTime;
    }
}
