package cz.jirimasek.dppnews.android.model.providers.services;

import static cz.jirimasek.dppnews.android.commons.enums.Transport.BUS;
import static cz.jirimasek.dppnews.android.commons.enums.Transport.FERRY;
import static cz.jirimasek.dppnews.android.commons.enums.Transport.FUNICULAR;
import static cz.jirimasek.dppnews.android.commons.enums.Transport.SUBWAY;
import static cz.jirimasek.dppnews.android.commons.enums.Transport.SUBWAY_ENTRY;
import static cz.jirimasek.dppnews.android.commons.enums.Transport.TRAM;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import cz.jirimasek.dppnews.android.model.entities.Platform;
import cz.jirimasek.dppnews.android.model.entities.Stop;
import cz.jirimasek.dppnews.android.model.providers.PublicTransportProvider;

/**
 * <code>NearestStopsProvider</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class NearestStopsProvider
{

    private Context context;
    private NearestStopsObserver observer;
    private GeoPoint point;
    
    /**
     * 
     * @return
     */
    protected Context getContext()
    {
        return context;
    }

    /**
     * 
     * @param context
     */
    protected void setContext(Context context)
    {
        this.context = context;
    }

    /**
     * 
     * @return
     */
    protected NearestStopsObserver getObserver()
    {
        return observer;
    }

    /**
     * 
     * @param observer
     */
    protected void setObserver(NearestStopsObserver observer)
    {
        this.observer = observer;
    }

    /**
     * 
     * @return
     */
    protected GeoPoint getPoint()
    {
        return point;
    }

    /**
     * 
     * @param point
     */
    protected void setPoint(GeoPoint point)
    {
        this.point = point;
    }

    /**
     * 
     * @param context
     * @param observer
     * @param point
     */
    public void getNearestStops(Context context, NearestStopsObserver observer, GeoPoint point)
    {
        this.context = context;
        this.observer = observer;
        this.point = point;
        
        new Thread(new Runnable() {

            public void run()
            {
                // Retrieve nearest stops
            
                String stopsUri = PublicTransportProvider.CONTENT_URI + "/stops";
            
                Uri uri = Uri.parse(stopsUri);
                
                // Build array with GPS coordinates of current location
            
                String[] selectionParams = new String[2];
            
                selectionParams[0] = String.valueOf(getPoint().getLatitudeE6());
                selectionParams[1] = String.valueOf(getPoint().getLongitudeE6());
                
                // Ask content provider for the nearest stops
            
                Cursor c = getContext().getContentResolver().query(uri, null, null, selectionParams, null);
         
                gotNearestStops(c);
            }
        }).start();
    }
    
    /**
     * 
     * @param c
     */
    protected void gotNearestStops(Cursor c)    
    {
        Map<String, Stop> stops = new HashMap<String, Stop>();
        
        if (c != null && c.moveToFirst())
        {
            do
            {
                String name = c.getString(1);
                String transport = c.getString(5);
                
                Platform platform = new Platform();
                
                platform.setLatitude(Integer.parseInt(c.getString(2)));
                platform.setLongitude(Integer.parseInt(c.getString(3)));
                
                // Get transport type
                
                if (transport.equals("Bus"))
                {
                    platform.setTransport(BUS);
                }
                else if (transport.equals("Tram"))
                {
                    platform.setTransport(TRAM);
                }
                else if (transport.equals("Subway"))
                {
                    platform.setTransport(SUBWAY);
                }
                else if (transport.equals("Subway entry"))
                {
                    platform.setTransport(SUBWAY_ENTRY);
                }
                else if (transport.equals("Funicular"))
                {
                    platform.setTransport(FUNICULAR);
                }
                else if (transport.equals("Ferry"))
                {
                    platform.setTransport(FERRY);
                }
                
                // Get available lines
                
                try
                {
                    JSONArray array = new JSONArray(c.getString(6));
                    
                    for (int i = 0 ; i < array.length() ; i++)
                    {
                        platform.addLine(array.getString(i));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                
                // Add platform to stop
                
                if (!stops.containsKey(name))
                {
                    Stop stop = new Stop();
                    
                    stop.setName(name);
                    
                    stops.put(name, stop);
                }
                
                Stop stop = stops.get(name);
                
                stop.addPlaftorm(platform);
            }
            while (c.moveToNext());            
        }
        
        // Pass list of nearest stops to observer
        
        observer.gotNearestStops(stops.values());
    }
}
