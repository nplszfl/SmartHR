package com.smarthr.candidate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.candidate.dto.CandidateDTO;
import com.smarthr.candidate.dto.CandidateRequest;
import com.smarthr.candidate.model.Candidate;
import com.smarthr.candidate.repository.CandidateRepository;
import com.smarthr.common.dto.PageResponse;
import com.smarthr.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateDTO create(CandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setResumeUrl(request.getResumeUrl());
        candidate.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        candidate.setJobId(request.getJobId());
        candidate.setEducation(request.getEducation());
        candidate.setExperienceYears(request.getExperienceYears());
        candidate.setSkills(request.getSkills());
        candidate.setSource(request.getSource());
        candidate.setNotes(request.getNotes());
        candidate.setAppliedAt(LocalDateTime.now());
        candidate.setInterviewerId(request.getInterviewerId());
        
        candidateRepository.insert(candidate);
        return toDTO(candidate);
    }

    public CandidateDTO update(Long id, CandidateRequest request) {
        Candidate candidate = candidateRepository.selectById(id);
        if (candidate == null) {
            throw new BusinessException(404, "Candidate not found");
        }
        
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setResumeUrl(request.getResumeUrl());
        if (request.getStatus() != null) candidate.setStatus(request.getStatus());
        if (request.getJobId() != null) candidate.setJobId(request.getJobId());
        candidate.setEducation(request.getEducation());
        candidate.setExperienceYears(request.getExperienceYears());
        candidate.setSkills(request.getSkills());
        candidate.setSource(request.getSource());
        candidate.setNotes(request.getNotes());
        if (request.getInterviewerId() != null) candidate.setInterviewerId(request.getInterviewerId());
        
        candidateRepository.updateById(candidate);
        return toDTO(candidate);
    }

    public void delete(Long id) {
        candidateRepository.deleteById(id);
    }

    public CandidateDTO getById(Long id) {
        Candidate candidate = candidateRepository.selectById(id);
        if (candidate == null) {
            throw new BusinessException(404, "Candidate not found");
        }
        return toDTO(candidate);
    }

    public PageResponse<CandidateDTO> list(Integer page, Integer size, Integer status, Long jobId, String source) {
        Page<Candidate> pageParam = new Page<>(page, size);
        IPage<Candidate> result = candidateRepository.findByConditions(pageParam, status, jobId, source, null, null);
        
        List<CandidateDTO> records = result.getRecords().stream().map(this::toDTO).toList();
        return PageResponse.of(result.getTotal(), page, size, records);
    }

    public List<CandidateDTO> getByJobId(Long jobId) {
        return candidateRepository.findByJobId(jobId).stream().map(this::toDTO).toList();
    }

    public List<CandidateDTO> getByStatus(Integer status) {
        return candidateRepository.findByStatus(status).stream().map(this::toDTO).toList();
    }

    public void updateStatus(Long id, Integer status) {
        Candidate candidate = candidateRepository.selectById(id);
        if (candidate == null) {
            throw new BusinessException(404, "Candidate not found");
        }
        candidate.setStatus(status);
        candidateRepository.updateById(candidate);
    }

    public Long countByStatus(Integer status) {
        return candidateRepository.countByStatus(status);
    }

    private CandidateDTO toDTO(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());
        dto.setPhone(candidate.getPhone());
        dto.setResumeUrl(candidate.getResumeUrl());
        dto.setStatus(candidate.getStatus());
        dto.setJobId(candidate.getJobId());
        dto.setEducation(candidate.getEducation());
        dto.setExperienceYears(candidate.getExperienceYears());
        dto.setSkills(candidate.getSkills());
        dto.setSource(candidate.getSource());
        dto.setNotes(candidate.getNotes());
        dto.setAppliedAt(candidate.getAppliedAt());
        dto.setInterviewerId(candidate.getInterviewerId());
        dto.setCreateTime(candidate.getCreateTime());
        return dto;
    }
}