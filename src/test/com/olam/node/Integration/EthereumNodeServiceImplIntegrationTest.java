//package com.olam.node.service.infrastructure;
//
//
//import com.olam.node.service.infrastructure.blockchain.EthereumNodeServiceImpl;
//import com.olam.node.utils.Web3jUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.ResourceUtils;
//import org.web3j.crypto.*;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.methods.response.EthSendTransaction;
//import org.web3j.protocol.core.methods.response.TransactionReceipt;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.utils.Numeric;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//
//@RunWith(SpringRunner.class)
//@TestPropertySource(
//        locations = "classpath:application-test.properties")
//public class EthereumNodeServiceImplIntegrationTest {
//
//
//    private static final String WALLET_PASSWORD = "";
//    private final BigInteger GAS_LIMIT = BigInteger.valueOf(5000000);
//    private final BigInteger GAS_PRICE = BigInteger.valueOf(4);
//    @Autowired
//    public Environment environment;
//
//    private EthereumNodeServiceImpl service;
//    private Web3j web3j;
//    private Credentials credentials;
//    private File transportBIN;
//
//    private Web3jUtils utils;
//
//    @Before
//    public void setup() throws IOException, CipherException {
//
//
//        String url = environment.getProperty("rpc.url.rinkeby");
//        service = new EthereumNodeServiceImpl(url);
//        utils = new Web3jUtils(url);
//
//        //setup for creating a raw transaction
//        web3j = Web3j.build(new HttpService(url));
//        transportBIN = ResourceUtils.getFile("classpath:contracts/Transport.bin");
//        File keystorefile = ResourceUtils.getFile("classpath:accounts/keystore_file01.json");
//        credentials = WalletUtils.loadCredentials(WALLET_PASSWORD, keystorefile);
//
//
//    }
//
////    @Test
//    public void relaySignedTransaction() throws Exception {
//
//        BigInteger nonce = utils.getNonce(credentials.getAddress());
//
//        RawTransaction rawTrx = RawTransaction.createContractTransaction(
//                nonce,
//                GAS_PRICE,
//                GAS_LIMIT,
//                BigInteger.ZERO,
//                new String(Files.readAllBytes(Paths.get(transportBIN.getPath()))));
//
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTrx, credentials);
//        String message = "olam";
//        String hexValue = Numeric.toHexString(signedMessage);
//        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//        String transactionHash = ethSendTransaction.getTransactionHash();
//        TransactionReceipt transactionReceipt = utils.waitForTransactionReceipt(transactionHash);
//        transactionReceipt.getContractAddress();
//
//        Sign.SignatureData signatureData = Sign.signMessage(message.getBytes(), credentials.getEcKeyPair());
//        System.out.println("sig data: " + signatureData.toString());
//        BigInteger key = Sign.signedMessageToKey(message.getBytes(), signatureData);
//    }
//
//
//}