package com.sonnh.retailmanagerv2.controller.admin;

import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.dto.request.admin.UserDto;
import com.sonnh.retailmanagerv2.security.JwtUtils;
import com.sonnh.retailmanagerv2.service.impl.OrderInStoreServiceImpl;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Login (Admin)")
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StoreRepository storeRepository;
    private OrderInStoreServiceImpl orderInStoreService;

    @PostMapping("/login")
    public String login(@RequestBody UserDto dto) {
        System.out.println("Chay vo day ne ");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UUID storeId = storeRepository.findStoreIdByUsername(userDetails.getUsername());
//        String result = jwtUtils.generateToken(userDetails,storeId);
//        System.out.println("result: " + result);
//        orderInStoreService.getInventoryFromDbToRedis(storeId);
        return jwtUtils.generateToken(userDetails,storeId);
    }

}
