package com.company.bcpayments.repository;

import com.company.bcpayments.wrapper.StakingToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StakingTokenRepository {

    private final Web3j ethereumClient;

    private final Environment environment;


    private static final String PREFIX_ZERO = "0x000000000000000000000000";

    public static final Event TRANSFER_EVENT = new Event("Transfer", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {},new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));

    private static final String TRANSFER_EVENT_HASH = EventEncoder.encode(TRANSFER_EVENT);

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

    public List<JSONObject> getStakingEvents() throws NullPointerException, ResponseStatusException, Exception {
        List<JSONObject> transferList = new ArrayList<>();
        String contractAddress = getContractAddress();
        Credentials credentials = getOwnerCredentials();
        StakingToken token = loadTokenContract(contractAddress, credentials);
        String targetAddress = credentials.getAddress();

        //Modifico la direccion para que pueda ser tratada por Web3j
        if (targetAddress.isEmpty()){
            throw new NullPointerException();
        }
        String targetAddressHex = targetAddress.replaceFirst("0x","");

        EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                getContractAddress());
        ethFilter.addOptionalTopics(null, PREFIX_ZERO + targetAddressHex);
        log.info("Getting Txs of: " + targetAddressHex);
        ethereumClient.ethLogFlowable(ethFilter).subscribe(event -> {
            JSONObject res = new JSONObject();
            String eventHash = event.getTopics().get(0);
            if(eventHash.equals(TRANSFER_EVENT_HASH)) {
                String sendAddress = event.getTopics().get(1);
                String receiveAddress = event.getTopics().get(2);
                sendAddress = sendAddress.replaceFirst(PREFIX_ZERO, "0x");
                receiveAddress = receiveAddress.replaceFirst(PREFIX_ZERO, "0x");
                String hex = event.getData().replace("x", "0");

                int dcml = Integer.parseInt(hex, 16);
                BigInteger value = BigInteger.valueOf(dcml);
                String balance = String.valueOf(value.doubleValue() / Math.pow(10, 100));

                EthBlock ethBlock = ethereumClient.ethGetBlockByNumber(DefaultBlockParameter.valueOf(event.getBlockNumber()), false).send();
                long timeStamp = ethBlock.getBlock().getTimestamp().longValue();
                Date date = new java.util.Date(timeStamp * 1000L);
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = sdf.format(date);

                res.put("Sender_Address", sendAddress);
                res.put("Receiver_Address", receiveAddress);
                res.put("Amount", balance);
                res.put("Date", formattedDate);
                transferList.add(res);
            }
        }, error ->{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
        return transferList;
    }
}
