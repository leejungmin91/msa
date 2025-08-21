package com.store.product.feature.catalog.infra

import com.store.product.feature.catalog.domain.*
import jakarta.persistence.*

@Entity @Table(name = "products")
class ProductJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true) val code: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
){
    fun toDomain() = Product(
        id = id?.let { ProductId(it) },
        code = code, name = name, description = description, price = price, stock = stock
    )
    companion object {
        fun from(p: Product) = ProductJpaEntity(
            id = p.id?.value, code = p.code, name = p.name, description = p.description, price = p.price, stock = p.stock
        )
    }
}
