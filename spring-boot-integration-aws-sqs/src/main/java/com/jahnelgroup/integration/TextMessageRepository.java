package com.jahnelgroup.integration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextMessageRepository extends JpaRepository<TextMessage, String> {
}
