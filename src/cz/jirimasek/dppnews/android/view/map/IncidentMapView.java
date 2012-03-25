package cz.jirimasek.dppnews.android.view.map;

import cz.jirimasek.dppnews.android.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import cz.jirimasek.dppnews.android.view.list.IncidentListView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class IncidentMapView extends MapActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        
        // Latitude: 50.099372
        // Longitude: 14.395331 
        

        int lat = (int) (50.099372 * 1E6);
        int lng = (int) (14.395331 * 1E6);
        
        GeoPoint point = new GeoPoint(lat, lng);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        
        MapController mapController = mapView.getController();
        
        mapController.setCenter(point);
        mapController.setZoom(16);
        
        //mapController.animateTo(point);
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_list:
                Intent myIntent = new Intent(this, IncidentListView.class);
                startActivityForResult(myIntent, 0);
                return true;

            case R.id.menu_setting:
                Toast.makeText(IncidentMapView.this, "Save is Selected",
                        Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
}
