package com.chapterly.service;

import com.chapterly.dto.PaymentDto;
import com.chapterly.dto.PaymentVerification;
import com.chapterly.dto.ProductDto;
import com.chapterly.entity.Payment;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PaymentService {
    public PaymentDto savePayment(PaymentDto payment);
    public PaymentDto getPaymentDetailsByPaymentId(Long paymentId);
    public boolean deletePaymentDetails(Long paymentId);
    public boolean refundPayment(String transactionId, BigDecimal amount);
    public String sendPaymentConfirmation(String transactionId);
    public String generatePaymentReport(String reportType, Timestamp startDate, Timestamp endDate);
    public void reconcilePayments(Timestamp startDate, Timestamp endDate);

    List<PaymentDto> getAllPayments();

    boolean getResult(PaymentVerification paymentVerification) throws RazorpayException;
}
