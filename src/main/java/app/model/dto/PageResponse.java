package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
public class PageResponse {

    private long size;
    private long number;
    private long totalElements;
    private long totalPages;

}
