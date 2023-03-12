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

    @PostMapping("/stakeTokens")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public TransactionReceipt stakeTokens(@ApiParam(value = " Id del usuario ",  required = true)
                                             @RequestParam double value) throws Exception {

        return userService.stakeTokens( value);
    }

    @PostMapping("/unstakeTokens")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public TransactionReceipt unstakeTokens() throws Exception {

        return userService.unstakeTokens();
    }

    @GetMapping("/StakingToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getTotalStakingTokensUser() throws Exception {

        return userService.getTotalStakingTokensUser();
    }

    @GetMapping("/RewardToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getTotalRewardTokensUser() throws Exception {

        return userService.getTotalRewardTokensUser();
    }
}
