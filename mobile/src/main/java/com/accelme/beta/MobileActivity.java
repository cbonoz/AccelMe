package com.accelme.beta;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import me.denley.courier.BackgroundThread;
import me.denley.courier.Courier;
import me.denley.courier.ReceiveMessages;

public class MobileActivity extends AppCompatActivity {
    private static final String TAG = "MobileActivity";

    //routes for wear - mobile communication
    private static final String SEND_DATA_ = "/data";
    private static final String START_MEASUREMENT_ = "/start";
    private static final String STOP_MEASUREMENT_ = "/stop";

    //max value for acceleration magnitude scale
    private static final Double MAX_ACCEL = 100.0;

    //XML Objects
    private static LinearLayout mainLayout;
    private static TextView accelText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_activity);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        accelText = (TextView) findViewById(R.id.accelText);
        accelText.setText("0");

        Courier.startReceiving(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Courier.stopReceiving(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateScreenColor(Double value) {
        //update the UI to a new scaled color between 1 and 255 for green based on magnitude of received acceleration value
        Double scaledValue = (value / MAX_ACCEL) * 255.0;
        if (scaledValue>255.0) scaledValue = 255.0;

        String negatedColor = Integer.toHexString(255 - scaledValue.intValue());
        if (negatedColor.length()!=2) negatedColor = "0" + negatedColor;

        //create different tones of green by subtracting out other colors
        String colorString = "#" + negatedColor + "ff" + negatedColor;

//        Log.d(TAG, "Scaled Value: " + scaledValue + ", color: " + colorString); //will print very rapidly (~5hz when active)

        //set the background color and text
        mainLayout.setBackgroundColor(Color.parseColor(colorString));
        String greenColor = Integer.toHexString(scaledValue.intValue());
        accelText.setText(greenColor);
    }

    @BackgroundThread
    @ReceiveMessages(SEND_DATA_)
    public void onDataMessage(String s) {
        Double value;
        double x,y,z;

        try {
            JSONObject jsonBody = new JSONObject(s);
            x = jsonBody.getDouble("x");
            y = jsonBody.getDouble("y");
            z = jsonBody.getDouble("z");
            value = Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        updateScreenColor(value);
    }

    public void startButtonHandler(View v) {

        Courier.deliverMessage(this, START_MEASUREMENT_, "");
        Toast.makeText(this, "Sensors Started", Toast.LENGTH_SHORT).show();
    }

    public void stopButtonHandler(View v) {

        Courier.deliverMessage(this, STOP_MEASUREMENT_,"");
        Toast.makeText(this, "Sensors Stopped", Toast.LENGTH_SHORT).show();
    }


}
