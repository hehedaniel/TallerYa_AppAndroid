package com.danielhermoso.cliente.encriptador;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureUtils {

    // Para pasar de un salt a un string (para enviarlo)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes)
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

        return sb.toString();

    }

    public static String hashearPass(String pass) {
        String passHasheada = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] byteOfTextToHash = pass.getBytes(StandardCharsets.UTF_8);

            byte[] hashedByteArray = md.digest(byteOfTextToHash);

            passHasheada = bytesToHex(hashedByteArray);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return passHasheada;
    }

}