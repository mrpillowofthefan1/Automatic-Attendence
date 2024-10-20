package com.example.myapplication;

import static android.content.ContentValues.TAG;


import android.content.Intent;


import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;



import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import java.nio.charset.StandardCharsets;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {
    MyHostApduService card = new MyHostApduService();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://citybot-nwlm-default-rtdb.firebaseio.com/");
    NdefRecord uriRecord = new NdefRecord(
            NdefRecord.TNF_ABSOLUTE_URI,
            "application/vnd.com.example.android.beam".getBytes(StandardCharsets.US_ASCII),
            new byte[0], new byte[0]);

    NdefRecord mimeRecord = new NdefRecord(
            NdefRecord.TNF_MIME_MEDIA,
            "application/vnd.com.example.android.beam".getBytes(StandardCharsets.US_ASCII),
            new byte[0], "Beam me up, Android!".getBytes(StandardCharsets.US_ASCII));
    byte[] payload;
    String domain = "com.Attendance";
    String type = "externalType";
    NdefRecord extRecord = NdefRecord.createExternal(domain, type, payload);
    String readableTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                MifareUltralight.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
                readableTag = readTag(tag);
                if (readableTag != null) {
                    Log.d(TAG, "onNewIntent: " + readableTag);
                }
                Log.d(TAG, "onNewIntent: " + messages);

            }
        }
    }


    public String readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            return new String(payload, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            Log.e(TAG, "IOException while reading MifareUltralight message...", e);
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }

        return null;
    }


}