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


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    final int REQUEST_CODE_BLUETOOTH = 1;
    private static final int REQUEST_RECOGNITION = 1;

    private TextToSpeech tts;

    private Button startRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startRecognizer = (Button) findViewById(R.id.startrecognizer);
        startRecognizer.setEnabled(false);
        tts = new TextToSpeech(this, this);
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

            ToggleButton t = (ToggleButton) v;
            lampState(t);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if ((requestCode == REQUEST_RECOGNITION) & (resultCode == RESULT_OK)) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            analyzeSpeech(adapter);
        }

    }
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public void speechButton(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Recognize");
        startActivityForResult(intent, REQUEST_RECOGNITION);
    }

    public void onInit(int arg0) {
        startRecognizer.setEnabled(true);
    }
    public void analyzeSpeech(ArrayAdapter<?> adapter){
        int count = adapter.getCount();
        ToggleButton tb;
        boolean flag =false;
        try {
            for (int i = 0; i < count; i++) {
                String sr = adapter.getItem(i).toString();
                if (sr.equals("зелёный") || sr.equals("зеленый")) {
                    tb = (ToggleButton) findViewById(R.id.lamp1);
                    changeButtonState(tb);
                    lampState(tb);
                    msg("Green");
                    flag = true;
                    break;
                }
                if (sr.equals("желтый") || sr.equals("жёлтый")) {
                    tb = (ToggleButton) findViewById(R.id.lamp2);
                    changeButtonState(tb);
                    lampState(tb);
                    msg("yellow");
                    flag = true;
                    break;
                }
                if (sr.equals("красный")) {
                    tb = (ToggleButton) findViewById(R.id.lamp3);
                    changeButtonState(tb);
                    lampState(tb);
                    msg("red");
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                msg("Не распознано");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void changeButtonState(ToggleButton tb){
        tb.setChecked(!tb.isChecked());
    }

    public void lampState(ToggleButton tb){

        if(BluetoothActivity.outputStream != null) {
            try {
                if (tb.getId() == R.id.lamp1) {
                    if(tb.isChecked()) {
                        BluetoothActivity.outputStream.write(11);
                    }else {
                        BluetoothActivity.outputStream.write(10);
                    }
                }

                if (tb.getId() == R.id.lamp2) {
                    if(tb.isChecked()) {
                        BluetoothActivity.outputStream.write(21);
                    }else {
                        BluetoothActivity.outputStream.write(20);
                    }
                }

                if (tb.getId() == R.id.lamp3) {
                    if(tb.isChecked()) {
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
}
