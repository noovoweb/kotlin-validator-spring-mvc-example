package com.noovoweb.project.request.scenario

import com.noovoweb.validator.*

@Validated
data class TwoLevelsRequest(
    @Valid val order: Order,
    @Valid val customer: Customer
)

@Validated
data class Order(
    @Required val id: String?,
    @Required @MinLength(3) val status: String?,
    @Required @Min(0.0) val total: Double?
)

@Validated
data class Customer(
    @Required @MinLength(2) val name: String?,
    @Valid val address: Address
)

@Validated
data class Address(
    @Required @MinLength(2) val street: String?,
    @Required @MinLength(2) val city: String?,
    @Required @Pattern("^[A-Z0-9-]{4,10}$") val postalCode: String?
)

