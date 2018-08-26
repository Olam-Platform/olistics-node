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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
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
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405160608062002045833981016040818152825160208085015194830151838501845260078086527f6d616e6167657200000000000000000000000000000000000000000000000000928601928352935192959490936000933393600493928291908083835b602083106200009a5780518252601f19909201916020918201910162000079565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260078085527f73686970706572000000000000000000000000000000000000000000000000009185019182529451899560049594509092508291908083835b602083106200014a5780518252601f19909201916020918201910162000129565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260088085527f72656365697665720000000000000000000000000000000000000000000000009185019182529451889560049594509092508291908083835b60208310620001fa5780518252601f199092019160209182019101620001d9565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054600160a060020a031916600160a060020a039790971696909617909555838501855260078085527f6d616e6167657200000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620002ab5780518252601f1990920191602091820191016200028a565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001810188556000978852968390209096018054600160a060020a031916600160a060020a0390971696909617909555838501855260078085527f7368697070657200000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620003725780518252601f19909201916020918201910162000351565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001810188556000978852968390209096018054600160a060020a031916600160a060020a0390971696909617909555838501855260088085527f7265636569766572000000000000000000000000000000000000000000000000918501918252945160059560049594509092508291908083835b60208310620004395780518252601f19909201916020918201910162000418565b51815160001960209485036101000a019081169019919091161790529201948552506040805194859003820185205486546001818101895560009889528489209091018054600160a060020a031916600160a060020a039093169290921790915533875260068352818720868301909252600786527f6d616e616765720000000000000000000000000000000000000000000000000086840190815282549182018084559288529290962094519095620004f995019350909150620006ae565b5050600160a060020a03841660009081526006602090815260408083208151808301909252600782527f736869707065720000000000000000000000000000000000000000000000000082840190815281546001810180845592865293909420915190936200056e93929092019190620006ae565b5050600160a060020a03831660009081526006602090815260408083208151808301909252600882527f72656365697665720000000000000000000000000000000000000000000000008284019081528154600181018084559286529390942091519093620005e393929092019190620006ae565b505060008290556040805180820190915260048082527f6e6f6e650000000000000000000000000000000000000000000000000000000060209092019182526200063091600191620006ae565b50600090505b600554811015620006a45760058054829081106200065057fe5b60009182526020918290200154604080518581529051600160a060020a03909216927fbed15b7c69aa5546e99fb1197813242aa21b51bed4cb241057730f2b15e6e5e492918290030190a260010162000636565b5050505062000753565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620006f157805160ff191683800117855562000721565b8280016001018555821562000721579182015b828111156200072157825182559160200191906001019062000704565b506200072f92915062000733565b5090565b6200075091905b808211156200072f57600081556001016200073a565b90565b6118e280620007636000396000f30060806040526004361061008d5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166317cd349f81146100925780631865c57d1461018a57806344276733146102145780636735b3a7146102355780636a0bd35a1461029b57806397430b48146102f6578063bf40fac114610418578063d9038f3c1461048d575b600080fd5b34801561009e57600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100eb9436949293602493928401919081908401838280828437509497506105249650505050505050565b604051808060200185815260200184600160a060020a0316600160a060020a03168152602001838152602001828103825286818151815260200191508051906020019080838360005b8381101561014c578181015183820152602001610134565b50505050905090810190601f1680156101795780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561019657600080fd5b5061019f6106c6565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101d95781810151838201526020016101c1565b50505050905090810190601f1680156102065780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561022057600080fd5b5061019f600160a060020a03600435166107b3565b34801561024157600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261029994369492936024939284019190819084018382808284375094975050509235600160a060020a031693506108cf92505050565b005b3480156102a757600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100eb9436949293602493928401919081908401838280828437509497505093359450610ae39350505050565b34801561030257600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261040694369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375050604080516020808901358a01803580830284810184018652818552999c8b359c909b909a950198509296508101945090925082919085019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750949750610ef49650505050505050565b60408051918252519081900360200190f35b34801561042457600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261047194369492936024939284019190819084018382808284375094975061143d9650505050505050565b60408051600160a060020a039092168252519081900360200190f35b34801561049957600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261029994369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506114ae9650505050505050565b6060600080600080610535336116d9565b1515610587576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6003866040518082805190602001908083835b602083106105b95780518252601f19909201916020918201910161059a565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205460ff16151591506106429050576040805160e560020a62461bcd02815260206004820152601a60248201527f756e6b6e6f776e20646f63756d656e7420726571756573746564000000000000604482015290519081900360640190fd5b60016002876040518082805190602001908083835b602083106106765780518252601f199092019160209182019101610657565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220549290920392506106b6915087905082610ae3565b9450945094509450509193509193565b60606106d1336116d9565b1515610723576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107a85780601f1061077d576101008083540402835291602001916107a8565b820191906000526020600020905b81548152906001019060200180831161078b57829003601f168201915b505050505090505b90565b60606107be336116d9565b1515610810576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b600160a060020a03821660009081526006602052604081208054909190811061083557fe5b600091825260209182902001805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156108c35780601f10610898576101008083540402835291602001916108c3565b820191906000526020600020905b8154815290600101906020018083116108a657829003601f168201915b50505050509050919050565b60408051808201825260078082527f6d616e616765720000000000000000000000000000000000000000000000000060208301908152925160049390918291908083835b602083106109325780518252601f199092019160209182019101610913565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316331491506109e69050576040805160e560020a62461bcd028152602060048201526024808201527f6f6e6c79206d616e6167657220697320616c6c6f77656420746f20616464207260448201527f6f6c657300000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b806004836040518082805190602001908083835b60208310610a195780518252601f1990920191602091820191016109fa565b51815160209384036101000a600019018019909216911617905292019485525060408051948590038201909420805473ffffffffffffffffffffffffffffffffffffffff19908116600160a060020a03978816179091556005805460018181019092557f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db00180549092169688169687179091556000958652600682529385208054948501808255908652948190208751610add95919091019350908701915061175f565b50505050565b60606000806000806000610af6336116d9565b1515610b48576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6003886040518082805190602001908083835b60208310610b7a5780518252601f199092019160209182019101610b5b565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205460ff1615159150610c039050576040805160e560020a62461bcd02815260206004820152601a60248201527f756e6b6e6f776e20646f63756d656e7420726571756573746564000000000000604482015290519081900360640190fd5b6002886040518082805190602001908083835b60208310610c355780518252601f199092019160209182019101610c16565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205489109150610cbb9050576040805160e560020a62461bcd02815260206004820181905260248201527f696c6c6567616c2076657273696f6e206e756d62657220726571756573746564604482015290519081900360640190fd5b6002886040518082805190602001908083835b60208310610ced5780518252601f199092019160209182019101610cce565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508991508110610d2857fe5b906000526020600020906005020160010160009054906101000a9004600160a060020a031691506002886040518082805190602001908083835b60208310610d815780518252601f199092019160209182019101610d62565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508991508110610dbc57fe5b90600052602060002090600502016002015490506002886040518082805190602001908083835b60208310610e025780518252601f199092019160209182019101610de3565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922080549092508991508110610e3d57fe5b6000918252602091829020600590910201805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815291928a9286928692869190830182828015610eda5780601f10610eaf57610100808354040283529160200191610eda565b820191906000526020600020905b815481529060010190602001808311610ebd57829003601f168201915b505050505093509550955095509550505092959194509250565b6000806000610f02336116d9565b1515610f54576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b6002886040518082805190602001908083835b60208310610f865780518252601f199092019160209182019101610f67565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185206080860182528c865233868401528582018c90528151600080825281850190935260608701528054600181018083559183529183902086518051929796506005909302019350611005928492019061175f565b5060208281015160018301805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909216919091179055604083015160028301556060830151805161105b92600385019201906117dd565b5050505060016002896040518082805190602001908083835b602083106110935780518252601f199092019160209182019101611074565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420548d5195900396506001946003948e9450925082918401908083835b602083106110f95780518252601f1990920191602091820191016110da565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220805460ff19169315159390931790925550505b845181101561143257611160858281518110151561115157fe5b906020019060200201516116d9565b156113c9576002886040518082805190602001908083835b602083106111975780518252601f199092019160209182019101611178565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220805490925084915081106111d257fe5b906000526020600020906005020160030185828151811015156111f157fe5b602090810291909101810151825460018101845560009384529190922001805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909216919091179055835184908290811061124757fe5b906020019060200201516002896040518082805190602001908083835b602083106112835780518252601f199092019160209182019101611264565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220805490925085915081106112be57fe5b9060005260206000209060050201600401600087848151811015156112df57fe5b6020908102909101810151600160a060020a0316825281019190915260400160002055845185908290811061131057fe5b90602001906020020151600160a060020a03167ffdd757dccbe6344e394b0ceedad793a09de9a8bb4b81ecece33d4c08d6afc7cd89846040518080602001838152602001828103825284818151815260200191508051906020019080838360005b83811015611389578181015183820152602001611371565b50505050905090810190601f1680156113b65780820380516001836020036101000a031916815260200191505b50935050505060405180910390a261142a565b604080516020808252601a908201527f646f63756d656e7420726563697069656e7420756e6b6e6f776e0000000000008183015290517f50703eead77dcbda81f935040112d241747f270515a28ca4b86d34b0f30e68419181900360600190a15b600101611137565b509695505050505050565b60006004826040518082805190602001908083835b602083106114715780518252601f199092019160209182019101611452565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b60006114b9336116d9565b151561150b576040805160e560020a62461bcd0281526020600482015260226024820152600080516020611897833981519152604482015260f260020a611c9d02606482015290519081900360840190fd5b5060005b6005548110156116c657600580548290811061152757fe5b6000918252602091829020015460408051606080825260018054600260001982841615610100020190911604918301829052600160a060020a03909416947fbc4ef7dd9495f7559ca71bee14700cae76c853f9f8aa42c9f4e872104ffded1b9493899389939092839290830191908301906080840190889080156115ec5780601f106115c1576101008083540402835291602001916115ec565b820191906000526020600020905b8154815290600101906020018083116115cf57829003601f168201915b5050848103835286518152865160209182019188019080838360005b83811015611620578181015183820152602001611608565b50505050905090810190601f16801561164d5780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b83811015611680578181015183820152602001611668565b50505050905090810190601f1680156116ad5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a260010161150f565b8251610add90600190602086019061175f565b600080805b600160a060020a03841660009081526006602052604090205481101561175857600160a060020a038416600090815260066020526040812080548390811061172257fe5b9060005260206000200180546001816001161561010002031660029004905011156117505760019150611758565b6001016116de565b5092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106117a057805160ff19168380011785556117cd565b828001600101855582156117cd579182015b828111156117cd5782518255916020019190600101906117b2565b506117d992915061184b565b5090565b82805482825590600052602060002090810192821561183f579160200282015b8281111561183f578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039091161782556020909201916001909101906117fd565b506117d9929150611865565b6107b091905b808211156117d95760008155600101611851565b6107b091905b808211156117d957805473ffffffffffffffffffffffffffffffffffffffff1916815560010161186b5600726f6c65206973206e6f742070617274206f662074686973207472616e73706fa165627a7a72305820c87d90f4738d966d8f9f3a15e6e5c9c15de7f305879f9d3175fb2d210eb418090029";

    public static final String FUNC_REQUESTDOCUMENT = "requestDocument";

    public static final String FUNC_GETSTATE = "getState";

    public static final String FUNC_GETROLE = "getRole";

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

    public RemoteCall<Tuple4<String, BigInteger, String, BigInteger>> requestDocument(String name) {
        final Function function = new Function(FUNC_REQUESTDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<String, BigInteger, String, BigInteger>>(
                new Callable<Tuple4<String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple4<String, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, BigInteger, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> getState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getRole(String roleAddress) {
        final Function function = new Function(FUNC_GETROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(roleAddress)), 
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

    public RemoteCall<Tuple4<String, BigInteger, String, BigInteger>> requestDocument(String name, BigInteger version) {
        final Function function = new Function(FUNC_REQUESTDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.generated.Uint256(version)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<String, BigInteger, String, BigInteger>>(
                new Callable<Tuple4<String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple4<String, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, BigInteger, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
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
