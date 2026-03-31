package com.creditflow.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity tells JPA "this Java class is a database table"
// Without this annotation, JPA completely ignores this class
@Entity

// @Table tells JPA the exact table name to use in PostgreSQL
// If you skip this, JPA uses the class name — "Merchant" becomes "merchant"
// We're being explicit so there's no ambiguity
@Table(name = "merchants")
public class Merchant {

    // @Id tells JPA "this field is the primary key"
    // Every table needs exactly one primary key — it's the unique identifier for each row
    @Id

    // @GeneratedValue tells JPA "don't make me set this manually, generate it automatically"
    // GenerationType.IDENTITY means PostgreSQL itself generates the ID
    // using an auto-incrementing sequence (1, 2, 3, 4...)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column lets you configure how this field maps to a database column
    // nullable = false means PostgreSQL will REJECT any insert that leaves this blank
    // This is a database-level constraint, not just a Java check — much stronger
    @Column(nullable = false)
    private String name;

    // unique = true means no two merchants can have the same email
    // nullable = false means email is required
    // Again — these are enforced by PostgreSQL itself, not just your Java code
    @Column(nullable = false, unique = true)
    private String email;

    // This stores what kind of business the merchant is
    // e.g. "RETAIL", "FOOD", "SERVICES"
    @Column(nullable = false)
    private String businessType;

    // updatable = false is a powerful trick:
    // it tells JPA "set this value when inserting, but NEVER change it on update"
    // This gives us an immutable audit trail — we'll always know when a record was created
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @PrePersist means "run this method automatically right before
    // inserting this object into the database for the first time"
    // This is how we set createdAt without ever having to remember to do it manually
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Constructors ───────────────────────────────────────
    // JPA requires a no-argument constructor to exist
    // It uses reflection to create instances of your class internally
    // We make it protected so your own code can't accidentally use it
    protected Merchant() {}

    // This is the constructor YOU use when creating a new merchant
    public Merchant(String name, String email, String businessType) {
        this.name = name;
        this.email = email;
        this.businessType = businessType;
    }

    // ── Getters ────────────────────────────────────────────
    // JPA and Jackson (the JSON library) both need getters to read your fields
    // We don't add setters for id and createdAt because nothing should ever change them
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBusinessType() { return businessType; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ────────────────────────────────────────────
    // Only name and businessType are updatable — email changes are sensitive
    // and should go through a dedicated process later
    public void setName(String name) { this.name = name; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
}