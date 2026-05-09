package com.smarthr.candidate.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.candidate.model.Candidate;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface CandidateRepository extends BaseMapper<Candidate> {
    IPage<Candidate> findByConditions(Page<Candidate> page, 
                                      @Param("status") Integer status,
                                      @Param("jobId") Long jobId,
                                      @Param("source") String source,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
    
    List<Candidate> findByStatus(@Param("status") Integer status);
    
    List<Candidate> findByJobId(@Param("jobId") Long jobId);
    
    Long countByStatus(@Param("status") Integer status);
}