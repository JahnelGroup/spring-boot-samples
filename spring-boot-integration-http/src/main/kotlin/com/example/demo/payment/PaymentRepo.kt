package com.example.demo.payment

import org.springframework.data.repository.CrudRepository

interface PaymentRepo : CrudRepository<Payment, Long>