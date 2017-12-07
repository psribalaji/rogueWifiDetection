package com.example.balaji.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainText = (TextView) findViewById(R.id.mainText);
            b = (Button) findViewById(R.id.button);
        // Initiate wifi service manager
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Check for wifi is disabled
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }

        // wifi scaned value broadcast receiver
        receiverWifi = new WifiReceiver();

        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        mainText.setText("Starting Scan...");


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }




    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
        Log.i("mes","Broadcatstooper");
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");

            for(int i = 0; i <wifiList.size(); i++) {

                sb.append(new Integer(i + 1).toString() + ". ");
                sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");

                String ssid = wifiList.get(i).SSID;
                int size = wifiList.size();
                String bssid = wifiList.get(i).BSSID;
                String cap = wifiList.get(i).capabilities;
                int level = wifiList.get(i).level;
                int frequency = wifiList.get(i).frequency;
                int timestamp = (int) wifiList.get(i).timestamp;
         /*   Log.i("Count", String.valueOf(wifiList.size()));
            Log.i("BSSID", wifiList.get(i).BSSID);
            Log.i("SSID", wifiList.get(i).SSID);
            Log.i("level", String.valueOf(wifiList.get(i).level));
            Log.i("frequency", String.valueOf(wifiList.get(i).frequency));
            Log.i("capabilities", wifiList.get(i).capabilities); */
            Log.i("Details",wifiList.get(i).toString());
                try {
                    Log.i("test", "test");
                    SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("Wifi", MODE_PRIVATE, null);
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS records17 (count INT(100), ssid VARCHAR, bssid VARCHAR, capabilities VARCHAR, level INT(2000), frequency INT(5000), timestamp INT(10000), lat VARCHAR, lon VARCHAR)");
                    sqLiteDatabase.execSQL("INSERT INTO records17 VALUES (" + size + ",'" + ssid + "','" + bssid + "','" + cap + "'," + level + "," + frequency + ", " + timestamp + ",'at','lon')");
                    Toast.makeText(getApplicationContext(),"SAVED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                }

                mainText.setText(sb);
            }

    }}

    public void Save(){
       // Intent intent = new Intent(this,Main4Activity.class);
 onPause();


try{
        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("Wifi", MODE_PRIVATE, null);

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM records17",null);
                  cursor.moveToFirst();
                int nameIndex = cursor.getColumnIndex("count");
                int secondindex = cursor.getColumnIndex("ssid");
                int thirdindex = cursor.getColumnIndex("bssid");
                int fourth = cursor.getColumnIndex("capabilities");
                int fifth = cursor.getColumnIndex("level");
                int sixth = cursor.getColumnIndex("frequency");
                int seventh = cursor.getColumnIndex("timestamp");
                int eight = cursor.getColumnIndex("lat");
                int nine = cursor.getColumnIndex("lon");
             //   cursor.moveToFirst();
                int c = cursor.getCount();
                while(!cursor.isLast()) {
                    //  cursor.moveToFirst();
                    Log.i("Dbcount", cursor.getString(nameIndex));
                    Log.i("Dbssid", cursor.getString(secondindex));
                    Log.i("Dbbssid", cursor.getString(thirdindex));
                    Log.i("Dbcap", cursor.getString(fourth));
                    Log.i("Dbblevel", cursor.getString(fifth));
                    Log.i("Dbbfreq", cursor.getString(sixth));
                    Log.i("Dbbtimes", cursor.getString(seventh));
                    Log.i("Dblat",cursor.getString(eight));
                    Log.i("Dblon",cursor.getString(nine));

                    cursor.moveToNext();


                }

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }




    }
}



