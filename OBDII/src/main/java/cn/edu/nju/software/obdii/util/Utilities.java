package cn.edu.nju.software.obdii.util;

import android.content.Context;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Some utility methods
 */
public class Utilities {
    private static MessageDigest sMD5Digest;
    private static MessageDigest mSha1Digest;

    public static void showMessage(Context context, int messageID) {
        if (context != null) {
            Toast.makeText(context, messageID, Toast.LENGTH_LONG).show();
        }
    }

    public static void showMessage(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static String md5(String string) {
        if (sMD5Digest == null) {
            try {
                sMD5Digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        sMD5Digest.update(string.getBytes());

        byte messageDigest[] = sMD5Digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }

    public static String sha1(String s) {
        if (mSha1Digest == null) {
            try {
                mSha1Digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        byte[] data = mSha1Digest.digest(s.getBytes());
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

}
