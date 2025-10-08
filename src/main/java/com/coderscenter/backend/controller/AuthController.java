package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.user.response.AuthDTO;
import com.coderscenter.backend.dtos.user.request.LoginDTO;
import com.coderscenter.backend.dtos.user.request.UserRegisterRequestDTO;
import com.coderscenter.backend.exceptions.UserAlreadyExistsException;
import com.coderscenter.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final  UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){

        AuthDTO authDTO;

        try {
            authDTO = userService.login(loginDTO);
        }catch (UsernameNotFoundException e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }  catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDTO registerDTO){

        AuthDTO authDTO;

        try {
            authDTO = userService.register(registerDTO);
        }catch (UserAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authDTO, HttpStatus.CREATED);
    }
}
