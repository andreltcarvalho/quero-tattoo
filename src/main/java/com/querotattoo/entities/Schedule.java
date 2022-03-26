package com.querotattoo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "tb_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Endereço nao pode ser nulo")
    @NotBlank(message = "Endereço não pode estar em branco")
    private String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm")
    private Instant dateToLog;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm")
    private Instant eventDate;

    @NotNull
    @OneToOne
    private TattooEstimate tattooEstimate;

    private String status;
}
