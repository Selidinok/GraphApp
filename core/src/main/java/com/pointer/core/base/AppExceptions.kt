package com.pointer.core.base

sealed class AppExceptions(message: String): Exception(message) {
    data class NetworkOffException(override val message: String = "Network connection is lost") : AppExceptions(message)
    data class NetworkException(override val message: String) : AppExceptions(message)
    data class GraphException(override val message: String) : AppExceptions(message)
    data class ValidationException(override val message: String = "Only digits allowed") : AppExceptions(message)
}