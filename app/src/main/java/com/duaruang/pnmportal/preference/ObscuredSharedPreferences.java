package com.duaruang.pnmportal.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * http://stackoverflow.com/questions/785973/what-is-the-most-appropriate-way-to-store-user-settings-in-android-application/6393502#6393502
 * Warning, this gives a false sense of security.  If an attacker has enough access to
 * acquire your password store, then he almost certainly has enough access to acquire your
 * source binary and figure out your encryption key.  However, it will prevent casual
 * investigators from acquiring passwords, and thereby may prevent undesired negative
 * publicity.
 */
public class ObscuredSharedPreferences implements SharedPreferences {

    private static final String UTF8 = "UTF-8";
    protected SharedPreferences mSharedPreferences;
    protected Context mContext;

    /**
     * INSERT A RANDOM PASSWORD HERE.
     * Don't use anything you wouldn't want to
     * getString out there if someone decompiled
     * your app.
     */
    private char[] mSecretCode;

    public ObscuredSharedPreferences(Context context, SharedPreferences preferences, String secretCode) {
        this.mSharedPreferences = preferences;
        this.mContext = context;
        int keyLength = 16;
        if (secretCode.length() < keyLength) {
            int pad = keyLength - secretCode.length();
            for (int i = 0; i < pad; i++) {
                secretCode += "0";
            }
        } else if (secretCode.length() > keyLength) {
            secretCode = secretCode.substring(0, keyLength);
        }
        this.mSecretCode = secretCode.toCharArray();
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? decrypt(v) : defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? Integer.parseInt(decrypt(v)) : defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? Long.parseLong(decrypt(v)) : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? Float.parseFloat(decrypt(v)) : defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? Boolean.parseBoolean(decrypt(v)) : defValue;
    }

    @Override
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /*
    protected String encrypt(String value) {
        try {
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(mSecretCode));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected String decrypt(String value) {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(mSecretCode));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * Encrypts the text.
     * @param clearText The text you want to encrypt
     * @return Encrypted data if successful, or null if unsucessful
     */
    protected String encrypt(String clearText) {
        byte[] encryptedText = null;
        try {
            byte[] keyData = new String(mSecretCode).getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(clearText.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Decrypts the text
     * @param encryptedText The text you want to encrypt
     * @return Decrypted data if successful, or null if unsucessful
     */
    protected String decrypt (String encryptedText) {
        byte[] clearText = null;
        try {
            byte[] keyData = new String(mSecretCode).getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(clearText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public class Editor implements SharedPreferences.Editor {

        protected SharedPreferences.Editor mEditor;

        public Editor(){
            this.mEditor = ObscuredSharedPreferences.this.mSharedPreferences.edit();
        }

        @Override
        public Editor putString(String key, String value) {
            mEditor.putString(key, encrypt(value));
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            mEditor.putStringSet(key, values);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            mEditor.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            mEditor.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            mEditor.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            mEditor.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public Editor remove(String key) {
            mEditor.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        public void apply() {
            mEditor.apply();
        }
    }
}
