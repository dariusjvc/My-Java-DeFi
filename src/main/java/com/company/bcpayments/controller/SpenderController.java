package com.company.bcpayments.controller;


import com.company.bcpayments.annotation.SwaggerDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/spender")
@Slf4j
@SwaggerDoc
public class SpenderController {

    @GetMapping("/stakingToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getTotalStakingTokensSpender() throws Exception {

        return "0";
    }
    @GetMapping("/rewardToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getTotalRewardTokensSpender() throws Exception {

        return "0";
    }
}
