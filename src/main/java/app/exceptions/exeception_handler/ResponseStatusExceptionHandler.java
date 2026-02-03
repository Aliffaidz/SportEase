package app.exceptions.exeception_handler;

import app.model.dto.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ResponseStatusExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public Response<String> responseStatusExceptionHandler(ResponseStatusException e){
        return Response.<String>builder()
                .messages("failed")
                .data(e.getMessage())
                .build();
    }
}
