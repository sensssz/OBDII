package cn.edu.nju.software.obdii.util;

import android.content.Context;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Some utility methods
 */
public class Utilities {
    private static MessageDigest sMD5Digest;

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

    public static String MD5(String string) {
        if (sMD5Digest == null) {
            try {
                sMD5Digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        sMD5Digest.update(string.getBytes());
        return new String(sMD5Digest.digest());
    }

}
