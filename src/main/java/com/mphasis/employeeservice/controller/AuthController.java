package com.mphasis.employeeservice.controller;

import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.dto.LoginRequestDTO;
import com.mphasis.employeeservice.service.AuthService;
import com.mphasis.employeeservice.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    private final FileStorageService fileStorageService;

    public AuthController(AuthService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/signup")
    public ResponseEntity<EmployeeResponseDTO> signup(
            @Valid @ModelAttribute EmployeeRequestDTO request
            ) {
        String fileName = fileStorageService.storeFile(request.getProfilePic());
        System.out.println(fileName);
        System.out.println("Here is the Object : "+request);

//        request.setProfilePic(fileName);

        EmployeeResponseDTO responseDTO = service.signup(request, fileName);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<EmployeeResponseDTO> login(@Valid @RequestBody LoginRequestDTO request)
    {
        EmployeeResponseDTO responseDTO = service.login(request);
        return ResponseEntity.ok(responseDTO);
    }


}
