package com.company.bcpayments.controller;


import com.company.bcpayments.annotation.SwaggerDoc;
import com.company.bcpayments.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
@SwaggerDoc
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/approveStaking")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public TransactionReceipt approveStaking(@ApiParam(value = " Id del usuario ",  required = true)
                                                 @RequestParam double value) throws Exception {

        return userService.approveStaking( value);
    }
}
