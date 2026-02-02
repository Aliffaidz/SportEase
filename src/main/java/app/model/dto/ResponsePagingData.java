package app.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.web.PagedModel;

@Data
@Builder
public class ResponsePagingData <T> {

    private String messages;
    private T data;
    private PageResponse page;

}
