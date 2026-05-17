package com.smarthr.analytics.client;

import com.smarthr.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "employee-service", url = "${employee.service.url:http://localhost:8081}")
public interface EmployeeClient {

    @GetMapping("/employees/count/active")
    ApiResponse<Long> getTotalActiveCount();

    @GetMapping("/employees/count/status/{status}")
    ApiResponse<Long> countByStatus(@PathVariable("status") String status);

    @GetMapping("/employees/count/department/{department}")
    ApiResponse<Long> countByDepartment(@PathVariable("department") String department);

    @GetMapping("/employees/on-leave")
    ApiResponse<List<Object>> getOnLeave();

    @GetMapping("/employees/recently-hired")
    ApiResponse<List<Object>> getRecentlyHired(@RequestParam(defaultValue = "30") int days);

    @GetMapping("/employees/due-for-review")
    ApiResponse<List<Object>> getDueForReview(@RequestParam(defaultValue = "365") int days);

    @GetMapping("/employees/statistics")
    ApiResponse<Map<String, Object>> getStatistics();

    @GetMapping("/employees/headcount-trend")
    ApiResponse<Map<String, Object>> getHeadcountTrend(@RequestParam(defaultValue = "12") int months);
}
