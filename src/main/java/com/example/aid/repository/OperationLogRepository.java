package com.example.aid.repository;

import com.example.aid.model.OperationLog;
import com.example.aid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    // 按用户查询操作日志
    List<OperationLog> findByUserOrderByOperationTimeDesc(User user);
    
    // 按角色查询操作日志
    List<OperationLog> findByRoleOrderByOperationTimeDesc(String role);
    
    // 按操作类型查询
    List<OperationLog> findByOperationTypeOrderByOperationTimeDesc(String operationType);
    
    // 按时间范围查询
    List<OperationLog> findByOperationTimeBetweenOrderByOperationTimeDesc(LocalDateTime startTime, LocalDateTime endTime);
    
    // 复合查询：按角色和时间范围
    List<OperationLog> findByRoleAndOperationTimeBetweenOrderByOperationTimeDesc(String role, LocalDateTime startTime, LocalDateTime endTime);
    
    // 复合查询：按操作类型和时间范围
    List<OperationLog> findByOperationTypeAndOperationTimeBetweenOrderByOperationTimeDesc(String operationType, LocalDateTime startTime, LocalDateTime endTime);
    
    // 统计查询：按角色统计操作次数
    @Query("SELECT ol.role, COUNT(ol) FROM OperationLog ol GROUP BY ol.role")
    List<Object[]> countByRole();
    
    // 统计查询：按操作类型统计次数
    @Query("SELECT ol.operationType, COUNT(ol) FROM OperationLog ol GROUP BY ol.operationType")
    List<Object[]> countByOperationType();
}


