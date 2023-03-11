package com.company.bcpayments.controller;

import com.company.bcpayments.annotation.SwaggerDoc;
import com.company.bcpayments.service.SmartContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/SC")
@Slf4j
@SwaggerDoc
public class SmartContractController {

    private final SmartContractService smartContractService;

    public SmartContractController(SmartContractService smartContractService) {
        this.smartContractService = smartContractService;
    }

    @GetMapping("/StakingToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getSTTotal() throws Exception {

        return smartContractService.getTotalStakingTokensOwner();
    }
    @GetMapping("/RewardToken/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getRTTotal() throws Exception {

        return smartContractService.getTotalRewardTokensOwner();
    }

}
