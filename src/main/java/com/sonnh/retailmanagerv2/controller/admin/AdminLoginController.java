package com.sonnh.retailmanagerv2.controller.admin;

import com.sonnh.retailmanagerv2.dto.request.admin.UserDto;
import com.sonnh.retailmanagerv2.security.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Login (Admin)")
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public String login(@RequestBody UserDto dto) {
        System.out.println("Chay vo day ne ");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String result = jwtUtils.generateToken(userDetails);
        System.out.println("result: " + result);
        return result;
    }

}
