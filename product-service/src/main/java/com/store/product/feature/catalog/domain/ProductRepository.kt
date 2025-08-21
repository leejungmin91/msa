package com.store.product.feature.catalog.domain

interface ProductRepository {
    fun findByCode(code: String): Product?
    fun save(product: Product): Product
}
