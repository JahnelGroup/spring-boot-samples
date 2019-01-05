package com.example.api.errors

import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class WebApiExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(status, "Something went wrong with your request.", emptyList(), ex.localizedMessage)
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val errors = mutableListOf<String>()
        ex.bindingResult.fieldErrors.forEach { errors.add("${it.field}: ${it.defaultMessage}") }
        ex.bindingResult.globalErrors.forEach { errors.add("${it.objectName}: ${it.defaultMessage}") }
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, errors)
        return handleExceptionInternal(ex, apiError, headers, apiError.status, request)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val errors = mutableListOf<String>()
        ex.bindingResult.fieldErrors.forEach { errors.add("${it.field}: ${it.defaultMessage}") }
        ex.bindingResult.globalErrors.forEach { errors.add("${it.objectName}: ${it.defaultMessage}") }
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, errors)
        return handleExceptionInternal(ex, apiError, headers, apiError.status, request)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, "${ex.value} value for ${ex.propertyName} should be of type ${ex.requiredType}")
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleMissingServletRequestPart(ex: MissingServletRequestPartException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex.requestPartName + " part is missing")
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex.parameterName + " parameter is missing")
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex.name + " should be of type " + ex.requiredType!!.name)
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        val errors = mutableListOf<String>()
        ex.constraintViolations.forEach { errors.add("$it.rootBeanClass.name $it.propertyPath: $it.message") }
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.localizedMessage, errors)
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleNoHandlerFoundException(ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.NOT_FOUND, ex.localizedMessage, "No handler found for " + ex.httpMethod + " " + ex.requestURL)
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.method)
        builder.append(" method is not supported for this request. Supported methods are ")
        ex.supportedHttpMethods!!.forEach { t -> builder.append(t.toString() + " ") }
        val apiError = ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.localizedMessage, builder.toString())
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    override fun handleHttpMediaTypeNotSupported(ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach { t -> builder.append(t.toString() + " ") }
        val apiError = ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.localizedMessage, builder.substring(0, builder.length - 2))
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage, "error occurred")
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

}