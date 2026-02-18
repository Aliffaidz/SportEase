package app.model.dto;

import java.util.List;

public record DashboardResponse(
        BookingSummaryDashboard summary, // Data dari record yang Anda buat
        List<ChartDataDetail> charts,    // Data lingkaran (7 hari atau 4 minggu)
        String filterType                // Menandai apakah ini "WEEKLY" atau "MONTHLY"
) {}