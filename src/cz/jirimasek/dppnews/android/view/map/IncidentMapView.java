package cz.jirimasek.dppnews.android.view.map;

import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import cz.jirimasek.dppnews.android.R;
import cz.jirimasek.dppnews.android.location.LocationObserver;
import cz.jirimasek.dppnews.android.location.LocationProvider;
import cz.jirimasek.dppnews.android.model.entities.Stop;
import cz.jirimasek.dppnews.android.model.providers.services.NearestStopsObserver;
import cz.jirimasek.dppnews.android.model.providers.services.NearestStopsProvider;
import cz.jirimasek.dppnews.android.view.list.IncidentListView;

/**
 * <code>IncidentMapView</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class IncidentMapView extends MapActivity implements LocationObserver,
        NearestStopsObserver
{

    private LocationProvider locationProvider;
    private NearestStopsProvider nearestStopsProvider;
    private ProgressDialog progressDialog;

    /**
     * 
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        locationProvider = new LocationProvider();
        nearestStopsProvider = new NearestStopsProvider();

        setContentView(R.layout.mapview);
    }

    /**
     * 
     */
    @Override
    public void onResume()
    {
        super.onResume();

        progressDialog = ProgressDialog.show(this, null,
                "Načítám aktuální pozici…");

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
    @Override
    public void gotLocation(Location location)
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        // Latitude: 50.099372
        // Longitude: 14.395331

        // Process current position

        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);

        GeoPoint point = new GeoPoint(lat, lng);

        // Center map to current position

        MapView mapView = (MapView) findViewById(R.id.mapview);

        MapController mapController = mapView.getController();

        mapController.setCenter(point);
        mapController.setZoom(16);

        mapController.animateTo(point);

        // Mark current position in map

        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = getResources().getDrawable(
                R.drawable.direction_down);

        StopOverlay locationMarker = new StopOverlay(drawable);
        OverlayItem locationDescription = new OverlayItem(point, "", "");

        locationMarker.addOverlay(locationDescription);
        mapOverlays.add(locationMarker);

        // Retrieve nearest stops
        
        nearestStopsProvider.getNearestStops(this, this, point);
    }

    /**
     * 
     * @param stops
     */
    @Override
    public void gotNearestStops(Collection<Stop> stops)
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
}
