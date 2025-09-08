//package com.example.store.exception;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Проверка, является ли запрос от Swagger
//    private boolean isSwaggerRequest() {
//        try {
//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            if (requestAttributes instanceof ServletRequestAttributes) {
//                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//                String requestUri = request.getRequestURI();
//                return requestUri != null &&
//                        (requestUri.contains("/v3/api-docs") ||
//                                requestUri.contains("/swagger-ui") ||
//                                requestUri.contains("/api-docs"));
//            }
//        } catch (Exception e) {
//            // Игнорируем ошибки при проверке
//        }
//        return false;
//    }
//
//    // Обработка нарушения уникальности
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DataIntegrityViolationException ex) {
//        if (isSwaggerRequest()) {
//            return null; // пропускаем обработку для Swagger
//        }
//
//        if (ex.getMessage().contains("email") || ex.getMessage().contains("duplicate")) {
//            ErrorResponse error = new ErrorResponse(
//                    "CONFLICT",
//                    "Email already exists",
//                    HttpStatus.CONFLICT.value()
//            );
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
//        );
//    }
//
//    // Обработка ошибок валидации @Valid
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        if (isSwaggerRequest()) {
//            return null; // пропускаем обработку для Swagger
//        }
//
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        ErrorResponse error = new ErrorResponse(
//                "VALIDATION_ERROR",
//                "Validation failed: " + String.join(", ", errors),
//                HttpStatus.BAD_REQUEST.value()
//        );
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    // Общая обработка всех исключений
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
//        if (isSwaggerRequest()) {
//            return null; // пропускаем обработку для Swagger
//        }
//
//        ErrorResponse error = new ErrorResponse(
//                "INTERNAL_ERROR",
//                "An unexpected error occurred",
//                HttpStatus.INTERNAL_SERVER_ERROR.value()
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//}