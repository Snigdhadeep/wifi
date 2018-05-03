package com.example.diku.testwifilist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private Button btnRefresh;
    ListAdapter adapter;
    ListView lvWifiDetails;
    List wifiList;
    int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION=1125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }else{
            Toast.makeText(this, "ok1", Toast.LENGTH_SHORT).show();
            //do something, permission was previously granted; or legacy device
        }

        lvWifiDetails = (ListView) findViewById(R.id.lvWifiDetails);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
       // mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
         mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        scanWifiList();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifiList();
            }
        });



    }
    private void setAdapter() {
        adapter = new com.example.diku.testwifilist.ListAdapter(getApplicationContext(),wifiList);
        lvWifiDetails.setAdapter(adapter);
    }
    private void scanWifiList() {
        mainWifi.startScan();
        wifiList = mainWifi.getScanResults();
        setAdapter();
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        }
    }


    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            // Do something with granted permission
         //   lvWifiDetails.getScanningResults();
        }
    }
}
