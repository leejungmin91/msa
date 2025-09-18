package com.store.product.feature.catalog.app

import com.store.product.feature.catalog.domain.Product
import com.store.product.feature.catalog.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReadProduct(
    private val productRepository: ProductRepository
) {
    @Transactional
    fun handle(cmd: ReadProductCmd): Product {
        return productRepository.findByCodeOrThrow(cmd.code)
    }
}
