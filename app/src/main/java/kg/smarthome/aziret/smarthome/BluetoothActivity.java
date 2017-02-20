package kg.smarthome.aziret.smarthome;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import kg.smarthome.aziret.tools.Tools;

public class BluetoothActivity extends AppCompatActivity {
    BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    String address = null;
    private ProgressDialog progress;
    BluetoothSocket btSocket;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ListView lv;
    static OutputStream outputStream = null;
    ToggleButton btState;
    Button showDevLs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listView);

        btState = (ToggleButton) findViewById(R.id.bt_state);
        btState.setChecked(btAdapter.isEnabled());
        showDevLs = (Button) findViewById(R.id.showDevLs);
        showDevLs.setEnabled(btState.isChecked());
    }

    public void changeBTState(View v){
        boolean state = btState.isChecked();
        if(!state){
            btAdapter.disable();
            final ArrayAdapter adapter = null;
            lv.setAdapter(adapter);
            showDevLs.setEnabled(btState.isChecked());
        }else{
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            showDevLs.setEnabled(btState.isChecked());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        btState.setChecked(btAdapter.isEnabled());
        showDevLs.setEnabled(btState.isChecked());
        if(!btState.isChecked()){
            btSocket = null;
            isBtConnected = false;
        }
    }

    public void showDevList(View v){
        ArrayList list = new ArrayList();
        pairedDevices = btAdapter.getBondedDevices();


        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName() + "\n" + bt.getAddress());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(myListClickListener);
    }
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);
            new ConnectBT().execute(); //Call the class to connect
        }
    };
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_LONG);
            progress = ProgressDialog.show(BluetoothActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog

        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    btAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = btAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
                try{
                    BluetoothActivity.outputStream = btSocket.getOutputStream();
                }catch (Exception e){
                    Toast.makeText(BluetoothActivity.this,"Failed to send",Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
            progress.dismiss();
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
