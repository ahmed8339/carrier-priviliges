package com.spectrum.cm.headless;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Copied from https://android.googlesource.com/platform/frameworks/base/+/master/telephony/java/android/telephony/UiccAccessRule.java
 */
public class UiccAccessRule {
    public static final String TAG = UiccAccessRule.class.getSimpleName();
    /**
     * Gets all of the Signatures from the given PackageInfo.
     * @hide
     */
    @NonNull
    public static List<Signature> getSignatures(PackageInfo packageInfo) {
        Signature[] signatures = packageInfo.signatures;
        SigningInfo signingInfo = packageInfo.signingInfo;
        if (signingInfo != null) {
            signatures = signingInfo.getSigningCertificateHistory();
            if (signingInfo.hasMultipleSigners()) {
                signatures = signingInfo.getApkContentsSigners();
            }
        }
        return (signatures == null) ? Collections.EMPTY_LIST : Arrays.asList(signatures);
    }
    /**
     * Converts a Signature into a Certificate hash usable for comparison.
     * @hide
     */
    public static byte[] getCertHash(Signature signature, String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            return md.digest(signature.toByteArray());
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, "NoSuchAlgorithmException: " + ex);
        }
        return null;
    }
}
