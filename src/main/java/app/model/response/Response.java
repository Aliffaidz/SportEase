package app.model.response;

import lombok.Builder;

@Builder
public class Response <T>{

    private String messages;
    private T data;
    
}
