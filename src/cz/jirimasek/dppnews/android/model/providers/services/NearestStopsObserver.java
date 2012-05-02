package cz.jirimasek.dppnews.android.model.providers.services;

import java.util.Collection;

import cz.jirimasek.dppnews.android.model.entities.Stop;

/**
 * <code>NearestStopsObserver</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public interface NearestStopsObserver
{

    public void gotNearestStops(Collection<Stop> stops);
}
