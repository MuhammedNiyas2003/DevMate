package com.devmate.dashboard.controller;

import com.devmate.common.response.ApiResponse;
import com.devmate.dashboard.dto.DashboardStatsResponse;
import com.devmate.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStatsResponse> getStats() {
        return ApiResponse.success("Dashboard statistics fetched successfully",
                dashboardService.getStats());
    }
}