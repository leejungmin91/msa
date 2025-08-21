package com.store.product.feature.catalog.api

data class ProductResponse(val id: Long, val code: String, val name: String, val description: String, val price: Double, val stock: Int)
