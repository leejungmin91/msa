package com.store.product.feature.catalog.app

import com.store.product.feature.catalog.domain.Product
import com.store.product.feature.catalog.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterProduct(
    private val productRepository: ProductRepository
) {
    @Transactional
    fun handle(cmd: RegisterProductCmd): Product {
        require(productRepository.findByCode(cmd.code) == null) { "이미 존재하는 상품 코드입니다." }
        return productRepository.save(Product(code = cmd.code, name = cmd.name, description = cmd.description, price = cmd.price, stock = cmd.stock))
    }
}
