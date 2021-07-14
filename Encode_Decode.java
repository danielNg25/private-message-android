

package aes;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;



public class Encode_Decode {
    
    
    public String encrypt(String strToEncrypt, String myKey, String name) {
      try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = myKey.getBytes("UTF-8");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(name.getBytes("UTF-8"))) + ":" + Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }
    public String[] decrypt(String str_nameToDecrypt, String myKey) {
      try {
    	  	String[] output = new String[2];
    	  	String parts[] = str_nameToDecrypt.split(":");
    	  	String name = parts[0];
    	  	String strToDecrypt = parts[1];
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = myKey.getBytes("UTF-8");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            output[1] = new String(cipher.doFinal(Base64.getDecoder().decode(name)));
            output[2] = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return output;
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }   
}