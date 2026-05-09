package com.smarthr.candidate.controller;

import com.smarthr.candidate.dto.CandidateDTO;
import com.smarthr.candidate.dto.CandidateRequest;
import com.smarthr.candidate.service.CandidateService;
import com.smarthr.common.dto.ApiResponse;
import com.smarthr.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping
    public ApiResponse<CandidateDTO> create(@Valid @RequestBody CandidateRequest request) {
        return ApiResponse.success(candidateService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<CandidateDTO> update(@PathVariable Long id, @Valid @RequestBody CandidateRequest request) {
        return ApiResponse.success(candidateService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        candidateService.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<CandidateDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(candidateService.getById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<CandidateDTO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String source) {
        return ApiResponse.success(candidateService.list(page, size, status, jobId, source));
    }

    @GetMapping("/job/{jobId}")
    public ApiResponse<List<CandidateDTO>> getByJobId(@PathVariable Long jobId) {
        return ApiResponse.success(candidateService.getByJobId(jobId));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<CandidateDTO>> getByStatus(@PathVariable Integer status) {
        return ApiResponse.success(candidateService.getByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        candidateService.updateStatus(id, status);
        return ApiResponse.success("Status updated successfully", null);
    }

    @GetMapping("/count/status/{status}")
    public ApiResponse<Long> countByStatus(@PathVariable Integer status) {
        return ApiResponse.success(candidateService.countByStatus(status));
    }
}