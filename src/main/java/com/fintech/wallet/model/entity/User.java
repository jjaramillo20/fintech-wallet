package com.fintech.wallet.model.entity;


import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100, name = "lastname")
    private String lastName;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255, name = "password_hash")
    private String password;

    //TODO: Revisar el tipo fetch, problema con hibernate pues lo ignora y fuerza eager
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    protected User() {
    }

    public User(String name, String lastName, String email, String password) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("The name cannot be null or empty.");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("The last name cannot be null or empty.");
        if (email == null || !email.contains("@") || email.isBlank()) throw new IllegalArgumentException("Invalid email format");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Invalid password format");
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User other = (User) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setWallet(Wallet wallet) {
        if (wallet == null){
            throw new IllegalArgumentException("The wallet cannot be null");
        }
        wallet.setUser(this);
        this.wallet=wallet;
    }


}
