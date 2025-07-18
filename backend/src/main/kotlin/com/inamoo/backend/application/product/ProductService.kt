package com.inamoo.backend.application.product

import com.inamoo.backend.domain.product.Product
import com.inamoo.backend.infrastructure.product.ProductEntity
import com.inamoo.backend.infrastructure.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    @Transactional(readOnly = true)
    fun findAll(): List<Product> =
        productRepository.findAll().map { it.toDomain() }

    @Transactional
    fun create(product: Product): Product {
        val entity = ProductEntity(
            name = product.name,
            description = product.description,
            price = product.price,
            imageUrl = product.imageUrl,
            category = product.category,
            createdAt = product.createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = product.updatedAt ?: java.time.LocalDateTime.now(),
            userId = product.userId
        )
        return productRepository.save(entity).toDomain()
    }

    private fun ProductEntity.toDomain() = Product(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        category = this.category,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userId = this.userId
    )
}
