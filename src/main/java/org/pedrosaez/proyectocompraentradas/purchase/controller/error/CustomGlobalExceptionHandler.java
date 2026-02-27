package org.pedrosaez.proyectocompraentradas.purchase.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.*;


@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> springHandleNotFound(EventNotFoundException ex,
                                                                HttpServletRequest request) {
        logger.info("------ springHandleNotFound()");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", ex.getLocalizedMessage());
        body.put("autor", "Pedro");
        body.put("empresa", "Accenture");

        return new ResponseEntity<Object>(body,HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        logger.info("------ handleMethodArgumentNotValid()");

        // Obtener errores de validación por campo
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        body.put("status", status.value());
        body.put("error", status.toString());
        body.put("message", ex.getLocalizedMessage());
        body.put("autor", "Pedro");
        body.put("empresa", "Accenture");
        body.put("errors", errors);


        return new ResponseEntity<>(body, headers, status);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("------ handleHttpRequestMethodNotSupported()");
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        body.put("status", status.value());
        body.put("error", ex.getLocalizedMessage());
        body.put("message", builder.toString());
        body.put("autor", "Pedro");
        body.put("empresa", "Accenture");

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(PurchaseException.class)
    public ResponseEntity<Map<String, Object>> handlePurchaseException(PurchaseException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getCode());
        body.put("message", ex.getMessage());

        HttpStatus status = ex.getCode().startsWith("400")
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(body, status);
    }
}
