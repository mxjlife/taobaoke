package com.mxjlife.taobaoke.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * 类说明: 加密解密方法
 * 
 * @author mxj
 * @email xj.meng@sinowaycredit.com
 * @version 创建时间：2017年7月28日 上午10:57:40
 */
@Slf4j
public class SecurityUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 对byte 数组进行 base 64 编码
	 * 
	 * @param bytes 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * 对字符串进行 base 64 编码
	 * 
	 * @param source 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(String source) {
		return source == null ? null : base64Encode(source.getBytes());
	}

	/**
	 * 将base64编码的字符串解为真正的字符串
	 * 
	 * @param base64Code base64编码串
	 * @return
	 */
	public static String base64ToString(String base64Code) {
		String result = null;
		if (base64Code != null && base64Code.trim().length() > 0) {
			try {
				result = new String(base64Decode(base64Code), DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				log.error("base64串转字符串失败!!!", e);
			}
		}
		return result;
	}

	/**
	 * 解码 base 64 字符串   返回byte[]数组
	 * 
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 */
    public static byte[] base64Decode(String base64Code) {
		byte[] result = null;
		try {
			result = base64Code == null ? null : Base64.decodeBase64(base64Code);
		} catch (Exception e) {
			log.error("base64串\"" + base64Code + "\"解码失败!!!", e);
		}
		return result;
	}

	/**
	 * AES加密 为base64字符串
	 * 
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[] 使用base64加密后返回
	 * @throws Exception
	 */
	public static String AESEncrypt(String content, String encryptKey) {
		if (content == null) {
			return null;
		}
		String result = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// 在Linux系统环境下， 使用 kgen.init(128, new
			// SecureRandom(encryptKey.getBytes()));会导致每次加密的结果不一致
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encryptKey.getBytes());
			kgen.init(128, secureRandom);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
			byte[] doFinal = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
			result = base64Encode(doFinal);
		} catch (Exception e) {
			log.error("AES加密失败!!!", e);
		}
		return result;
	}

	/**
	 * 对byte[] 使用AES解密
	 * 
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String AESDecryptByByte(byte[] encryptBytes, String decryptKey) {
		String result = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(decryptKey.getBytes());
			kgen.init(128, secureRandom);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
			byte[] decryptBytes = cipher.doFinal(encryptBytes);
			result = new String(decryptBytes);
		} catch (Exception e) {
			log.error("AES解密失败!!!", e);
		}
		return result;
	}

	/**
	 * 将base64字符串使用 AES解密
	 * 
	 * @param content 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String AESDecrypt(String content, String decryptKey) {
		return content == null ? null : AESDecryptByByte(base64Decode(content), decryptKey);
	}

	/**
	 * HMACSHA256加密调用的入口
	 * 
	 * @param data
	 * @return
	 */
	public static String HMACSHA256Encrypt(String data, String secret) {
		byte[] key = secret == null ? null : secret.getBytes();
		if (data == null || "".equals(data.trim())) {
			return null;
		} else {
			return HMAC(data.getBytes(), key, "HmacSHA256");
		}
	}

	public static String HMACMD5Encrypt(String data, String secret) {
		byte[] key = secret == null ? null : secret.getBytes();
		if (data == null || "".equals(data.trim())) {
			return null;
		} else {
			return HMAC(data.getBytes(), key, "HmacMD5");
		}
	}
	
	/**
	 * 使用HMAC对数据进行散列加密
	 * 
	 * @param data 需要进行加密的数据
	 * @param key secret
	 * @param encryptType 加密类型
	 * @return
	 */
	private static String HMAC(byte[] data, byte[] key, String encryptType) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, encryptType);
			Mac mac = Mac.getInstance(encryptType);
			mac.init(signingKey);
			byte[] doFinal = mac.doFinal(data);
			return bytesToHex(doFinal);
		} catch (Exception e) {
			log.error(encryptType + "对数据进行散列加密失败!!!", e);
		}
		return null;
	}

	/**
     * 传入文本内容，返回 SHA-256 串
     * 
     * @param strText
     * @return
     */

    public static String SHA256Encrypt(String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     * 
     * @param strText
     * @return
     */
    public static String SHA512Encrypt(String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * 字符串 SHA 散列加密
     * 
     * @param strText
     * @return
     */
    private static String SHA(String strText, String strType) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并传入加密类型
                MessageDigest digest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                digest.update(strText.getBytes());
                // 得到 byte 类型结果
                byte[] byteBuffer = digest.digest();
                // 转换为十六进制返回结果
                strResult = bytesToHex(byteBuffer);
            } catch (NoSuchAlgorithmException e) {
                log.error(strType + " 加密失败!!!", e);
            }
        }
        return strResult;
    }
    
    /**
     * 将字节数组转化为16进制
     * 一个字节占8位, 一个十六进制数占4位, 一个字节就是2个十六进制数
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes){  
        final String HEX = "0123456789abcdef";  
        StringBuilder sb = new StringBuilder();  
        for (byte b : bytes){  
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt((b >> 4) & 0x0f));  
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt(b & 0x0f));  
        }  
        return sb.toString();  
    }  
    
    /**
     * 调用第三方库进行md5加密
     * @param data
     * @return
     */
    public static String MD5(String data){
        return DigestUtils.md5Hex(data);
    }
    
    /**
     * jdk中的md5加密
     * @param source
     * @return
     */
    public static String JDKMD5(String source) {  
        String result = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            byte[] array = md.digest(source.getBytes(DEFAULT_CHARSET));
            result = bytesToHex(array);
        } catch (Exception e) {
            log.error("MD5加密失败", e);
        }  
        return result;  
    }  
    

	/* ---------------------------------------  RSA 加解密开始  ------------------------------------------  */
	/**
	 * 得到公钥
	 * @param publicKey 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		return key;
	}

	/**
	 * 得到私钥
	 * @param privateKey 密钥字符串（经过base64编码）
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {
		//通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		return key;
	}


	/**
	 * RSA公钥加密
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String rsaPublicEncrypt(String data, RSAPublicKey publicKey){
		try{
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(DEFAULT_CHARSET), publicKey.getModulus().bitLength()));
		}catch(Exception e){
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA私钥解密
	 * @param data
	 * @param privateKeyStr
	 * @return
	 */

	public static String rsaPrivateDecrypt(String data, String privateKeyStr){
		try{

			RSAPrivateKey privateKey = getPrivateKey(privateKeyStr);
			return rsaPrivateDecrypt(data, privateKey);
		}catch(Exception e){
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA私钥解密
	 * @param data
	 * @param privateKey
	 * @return
	 */

	public static String rsaPrivateDecrypt(String data, RSAPrivateKey privateKey){
		try{
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), DEFAULT_CHARSET);
		}catch(Exception e){
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
		int maxBlock = 0;
		if(opmode == Cipher.DECRYPT_MODE){
			maxBlock = keySize / 8;
		}else{
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try{
			while(datas.length > offSet){
				if(datas.length-offSet > maxBlock){
					buff = cipher.doFinal(datas, offSet, maxBlock);
				}else{
					buff = cipher.doFinal(datas, offSet, datas.length-offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		}catch(Exception e){
			throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultDatas;
	}

	/**
	 * RSA公钥加密
	 * @param data
	 * @param publicKeyStr
	 * @return
	 */
	public static String rsaPublicEncrypt(String data, String publicKeyStr){
		try{
			RSAPublicKey publicKey = getPublicKey(publicKeyStr);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(DEFAULT_CHARSET), publicKey.getModulus().bitLength()));
		}catch(Exception e){
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	/* ---------------------------------------  RSA 加解密结束  ------------------------------------------  */
}
