package com.example.myapplication;

import android.app.Service;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MyHostApduService extends HostApduService {
        @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        return null;
    }
    @Override
    public void onDeactivated(int reason) {
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();;
    }
}

