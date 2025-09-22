package com.store.product.feature.catalog.api.advice

import com.store.common.http.ApiResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class, EntityNotFoundException::class)
    fun handleProductNotFound(ex: Exception, req: HttpServletRequest): ResponseEntity<ApiResponse> {
        return ResponseEntity
            .ok(ApiResponse.fail(ex.message))
    }
}
