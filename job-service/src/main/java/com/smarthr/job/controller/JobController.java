package com.smarthr.job.controller;

import com.smarthr.job.dto.JobDTO;
import com.smarthr.job.dto.JobRequest;
import com.smarthr.job.service.JobService;
import com.smarthr.common.dto.ApiResponse;
import com.smarthr.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ApiResponse<JobDTO> create(@Valid @RequestBody JobRequest request) {
        return ApiResponse.success(jobService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<JobDTO> update(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        return ApiResponse.success(jobService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<JobDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(jobService.getById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<JobDTO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String location) {
        return ApiResponse.success(jobService.list(page, size, status, department, location));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<JobDTO>> getByStatus(@PathVariable Integer status) {
        return ApiResponse.success(jobService.getByStatus(status));
    }

    @GetMapping("/department/{department}")
    public ApiResponse<List<JobDTO>> getByDepartment(@PathVariable String department) {
        return ApiResponse.success(jobService.getByDepartment(department));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        jobService.updateStatus(id, status);
        return ApiResponse.success("Status updated successfully", null);
    }

    @PostMapping("/{id}/increment-application")
    public ApiResponse<Void> incrementApplication(@PathVariable Long id) {
        jobService.incrementApplicationCount(id);
        return ApiResponse.success("Application count incremented", null);
    }

    @GetMapping("/count/status/{status}")
    public ApiResponse<Long> countByStatus(@PathVariable Integer status) {
        return ApiResponse.success(jobService.countByStatus(status));
    }
}