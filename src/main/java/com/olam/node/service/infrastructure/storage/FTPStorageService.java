package com.olam.node.service.infrastructure.storage;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;


public class FTPStorageService implements IStorageService {
    @Override
    public String getDataIdentifier(byte[] data) {
        throw new NotImplementedException();
    }

    @Override
    public String save(byte[] data) {
        throw new NotImplementedException();
    }

    @Override
    public void saveTo(byte[] data, String url) throws Exception {
        URI uri = new URI(url);
        FTPClient ftpClient = new FTPClient();

        if (ftpConnect(ftpClient, uri)) {
            InputStream iStream = new ByteArrayInputStream(data);

            ftpClient.storeFile(uri.getPath(), iStream);
        }
    }

    @Override
    public byte[] read(String url) throws Exception {
        URI uri = new URI(url);
        FTPClient ftpClient = new FTPClient();
        byte[] fileBytes = null;


        if (ftpConnect(ftpClient, uri)) {
            OutputStream oStream = new ByteArrayOutputStream();
            boolean b = ftpClient.retrieveFile(uri.getPath(), oStream);

            fileBytes = ((ByteArrayOutputStream) oStream).toByteArray();
        }

        return fileBytes;
    }

    private boolean ftpConnect(FTPClient ftpClient, URI uri) throws Exception {
        String host = uri.getHost();
        String[] userInfo = uri.getUserInfo().split(":");
        String user = userInfo[0];
        String password = userInfo[1];

        ftpClient.connect(host);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }

        return ftpClient.login(user, password);
    }
}
