package com.example.demo.payment.integration

import com.example.demo.payment.Payment
import com.example.demo.payment.PaymentRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.handler.GenericHandler
import org.springframework.integration.http.dsl.Http

@Configuration
class PaymentIntegration(var paymentRepo: PaymentRepo) {

    val paymentUrl = "/api/payments"

    @Bean
    fun httpGetFlow(): IntegrationFlow =
            IntegrationFlows.from(
                Http.inboundGateway("$paymentUrl/{id}").requestMapping {
                    it.methods(HttpMethod.GET)
                }.payloadExpression("#pathVariables.id")
            ).handle(GenericHandler<Long> { id, _ -> paymentRepo.findById(id).orElseThrow({ResourceNotFoundException()})}
            ).get()

    @Bean
    fun httpPostFlow(): IntegrationFlow =
            IntegrationFlows.from(
                    Http.inboundGateway("$paymentUrl").requestMapping {
                        it.methods(HttpMethod.POST)
                    }.requestPayloadType(Payment::class.java)
            ).handle(GenericHandler<Payment> { payment, _ -> paymentRepo.save(payment) }
            ).get()

}