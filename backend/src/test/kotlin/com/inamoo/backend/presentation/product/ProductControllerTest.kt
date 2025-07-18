package com.inamoo.backend.presentation.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.inamoo.backend.application.product.ProductService
import com.inamoo.backend.domain.product.Product
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ProductController::class)
class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @MockBean
    lateinit var productService: ProductService

    @Test
    fun `GET all products returns list`() {
        val products = listOf(
            Product(id = 1, name = "A", price = 1000, category = "test"),
            Product(id = 2, name = "B", price = 2000, category = "test")
        )
        given(productService.findAll()).willReturn(products)

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(products)))
        verify(productService).findAll()
    }

    @Test
    fun `POST create product returns created product`() {
        val product = Product(id = 1, name = "A", price = 1000, category = "test")
        given(productService.create(product)).willReturn(product)

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(product)))
        verify(productService).create(product)
    }
}
