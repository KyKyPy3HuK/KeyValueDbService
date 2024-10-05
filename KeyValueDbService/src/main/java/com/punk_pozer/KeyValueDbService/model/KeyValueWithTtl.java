package com.punk_pozer.KeyValueDbService.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "KeyValueWithTtl")
public class KeyValueWithTtl {

    @Id
    @Column(name = "idKey", nullable = false)
    String key;

    @Column(name = "contentValue")
    String value;

    @Column(name = "ttl", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date ttl;

    public KeyValueWithTtl(String key, String value, Date ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

    public KeyValueWithTtl(){}

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
