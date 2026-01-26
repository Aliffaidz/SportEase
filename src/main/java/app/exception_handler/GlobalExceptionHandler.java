package app.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,String>> globalMessageException(ResponseStatusException e){
        log.info("exception handle global");
        Map<String,String> messages = new HashMap<>();
        messages.put("messages",e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(messages);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentException(MethodArgumentNotValidException e){
        log.info("exception handler method argumen");
        Map<String,String> messages = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> messages.put(fieldError.getField(),fieldError.getDefaultMessage()));
        return ResponseEntity.status(e.getStatusCode()).body(messages);
    }
}
