package com.querotattoo.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Table(name = "tb_studio_addresses")
@Data
public class StudioAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "O CEP não pode ser nulo")
    @Column(name = "address_cep")
    @NotBlank(message = "O endereço não pode estar em branco.")
    private String cep;

    @NotNull(message = "O endereço não pode ser nulo.")
    @NotBlank(message = "O endereço não pode estar em branco.")
    @Column(name = "address_street")
    private String streetWithNumber;

    @NotNull(message = "A cidade não pode ser Nula")
    @ManyToOne
    @JoinColumn(name = "address_city_id")
    private City city;

    @NotNull(message = "O nome do estudio não pode ser nulo")
    @NotBlank
    private String name;
}