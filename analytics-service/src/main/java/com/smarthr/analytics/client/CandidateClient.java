package com.smarthr.analytics.client;

import com.smarthr.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "candidate-service", url = "${candidate.service.url:http://localhost:8082}")
public interface CandidateClient {

    @GetMapping("/candidates/count/status/{status}")
    ApiResponse<Long> countByStatus(@PathVariable("status") Integer status);

    @GetMapping("/candidates/count/total")
    ApiResponse<Long> countTotal();

    @GetMapping("/candidates/job/{jobId}")
    ApiResponse<List<Object>> getByJobId(@PathVariable("jobId") Long jobId);

    @GetMapping("/candidates/status/{status}")
    ApiResponse<List<Object>> getByStatus(@PathVariable("status") Integer status);
}
