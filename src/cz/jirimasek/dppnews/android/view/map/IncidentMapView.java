package cz.jirimasek.dppnews.android.view.map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import cz.jirimasek.dppnews.android.R;
import cz.jirimasek.dppnews.android.location.LocationObserver;
import cz.jirimasek.dppnews.android.location.LocationProvider;
import cz.jirimasek.dppnews.android.view.list.IncidentListView;

/**
 * <code>IncidentMapView</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */

public class IncidentMapView extends MapActivity implements LocationObserver
{

    private LocationProvider locationProvider;
    
    /**
     * 
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        locationProvider = new LocationProvider();
        
        setContentView(R.layout.mapview);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        locationProvider.getLocation(this, this);
    }

    /**
     * 
     * @param menu
     */
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        
        return true;
    }

    /**
     * 
     * @param item
     */
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

    /**
     * 
     */
    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
    
    /* ********************************************************************** */
    /* ************************* LOCATION OBSERVER ************************** */
    /* ********************************************************************** */

    /**
     * 
     * @param location
     */
    public void gotLocation(Location location)
    {
        
        // Latitude: 50.099372
        // Longitude: 14.395331 

        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        
        GeoPoint point = new GeoPoint(lat, lng);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        
        MapController mapController = mapView.getController();
        
        mapController.setCenter(point);
        mapController.setZoom(16);
        
        mapController.animateTo(point);
        
        System.out.println("OBDRZENO");
    }
}
