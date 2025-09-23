package com.store.product.feature.catalog.api

import com.store.common.http.ApiResponse
import com.store.product.feature.catalog.app.ReadProduct
import com.store.product.feature.catalog.app.ReadProductCmd
import com.store.product.feature.catalog.app.RegisterProduct
import com.store.product.feature.catalog.app.RegisterProductCmd
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("\${api.prefix}/products")
class ProductCommandController(
    private val registerProduct: RegisterProduct,
    private val readProduct: ReadProduct
) {
    @PostMapping
    fun register(@RequestBody req: RegisterProductRequest): ApiResponse {
        val p = registerProduct.handle(RegisterProductCmd(req.code, req.name, description = req.description, price = req.price, stock = req.stock ))
        val product = ProductResponse(id = p.id!!.value, code = p.code, name = p.name, description = p.description, price = p.price, stock = p.stock)
        return ApiResponse.success(product)
    }

    @GetMapping
    fun read(@RequestBody req: ReadProductRequest): ApiResponse {
        val p = readProduct.handle(ReadProductCmd(req.code))
        val product = ProductResponse(id = p.id!!.value, code = p.code, name = p.name, description = p.description, price = p.price, stock = p.stock)
        return ApiResponse.success(product)
    }
}
