package com.querotattoo.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class StudioAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "address_cep")
    private String cep;

    @Column(name = "address_street")
    private String streetWithNumber;

    @ManyToOne
    @JoinColumn(name = "address_city_id")
    private City city;
}