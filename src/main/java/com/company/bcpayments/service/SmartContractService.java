package com.company.bcpayments.service;

import com.company.bcpayments.repository.RewardTokenRepository;
import com.company.bcpayments.repository.StakingTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class SmartContractService {
    private final StakingTokenRepository StakingToken;
    private final RewardTokenRepository RewardToken;

    public SmartContractService(StakingTokenRepository stakingToken, RewardTokenRepository rewardToken) {
        StakingToken = stakingToken;
        RewardToken = rewardToken;
    }


    public String getTotalStakingTokensOwner() throws Exception {
     return StakingToken.getTotalStakingTokensOwner();
    }


    public String getTotalRewardTokensOwner() throws Exception {
        return RewardToken.getTotalRewardTokensOwner();
    }
}
