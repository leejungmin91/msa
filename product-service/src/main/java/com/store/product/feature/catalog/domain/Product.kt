package com.store.product.feature.catalog.domain

data class Product(
    val id: ProductId? = null,
    val code: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
)
