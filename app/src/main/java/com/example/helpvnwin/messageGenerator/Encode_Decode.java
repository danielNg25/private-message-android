package com.example.helpvnwin.messageGenerator;

import java.security.MessageDigest;
import java.util.Arrays;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;



public class Encode_Decode {
    
    
    public static String encrypt(String strToEncrypt, String myKey, String[] list_name) {
      try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = myKey.getBytes("UTF-8");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String temp;
            temp = Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.DEFAULT);
            for (int i = list_name.length - 1; i >= 0; i--) {
                 temp = temp + ":" + Base64.encodeToString(cipher.doFinal(list_name[i].getBytes("UTF-8")), Base64.DEFAULT);
            }
            return temp;
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }
    public static String[] decrypt(String str_nameToDecrypt, String myKey) {
      try {

    	  	String parts[] = str_nameToDecrypt.split(":");
            int len = parts.length;
            String outputs[] = new String[2];
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = myKey.getBytes("UTF-8");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            outputs[0] = new String(cipher.doFinal(Base64.decode(parts[0],Base64.DEFAULT) ));
            if (len > 2) {
                 outputs[1] = new String(cipher.doFinal(Base64.decode(parts[len-2],Base64.DEFAULT)));
            }
            else {
                outputs[1] = new String("end");
            }
          return outputs;
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }   
}