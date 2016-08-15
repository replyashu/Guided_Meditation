package com.application.pramod.guidedmeditation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pramod on 15-08-2016.
 */
public class Fragment_flip extends Fragment {
    PowerManager pm;
    PowerManager.WakeLock wl;
    Vibrator vib;
    private boolean lastUpdate = false;
    private long lastTime;
    TextView countText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Find the ListView resource.
        View v = inflater.inflate(R.layout.fragment_flip_layout, container, false);

        // Get instance of Vibrator from current Context
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();

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
        wl.release();
    }

    /** Called when the activity is no longer visible. */
    @Override
    public void onStop() {
        super.onStop();
        wl.release();
    }
}
