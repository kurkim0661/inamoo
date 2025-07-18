package com.inamoo.backend.infrastructure.product

import com.inamoo.backend.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long>
