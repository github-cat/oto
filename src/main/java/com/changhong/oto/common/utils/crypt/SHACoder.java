package com.changhong.oto.common.utils.crypt;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import org.apache.commons.lang.StringUtils;

/**
 * 默认为SHA-256,推荐使用SHA-256及以上加密方式
 * @ClassName SHACoder
 * @Description TODO
 * @author 向阳
 * @Date 2017年1月11日 下午3:21:45
 * @version 1.0.0
 */
public class SHACoder {
	
	public static final String KEY_SHA = "SHA-256";
	
	public enum Type{
		HEX,BASE64
	}
	
	public static byte[] baseEncryptSHA(byte[] s)
			throws Exception {
		return baseEncryptSHA(s,null,KEY_SHA);
	}
	
	public static byte[] baseEncryptSHA(byte[] s,String encrypt)
			throws Exception {
		return baseEncryptSHA(s,null,encrypt);
	}
	
	public static String baseEncryptSHA(byte[] s,String encrypt,Type t)
			throws Exception {
		return baseEncryptSHA(s,null,encrypt,t);
	}
	
	public static byte[] baseEncryptSHA(byte[] s,byte[] salt,String encrypt)
			throws Exception {
		byte[] t = null;
		if(salt==null||salt.length==0){
			t = s;
		}else{
			ByteBuffer bf = ByteBuffer.allocate(s.length + salt.length);
			bf.put(s);
			bf.put(salt);
			t = bf.array();
		}
		MessageDigest sha = MessageDigest.getInstance(encrypt);
		sha.update(t);
		byte b[] = sha.digest();
		return b;
	}
	
	public static String baseEncryptSHA(byte[] s,byte[] salt,String encrypt,Type t)
			throws Exception {
		switch (t) {
		case BASE64:
			return BASE64Coder.encode((baseEncryptSHA(s,salt,encrypt)));
		default:
			return CryptUtils.toHex(baseEncryptSHA(s,salt,encrypt));
		}
	}
	
	public static String encryptSHA(String s)
			throws Exception {
		return encryptSHA(s,null,KEY_SHA,CryptUtils.DEFAULT_CHARSET,Type.HEX);
	}
	
	public static String encryptSHA(String s,String salt)
			throws Exception {
		return encryptSHA(s,salt,KEY_SHA,CryptUtils.DEFAULT_CHARSET,Type.HEX);
	}
	
	
	public static String encryptSHA(String s,String salt,String encrypt)
			throws Exception {
		return encryptSHA(s,salt,encrypt,CryptUtils.DEFAULT_CHARSET,Type.HEX);
	}
	
	public static String encryptSHA(String s,String salt,String encrypt,String charset)
			throws Exception {
		return encryptSHA(s,salt,encrypt,charset,Type.HEX);
	}
	
	public static String encryptSHAWithEncrypt(String s,String encrypt)
			throws Exception {
		return encryptSHA(s,null,KEY_SHA,encrypt,Type.HEX);
	}
	
	public static String encryptSHAWithCharset(String s,String charset)
			throws Exception {
		return encryptSHA(s,null,KEY_SHA,charset,Type.HEX);
	}
	
	public static String encryptSHAWithCharset(String s,String salt,String charset)
			throws Exception {
		return encryptSHA(s,salt,KEY_SHA,charset,Type.HEX);
	}
	
	public static String encryptSHAWithType(String s,Type type)
			throws Exception {
		return encryptSHA(s,null,KEY_SHA,CryptUtils.DEFAULT_CHARSET,type);
	}
	
	public static String encryptSHAWithType(String s,String salt,Type type)
			throws Exception {
		return encryptSHA(s,salt,KEY_SHA,CryptUtils.DEFAULT_CHARSET,type);
	}
		
	public static String encryptSHA(String s,String salt,String encrypt,String charset,Type type)
			throws Exception {
		if(StringUtils.isEmpty(charset)){
			charset = CryptUtils.DEFAULT_CHARSET;
		}
		byte[] t = s.getBytes(charset);
		byte[] st = null;
		if(!StringUtils.isEmpty(salt)){
			st = salt.getBytes(charset);
		}
		if(StringUtils.isEmpty(encrypt)){
			encrypt = KEY_SHA;
		}
		if(type==null){
			type = Type.HEX;
		}
		return baseEncryptSHA(t,st,encrypt,type);
	}
}
