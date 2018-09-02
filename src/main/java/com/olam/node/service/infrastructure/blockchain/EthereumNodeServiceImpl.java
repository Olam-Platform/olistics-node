package com.olam.node.service.infrastructure.blockchain;

import com.olam.node.service.infrastructure.Transport;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EthereumNodeServiceImpl extends OfflineEthereumServiceImpl implements EthereumNodeService {
    private final int WAIT_TX_INTERVAL = 10000;     // in milliseconds
    private final int RINKEBY_AVERAGE_TX_TIME = 15000;
    private final int WAIT_TX_MAX_TRIES = 10;
    public final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    public final String MESSAGE = "RULE THE OLAM";

    protected Admin ethAdmin;

    public EthereumNodeServiceImpl(String rpcUrl) {
        super(rpcUrl);
        ethAdmin = Admin.build(new HttpService(RPC_URL));
    }

    @Override
    public Transport deployTransportContract(Credentials credentials, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception {
        return Transport.deploy(web3j, credentials, gasPrice, gasLimit, shipperAddress, receiverAddress, BigInteger.valueOf(msecSinceEpoc)).send();
    }

    @Override
    public Transport loadTransportContract(Credentials credentials, String contractAddress) {
        return Transport.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Override
    public void submitDocument(Credentials credentials, String contractAddress, String docName, String docUrl) {

    }

    @Override
    public Tuple2<String, byte[]> requestDocument(String contractAddress, String docName) {
        return null;
    }

    @Override
    public List<String> getAccounts() throws IOException {
        return ethAdmin.ethAccounts().send().getAccounts();
    }

    @Override
    public String createAccount(String password) throws IOException {
        return ethAdmin.personalNewAccount(password).send().getAccountId();
    }

    @Override
    public void sendEther(Credentials credentials, String recipient, float sum) throws Exception {
        Transfer.sendFunds(web3j, credentials, recipient, BigDecimal.valueOf(sum), Convert.Unit.ETHER).send();
    }

    @Override
    public float getEtherBalance(String accountAddress) throws IOException {
        EthGetBalance ethBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).send();

        return (0.000000000000000001f * (ethBalance.getBalance().floatValue()));
    }

    @Override
    public String sendDeployTx(String signedTx) {
        return sendTx(signedTx).get().getContractAddress();
    }

    @Override
    public void sendSubmitDocTx(String signedTx) {
        sendTx(signedTx);
    }

//    @Override
//    public boolean checkWritePermission(String signature, String shipmentId) {
//
//        BigInteger publicKey = getPublicKey(signature);
//
//        Transport shipment = Transport.load(shipmentId, this.web3j, credentials, gasPrice, gasLimit);
//        String role = null;
//        try {
//            role = shipment.getRole(publicKey.toString()).send();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return role.equals("Manager");
//    }

//    @Override
//    public boolean checkWritePermission(Sign.SignatureData signature, String shipmentId) {
//
//        BigInteger publicKey = Sign.recoverFromSignature(1,
//                new ECDSASignature(new BigInteger(1, signature.getR()), new BigInteger(1, signature.getS()))
//                , MESSAGE.getBytes());
//
//        Transport shipment = Transport.load(shipmentId, this.web3j, credentials, gasPrice, gasLimit);
//        String role = null;
//        try {
//            role = shipment.getRole(publicKey.toString()).send();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return role.equals("Manager");
//    }


    private Optional<TransactionReceipt> sendTx(String tx) {
        Optional<TransactionReceipt> result = null;
        EthGetTransactionReceipt receipt;

        try {
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(tx).send();
            Thread.sleep(RINKEBY_AVERAGE_TX_TIME);

            int nRetries = 0;
            String transactionHash;

            do {
                transactionHash = ethSendTransaction.getTransactionHash();

                if (transactionHash != null) {
                    receipt = web3j.ethGetTransactionReceipt(transactionHash).send();
                    if (receipt.getTransactionReceipt().isPresent()) {
                        result = receipt.getTransactionReceipt();
                    }
                }

                if (result == null) {
                    Thread.sleep(WAIT_TX_INTERVAL);
                }

                nRetries++;
            } while ((nRetries < WAIT_TX_MAX_TRIES) && (result == null));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*******PRIVATE UTILITY METHODS *****************/

    private Sign.SignatureData getSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        return sd;
    }

    private byte[] getMsgHash() {
        String prefix = PERSONAL_MESSAGE_PREFIX + MESSAGE.length();
        byte[] msgHash = Hash.sha3((prefix + MESSAGE).getBytes());
        return msgHash;
    }

    private BigInteger getPublicKey(String signature) {

        Sign.SignatureData sd = getSignatureData(signature);
        byte[] msgHash = getMsgHash();

        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                return publicKey;
            }
        }
        return null;
    }

}
