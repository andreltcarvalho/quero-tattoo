package com.querotattoo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Nome da cidade não pode estar em branco")
    @Column(name = "name", unique = true)
    private String name;

    @NotNull(message = "Nome do estado não pode ser nulo")
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

}

