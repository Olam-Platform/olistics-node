package com.olam.node.service.infrastructure;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class EthereumNodeServiceImpl extends OfflineEthereumServiceImpl implements EthereumNodeService {
    private final int WAIT_TX_INTERVAL        = 10000;     // in milliseconds
    private final int RINKEBY_AVERAGE_TX_TIME = 15000;
    private final int WAIT_TX_MAX_TRIES       =    10;

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

                if(result == null) {
                    Thread.sleep(WAIT_TX_INTERVAL);
                }

                nRetries++;
            } while ((nRetries < WAIT_TX_MAX_TRIES) && (result == null));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
