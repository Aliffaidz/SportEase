package app.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response <T>{

    private String messages;
    private T data;
    
}
