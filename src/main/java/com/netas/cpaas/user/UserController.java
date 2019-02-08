package com.netas.cpaas.user;

import com.netas.cpaas.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
}
