package com.mcdodik.disher.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.NoArgsConstructor

@Entity
@Table(name = "product")
@NoArgsConstructor
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var department: Department,
)
