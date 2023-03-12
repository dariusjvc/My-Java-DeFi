package com.company.bcpayments.repository;

import com.company.bcpayments.wrapper.StakingToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigInteger;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StakingTokenRepository {

    private final Web3j ethereumClient;

    private final Environment environment;


    private static final String PREFIX_ZERO = "0x000000000000000000000000";


    public String getName() throws NullPointerException, ResponseStatusException, Exception{
        String contractAddress = getContractAddress();
        Credentials credentials = getOwnerCredentials();
        //StakingToken token = loadTokenContract(contractAddress, credentials);
        StakingToken token = loadTokenContract(contractAddress, credentials);

        String tokenName = token.name().send();
        log.info("The token name is: " + tokenName);
        return tokenName;
    }



    public String getTotalStakingTokensOwner() throws NullPointerException, ResponseStatusException, Exception{
        //Cargo el contrato a partir de la direccion y la firma
        String contractAddress = getContractAddress();
        Credentials credentials = getOwnerCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        BigInteger balance = token.balanceOf(environment.getProperty("credentials.owner.public-key")).send();
        String totalTokens = String.valueOf(balance.divide(BigInteger.valueOf(100)));
        return totalTokens;
    }




    public String getTotalStakingTokensUser() throws NullPointerException, ResponseStatusException, Exception{
        //Cargo el contrato a partir de la direccion y la firma
        String contractAddress = getContractAddress();
        Credentials credentials = getUserCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        BigInteger balance = token.balanceOf(environment.getProperty("credentials.user.public-key")).send();
        String totalTokens = String.valueOf(balance.divide(BigInteger.valueOf(100)));
        return totalTokens;
    }
    @NotNull
    private  String getContractAddress() throws NullPointerException {

        String contractAddress = environment.getProperty("contract.staking.address","");

        if (contractAddress.isEmpty()) {
            StakingTokenRepository.log.error("Contract address is not valid");
            throw new NullPointerException();
        }
        return contractAddress;
    }

    @NotNull
    private Credentials getOwnerCredentials() throws ResponseStatusException {

        String password = environment.getProperty("credentials.owner.account.password", "");
        String keyPath = environment.getProperty("credentials.owner.account.path", "");
        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(password, keyPath);

        } catch (IOException e) {
            StakingTokenRepository.log.error("Can't load path {}", keyPath);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (CipherException e) {
            StakingTokenRepository.log.error("Can't decode key", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return credentials;
    }

    @NotNull
    private Credentials getUserCredentials() throws ResponseStatusException {

        String password = environment.getProperty("credentials.user.account.password", "");
        String keyPath = environment.getProperty("credentials.user.account.path", "");
        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(password, keyPath);

        } catch (IOException e) {
            StakingTokenRepository.log.error("Can't load path {}", keyPath);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (CipherException e) {
            StakingTokenRepository.log.error("Can't decode key", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return credentials;
    }

    @NotNull
    private StakingToken loadTokenContract(String contractAddress, Credentials credentials) {
        return StakingToken.load(
                contractAddress,
                ethereumClient,
                new FastRawTransactionManager(ethereumClient, credentials, new PollingTransactionReceiptProcessor(ethereumClient, 100, 400)),
                new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(9_000_000))
        );

    }


    public TransactionReceipt transferTokens(double value) throws NullPointerException, ResponseStatusException, Exception{
        String contractAddress = getContractAddress();
        Credentials credentials = getOwnerCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        TransactionReceipt result = token.transfer(environment.getProperty("credentials.user.public-key"), BigInteger.valueOf((long) value)).send();
        return result;
    }


    public TransactionReceipt transferStakingTokensToSpender(double value) throws NullPointerException, ResponseStatusException, Exception{
        String contractAddress = getContractAddress();
        Credentials credentials = getOwnerCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        TransactionReceipt result = token.transfer(environment.getProperty("contract.main.address"), BigInteger.valueOf((long) value)).send();
        return result;
    }
    public TransactionReceipt approveStaking(double value) throws NullPointerException, ResponseStatusException, Exception{
        String contractAddress = getContractAddress();
        Credentials credentials = getUserCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        TransactionReceipt result = token.approve(environment.getProperty("contract.main.address"), BigInteger.valueOf((long) value)).send();
        return result;
    }


}
