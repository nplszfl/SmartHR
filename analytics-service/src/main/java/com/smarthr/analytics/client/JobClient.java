package com.smarthr.analytics.client;

import com.smarthr.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "job-service", url = "${job.service.url:http://localhost:8084}")
public interface JobClient {

    @GetMapping("/jobs/count/status/{status}")
    ApiResponse<Long> countByStatus(@PathVariable("status") Integer status);

    @GetMapping("/jobs/count/total")
    ApiResponse<Long> countTotal();

    @GetMapping("/jobs/status/{status}")
    ApiResponse<List<Object>> getByStatus(@PathVariable("status") Integer status);

    @GetMapping("/jobs/department/{department}")
    ApiResponse<List<Object>> getByDepartment(@PathVariable("department") String department);
}
