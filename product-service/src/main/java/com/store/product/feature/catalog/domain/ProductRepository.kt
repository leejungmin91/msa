package com.store.product.feature.catalog.domain

import jakarta.persistence.EntityNotFoundException

interface ProductRepository {
    fun findByCode(code: String): Product?
    fun findByCodeOrThrow(code: String): Product =
        findByCode(code) ?: throw EntityNotFoundException("상품을 찾을 수 없습니다.")
    fun save(product: Product): Product
}
