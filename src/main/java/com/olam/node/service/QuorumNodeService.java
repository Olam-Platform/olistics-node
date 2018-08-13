/*
package com.olam.node.service;

import com.olam.node.sdk.Transport;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.List;


public class QuorumNodeService extends EthereumNodeService implements IQuorumNodeService {
    public QuorumNodeService(String rpcURL) {
        super(rpcURL);

        gasPrice = BigInteger.ZERO;
    }

    @Override
    public boolean Build() {
        web3j = Quorum.build(new HttpService(RPC_URL));
        ethAdmin = Admin.build(new HttpService(RPC_URL));

        return true;
    }

    @Override
    public Transport DeployTransportContract(
            List<String> privyFor, String accountAddress, String password, String shipperAddress, String receiverAddress, long msecSinceEpoc
    ) throws Exception {
        assert(web3j != null);
        assert (ethAdmin != null);

        Transport result = null;

        PersonalUnlockAccount personalUnlockAccount = ethAdmin.personalUnlockAccount(accountAddress, password).send();

        if (personalUnlockAccount.accountUnlocked()) {
            ClientTransactionManager transactionManager = new ClientTransactionManager(web3j, accountAddress, privyFor);

            result = Transport.deploy(web3j, transactionManager, gasPrice, gasLimit, shipperAddress, receiverAddress, BigInteger.valueOf(msecSinceEpoc)).send();
        }

        return result;
    }
}
*/