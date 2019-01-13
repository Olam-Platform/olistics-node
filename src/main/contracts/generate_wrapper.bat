solc ShipmentContract.sol --bin --abi --optimize -o bin --overwrite
web3j solidity generate --javaTypes bin\ShipmentContract.bin bin\ShipmentContract.abi -p com.olam.node.service.infrastructure.blockchain -o ..\java\

