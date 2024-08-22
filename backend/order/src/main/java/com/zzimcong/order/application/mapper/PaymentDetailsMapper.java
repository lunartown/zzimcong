package com.zzimcong.order.application.mapper;

import com.zzimcong.order.application.dto.PaymentDetailsRequest;
import com.zzimcong.order.domain.entity.PaymentDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaymentDetailsMapper.class})
public interface PaymentDetailsMapper {
    PaymentDetails paymentDetailsRequestToPaymentDetails(PaymentDetailsRequest paymentDetailsRequest);
}
