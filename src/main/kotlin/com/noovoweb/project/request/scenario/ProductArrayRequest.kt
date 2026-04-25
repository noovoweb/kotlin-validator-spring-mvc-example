package com.noovoweb.project.request.scenario

import com.noovoweb.validator.*

@Validated
data class ProductArrayRequest(
    @Required
    @MinSize(1)
    @Valid(each = true)
    val products: List<Product>?
)

@Validated
data class Product(
    @Required
    @MinLength(3)
    @MaxLength(100)
    val name: String?,

    @Required
    @MinLength(10)
    @MaxLength(500)
    val description: String?,

    @Required
    @Min(0.01)
    @Max(2000.0)
    val price: Double?,

    @Required
    @Min(0.0)
    val quantity: Int?,

    @Required
    @MinLength(3)
    @MaxLength(50)
    val category: String?,

    @Required(message = "SKU is mandatory")
    val sku: String?
)

