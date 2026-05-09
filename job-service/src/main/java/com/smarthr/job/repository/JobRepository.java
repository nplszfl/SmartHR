package com.smarthr.job.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smarthr.job.model.Job;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface JobRepository extends BaseMapper<Job> {
    IPage<Job> findByConditions(Page<Job> page,
                               @Param("status") Integer status,
                               @Param("department") String department,
                               @Param("location") String location);
    
    List<Job> findByStatus(@Param("status") Integer status);
    
    List<Job> findByDepartment(@Param("department") String department);
    
    Long countByStatus(@Param("status") Integer status);
}