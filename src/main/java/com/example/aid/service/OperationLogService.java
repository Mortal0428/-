package com.example.aid.service;

import com.example.aid.model.OperationLog;
import com.example.aid.model.User;
import com.example.aid.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationLogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    // 记录操作日志
    public void logOperation(User user, String operationType, String operationContent) {
        OperationLog log = new OperationLog();
        log.setUser(user);
        log.setRole(user.getRole());
        log.setOperationType(operationType);
        log.setOperationContent(operationContent);
        log.setOperationTime(LocalDateTime.now());
        operationLogRepository.save(log);
    }
    
    // 查询所有操作日志
    public List<OperationLog> findAll() {
        return operationLogRepository.findAll();
    }
    
    // 按用户查询
    public List<OperationLog> findByUser(User user) {
        return operationLogRepository.findByUserOrderByOperationTimeDesc(user);
    }
    
    // 按角色查询
    public List<OperationLog> findByRole(String role) {
        return operationLogRepository.findByRoleOrderByOperationTimeDesc(role);
    }
    
    // 按操作类型查询
    public List<OperationLog> findByOperationType(String operationType) {
        return operationLogRepository.findByOperationTypeOrderByOperationTimeDesc(operationType);
    }
    
    // 按时间范围查询
    public List<OperationLog> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return operationLogRepository.findByOperationTimeBetweenOrderByOperationTimeDesc(startTime, endTime);
    }
    
    // 复合查询：角色+时间范围
    public List<OperationLog> findByRoleAndTimeRange(String role, LocalDateTime startTime, LocalDateTime endTime) {
        return operationLogRepository.findByRoleAndOperationTimeBetweenOrderByOperationTimeDesc(role, startTime, endTime);
    }
    
    // 复合查询：操作类型+时间范围
    public List<OperationLog> findByOperationTypeAndTimeRange(String operationType, LocalDateTime startTime, LocalDateTime endTime) {
        return operationLogRepository.findByOperationTypeAndOperationTimeBetweenOrderByOperationTimeDesc(operationType, startTime, endTime);
    }
    
    // 统计：按角色统计
    public List<Object[]> countByRole() {
        return operationLogRepository.countByRole();
    }
    
    // 统计：按操作类型统计
    public List<Object[]> countByOperationType() {
        return operationLogRepository.countByOperationType();
    }
}


