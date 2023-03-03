package com.company.bcpayments.controller;


import com.company.bcpayments.annotation.SwaggerDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
@SwaggerDoc
public class UserController {

    @GetMapping("/name")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getName() {
        return "Joseph";
    }
}
