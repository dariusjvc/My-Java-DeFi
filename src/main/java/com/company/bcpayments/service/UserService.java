package com.company.bcpayments.service;

import com.company.bcpayments.repository.StakingTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Service
@Primary
@Slf4j
public class UserService {

    private final StakingTokenRepository StakingToken;

    public UserService(StakingTokenRepository stakingToken) {
        StakingToken = stakingToken;
    }


    public TransactionReceipt approveStaking(double value) throws Exception {
        return StakingToken.approveStaking(  value);
    }
}
