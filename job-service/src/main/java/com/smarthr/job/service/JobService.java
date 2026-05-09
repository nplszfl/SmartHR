package com.smarthr.job.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.job.dto.JobDTO;
import com.smarthr.job.dto.JobRequest;
import com.smarthr.job.model.Job;
import com.smarthr.job.repository.JobRepository;
import com.smarthr.common.dto.PageResponse;
import com.smarthr.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public JobDTO create(JobRequest request) {
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setResponsibilities(request.getResponsibilities());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setCurrency(request.getCurrency());
        job.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        job.setPostedBy(request.getPostedBy());
        job.setBenefits(request.getBenefits());
        job.setRemoteOption(request.getRemoteOption());
        job.setApplicationCount(0);
        
        jobRepository.insert(job);
        return toDTO(job);
    }

    public JobDTO update(Long id, JobRequest request) {
        Job job = jobRepository.selectById(id);
        if (job == null) {
            throw new BusinessException(404, "Job not found");
        }
        
        job.setTitle(request.getTitle());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setResponsibilities(request.getResponsibilities());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setCurrency(request.getCurrency());
        if (request.getStatus() != null) job.setStatus(request.getStatus());
        if (request.getPostedBy() != null) job.setPostedBy(request.getPostedBy());
        job.setBenefits(request.getBenefits());
        job.setRemoteOption(request.getRemoteOption());
        
        jobRepository.updateById(job);
        return toDTO(job);
    }

    public void delete(Long id) {
        jobRepository.deleteById(id);
    }

    public JobDTO getById(Long id) {
        Job job = jobRepository.selectById(id);
        if (job == null) {
            throw new BusinessException(404, "Job not found");
        }
        return toDTO(job);
    }

    public PageResponse<JobDTO> list(Integer page, Integer size, Integer status, String department, String location) {
        Page<Job> pageParam = new Page<>(page, size);
        IPage<Job> result = jobRepository.findByConditions(pageParam, status, department, location);
        
        List<JobDTO> records = result.getRecords().stream().map(this::toDTO).toList();
        return PageResponse.of(result.getTotal(), page, size, records);
    }

    public List<JobDTO> getByStatus(Integer status) {
        return jobRepository.findByStatus(status).stream().map(this::toDTO).toList();
    }

    public List<JobDTO> getByDepartment(String department) {
        return jobRepository.findByDepartment(department).stream().map(this::toDTO).toList();
    }

    public void updateStatus(Long id, Integer status) {
        Job job = jobRepository.selectById(id);
        if (job == null) {
            throw new BusinessException(404, "Job not found");
        }
        job.setStatus(status);
        if (status == 2) { // published
            job.setPublishedAt(LocalDateTime.now());
        } else if (status == 4) { // closed
            job.setClosedAt(LocalDateTime.now());
        }
        jobRepository.updateById(job);
    }

    public void incrementApplicationCount(Long id) {
        Job job = jobRepository.selectById(id);
        if (job != null) {
            job.setApplicationCount(job.getApplicationCount() + 1);
            jobRepository.updateById(job);
        }
    }

    public Long countByStatus(Integer status) {
        return jobRepository.countByStatus(status);
    }

    private JobDTO toDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDepartment(job.getDepartment());
        dto.setLocation(job.getLocation());
        dto.setEmploymentType(job.getEmploymentType());
        dto.setDescription(job.getDescription());
        dto.setRequirements(job.getRequirements());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setCurrency(job.getCurrency());
        dto.setStatus(job.getStatus());
        dto.setPostedBy(job.getPostedBy());
        dto.setPublishedAt(job.getPublishedAt());
        dto.setClosedAt(job.getClosedAt());
        dto.setApplicationCount(job.getApplicationCount());
        dto.setBenefits(job.getBenefits());
        dto.setRemoteOption(job.getRemoteOption());
        dto.setCreateTime(job.getCreateTime());
        return dto;
    }
}