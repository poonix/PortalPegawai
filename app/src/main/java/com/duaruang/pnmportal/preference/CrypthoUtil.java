package com.duaruang.pnmportal.preference;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by indri on 1/30/18.
 */

public class CrypthoUtil {

    private static final String TAG = CrypthoUtil.class.getSimpleName();

    private static final String KEY = "l0r3m1psum";
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    private static Object LOCK = new Object();

    private static CrypthoUtil sInstance;

    private Context mContext;

    public static CrypthoUtil getInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new CrypthoUtil(context);
            }
        }
        return sInstance;
    }

    private CrypthoUtil(Context context) {
        this.mContext = context;
    }

    public String decrypt(String cipher) {
        Key key;
        String decryptedResult;
        try {
            key = generateKey(KEY);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decode(cipher, Base64.NO_WRAP);
            decryptedResult = new String(c.doFinal(decordedValue), "UTF-8");
            Log.d(TAG, "decrypt : " + decryptedResult);
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal : " + e.getMessage());
        }
        return decryptedResult;
    }

    public String encrpt(String plain) {
        String enc = "";
        Cipher cipher;
        byte[] ency;
        Key key;
        try {
            key = generateKey(KEY);
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ency = cipher.doFinal(plain.getBytes());
            enc = Base64.encodeToString(ency, Base64.NO_WRAP);
            Log.d(TAG, "encrpt : " + enc);
        } catch (Exception ex) {
            throw new IllegalArgumentException("illegal : " + ex.getMessage());
        }
        return enc;
    }

    private static Key generateKey(String key) throws Exception {
        byte[] sKey = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sKey = sha.digest(sKey);
        sKey = Arrays.copyOf(sKey, 16);
        Key key1 = new SecretKeySpec(sKey, ALGORITHM);
        return key1;
    }

    /**
     * Generate secureId based on Settings.Secure.ANDROID_ID
     *
     * @param context
     * @return
     */
    public static String getSecureId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

