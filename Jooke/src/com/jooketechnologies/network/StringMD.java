package com.jooketechnologies.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringMD
{
  public static String MD2 = "MD2";
  public static String MD5 = "MD5";
  public static String SHA1 = "SHA-1";
  public static String SHA256 = "SHA-256";
  public static String SHA384 = "SHA-384";
  public static String SHA512 = "SHA-512";
  
  public static String getStringMessageDigest(String paramString1, String paramString2)
  {
	    byte[] buffer = paramString1.getBytes();
	    byte[] hash;
	    try{
	    	MessageDigest md = MessageDigest.getInstance(paramString2);
		    md.reset();
		    md.update(buffer);
		    hash=md.digest();
	    }
	    catch(NoSuchAlgorithmException exception){
	    	hash = null;
	    }
	    return toHexadecimal(hash);
  }
  
  public static String md5(String paramString)
  {
    return getStringMessageDigest(paramString, MD5);
  }
  
  public static String sha1(String paramString)
  {
    return getStringMessageDigest(paramString, SHA1);
  }
  
  private static String toHexadecimal(byte[] paramArrayOfByte)
  {
    String str = "";
    int i = paramArrayOfByte.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return str;
      }
      int k = 0xFF & paramArrayOfByte[j];
      if (Integer.toHexString(k).length() == 1) {
        str = str + "0";
      }
      str = str + Integer.toHexString(k);
    }
  }
}
