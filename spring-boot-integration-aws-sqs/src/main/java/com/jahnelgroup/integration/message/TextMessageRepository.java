package com.jahnelgroup.integration.message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextMessageRepository extends JpaRepository<TextMessage, String> {
}
