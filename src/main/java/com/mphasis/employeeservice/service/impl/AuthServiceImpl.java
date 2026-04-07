package com.mphasis.employeeservice.service.impl;

import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.dto.LoginRequestDTO;
import com.mphasis.employeeservice.exception.EmailAlreadyExistsException;
import com.mphasis.employeeservice.exception.InvalidCredentialsException;
import com.mphasis.employeeservice.mapper.EmployeeMapper;
import com.mphasis.employeeservice.model.Employee;
import com.mphasis.employeeservice.repository.EmployeeRepository;
import com.mphasis.employeeservice.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository repo;

    public AuthServiceImpl(EmployeeRepository repo){
        this.repo = repo;
    }

    @Override
    public EmployeeResponseDTO signup(EmployeeRequestDTO employeeRequestDTO, String fileName){
        if(repo.existsByEmail(employeeRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        Employee e = EmployeeMapper.toModel(employeeRequestDTO, fileName);

        Employee saved = repo.save(e);
        return EmployeeMapper.toDTO(saved);
    }

    @Override
    public EmployeeResponseDTO login(LoginRequestDTO request) {

        Employee e = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email"));

        if (!request.getPassword().equals(e.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return EmployeeMapper.toDTO(e);

    }

}
