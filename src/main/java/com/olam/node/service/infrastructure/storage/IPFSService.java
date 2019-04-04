package com.olam.node.service.infrastructure.storage;

import com.olam.node.sdk.IPFSCluster;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotSupportedException;
import java.io.IOException;

@Service
public class IPFSService implements IStorageService {
    @Autowired
    private IPFS        ipfs;

    @Autowired
    private IPFSCluster ipfsCluster;

    public IPFSService(IPFS ipfs) {
        this.ipfs = ipfs;
    }

    @Override
    public String getDataIdentifier(byte[] data) {
        return save(data, true);
    }

    @Override
    public void saveTo(byte[] data, String url) {
        throw new NotSupportedException();
    }

    @Override
    public String save(byte[] file) {
        return save(file, false);
    }

    private String save(byte[] data, boolean hashOnly) {
        String hash = null;

        try {
            NamedStreamable.ByteArrayWrapper streamable = new NamedStreamable.ByteArrayWrapper(data);
            MerkleNode addResult = ipfs.add(streamable, false, hashOnly).get(0);
            hash = addResult.hash.toString();

            //add support in ipfs cluster
            //cluster.pins.add(hash);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hash;
    }

    public byte[] read(String url) {
        byte[] dataContents = null;
        Multihash dataPointer = Multihash.fromBase58(url);

        try {
            dataContents = ipfs.cat(dataPointer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataContents;
    }
}
