package com.example.demo.payment.seed

import com.example.demo.payment.Payment
import com.example.demo.payment.PaymentRepo
import org.springframework.stereotype.Component
import java.math.BigDecimal
import javax.annotation.PostConstruct

@Component
class PaymentInit(val paymentRepo: PaymentRepo) {

    @PostConstruct
    fun init(){
        paymentRepo.saveAll(listOf(
                Payment(amount = BigDecimal("100.00")),
                Payment(amount = BigDecimal("200.00")),
                Payment(amount = BigDecimal("300.00")),
                Payment(amount = BigDecimal("400.00"))
        ))
    }

}