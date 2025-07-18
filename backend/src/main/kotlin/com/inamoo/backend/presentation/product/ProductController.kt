package com.inamoo.backend.presentation.product

import com.inamoo.backend.application.product.ProductService
import com.inamoo.backend.domain.product.Product
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAll(): List<Product> = productService.findAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody product: Product): Product = productService.create(product)
}
