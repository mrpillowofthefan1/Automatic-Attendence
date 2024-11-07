package com.example.myapplication;

import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.nio.charset.Charset;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MyHostApduService extends HostApduService {

    private static final String SELECT_APDU_HEADER = "00A40400"; // A sample APDU header for SELECT command
    private static final String RESPONSE_OK = "9000";  // Status OK response
    private static final String RESPONSE_ERROR = "6F00";  // General error response

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        String apduString = bytesToHex(apdu);
        Log.d("HCE", "APDU received: " + apduString);

        if (apduString.startsWith(SELECT_APDU_HEADER)) {
            byte[] response = "var name = 'John Doe'".getBytes(Charset.forName("UTF-8"));
            return concatenateArrays(response, hexStringToByteArray(RESPONSE_OK));
        } else {
            return hexStringToByteArray(RESPONSE_ERROR);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d("HCE", "HCE Deactivated: " + reason);
    }

    private byte[] concatenateArrays(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}


