package com.application.pramod.guidedmeditation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pram on 8/12/2016.
 */
public class Fragment_swipe extends Fragment {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Find the ListView resource.
        View v = inflater.inflate(R.layout.fragment_main_layout, container, false);
        mainListView = (ListView) v.findViewById( R.id.frag_main_listview );
        Log.d("MainActivity", "Left");

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
//        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
//        listAdapter.add( "Ceres" );
//        listAdapter.add( "Pluto" );
//        listAdapter.add( "Haumea" );
//        listAdapter.add( "Makemake" );
//        listAdapter.add( "Eris" );
        listAdapter.insert(com.application.pramod.guidedmeditation.Params.getCount().toString(), 0);

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
        v.setOnTouchListener(new com.application.pramod.guidedmeditation.OnSwipeTouchListener(getActivity()) {


            public void onSwipeTop() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                // Vibrate for 50 milliseconds
                com.application.pramod.guidedmeditation.Params.increment_count();
                listAdapter.insert(com.application.pramod.guidedmeditation.Params.getCount().toString(), 0);
                mainListView.setAdapter( listAdapter );

                Vibrator v;
                // Get instance of Vibrator from current Context
                v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                if(com.application.pramod.guidedmeditation.Params.getCount() >= com.application.pramod.guidedmeditation.Params.getCount_limit()) {
                    com.application.pramod.guidedmeditation.Params.setCount(0);
                    v.vibrate(4000);
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Session Complete!!!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });
        return v;
    }
}
