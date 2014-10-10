package edu.purdue.ugl;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;



public class EncryptHelper {
	
	private String password;
	private String SHApass;
	
	private String Key = "2IwehG2VEm3WhjLRMK/1aUPqAdW7KNvvRuskedxuOgOQ2jbO+wkKs5p5qJwh98GM";
	
	
	public EncryptHelper(){
		
		
	}
	
	public static String convertToHex(byte[] data) throws java.io.IOException {
		
		StringBuffer sb = new StringBuffer();
		String hex = null;
		
		hex = Base64.encodeToString(data,0,data.length,0);
		
		sb.append(hex);
	
        return sb.toString();
		
	}
	
	public final String encodeBase64(String hash){
		
		byte[] byteArray;
		byteArray = hash.getBytes();

		String base64 = Base64.encodeToString(byteArray, 2); 
		return base64.toString();
		
	}
		
	
	
	public String computeSHAHash(String pass){
		
		MessageDigest mdSha1 = null;
		
		try{
			mdSha1 = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		mdSha1.reset();
		byte[] data = mdSha1.digest(pass.getBytes());
		String sha1 = String.format("%0" + (data.length*2)+"x", new BigInteger(1,data));
		return sha1;
		
		
	}
	
	public final String computeMD5Hash(final String pass){
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(pass.getBytes());
			byte messageDigest[] = digest.digest();
	
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return "";		
		
	}
	
	public final String twoWayEncrypt(String str) {
		SecretKeySpec skeySpec = new SecretKeySpec(Key.getBytes(), "AES");
		
		
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(str.getBytes());
			
			String encrypttext = encodeBase64(encrypted.toString());
			
			return encrypttext;			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "";
				
		
	}
	
	public final String twoWayDecrypt(String str){
		SecretKeySpec skeySpec = new SecretKeySpec(Key.getBytes(), "AES");
		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			
			byte[] encrypted = str.getBytes();
			byte[] decrypt = cipher.doFinal(encrypted);
			
			return decrypt.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";			
	}
	
	
	

}
