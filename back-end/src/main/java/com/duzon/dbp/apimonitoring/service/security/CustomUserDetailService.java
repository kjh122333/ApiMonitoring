package com.duzon.dbp.apimonitoring.service.security;

import com.duzon.dbp.apimonitoring.advice.exception.EmployeeNotFoundError;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


/**
 * CustomUserDetailService
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    EmployeeRepo employeeRepo;
    
    //Employee
    public UserDetails loadUserByUsername(String no) {
        Employee employee = employeeRepo.findByIdx(Long.valueOf(no)).orElseThrow(EmployeeNotFoundError::new);
        return employee;
    }
}
