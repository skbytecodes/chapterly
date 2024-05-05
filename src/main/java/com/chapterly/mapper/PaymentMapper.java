package com.chapterly.mapper;

import com.chapterly.dto.PaymentDto;
import com.chapterly.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", source = "payment.paymentId")
    @Mapping(target = "order", source = "payment.order")
    @Mapping(target = "paymentMethod", source = "payment.paymentMethod")
    @Mapping(target = "transactionId", source = "payment.transactionId")
    @Mapping(target = "amount", source = "payment.amount")
    @Mapping(target = "paymentDate", source = "payment.paymentDate")
    PaymentDto toDto(Payment payment);
}
