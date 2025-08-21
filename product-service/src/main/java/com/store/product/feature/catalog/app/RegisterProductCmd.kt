package com.store.product.feature.catalog.app

data class RegisterProductCmd(val code: String, val name: String, val description: String, val price: Double, val stock: Int)
