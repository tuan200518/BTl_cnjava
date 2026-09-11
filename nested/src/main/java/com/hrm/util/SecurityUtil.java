package com.hrm.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class SecurityUtil {
    private static final SecureRandom RNG = new SecureRandom();
    private SecurityUtil() {}
    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[16]; RNG.nextBytes(salt);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, 256);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return "PBKDF2$120000$" + b64(salt) + "$" + b64(hash);
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
    public static boolean verifyPassword(String password, String encoded) {
        try {
            String[] p = encoded.split("\\$");
            if (p.length != 4 || !p[0].equals("PBKDF2")) return false;
            int iter = Integer.parseInt(p[1]); byte[] salt = Base64.getDecoder().decode(p[2]);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iter, 256);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(hash, Base64.getDecoder().decode(p[3]));
        } catch (Exception e) { return false; }
    }
    public static String encrypt(String plain, String secret) {
        try {
            byte[] iv = new byte[12]; RNG.nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key(secret), new GCMParameterSpec(128, iv));
            byte[] out = c.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            byte[] all = new byte[iv.length + out.length]; System.arraycopy(iv,0,all,0,iv.length); System.arraycopy(out,0,all,iv.length,out.length);
            return Base64.getEncoder().encodeToString(all);
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
    public static String decrypt(String encoded, String secret) {
        try {
            byte[] all = Base64.getDecoder().decode(encoded); byte[] iv = java.util.Arrays.copyOfRange(all,0,12); byte[] ct = java.util.Arrays.copyOfRange(all,12,all.length);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding"); c.init(Cipher.DECRYPT_MODE, key(secret), new GCMParameterSpec(128,iv));
            return new String(c.doFinal(ct), StandardCharsets.UTF_8);
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
    private static SecretKeySpec key(String secret) throws Exception { byte[] h = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8)); return new SecretKeySpec(h,"AES"); }
    private static String b64(byte[] b){return Base64.getEncoder().encodeToString(b);}
}
