package app.model.dto;

import app.model.dto.ChartDataDetail;

import java.util.List;

public record DashboardCircleResponse(
        List<ChartDataDetail> details
) {}