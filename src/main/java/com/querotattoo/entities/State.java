package com.querotattoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "tb_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", unique = true)
    @NotNull(message = "Nome do estado não pode ser nulo")
    private String name;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name = "state_id")
    private List<City> cidades;

    @Override
    public String toString() {
        return name;
    }
}
