package com.spectrum.cm.headless

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.service.carrier.CarrierIdentifier
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.spectrum.cm.headless.UiccAccessRule.getCertHash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        findViewById<TextView>(R.id.cp).text = "${telephonyManager.hasCarrierPrivileges()}"
        findViewById<TextView>(R.id.model).text = Build.MODEL
        findViewById<TextView>(R.id.build).text = Build.DISPLAY

        val sig = findViewById<TextView>(R.id.signature)
        val packageInfo = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNING_CERTIFICATES
        )
        val signatureList = UiccAccessRule.getSignatures(packageInfo)
        val sb = StringBuilder()
        for (signature in signatureList) {
            val certHash = getCertHash(signature, "SHA-1")
            val certHash256 = getCertHash(signature, "SHA-256")
            sb.append(bytesToHexString(certHash256))
            sb.append('\n')
        }
        sig.text = sb.toString()
        val euiccManager = getSystemService(Context.EUICC_SERVICE) as EuiccManager?

        val euiccEnabled = euiccManager?.isEnabled ?: false

        findViewById<TextView>(R.id.euicc).text = euiccEnabled.toString()



    }

    //Copied from IccUtils
    /**
     * Converts a byte array into a String of hexadecimal characters.
     *
     * @param bytes an array of bytes
     *
     * @return hex string representation of bytes array
     */
    fun bytesToHexString(bytes: ByteArray?): String? {
        if (bytes == null) return null
        val ret = java.lang.StringBuilder(2 * bytes.size)
        for (i in bytes.indices) {
            var b: Int
            b = 0x0f and (bytes[i].toInt() shr 4)
            ret.append("0123456789abcdef"[b])
            b = 0x0f and bytes[i].toInt()
            ret.append("0123456789abcdef"[b])
        }
        return ret.toString()
    }
}