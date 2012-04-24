package cz.jirimasek.dppnews.android.location;

import android.location.Location;

/**
 * <code>LocationObserver</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public interface LocationObserver
{
    /**
     * 
     * @param location
     */
    public void gotLocation(Location location);
}
