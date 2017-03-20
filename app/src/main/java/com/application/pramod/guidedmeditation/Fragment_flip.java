package com.application.pramod.guidedmeditation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by pramod on 15-08-2016.
 */
public class Fragment_flip extends Fragment implements SensorEventListener {
    PowerManager pm;
    PowerManager.WakeLock wl;
    Vibrator vib;
    private boolean lastUpdate = false;
    private long lastTime;
    TextView countText;
    private static SensorManager sensorManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Find the ListView resource.
        View v = inflater.inflate(R.layout.fragment_flip_layout, container, false);
        countText = (TextView) v.findViewById(R.id.count_value);
        countText.setText(Integer.toString(Params.getCount()));


        pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();
        // Get instance of Vibrator from current Context
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        lastTime = System.currentTimeMillis();

        return v;
    }

    /** Called when the activity is about to become visible. */
    @Override
    public void onStart() {
        super.onStart();
        wl.acquire();
    }

    /** Called when the activity has become visible. */
    @Override
    public void onResume() {
        super.onResume();
        wl.acquire();
    }

    /** Called when another activity is taking focus. */
    @Override
    public void onPause() {
        super.onPause();
        wakelockRelease();
    }

    /** Called when the activity is no longer visible. */
    @Override
    public void onStop() {
        super.onStop();
        wakelockRelease();
    }

    @Override
    public void onDetach() {
        super.onStop();
        wakelockRelease();
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onDestroy() {
        super.onStop();
        wakelockRelease();
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    public void wakelockRelease() {
        // sanity check for null as this is a public method
        if (wl != null) {
            try {
                wl.release();
            } catch (Throwable th) {
                // ignoring this exception, probably wakeLock was already released
            }
        } else {
            // should never happen during normal workflow
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        long actualTime = event.timestamp;

        if ((lastUpdate == true) && (z > 0)) {
            if (actualTime - lastTime < 200) {
                return;
            }

            // Vibrate for 50 milliseconds
            vib.vibrate(50);
            lastTime = actualTime;
            lastUpdate = false;
            Params.increment_count();
            countText.setText(Integer.toString(Params.getCount()));
        } else if ((lastUpdate == false) && (z < 0)) {
            if (actualTime - lastTime < 200) {
                return;
            }
            // Vibrate for 50 milliseconds
            vib.vibrate(50);
            lastTime = actualTime;
            lastUpdate = true;
            Params.increment_count();
            countText.setText(Integer.toString(Params.getCount()));
        }

        if (Params.getCount() >= Params.getCount_limit()) {
            Params.setCount(0);
            vib.vibrate(4000);
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Session Complete!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            countText.setText("0");
                        }
                    })
                    .create();
            dialog.show();
        }
    }
}
