package com.store.product.feature.catalog.domain

interface ProductRepository {
    fun findByCode(code: String): Product?
    fun findByCodeOrThrow(code: String): Product =
        findByCode(code) ?: throw NoSuchElementException("상품을 찾을 수 없습니다. code=$code")
    fun save(product: Product): Product
}
