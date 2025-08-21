package com.store.product.feature.catalog.api

import com.store.product.feature.catalog.app.RegisterProduct
import com.store.product.feature.catalog.app.RegisterProductCmd
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/products")
class ProductCommandController(
    private val registerProduct: RegisterProduct
) {
    @PostMapping
    fun register(@RequestBody req: RegisterProductRequest): ProductResponse {
        val p = registerProduct.handle(RegisterProductCmd(req.code, req.name, description = req.description, price = req.price, stock = req.stock ))
        return ProductResponse(id = p.id!!.value, code = p.code, name = p.name, description = p.description, price = p.price, stock = p.stock)
    }
}
