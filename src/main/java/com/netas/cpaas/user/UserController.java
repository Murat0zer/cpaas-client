package com.netas.cpaas.user;

import com.netas.cpaas.CustomException;
import com.netas.cpaas.user.model.LoginDto;
import com.netas.cpaas.user.model.register.RegistrationDto;
import com.netas.cpaas.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {

        try {
            userService.signin(loginDto.getUsername(), loginDto.getPassword());
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto registrationDto) {

        try {
            return ResponseEntity.ok(userService.signup(registrationDto));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

}
