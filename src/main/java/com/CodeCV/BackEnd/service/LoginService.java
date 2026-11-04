package com.CodeCV.BackEnd.service;

import com.CodeCV.BackEnd.dto.EmployeeLoginDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface LoginService {
    EmployeeLoginDTO login(String email, String password) throws Exception;
}
