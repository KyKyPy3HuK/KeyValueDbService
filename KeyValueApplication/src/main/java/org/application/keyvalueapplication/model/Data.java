package org.application.keyvalueapplication.model;

import jakarta.persistence.*;
import org.application.keyvalueapplication.repositories.DataRepository;

import java.util.Date;

@Entity
@Table
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "data_key",nullable = false)
    private String key;

    @Column(name = "data_value", nullable = false)
    private String value;

    @Column(name = "ttl", nullable = false)
    private Date ttl;

    public Data() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getTtl() {
        return ttl;
    }

    public void setTtl(Date ttl) {
        this.ttl = ttl;
    }
}
