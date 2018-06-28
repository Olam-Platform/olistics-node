package com.olam.node.service;

import com.olam.node.sdk.IPFSCluster;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class IPFSFileStorageService implements FileStorageService {

    @Autowired
    private IPFS ipfs;

//    IPFSCluster ipfsCluster = new IPFSCluster("127.0.0.1", 9094);
//    IPFSCluster cluster = new IPFSCluster("127.0.0.1", 9094);

    @Override
    public String save(MultipartFile file) {

        String hash = null;
        try {
            NamedStreamable.ByteArrayWrapper streamable = new NamedStreamable.ByteArrayWrapper(file.getBytes());
            MerkleNode node = ipfs.add(streamable).get(0);
            hash = node.hash.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }


    public Resource loadFileAsResource(String hash) {
        byte[] fileContents = null;
        Multihash filePointer = Multihash.fromBase58(hash);

        try {
            fileContents = ipfs.cat(filePointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Resource resource = new ByteArrayResource(fileContents);

        return resource;
    }


}
