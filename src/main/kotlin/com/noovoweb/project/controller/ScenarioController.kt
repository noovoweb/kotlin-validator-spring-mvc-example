package com.noovoweb.project.controller

import com.noovoweb.project.request.scenario.*
import com.noovoweb.project.response.DataResponse
import com.noovoweb.validator.spring.mvc.ValidationContextProvider
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mvc/api/scenario")
class ScenarioController(
    private val contextProvider: ValidationContextProvider
) {

    /**
     * Simulates a complete user registration form validation.
     * Tests multiple validators including:
     * - Email format validation
     * - Password strength requirements (min length, uppercase, lowercase, number)
     * - Password confirmation matching
     * - Name validation (required, alpha only, length constraints)
     * - Age validation (min/max range)
     * - Phone number format validation
     * - Terms acceptance validation
     */
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest, httpRequest: HttpServletRequest): DataResponse<Map<String, Any?>> = runBlocking {
        val cleaned = request.copy(phoneNumber = request.phoneNumber?.trim())
        RegisterRequestValidator().validate(cleaned, contextProvider.get(httpRequest))

        val responseData = mapOf(
            "email" to request.email,
            "firstName" to request.firstName,
            "lastName" to request.lastName,
            "age" to request.age,
            "phoneNumber" to cleaned.phoneNumber
        )

        DataResponse(
            data = responseData,
            message = "Registration successful! Welcome ${request.firstName} ${request.lastName}"
        )
    }

    /**
     * Validates a two-level nested payload structure.
     * For demonstration, it simply validates and echoes back
     * the order ID and customer city from the nested structure.
     */
    @PostMapping("/nested-two-levels")
    fun nestedTwoLevels(@RequestBody request: TwoLevelsRequest, httpRequest: HttpServletRequest): DataResponse<Map<String, Any?>> = runBlocking {
        TwoLevelsRequestValidator().validate(request, contextProvider.get(httpRequest))

        val summary = mapOf(
            "orderId" to request.order.id,
            "customerCity" to request.customer.address.city
        )

        DataResponse(
            data = summary,
            message = "Nested two-levels payload validated successfully"
        )
    }

    /**
     * Validates an array of products.
     * Tests collections and nested objects validators including:
     * - Array must contain at least 1 product
     * - Each product must have valid name, description, price, quantity, category
     * - Product names must be 3-100 characters
     * - Descriptions must be 10-500 characters
     * - Price must be between 0.01 and 2,000
     * - Quantity must be >= 0
     * - Category must be 3-50 characters
     * - SKU is required
     */
    @PostMapping("/products-array")
    fun productsArray(@RequestBody request: ProductArrayRequest, httpRequest: HttpServletRequest): DataResponse<Map<String, Any?>> = runBlocking {
        // @Valid(each = true) on products field automatically validates each Product in the array
        ProductArrayRequestValidator().validate(request, contextProvider.get(httpRequest))

        val totalValue = request.products?.sumOf { (it.price ?: 0.0) * (it.quantity ?: 0) } ?: 0.0
        val productCount = request.products?.size ?: 0

        val summary = mapOf(
            "productCount" to productCount,
            "totalInventoryValue" to totalValue,
            "products" to request.products?.map {
                mapOf(
                    "name" to it.name,
                    "category" to it.category,
                    "price" to it.price,
                    "quantity" to it.quantity
                )
            }
        )

        DataResponse(
            data = summary,
            message = "Products array validated successfully"
        )
    }

}
