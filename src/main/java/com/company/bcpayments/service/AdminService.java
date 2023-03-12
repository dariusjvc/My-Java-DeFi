package com.company.bcpayments.service;

import com.company.bcpayments.repository.MainTokenRepository;
import com.company.bcpayments.repository.StakingTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Service
@Primary
@Slf4j
public class AdminService {
    private final StakingTokenRepository StakingToken;
    private final MainTokenRepository MainToken;

    public AdminService(StakingTokenRepository bck, MainTokenRepository mainToken) {
        this.StakingToken = bck;
        MainToken = mainToken;
    }

    public String name(){
        String name ;

        try{
            name = StakingToken.getName();

            return name;

        } catch (Exception e){

            return "Error getting the token name";

        }
    }

    /*public String totalTokens(){


        //Comprueba los permisos


        String total;

        try{
            //Recibo y el resultado y lo envuelvo en la respuesta
            total = String.valueOf(StakingToken.getTotalTokens());

            return total;

        } catch (NullPointerException e){

            return "Error getting the total tokens";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/


    public TransactionReceipt transferTokens(double value) throws Exception {
        return StakingToken.transferTokens(value);
    }

    public TransactionReceipt transferStakingTokensToSpender(double value) throws Exception {
        return StakingToken.transferStakingTokensToSpender(value);
    }

    public TransactionReceipt rewardUser() throws Exception {
        return MainToken.rewardUser();
    }
}
