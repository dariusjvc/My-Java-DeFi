package com.company.bcpayments.controller;


import com.company.bcpayments.annotation.SwaggerDoc;
import com.company.bcpayments.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
@SwaggerDoc
public class UserController {
    UserService userService;
    @GetMapping("/name")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getName() {
        return "Joseph";
    }

    @PostMapping("/approveStaking")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String approveStaking() {

        //return userService.approveStaking();
        return "testApprove";
    }
}
