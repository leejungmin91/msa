package com.store.product.feature.catalog.infra

import com.store.product.feature.catalog.domain.Product
import com.store.product.feature.catalog.domain.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val jpa: ProductJpaRepository
) : ProductRepository {
    override fun findByCode(code: String) = jpa.findByCode(code)?.toDomain()
    override fun save(product: Product) = jpa.save(ProductJpaEntity.from(product)).toDomain()
}
