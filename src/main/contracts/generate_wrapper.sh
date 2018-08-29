#!/bin/bash

solc Transport.sol --bin --abi --optimize -o bin --overwrite
#solc Transport.sol --bin --abi --optimize -o ../../test/resources/contracts --overwrite
web3j solidity generate --javaTypes bin/Transport.bin bin/Transport.abi -p com.olam.node.sdk -o ../java/

