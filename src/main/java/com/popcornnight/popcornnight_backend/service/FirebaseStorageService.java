package com.popcornnight.popcornnight_backend.service;

import com.google.firebase.cloud.StorageClient;

import io.jsonwebtoken.io.IOException;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Service;

@Service
public class FirebaseStorageService {

    public String uploadQRCode(byte[] qrImageBytes, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        // You can give a path like "qr-codes/booking123.png"
        Blob blob = bucket.create("qr-codes/" + fileName, new ByteArrayInputStream(qrImageBytes), "image/png");

        // Make it publicly accessible (optional)
        blob.createAcl(com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(),
                com.google.cloud.storage.Acl.Role.READER));

        return "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();
    }
}
