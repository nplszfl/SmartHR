package com.smarthr.interview.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.interview.model.Interview;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository extends BaseMapper<Interview> {
    IPage<Interview> findByConditions(Page<Interview> page,
                                      @Param("candidateId") Long candidateId,
                                      @Param("jobId") Long jobId,
                                      @Param("interviewerId") Long interviewerId,
                                      @Param("status") Integer status,
                                      @Param("type") Integer type);

    List<Interview> findByCandidateId(@Param("candidateId") Long candidateId);

    List<Interview> findByJobId(@Param("jobId") Long jobId);

    List<Interview> findByInterviewerId(@Param("interviewerId") Long interviewerId);

    List<Interview> findByStatus(@Param("status") Integer status);

    List<Interview> findUpcoming(LocalDateTime before);

    Long countByStatus(@Param("status") Integer status);
}