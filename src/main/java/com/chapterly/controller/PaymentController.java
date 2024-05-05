package com.chapterly.controller;

import com.chapterly.dto.PaymentDto;
import com.chapterly.dto.PaymentVerification;
import com.chapterly.service.PaymentService;
import com.razorpay.RazorpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${payment.id}")
    private String id;

    @Value("${payment.secret}")
    private String secret;

    private Logger logger = LoggerFactory.getLogger("PaymentController");


    /***
     * saves payments in the db
     * @param paymentDto is object to be saved
     * @return response with message
     */
    @PostMapping("/addPaymentDetails")
    public ResponseEntity<?> addPaymentDetails(@RequestBody PaymentDto paymentDto) {
        System.out.println("...." + paymentDto);
        try {
            PaymentDto paymentResponse = paymentService.savePayment(paymentDto);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * returns payment by id
     * @param id is payment id {id}
     * @return a payment dto with payment info
     */
    @GetMapping("/getPaymentById/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable("id") Long id) {
        try {
            PaymentDto paymentById = paymentService.getPaymentDetailsByPaymentId(id);
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentId(paymentById.getPaymentId());

            return new ResponseEntity<>(paymentDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /***
     * returns all the payments present in db
     * @return all the payments present in db
     */
    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentDto> payments = paymentService.getAllPayments();
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR", e);
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /***
     * delete payment by id
     * @param id is payment id {id}
     * @return response with message
     */
    @DeleteMapping("/deletePaymentById/{id}")
    public ResponseEntity<?> deletePaymentById(@PathVariable("id") Long id) {
        try {
            paymentService.deletePaymentDetails(id);
            return new ResponseEntity<>("Payment Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payment/success")
    public ResponseEntity<String> getResult(@RequestBody PaymentVerification paymentVerification) throws RazorpayException, RazorpayException {
        boolean status = false;
        try {
            status = paymentService.getResult(paymentVerification);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(status)
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        return new ResponseEntity<>("FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
