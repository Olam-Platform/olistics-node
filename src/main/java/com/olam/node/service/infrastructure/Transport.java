package com.olam.node.service.infrastructure;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class Transport extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b50604051606080620020db833981016040818152825160208085015194830151838501845260078086527f6d616e6167657200000000000000000000000000000000000000000000000000928601928352935192959490936000933393600493928291908083835b602083106200009a5780518252601f19909201916020918201910162000079565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260078085527f73686970706572000000000000000000000000000000000000000000000000009185019182529451899560049594509092508291908083835b602083106200014a5780518252601f19909201916020918201910162000129565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260088085527f72656365697665720000000000000000000000000000000000000000000000009185019182529451889560049594509092508291908083835b60208310620001fa5780518252601f199092019160209182019101620001d9565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260078085527f6d616e6167657200000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620002ab5780518252601f1990920191602091820191016200028a565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001810188556000978852968390209096018054600160a060020a031916600160a060020a0390971696909617909555838501855260078085527f7368697070657200000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620003725780518252601f19909201916020918201910162000351565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001810188556000978852968390209096018054600160a060020a031916600160a060020a0390971696909617909555838501855260088085527f7265636569766572000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620004395780518252601f19909201916020918201910162000418565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001818101895560009889528489209091018054600160a060020a031916600160a060020a039093169290921790915533875260068352818720868301909252600786527f6d616e616765720000000000000000000000000000000000000000000000000086840190815282549182018084559288529290962094519095620004f995019350909150620006ae565b5050600160a060020a03841660009081526006602090815260408083208151808301909252600782527f736869707065720000000000000000000000000000000000000000000000000082840190815281546001810180845592865293909420915190936200056e93929092019190620006ae565b5050600160a060020a03831660009081526006602090815260408083208151808301909252600882527f72656365697665720000000000000000000000000000000000000000000000008284019081528154600181018084559286529390942091519093620005e393929092019190620006ae565b505060008290556040805180820190915260048082527f6e6f6e650000000000000000000000000000000000000000000000000000000060209092019182526200063091600191620006ae565b50600090505b600554811015620006a45760058054829081106200065057fe5b60009182526020918290200154604080518581529051600160a060020a03909216927fbed15b7c69aa5546e99fb1197813242aa21b51bed4cb241057730f2b15e6e5e492918290030190a260010162000636565b5050505062000753565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620006f157805160ff191683800117855562000721565b8280016001018555821562000721579182015b828111156200072157825182559160200191906001019062000704565b506200072f92915062000733565b5090565b6200075091905b808211156200072f57600081556001016200073a565b90565b61197880620007636000396000f30060806040526004361061008d5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166317cd349f8114610092578063442767331461018c578063631aa385146102225780636735b3a7146102375780636a0bd35a1461029d57806397430b48146102f8578063bf40fac11461041a578063d9038f3c1461048f575b600080fd5b34801561009e57600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100eb9436949293602493928401919081908401838280828437509497506105269650505050505050565b604080516020808201879052600160a060020a03861692820192909252606081018490526080810183905260a080825287519082015286519091829160c083019189019080838360005b8381101561014d578181015183820152602001610135565b50505050905090810190601f16801561017a5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b34801561019857600080fd5b506101ad600160a060020a03600435166106cd565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101e75781810151838201526020016101cf565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561022e57600080fd5b506101ad6107e9565b34801561024357600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261029b94369492936024939284019190819084018382808284375094975050509235600160a060020a031693506108d692505050565b005b3480156102a957600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100eb9436949293602493928401919081908401838280828437509497505093359450610aea9350505050565b34801561030457600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261040894369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375050604080516020808901358a01803580830284810184018652818552999c8b359c909b909a950198509296508101945090925082919085019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750949750610f8a9650505050505050565b60408051918252519081900360200190f35b34801561042657600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526104739436949293602493928401919081908401838280828437509497506114d39650505050505050565b60408051600160a060020a039092168252519081900360200190f35b34801561049b57600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261029b94369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506115449650505050505050565b606060008060008060006105393361176f565b151561058b576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6003876040518082805190602001908083835b602083106105bd5780518252601f19909201916020918201910161059e565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205460ff16151591506106469050576040805160e560020a62461bcd02815260206004820152601a60248201527f756e6b6e6f776e20646f63756d656e7420726571756573746564000000000000604482015290519081900360640190fd5b60016002886040518082805190602001908083835b6020831061067a5780518252601f19909201916020918201910161065b565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220549290920392506106ba915088905082610aea565b939b929a50909850965090945092505050565b60606106d83361176f565b151561072a576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b600160a060020a03821660009081526006602052604081208054909190811061074f57fe5b600091825260209182902001805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156107dd5780601f106107b2576101008083540402835291602001916107dd565b820191906000526020600020905b8154815290600101906020018083116107c057829003601f168201915b50505050509050919050565b60606107f43361176f565b1515610846576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156108cb5780601f106108a0576101008083540402835291602001916108cb565b820191906000526020600020905b8154815290600101906020018083116108ae57829003601f168201915b505050505090505b90565b60408051808201825260078082527f6d616e616765720000000000000000000000000000000000000000000000000060208301908152925160049390918291908083835b602083106109395780518252601f19909201916020918201910161091a565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316331491506109ed9050576040805160e560020a62461bcd028152602060048201526024808201527f6f6e6c79206d616e6167657220697320616c6c6f77656420746f20616464207260448201527f6f6c657300000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b806004836040518082805190602001908083835b60208310610a205780518252601f199092019160209182019101610a01565b51815160209384036101000a600019018019909216911617905292019485525060408051948590038201909420805473ffffffffffffffffffffffffffffffffffffffff19908116600160a060020a03978816179091556005805460018181019092557f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db00180549092169688169687179091556000958652600682529385208054948501808255908652948190208751610ae49591909101935090870191506117f5565b50505050565b6060600080600080600080610afe3361176f565b1515610b50576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6003896040518082805190602001908083835b60208310610b825780518252601f199092019160209182019101610b63565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205460ff1615159150610c0b9050576040805160e560020a62461bcd02815260206004820152601a60248201527f756e6b6e6f776e20646f63756d656e7420726571756573746564000000000000604482015290519081900360640190fd5b6002896040518082805190602001908083835b60208310610c3d5780518252601f199092019160209182019101610c1e565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220548a109150610cc39050576040805160e560020a62461bcd02815260206004820181905260248201527f696c6c6567616c2076657273696f6e206e756d62657220726571756573746564604482015290519081900360640190fd5b6002896040518082805190602001908083835b60208310610cf55780518252601f199092019160209182019101610cd6565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508a91508110610d3057fe5b906000526020600020906005020160010160009054906101000a9004600160a060020a031691506002896040518082805190602001908083835b60208310610d895780518252601f199092019160209182019101610d6a565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508a91508110610dc457fe5b90600052602060002090600502016002015490506002896040518082805190602001908083835b60208310610e0a5780518252601f199092019160209182019101610deb565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508a91508110610e4557fe5b906000526020600020906005020160000188838360028d6040518082805190602001908083835b60208310610e8b5780518252601f199092019160209182019101610e6c565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508e91508110610ec657fe5b6000918252602080832033845260046005909302019190910181526040918290205486548351601f60026000196101006001861615020190931692909204918201849004840281018401909452808452909291879190830182828015610f6d5780601f10610f4257610100808354040283529160200191610f6d565b820191906000526020600020905b815481529060010190602001808311610f5057829003601f168201915b505050505094509650965096509650965050509295509295909350565b6000806000610f983361176f565b1515610fea576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6002886040518082805190602001908083835b6020831061101c5780518252601f199092019160209182019101610ffd565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185206080860182528c865233868401528582018c9052815160008082528185019093526060870152805460018101808355918352918390208651805192979650600590930201935061109b92849201906117f5565b5060208281015160018301805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0390921691909117905560408301516002830155606083015180516110f19260038501920190611873565b5050505060016002896040518082805190602001908083835b602083106111295780518252601f19909201916020918201910161110a565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420548d5195900396506001946003948e9450925082918401908083835b6020831061118f5780518252601f199092019160209182019101611170565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220805460ff19169315159390931790925550505b84518110156114c8576111f685828151811015156111e757fe5b9060200190602002015161176f565b1561145f576002886040518082805190602001908083835b6020831061122d5780518252601f19909201916020918201910161120e565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092208054909250849150811061126857fe5b9060005260206000209060050201600301858281518110151561128757fe5b602090810291909101810151825460018101845560009384529190922001805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0390921691909117905583518490829081106112dd57fe5b906020019060200201516002896040518082805190602001908083835b602083106113195780518252601f1990920191602091820191016112fa565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092208054909250859150811061135457fe5b90600052602060002090600502016004016000878481518110151561137557fe5b6020908102909101810151600160a060020a031682528101919091526040016000205584518590829081106113a657fe5b90602001906020020151600160a060020a03167ffdd757dccbe6344e394b0ceedad793a09de9a8bb4b81ecece33d4c08d6afc7cd89846040518080602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561141f578181015183820152602001611407565b50505050905090810190601f16801561144c5780820380516001836020036101000a031916815260200191505b50935050505060405180910390a26114c0565b604080516020808252601a908201527f646f63756d656e7420726563697069656e7420756e6b6e6f776e0000000000008183015290517f50703eead77dcbda81f935040112d241747f270515a28ca4b86d34b0f30e68419181900360600190a15b6001016111cd565b509695505050505050565b60006004826040518082805190602001908083835b602083106115075780518252601f1990920191602091820191016114e8565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b600061154f3361176f565b15156115a1576040805160e560020a62461bcd028152602060048201526022602482015260008051602061192d833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b5060005b60055481101561175c5760058054829081106115bd57fe5b6000918252602091829020015460408051606080825260018054600260001982841615610100020190911604918301829052600160a060020a03909416947fbc4ef7dd9495f7559ca71bee14700cae76c853f9f8aa42c9f4e872104ffded1b9493899389939092839290830191908301906080840190889080156116825780601f1061165757610100808354040283529160200191611682565b820191906000526020600020905b81548152906001019060200180831161166557829003601f168201915b5050848103835286518152865160209182019188019080838360005b838110156116b657818101518382015260200161169e565b50505050905090810190601f1680156116e35780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b838110156117165781810151838201526020016116fe565b50505050905090810190601f1680156117435780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a26001016115a5565b8251610ae49060019060208601906117f5565b600080805b600160a060020a0384166000908152600660205260409020548110156117ee57600160a060020a03841660009081526006602052604081208054839081106117b857fe5b9060005260206000200180546001816001161561010002031660029004905011156117e657600191506117ee565b600101611774565b5092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061183657805160ff1916838001178555611863565b82800160010185558215611863579182015b82811115611863578251825591602001919060010190611848565b5061186f9291506118e1565b5090565b8280548282559060005260206000209081019282156118d5579160200282015b828111156118d5578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909116178255602090920191600190910190611893565b5061186f9291506118fb565b6108d391905b8082111561186f57600081556001016118e7565b6108d391905b8082111561186f57805473ffffffffffffffffffffffffffffffffffffffff191681556001016119015600726f6c65206973206e6f742070617274206f662074686973207472616e73706fa165627a7a72305820e2ded74c37d0b69f521d58fdaed30e67c054860014195ca15701265ea6cb4ba10029";

    public static final String FUNC_REQUESTDOCUMENT = "requestDocument";

    public static final String FUNC_GETROLE = "getRole";

    public static final String FUNC_GETSTATE = "GetState";

    public static final String FUNC_ADDROLE = "addRole";

    public static final String FUNC_SUBMITDOCUMENT = "submitDocument";

    public static final String FUNC_GETADDRESS = "getAddress";

    public static final String FUNC_SETSTATE = "setState";

    public static final Event TRANSPORTSTARTED_EVENT = new Event("TransportStarted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event DOCUMENTSUBMITTED_EVENT = new Event("DocumentSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event DOCUMENTREQUESTED_EVENT = new Event("DocumentRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event STATECHANGED_EVENT = new Event("StateChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event WARN_EVENT = new Event("Warn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    protected Transport(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Transport(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple5<String, BigInteger, String, BigInteger, byte[]>> requestDocument(String name) {
        final Function function = new Function(FUNC_REQUESTDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple5<String, BigInteger, String, BigInteger, byte[]>>(
                new Callable<Tuple5<String, BigInteger, String, BigInteger, byte[]>>() {
                    @Override
                    public Tuple5<String, BigInteger, String, BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, BigInteger, String, BigInteger, byte[]>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (byte[]) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<String> getRole(String roleAddress) {
        final Function function = new Function(FUNC_GETROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(roleAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> addRole(String role, String roleAddress) {
        final Function function = new Function(
                FUNC_ADDROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(role), 
                new org.web3j.abi.datatypes.Address(roleAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple5<String, BigInteger, String, BigInteger, byte[]>> requestDocument(String name, BigInteger version) {
        final Function function = new Function(FUNC_REQUESTDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.generated.Uint256(version)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple5<String, BigInteger, String, BigInteger, byte[]>>(
                new Callable<Tuple5<String, BigInteger, String, BigInteger, byte[]>>() {
                    @Override
                    public Tuple5<String, BigInteger, String, BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, BigInteger, String, BigInteger, byte[]>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (byte[]) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> submitDocument(String name, String url, BigInteger timeStamp, List<String> recipients, List<byte[]> keys) {
        final Function function = new Function(
                FUNC_SUBMITDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(url), 
                new org.web3j.abi.datatypes.generated.Uint256(timeStamp), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(keys, org.web3j.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getAddress(String role) {
        final Function function = new Function(FUNC_GETADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(role)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setState(String newState, String cause) {
        final Function function = new Function(
                FUNC_SETSTATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(newState), 
                new org.web3j.abi.datatypes.Utf8String(cause)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Transport> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String shipperAddress, String receiverAddress, BigInteger timeStamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(shipperAddress), 
                new org.web3j.abi.datatypes.Address(receiverAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(timeStamp)));
        return deployRemoteCall(Transport.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Transport> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String shipperAddress, String receiverAddress, BigInteger timeStamp) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(shipperAddress), 
                new org.web3j.abi.datatypes.Address(receiverAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(timeStamp)));
        return deployRemoteCall(Transport.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<TransportStartedEventResponse> getTransportStartedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSPORTSTARTED_EVENT, transactionReceipt);
        ArrayList<TransportStartedEventResponse> responses = new ArrayList<TransportStartedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransportStartedEventResponse typedResponse = new TransportStartedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.timeStamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransportStartedEventResponse> transportStartedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransportStartedEventResponse>() {
            @Override
            public TransportStartedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSPORTSTARTED_EVENT, log);
                TransportStartedEventResponse typedResponse = new TransportStartedEventResponse();
                typedResponse.log = log;
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.timeStamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TransportStartedEventResponse> transportStartedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSPORTSTARTED_EVENT));
        return transportStartedEventObservable(filter);
    }

    public List<DocumentSubmittedEventResponse> getDocumentSubmittedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DOCUMENTSUBMITTED_EVENT, transactionReceipt);
        ArrayList<DocumentSubmittedEventResponse> responses = new ArrayList<DocumentSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DocumentSubmittedEventResponse typedResponse = new DocumentSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DocumentSubmittedEventResponse> documentSubmittedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, DocumentSubmittedEventResponse>() {
            @Override
            public DocumentSubmittedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DOCUMENTSUBMITTED_EVENT, log);
                DocumentSubmittedEventResponse typedResponse = new DocumentSubmittedEventResponse();
                typedResponse.log = log;
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<DocumentSubmittedEventResponse> documentSubmittedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DOCUMENTSUBMITTED_EVENT));
        return documentSubmittedEventObservable(filter);
    }

    public List<DocumentRequestedEventResponse> getDocumentRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DOCUMENTREQUESTED_EVENT, transactionReceipt);
        ArrayList<DocumentRequestedEventResponse> responses = new ArrayList<DocumentRequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DocumentRequestedEventResponse typedResponse = new DocumentRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.submitter = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DocumentRequestedEventResponse> documentRequestedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, DocumentRequestedEventResponse>() {
            @Override
            public DocumentRequestedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DOCUMENTREQUESTED_EVENT, log);
                DocumentRequestedEventResponse typedResponse = new DocumentRequestedEventResponse();
                typedResponse.log = log;
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.submitter = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<DocumentRequestedEventResponse> documentRequestedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DOCUMENTREQUESTED_EVENT));
        return documentRequestedEventObservable(filter);
    }

    public List<StateChangedEventResponse> getStateChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STATECHANGED_EVENT, transactionReceipt);
        ArrayList<StateChangedEventResponse> responses = new ArrayList<StateChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StateChangedEventResponse typedResponse = new StateChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.previousState = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.currentState = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.cause = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<StateChangedEventResponse> stateChangedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, StateChangedEventResponse>() {
            @Override
            public StateChangedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STATECHANGED_EVENT, log);
                StateChangedEventResponse typedResponse = new StateChangedEventResponse();
                typedResponse.log = log;
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.previousState = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.currentState = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.cause = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<StateChangedEventResponse> stateChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STATECHANGED_EVENT));
        return stateChangedEventObservable(filter);
    }

    public List<WarnEventResponse> getWarnEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WARN_EVENT, transactionReceipt);
        ArrayList<WarnEventResponse> responses = new ArrayList<WarnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WarnEventResponse typedResponse = new WarnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<WarnEventResponse> warnEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, WarnEventResponse>() {
            @Override
            public WarnEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(WARN_EVENT, log);
                WarnEventResponse typedResponse = new WarnEventResponse();
                typedResponse.log = log;
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<WarnEventResponse> warnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WARN_EVENT));
        return warnEventObservable(filter);
    }

    public static Transport load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transport(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Transport load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transport(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransportStartedEventResponse {
        public Log log;

        public String recipient;

        public BigInteger timeStamp;
    }

    public static class DocumentSubmittedEventResponse {
        public Log log;

        public String recipient;

        public String name;

        public BigInteger version;
    }

    public static class DocumentRequestedEventResponse {
        public Log log;

        public String name;

        public BigInteger version;

        public String submitter;
    }

    public static class StateChangedEventResponse {
        public Log log;

        public String recipient;

        public String previousState;

        public String currentState;

        public String cause;
    }

    public static class WarnEventResponse {
        public Log log;

        public String message;
    }
}
