package com.smarthr.analytics.client;

import com.smarthr.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "interview-service", url = "${interview.service.url:http://localhost:8083}")
public interface InterviewClient {

    @GetMapping("/interviews/count/status/{status}")
    ApiResponse<Long> countByStatus(@PathVariable("status") Integer status);

    @GetMapping("/interviews/count/total")
    ApiResponse<Long> countTotal();

    @GetMapping("/interviews/candidate/{candidateId}")
    ApiResponse<List<Object>> getByCandidateId(@PathVariable("candidateId") Long candidateId);

    @GetMapping("/interviews/job/{jobId}")
    ApiResponse<List<Object>> getByJobId(@PathVariable("jobId") Long jobId);

    @GetMapping("/interviews/interviewer/{interviewerId}")
    ApiResponse<List<Object>> getByInterviewerId(@PathVariable("interviewerId") Long interviewerId);

    @GetMapping("/interviews/status/{status}")
    ApiResponse<List<Object>> getByStatus(@PathVariable("status") Integer status);

    @GetMapping("/interviews/upcoming")
    ApiResponse<List<Object>> getUpcoming(@RequestParam(defaultValue = "24") int hours);
}
