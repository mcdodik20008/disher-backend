package com.mcdodik.disher.core.controller

import com.mcdodik.disher.core.model.Department
import com.mcdodik.disher.core.model.Product
import com.mcdodik.disher.core.repository.ProductRepo
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
@Validated
class ProductController(
    private val repo: ProductRepo,
) {
    data class CreateProductReq(
        @field:NotBlank val name: String,
        @field:NotNull val department: Department,
    )

    @GetMapping
    fun list(): List<Product> = repo.findAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody body: CreateProductReq,
    ): Product = repo.save(Product(name = body.name, department = body.department))
}
