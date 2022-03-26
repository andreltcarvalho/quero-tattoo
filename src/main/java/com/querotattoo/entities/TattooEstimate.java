package com.querotattoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_tattoo_estimate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TattooEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull(message = "A ideia não pode ser nula")
    @NotBlank(message = "A ideia não pode estar em branco")
    private String idea;

    private String size;

    @ElementCollection
    @NotNull
    @NotEmpty
    private List<String> tattooReferences;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    @NotNull(message = "O Local da tatuagem não pode ser nulo")
    @NotBlank(message = "O Local da tatuagem não pode estar em branco")
    private String placeOfBody;

    @JsonIgnore
    @OneToOne
    private Schedule schedule;

    private String status;

}
