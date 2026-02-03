package app.exceptions.exeception_handler;

import app.model.dto.Response;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AttributeExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Map<String,String>> methodArgumentNotValidExceptions(MethodArgumentNotValidException e){
        Map<String,String> data = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(value -> data.put(value.getField(), value.getDefaultMessage()));
        return Response.<Map<String, String>>builder()
                .messages("failed")
                .data(data)
                .build();
    }


}
