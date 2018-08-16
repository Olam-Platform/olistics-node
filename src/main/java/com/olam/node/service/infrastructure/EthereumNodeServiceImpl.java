package com.olam.node.service.infrastructure;

import com.olam.node.sdk.Transport;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class EthereumNodeServiceImpl implements EthereumNodeService {

    protected Web3j web3j = null;
    protected Admin ethAdmin = null;
    protected BigInteger gasPrice = BigInteger.valueOf(22000000000L);
    protected BigInteger gasLimit = BigInteger.valueOf(4300000);
    protected final String RPC_URL;

    public EthereumNodeServiceImpl(String rpcUrl) {
        this.RPC_URL = rpcUrl;
        this.web3j = Web3j.build(new HttpService(RPC_URL));
        this.ethAdmin = Admin.build(new HttpService(RPC_URL));
    }

    @Override
    public Transport DeployTransportContract(Credentials credentials, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception {
        assert (web3j != null);
        assert (ethAdmin != null);

        return Transport.deploy(web3j, credentials, gasPrice, gasLimit, shipperAddress, receiverAddress, BigInteger.valueOf(msecSinceEpoc)).send();
    }

    @Override
    public Transport LoadTransportContract(Credentials credentials, String contractAddress) {
        assert (web3j != null);

        return Transport.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Override
    public String CreateAccount(String password) throws IOException {
        assert (ethAdmin != null);

        NewAccountIdentifier ethAccount = ethAdmin.personalNewAccount(password).send();

        return ethAccount.getAccountId();
    }

    @Override
    public List<String> GetAccounts() throws IOException {
        assert (ethAdmin != null);

        return ethAdmin.ethAccounts().send().getAccounts();
    }

    @Override
    public Credentials LoadCredentials(File keystoreFile, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, keystoreFile);
    }

    @Override
    public void SendEther(Credentials credentials, String recipient, float sum) throws Exception {
        Transfer.sendFunds(web3j, credentials, recipient, BigDecimal.valueOf(sum), Convert.Unit.ETHER).send();
    }

    @Override
    public float GetEtherBalance(String accountAddress) throws IOException {
        assert (web3j != null);
        EthGetBalance ethBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).send();

        return (0.000000000000000001f * (ethBalance.getBalance().floatValue()));
    }

    @Override
    public String relaySignedTransaction(String signedTransaction) {
        String contractAddress = null;
        EthGetTransactionReceipt receipt;

        try {

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).send();
            String transactionHash = ethSendTransaction.getTransactionHash();

            if (transactionHash != null) {
                receipt = web3j.ethGetTransactionReceipt(transactionHash).send();
                if (receipt.getTransactionReceipt().isPresent()) {
                    contractAddress = receipt.getTransactionReceipt().get().getContractAddress();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contractAddress;
    }
}
