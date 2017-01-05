package com.changhong.oto.common.utils.crypt;

import java.io.UnsupportedEncodingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.changhong.cacsdk.api.MainApi;
import com.changhong.cacsdk.entity.CryptEntity;

public class MainApiUtils {
	
	private static final MainApi api = new MainApi();
	private static final BASE64Encoder BASE64ENCODER =new BASE64Encoder();
	private static final BASE64Decoder BASE64DECODER =new BASE64Decoder();
	
	//加密用到的key值
	private static final byte[] KEY = null;
	
	
	/**
	 * 
	 * @Description 返回字符串的加密字符串
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptString(String data) throws UnsupportedEncodingException{
		CryptEntity aesEncrypt = api.aesEncrypt(data.getBytes("UTF-8"), KEY);
		String cryptStr = BASE64ENCODER.encode(aesEncrypt.getParam());
		return cryptStr;
	}
	
	
	/**
	 * 
	 * @Description 返回data的json字符串加密字符串
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptJson(Object data) throws UnsupportedEncodingException{
		String  jsonStr = JSONObject.toJSONString(data);
		CryptEntity aesEncrypt = api.aesEncrypt(jsonStr.getBytes("UTF-8"), KEY);
		String cryptStr = BASE64ENCODER.encode(aesEncrypt.getParam());
		return cryptStr;
	}
	
}
