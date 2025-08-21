package com.store.product.feature.catalog.infra

import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, Long> {
    fun findByCode(code: String): ProductJpaEntity?
}
