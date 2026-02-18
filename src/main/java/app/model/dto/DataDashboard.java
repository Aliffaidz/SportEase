package app.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataDashboard {

    private BookingSummaryDashboard bookingSummaryDashboard;
    private List<ChartDataDetail> dataDetailList;

}
