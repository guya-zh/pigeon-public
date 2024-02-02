package cn.guyasc.pigeon.data.encryption.handler.base;

import cn.guyasc.pigeon.core.util.ObjectUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 加解密工具
 *
 * @author guya
 * @since 2024/1/25 15:29
 */
public class EncryptUtil {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String SHA256 = "SHA-256";
    public static final String HMAC_MD5 = "HmacMD5";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String AES = "AES";
    /**
     * 加密方式
     */
    private static final String KEY_CHARSET = "ASCII";

    /**
     * 编码方式
     */
    private static final String CHARSET = "UTF-8";

    /**
     * AES：加密算法
     * ECB：加密模式
     * PKCS5Padding：填充方式
     */
    private static final String ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * AES：加密算法
     * cbc：加密模式
     * PKCS5Padding：填充方式
     */
    private static final String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encByAesCbc(String content, String key, String offset) {
        if (ObjectUtil.isEmpty(content)) {
            return content;
        }
        try {
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(KEY_CHARSET), AES);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decByAesCbc(String content, String key, String offset) {
        if (ObjectUtil.isEmpty(content)) {
            return content;
        }
        try {
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(KEY_CHARSET), AES);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encByAesEcb(String content, String key) {
        if (ObjectUtil.isEmpty(content)) {
            return content;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(KEY_CHARSET), AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decByAesEcb(String content, String key) {
        if (ObjectUtil.isEmpty(content)) {
            return content;
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(KEY_CHARSET), AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encByBase64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    public static String decByBase64(String content) {
        return new String(Base64.getDecoder().decode(content.getBytes()));
    }

    public static String parseByte2HexStr(byte[] buf) {
        if (buf == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String encByMd5(String res, boolean toLowerCase) {
        String byte2HexStr = parseByte2HexStr(messageDigest(res, MD5));
        if (toLowerCase && byte2HexStr != null) {
            return byte2HexStr.toLowerCase();
        }
        return byte2HexStr;
    }


    public static String encByMd5(String str, String salt) {
        return parseByte2HexStr(keyGeneratorMac(str, HMAC_MD5, salt));
    }

    public static String encBySha1(String str, boolean toLowerCase) {
        String byte2HexStr = parseByte2HexStr(messageDigest(str, SHA1));
        if (toLowerCase && byte2HexStr != null) {
            return byte2HexStr.toLowerCase();
        }
        return byte2HexStr;
    }

    public static String encBySha1(String str) {
        return parseByte2HexStr(messageDigest(str, SHA1));
    }

    public static String encBySha256(String str) {
        return parseByte2HexStr(messageDigest(str, SHA256));
    }

    public static String encBySha256(String str, boolean toLowerCase) {
        String byte2HexStr = parseByte2HexStr(messageDigest(str, SHA256));
        if (toLowerCase && byte2HexStr != null) {
            return byte2HexStr.toLowerCase();
        }
        return byte2HexStr;
    }

    public static String encBySha1(String str, String key) {
        return parseByte2HexStr(keyGeneratorMac(str, HMAC_SHA1, key));
    }


    private static byte[] messageDigest(String str, String algorithm) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = str.getBytes(CHARSET);
            return md.digest(resBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] keyGeneratorMac(String str, String algorithm, String key) {
        if (str == null) {
            return null;
        }
        try {
            SecretKey sk;
            if (key == null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            } else {
                byte[] keyBytes = key.getBytes(CHARSET);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            return mac.doFinal(str.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}