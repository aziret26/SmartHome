package kg.smarthome.aziret.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import kg.smarthome.aziret.tools.Tools;


public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE_BLUETOOTH = 1;
    private static final int REQUEST_RECOGNITION = 1;

    private TextToSpeech tts;

    private Button startRecognizer;
    private Spinner spinnerResult;

    Tools tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        startRecognizer = (Button) findViewById(R.id.startrecognizer);
        startRecognizer.setEnabled(false);
        spinnerResult = (Spinner) findViewById(R.id.result);
        tts = new TextToSpeech(this, this);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bt_settings) {
            Intent intent = new Intent(this, BluetoothActivity.class);
            //startActivityForResult(intent,REQUEST_CODE_BLUETOOTH);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void lampButton(View v){

        if(BluetoothActivity.outputStream != null) {
            int id = v.getId();
            ToggleButton t = (ToggleButton) v;
            try {
                    if (id == R.id.lamp1) {
                        if(t.isChecked()) {
                            BluetoothActivity.outputStream.write(11);
                        }else {
                            BluetoothActivity.outputStream.write(10);
                        }
                    }

                    if (id == R.id.lamp2) {
                        if(t.isChecked()) {
                            BluetoothActivity.outputStream.write(21);
                        }else {
                            BluetoothActivity.outputStream.write(20);
                        }
                    }

                    if (id == R.id.lamp3) {
                        if(t.isChecked()) {
                            BluetoothActivity.outputStream.write(31);
                        }else {
                            BluetoothActivity.outputStream.write(30);
                        }
                    }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            msg("no device is connected");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /*
        if ((requestCode == REQUEST_RECOGNITION) & (resultCode == RESULT_OK)) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerResult.setAdapter(adapter);
            spinnerResult.setOnItemSelectedListener(spinnerResultOnItemSelectedListener);
        }
*/
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public void speechButton(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Recognize");
        startActivityForResult(intent, REQUEST_RECOGNITION);
    }
    /*
    private Spinner.OnItemSelectedListener spinnerResultOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String sr = parent.getItemAtPosition(position).toString();
            ToggleButton tb;

            int c =parent.getCount();
            boolean flag;
            for(int i = 0;i < c;i++){
                String sr = parent.getItemAtPosition(position).toString();
                if(sr.equals("зелёный") || sr.equals("зелёный")){
                    tb = (ToggleButton) findViewById(R.id.lamp3);
                }

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    */
    public void onInit(int arg0) {
        startRecognizer.setEnabled(true);
    }

}
