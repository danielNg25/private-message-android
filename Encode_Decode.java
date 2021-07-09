import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encode_Decode {
	
	private static String TRANS_MODE = "Blowfish";
    private static String BLOWFISH_KEY = "BLOWFISH_KEY";
	
    public Encode_Decode() {
    	super();
    }
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public String encrypt(String password) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
    	SecretKeySpec keySpec = new SecretKeySpec(BLOWFISH_KEY.getBytes("Windows-31J"),TRANS_MODE);
    	Cipher cipher;
        cipher = Cipher.getInstance(TRANS_MODE);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] passByte;
        passByte = cipher.doFinal(password.getBytes("Windows-31J"));

        return new String(bytesToHex(passByte));
    }
    
}

