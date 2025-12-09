package com.sonnh.retailmanagerv2.controller.employee;

import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.dto.request.admin.UserDto;
import com.sonnh.retailmanagerv2.security.JwtUtils;
import com.sonnh.retailmanagerv2.service.impl.OrderInStoreServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Login (Employee)")
@RequestMapping("/api/employee/auth")
@RequiredArgsConstructor
public class EmployeeLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StoreRepository storeRepository;
    @Autowired
    private OrderInStoreServiceImpl orderInStoreService;
    @PostMapping("/login")
    public String login(@RequestBody UserDto dto) {
        System.out.println("Chay vo day ne ");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UUID storeId = storeRepository.findStoreIdByUsername(userDetails.getUsername());
//        String result = jwtUtils.generateToken(userDetails,storeId);
        System.out.println("StoreId : " + storeId);
        orderInStoreService.createInventoryFromDbToRedis(storeId);
        return jwtUtils.generateToken(userDetails,storeId);
    }
}
