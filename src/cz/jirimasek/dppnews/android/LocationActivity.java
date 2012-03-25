package cz.jirimasek.dppnews.android;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LocationActivity extends ListActivity
{

    public void makeUseOfNewLocation(Location location)
    {
        System.out.println(String.format("Latitude: %f", location.getLatitude()));
        System.out.println(String.format("Longitude: %f", location.getLongitude()));
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

          // Register the listener with the Location Manager to receive location updates
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
          
          //String locationProvider = LocationManager.GPS_PROVIDER;
          
          //Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

          //System.out.println("Latitude: " + lastKnownLocation.getLatitude());
          //System.out.println("Longitude: " + lastKnownLocation.getLongitude());
        
          // Remove the listener you previously added
          //locationManager.removeUpdates(locationListener);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/1" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.incidentlist, R.id.label, values);
        
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

}
