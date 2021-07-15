



import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;



public class Encode_Decode {
    
    
    public String encrypt(String strToEncrypt, String myKey, String[] list_name) {
      try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = myKey.getBytes("UTF-8");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String temp;
            temp = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            for (int i = list_name.length - 1; i >= 0; i--) {
            	temp = temp + ":" + Base64.getEncoder().encodeToString(cipher.doFinal(list_name[i].getBytes("UTF-8")));
            }
            return temp;
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }
    public String[] decrypt(String str_nameToDecrypt, String myKey) {
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
            if (len > 2) {
            	outputs[1] = new String(cipher.doFinal(Base64.getDecoder().decode(parts[len-2])));
            	outputs[0] = "";
            }
            else {
            	outputs[0] = new String(cipher.doFinal(Base64.getDecoder().decode(parts[0])));
            	outputs[1] = new String("end");
            }
            return outputs;
      } catch (Exception e) {
            System.out.println(e.toString());
      }
      return null;
    }
/*    
    public String[] output(String str_nameToDecrypt, String myKey) {
    	try {
    		String parts[] = str_nameToDecrypt.split(":");
    		int len = parts.length;
    		String[] outputs = new String[2];
    		while (len > 1) {
    			String temp = null;
    			for (int i = 0;i < len;i++) {
    				temp = temp + ":" + parts[i];
    			}
    			outputs = this.decrypt(temp, myKey);
    			len--;
    			System.out.println(outputs[0]);
    			return outputs;
    		}
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
		return null;
    }
*/ 
    public static void main(String[] args) {
      String secretKey = "keykey";
      String originalString = "This is a plain message";
      String[] list_name = {"Alice","f1","f2","Bob"};
        
      Encode_Decode testAES = new Encode_Decode();
      String encryptedString = testAES.encrypt(originalString, secretKey, list_name);
      System.out.println("Encrypt: " + encryptedString);
      String[] parts = encryptedString.split(":");
      int len = parts.length;
      while (len > 1) {
			String temp = parts[0];
			for (int i = 1; i < len; i++) {
				temp = temp + ":" + parts[i];
			}
			String[] outputs = testAES.decrypt(temp, secretKey);
			len--;
			System.out.println(outputs[1]);
		}
    }
}