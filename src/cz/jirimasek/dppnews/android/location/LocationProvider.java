package cz.jirimasek.dppnews.android.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * <code>LocationProvider</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class LocationProvider
{

    private boolean gpsEnabled;
    private boolean networkEnabled;

    private LocationObserver observer;
    private LocationManager manager;
    private Timer timer;

    /**
     * 
     * @param context
     * @param observer
     * @return
     */
    public boolean getLocation(Context context, LocationObserver observer)
    {
        this.observer = observer;

        // Get location manager

        if (manager == null)
        {
            manager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        // Get if the GPS location provider is enabled

        try
        {
            gpsEnabled = manager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception ex)
        {
            gpsEnabled = false;

            System.err.println("GPS Location Provider is not enabled.");
        }

        // Get if the network location provider is enabled

        try
        {
            networkEnabled = manager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception ex)
        {
            networkEnabled = false;

            System.err.println("Network Location Provider is not enabled.");
        }

        // Return false if no location provider is enabled

        if (!gpsEnabled && !networkEnabled)
        {
            return false;
        }

        // Request location

        if (gpsEnabled)
        {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListenerGps);
        }

        if (networkEnabled)
        {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, locationListenerNetwork);
        }

        // Start timer which call obtaining of the last
        // known location after it runs out

        timer = new Timer();
        timer.schedule(new LastLocationProvider(), 20000);

        return true;
    }

    /* ********************************************************************** */
    /* *********************** GPS LOCATION LISTENER ************************ */
    /* ********************************************************************** */

    LocationListener locationListenerGps = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            timer.cancel();
            observer.gotLocation(location);
            manager.removeUpdates(this);
            manager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider)
        {}

        public void onProviderEnabled(String provider)
        {}

        public void onStatusChanged(String provider, int status, Bundle extras)
        {}
    };

    /* ********************************************************************** */
    /* ********************* NETWORK LOCATION LISTENER ********************** */
    /* ********************************************************************** */

    LocationListener locationListenerNetwork = new LocationListener() {

        @Override
        public void onLocationChanged(Location location)
        {
            timer.cancel();
            observer.gotLocation(location);
            manager.removeUpdates(this);
            manager.removeUpdates(locationListenerGps);
        }

        @Override
        public void onProviderDisabled(String provider)
        {}

        @Override
        public void onProviderEnabled(String provider)
        {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {}
    };

    /* ********************************************************************** */
    /* *************************** LAST LOCATION **************************** */
    /* ********************************************************************** */

    /**
     * <code>LastLocationProvider</code>
     * 
     */
    class LastLocationProvider extends TimerTask
    {

        /**
         * 
         */
        @Override
        public void run()
        {

            // Stop waiting for updates

            manager.removeUpdates(locationListenerGps);
            manager.removeUpdates(locationListenerNetwork);

            Location net_loc = null;
            Location gps_loc = null;

            // Get last known location from GPS location provider

            if (gpsEnabled)
            {
                gps_loc = manager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            // Get last known location from network location provider

            if (networkEnabled)
            {
                net_loc = manager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            // If both provider return last known location, return newer one

            if (gps_loc != null && net_loc != null)
            {
                if (gps_loc.getTime() > net_loc.getTime())
                {
                    observer.gotLocation(gps_loc);
                }
                else
                {
                    observer.gotLocation(net_loc);
                }

                return;
            }

            // Return location from GPS location provider

            if (gps_loc != null)
            {
                observer.gotLocation(gps_loc);

                return;
            }

            // Return location from network location provider

            if (net_loc != null)
            {
                observer.gotLocation(net_loc);

                return;
            }

            // Return null if there is no last known location

            observer.gotLocation(null);
        }
    }
}
