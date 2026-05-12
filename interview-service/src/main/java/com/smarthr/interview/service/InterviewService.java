package com.smarthr.interview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.interview.dto.InterviewDTO;
import com.smarthr.interview.dto.InterviewRequest;
import com.smarthr.interview.model.Interview;
import com.smarthr.interview.repository.InterviewRepository;
import com.smarthr.common.dto.PageResponse;
import com.smarthr.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;

    public InterviewDTO create(InterviewRequest request) {
        Interview interview = new Interview();
        interview.setCandidateId(request.getCandidateId());
        interview.setJobId(request.getJobId());
        interview.setInterviewerId(request.getInterviewerId());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setDuration(request.getDuration() != null ? request.getDuration() : 60);
        interview.setLocation(request.getLocation());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setType(request.getType() != null ? request.getType() : 1);
        interview.setStatus(1); // scheduled
        interview.setNotes(request.getNotes());

        interviewRepository.insert(interview);
        return toDTO(interview);
    }

    public InterviewDTO update(Long id, InterviewRequest request) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }

        if (request.getCandidateId() != null) interview.setCandidateId(request.getCandidateId());
        if (request.getJobId() != null) interview.setJobId(request.getJobId());
        if (request.getInterviewerId() != null) interview.setInterviewerId(request.getInterviewerId());
        if (request.getScheduledAt() != null) interview.setScheduledAt(request.getScheduledAt());
        if (request.getDuration() != null) interview.setDuration(request.getDuration());
        if (request.getLocation() != null) interview.setLocation(request.getLocation());
        if (request.getMeetingLink() != null) interview.setMeetingLink(request.getMeetingLink());
        if (request.getType() != null) interview.setType(request.getType());
        if (request.getNotes() != null) interview.setNotes(request.getNotes());

        interviewRepository.updateById(interview);
        return toDTO(interview);
    }

    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

    public InterviewDTO getById(Long id) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }
        return toDTO(interview);
    }

    public PageResponse<InterviewDTO> list(Integer page, Integer size, Long candidateId, Long jobId,
                                           Long interviewerId, Integer status, Integer type) {
        Page<Interview> pageParam = new Page<>(page, size);
        IPage<Interview> result = interviewRepository.findByConditions(
                pageParam, candidateId, jobId, interviewerId, status, type);

        List<InterviewDTO> records = result.getRecords().stream().map(this::toDTO).toList();
        return PageResponse.of(result.getTotal(), page, size, records);
    }

    public List<InterviewDTO> getByCandidateId(Long candidateId) {
        return interviewRepository.findByCandidateId(candidateId).stream().map(this::toDTO).toList();
    }

    public List<InterviewDTO> getByJobId(Long jobId) {
        return interviewRepository.findByJobId(jobId).stream().map(this::toDTO).toList();
    }

    public List<InterviewDTO> getByInterviewerId(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId).stream().map(this::toDTO).toList();
    }

    public List<InterviewDTO> getByStatus(Integer status) {
        return interviewRepository.findByStatus(status).stream().map(this::toDTO).toList();
    }

    public List<InterviewDTO> getUpcoming(int hours) {
        LocalDateTime before = LocalDateTime.now().plusHours(hours);
        return interviewRepository.findUpcoming(before).stream().map(this::toDTO).toList();
    }

    public InterviewDTO startInterview(Long id) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }
        interview.setStatus(2); // in-progress
        interview.setStartedAt(LocalDateTime.now());
        interviewRepository.updateById(interview);
        return toDTO(interview);
    }

    public InterviewDTO completeInterview(Long id, Integer rating, String feedback, String recommendation) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }
        interview.setStatus(3); // completed
        interview.setEndedAt(LocalDateTime.now());
        if (rating != null) interview.setRating(rating);
        if (feedback != null) interview.setFeedback(feedback);
        if (recommendation != null) interview.setResultRecommendation(recommendation);
        interviewRepository.updateById(interview);
        return toDTO(interview);
    }

    public InterviewDTO cancelInterview(Long id) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }
        interview.setStatus(4); // cancelled
        interviewRepository.updateById(interview);
        return toDTO(interview);
    }

    public InterviewDTO rescheduleInterview(Long id, LocalDateTime newScheduledAt) {
        Interview interview = interviewRepository.selectById(id);
        if (interview == null) {
            throw new BusinessException(404, "Interview not found");
        }
        interview.setStatus(5); // rescheduled
        interview.setScheduledAt(newScheduledAt);
        interviewRepository.updateById(interview);
        return toDTO(interview);
    }

    public Long countByStatus(Integer status) {
        return interviewRepository.countByStatus(status);
    }

    private InterviewDTO toDTO(Interview interview) {
        InterviewDTO dto = new InterviewDTO();
        dto.setId(interview.getId());
        dto.setCandidateId(interview.getCandidateId());
        dto.setJobId(interview.getJobId());
        dto.setInterviewerId(interview.getInterviewerId());
        dto.setScheduledAt(interview.getScheduledAt());
        dto.setDuration(interview.getDuration());
        dto.setLocation(interview.getLocation());
        dto.setMeetingLink(interview.getMeetingLink());
        dto.setType(interview.getType());
        dto.setStatus(interview.getStatus());
        dto.setNotes(interview.getNotes());
        dto.setRating(interview.getRating());
        dto.setFeedback(interview.getFeedback());
        dto.setStartedAt(interview.getStartedAt());
        dto.setEndedAt(interview.getEndedAt());
        dto.setResultRecommendation(interview.getResultRecommendation());
        dto.setCreateTime(interview.getCreateTime());
        return dto;
    }
}