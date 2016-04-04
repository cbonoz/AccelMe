package com.accelme.beta;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.denley.courier.BackgroundThread;
import me.denley.courier.Courier;
import me.denley.courier.ReceiveMessages;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private static final String SEND_DATA_ = "/data";
    private static final String START_MEASUREMENT_ = "/start";
    private static final String STOP_MEASUREMENT_ = "/stop";

    private static final int SENS_LINEAR_ACCELERATION = Sensor.TYPE_LINEAR_ACCELERATION;

    //Sensor related
    private SensorManager mSensorManager;
    private Sensor linearAccelerationSensor;

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Log.d(TAG, "Sensor delay normal: " + SensorManager.SENSOR_DELAY_NORMAL);

        Courier.startReceiving(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Courier.stopReceiving(this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    //default updateDisplay function from android, watch UI updates turned off for this app
    private void updateDisplay() {
//        if (isAmbient()) {
//            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
//            mTextView.setTextColor(getResources().getColor(android.R.color.white));
//            mClockView.setVisibility(View.VISIBLE);
//
//            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
//        } else {
//            mContainerView.setBackground(null);
//            mTextView.setTextColor(getResources().getColor(android.R.color.black));
//            mClockView.setVisibility(View.GONE);
//        }
    }



    //Sensor Related
    private void startMeasurement() {
        Log.i(TAG, "Start Measurement");

        linearAccelerationSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);

        if (linearAccelerationSensor != null) {
            mSensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);// 1000000, 1000000);
        }  else {
            Log.d(TAG, "No Linear Acceleration Sensor found");
        }


    }


    private void stopMeasurement() {
        Log.i(TAG, "Stop Measurement");
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        int sensorId = event.sensor.getType();

//        timestamp for fun, there are other values that can be pulled out of the sensorEvent
//        such as accuracy and sensor type (useful for multiple sensors)
        long timestamp = event.timestamp; //System.currentTimeMillis();

        float[] values = event.values;
        JSONObject jsonValue = new JSONObject();
        try {
            jsonValue.put("x", values[0]);
            jsonValue.put("y", values[1]);
            jsonValue.put("z", values[2]);
            jsonValue.put("timestamp", timestamp);

        } catch (Exception e) {
            Log.e(TAG, "Error creating Sensor Data point");
            return;
        }
        String data = jsonValue.toString();
        Log.d(TAG, "Sent data: " + data);
        Courier.deliverMessage(this, SEND_DATA_, data);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @BackgroundThread
    @ReceiveMessages(START_MEASUREMENT_)
    public void onStartMessage(String s) { startMeasurement(); }


    @BackgroundThread
    @ReceiveMessages(STOP_MEASUREMENT_)
    public void onStopMessage(String s) {
        stopMeasurement();
    }
}
