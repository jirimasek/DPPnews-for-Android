package cz.jirimasek.dppnews.android;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class HelloMapView extends MapActivity
{

    LinearLayout linearLayout;
    MapView      mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mapView = (MapView) findViewById(R.id.maplayout);
        mapView.setBuiltInZoomControls(true);
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
