package cz.jirimasek.dppnews.android.view.map;

import java.util.Collection;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import cz.jirimasek.dppnews.android.R;
import cz.jirimasek.dppnews.android.commons.CollectionUtils;
import cz.jirimasek.dppnews.android.location.LocationObserver;
import cz.jirimasek.dppnews.android.location.LocationProvider;
import cz.jirimasek.dppnews.android.model.entities.Platform;
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
    private Collection<Stop> stops;

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

        MapView mapView = (MapView) findViewById(R.id.mapview);

        mapView.setBuiltInZoomControls(true);
    }

    /**
     * 
     */
    @Override
    public void onResume()
    {
        super.onResume();

        // Clear overlays

        MapView mapView = (MapView) findViewById(R.id.mapview);
        List<Overlay> mapOverlays = mapView.getOverlays();

        if (!mapOverlays.isEmpty())
        {
            mapOverlays.clear();
            mapView.invalidate();
        }

        // Create progress dialog

        Resources resources = getResources();
        String message = resources.getString(R.string.waiting_for_location);

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);

        progressDialog.show();

        // Start waiting for current location

        locationProvider.getLocation(this, this);
    }

    /* ********************************************************************** */
    /* ******************************** MENU ******************************** */
    /* ********************************************************************** */

    /**
     * 
     * @param menu
     */
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

        MarkerOverlay locationMarker = new MarkerOverlay(drawable);
        OverlayItem locationDescription = new OverlayItem(point, "", "");

        locationMarker.addOverlay(locationDescription);
        mapOverlays.add(locationMarker);

        // Retrieve nearest stops

        if (progressDialog != null && progressDialog.isShowing())
        {
            runOnUiThread(new Runnable() {

                @Override
                public void run()
                {
                    Resources resources = getResources();
                    String message = resources
                            .getString(R.string.waiting_for_nearest_stops);

                    progressDialog.setMessage(message);
                }
            });
        }

        nearestStopsProvider.getNearestStops(this, this, point);
    }

    /* ********************************************************************** */
    /* *********************** NEAREST STOPS OBSERVER *********************** */
    /* ********************************************************************** */

    /**
     * 
     * @param stops
     */
    @Override
    public void gotNearestStops(Collection<Stop> collection)
    {
        this.stops = collection;

        runOnUiThread(new Runnable() {

            public void run()
            {

                MapView mapView = (MapView) findViewById(R.id.mapview);
                List<Overlay> mapOverlays = mapView.getOverlays();

                Drawable busIcon = getResources().getDrawable(R.drawable.bus);
                Drawable funicularIcon = getResources().getDrawable(
                        R.drawable.funicular);
                Drawable tramIcon = getResources().getDrawable(
                        R.drawable.tramway);
                Drawable subwayIcon = getResources().getDrawable(
                        R.drawable.underground);

                MarkerOverlay busOverlay = new MarkerOverlay(busIcon);
                MarkerOverlay funicularOverlay = new MarkerOverlay(
                        funicularIcon);
                MarkerOverlay tramOverlay = new MarkerOverlay(tramIcon);
                MarkerOverlay subwayOverlay = new MarkerOverlay(subwayIcon);

                for (Stop stop : stops)
                {
                    if (!CollectionUtils.isNullOrEmpty(stop.getPlatforms()))
                    {

                        for (Platform platform : stop.getPlatforms())
                        {
                            GeoPoint point = new GeoPoint(
                                    platform.getLatitude(),
                                    platform.getLongitude());

                            OverlayItem platformMarker = new OverlayItem(point,
                                    stop.getName(), null);

                            switch (platform.getTransport())
                            {
                                case BUS:
                                    busOverlay.addOverlay(platformMarker);
                                    break;

                                case FUNICULAR:
                                    funicularOverlay.addOverlay(platformMarker);
                                    break;

                                case TRAM:
                                    tramOverlay.addOverlay(platformMarker);
                                    break;

                                case SUBWAY:
                                case SUBWAY_ENTRY:
                                    subwayOverlay.addOverlay(platformMarker);
                                    break;
                            }
                        }
                    }
                }

                mapOverlays.add(busOverlay);
                mapOverlays.add(funicularOverlay);
                mapOverlays.add(tramOverlay);
                mapOverlays.add(subwayOverlay);

                mapView.invalidate();

                // Dismiss progress dialog

                if (progressDialog != null && progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
            }
        });
    }
}
