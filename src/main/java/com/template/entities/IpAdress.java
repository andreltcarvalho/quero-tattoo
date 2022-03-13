package com.template.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "enderecos")
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpAdress)) return false;
        IpAdress ipAdress = (IpAdress) o;
        return getId().equals(ipAdress.getId()) && ip.equals(ipAdress.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), ip);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
