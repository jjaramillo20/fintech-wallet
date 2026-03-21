package com.fintech.wallet.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @JdbcTypeCode(Types.CHAR)
    @Column(columnDefinition = "char(3)", nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    protected Currency() {
    }

    public Currency(String id, String name) {
        if (id == null || id.length() != 3) throw new IllegalArgumentException("Currency ID must be exactly 3 chars (ISO-4217)");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.id = id.toUpperCase();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}