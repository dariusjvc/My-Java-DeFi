package com.company.bcpayments.controller;

import com.company.bcpayments.service.AdminService;
import com.company.bcpayments.annotation.SwaggerDoc;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@RestController
@RequestMapping("api/v1/admin")
@Slf4j
@SwaggerDoc

public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /*@GetMapping("/name")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String getName() {
         return adminService.name();
    }*/

    /*@GetMapping("/totalMinted")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public String totalTokensCirculating( ) {
        return adminService.totalTokens();
    }*/


    @PostMapping("/transferStakingTokensToUser")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public TransactionReceipt transferToken(@ApiParam(value = " Id del usuario ",  required = true)
                                    @RequestParam double value) throws Exception {
        return adminService.transferTokens(value);
    }

    @PostMapping("/rewardUser")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public TransactionReceipt rewardUser() throws Exception {
        return adminService.rewardUser();
    }


}
