package com.duzon.dbp.apimonitoring.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeSameIdError;
import com.duzon.dbp.apimonitoring.advice.exception.IdAndTokenNotMatchError;
import com.duzon.dbp.apimonitoring.advice.exception.SqlIndexError;
import com.duzon.dbp.apimonitoring.config.security.JwtTokenProvider;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.request.ReqCertificationDto;
import com.duzon.dbp.apimonitoring.dto.request.ReqEmployeeUpdateDto;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * EmployeeService
 */
@Service
public class EmployeeService {

	@Autowired
    EmployeeRepo employeeRepo;
    
    @Autowired
    JsonParseService jsonParseService;
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    public List<Map<String, Object>> findEmployeeList() {
        return jsonParseService.jsonEmployeeListMapParse(employeeRepo.findEmployeeList());
    }
    
    public Map<String, Object> employeeGet(String id) {
        Employee em = IdAndTokenMatch(id);
        return jsonParseService.jsonEmployeeMapParse(employeeRepo.employeeGet(em.getEmployee_no()));
    }
    
    public Map<String, String> Signin(ReqCertificationDto dto){
        Employee employee = employeeRepo.findById(dto.getId()).orElseThrow(EmployeeIdSigninFailedError::new);
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword()))
            throw new EmployeeIdSigninFailedError();

        String group_name = employeeRepo.employeeGroupName(employee.getEmployee_no());

        Map<String, String> map = new HashMap<>();
        map.put("token", jwtTokenProvider.createToken(String.valueOf(employee.getEmployee_no()), employee.getRoles()));
        map.put("getid", employee.getId());
        map.put("employee_no", String.valueOf(employee.getEmployee_no()));
        map.put("certification", String.valueOf(employee.getCertification()));
        map.put("group_name", group_name);
        map.put("name", employee.getName());
        
        return map;
    }

    public List<Map<String, Object>> employeeDataList() {
        return employeeRepo.employeeDataList();
    }
    
    public void ColumnsConfigSave(String idx, Map<String, Object> config) {
        Employee employee = IdAndTokenMatch(idx);

        Optional<Employee> em = employeeRepo.findById(employee.getEmployee_no());
        Map<String, Object> emPatch = new HashMap<String, Object>();
        
        if (em.get().getColumns_config() != null) {
            emPatch.putAll(em.get().getColumns_config());
        }
        for (String key : config.keySet()) {
            emPatch.put(key, config.get(key));
        }
        
        em.get().setColumns_config(emPatch);
        employeeRepo.save(em.get());
	}
    
	public void employeeGetUpdate(String idx, ReqEmployeeUpdateDto employee) {
        Employee em = IdAndTokenMatch(idx);
        
        em.setEmployee_contact(employee.getEmployee_contact());
        em.setGroup_no(employee.getGroup_no());
        em.setMail(employee.getMail());
        em.setName(employee.getName());
        
        if (employee.getPassword() != null) {
            em.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        
        employeeRepo.save(em);
    }
    
    public void employeePasswordUpdate(String idx, String password) {
        Employee em = IdAndTokenMatch(idx);
        
        em.setPassword(passwordEncoder.encode(password));
        
        if (em.getCertification() == 0) {
            em.setCertification(1);
        }
        
        employeeRepo.save(em);
	}
    
	public String Check(String id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (id.equals(userDetails.getUsername())) {
            return userDetails.getAuthorities().toString();
        } else {
            throw new IdAndTokenNotMatchError();
        }
	}
    
	public void SelfCertification(ReqCertificationDto dto) {
        Employee employee = employeeRepo.findById(dto.getId()).orElseThrow(EmployeeIdSigninFailedError::new);
        
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword()))
        throw new EmployeeIdSigninFailedError();
        
        if(!userDetails.getUsername().equals(dto.getId())){
            throw new IdAndTokenNotMatchError();
        }
    }
    
	public Map<String, Object> employeeInfoCount(String id) {
        IdAndTokenMatch(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Count", employeeRepo.employeeInfoCountErr(id) + employeeRepo.employeeInfoCountDelay(id));
        map.put("MainCount", employeeRepo.employeeInfoCountErrDelayMain(id));
        map.put("SubCount", employeeRepo.employeeInfoCountErrDelaySub(id));
        map.put("NotiCount", employeeRepo.employeeInfoNotiCount(id));
        
        return map;
	}
    
	public Map<String, List<Map<String, Object>>> employeeInfo(String id) {
        IdAndTokenMatch(id);
        Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
        map.put("AllList", jsonParseService.jsonEmployeeInfoListMapParse(employeeRepo.employeeInfoErr(id), employeeRepo.employeeInfoDelay(id)));
        map.put("MainList", jsonParseService.jsonEmployeeInfoListMapParse(employeeRepo.employeeInfoErrMain(id), employeeRepo.employeeInfoDelayMain(id)));
        map.put("SubList", jsonParseService.jsonEmployeeInfoListMapParse(employeeRepo.employeeInfoErrSub(id), employeeRepo.employeeInfoDelaySub(id)));
        
        return map;
    }
    
    // id 와 토큰의 id와 비교
    public Employee IdAndTokenMatch(String id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee em = employeeRepo.findById(id).orElseThrow(EmployeeNotFoundError::new);

        if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            if (!em.getId().equals(userDetails.getUsername())) {
                throw new IdAndTokenNotMatchError();
            }
        }
        return em;
    }
    
    //! == admin ==
    @Transactional
    public void SaveAll(List<Employee> employee) {
        for (int i = 0; i < employee.size(); i++) {
            if (!employeeRepo.findById(employee.get(i).getId()).isPresent()) {
                employee.get(i).setPassword(passwordEncoder.encode(employee.get(i).getPassword()));
                employee.get(i).setRoles(Collections.singletonList("ROLE_USER"));
            } else
                throw new EmployeeSameIdError();
        }
        try {
            employeeRepo.saveAll(employee);
        } catch (Exception e) {
            throw new EmployeeSameIdError();
        }
    }

    //! == admin ==
    @Transactional
    public void isDeleteAllT(List<Long> idx) {
        try {
            for (int i = 0; i < idx.size(); i++) {
                Optional<Employee> employee = employeeRepo.findById(idx.get(i));
                employee.get().setIsDeleted("T");
                employeeRepo.save(employee.get());
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
    }

    //! == admin ==
    public List<String> employeeListIdCheck(List<String> idx) {
        return employeeRepo.employeeListIdCheck(idx);
    }
}