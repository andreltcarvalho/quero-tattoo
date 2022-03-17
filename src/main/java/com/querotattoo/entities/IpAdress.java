package com.querotattoo.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ip_adresses")
@Data
public class IpAdress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    public IpAdress(Long id, String ip) {
        this.id = id;
        this.ip=ip;
    }
    public IpAdress(){

    }


}
