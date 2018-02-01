package com.jahnelgroup.rest.auction;

import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Person POJO.
 */
@Entity
@Data
public class Person extends AbstractAggregateRoot implements Identifiable<Long> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Size(max = 2, min = 2)
    private String state;

}
