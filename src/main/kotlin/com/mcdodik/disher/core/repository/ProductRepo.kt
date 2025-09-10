package com.mcdodik.disher.core.repository

import com.mcdodik.disher.core.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepo : JpaRepository<Product, Long>
