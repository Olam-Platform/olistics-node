package com.olam.node.service.infrastructure;

import com.olam.node.sdk.IPFSCluster;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IPFSDataStorageService implements DataStorageService {

    @Autowired
    private IPFS ipfs;

    @Autowired
    private IPFSCluster cluster;

    @Override
    public String getdataIdentifier(byte[] data) {
        return save(data, true);
    }

    @Override
    public String save(byte[] file) {
        return save(file,false);
    }



    private String save(byte[] data, boolean hashOnly) {
        String hash = null;

        /*
        try {
            NamedStreamable.ByteArrayWrapper streamable = new NamedStreamable.ByteArrayWrapper(data);
            MerkleNode node = ipfs.add(streamable,false,hashOnly).get(0);

            cluster.pins.add(hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        return hash;
    }


    public Resource loadDataAsResource(String hash) {
        byte[] dataContents = null;
        Multihash dataPointer = Multihash.fromBase58(hash);
        Resource resource = null;

        try {
            dataContents = ipfs.cat(dataPointer);
            resource = new ByteArrayResource(dataContents);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

}
