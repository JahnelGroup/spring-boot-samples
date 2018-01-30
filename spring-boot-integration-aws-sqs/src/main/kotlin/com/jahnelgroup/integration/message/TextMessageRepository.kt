package com.jahnelgroup.integration.message

import org.springframework.data.jpa.repository.JpaRepository

interface TextMessageRepository : JpaRepository<TextMessage, String>