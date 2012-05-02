package cz.jirimasek.dppnews.android.view.map;

import java.util.LinkedList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * <code>StopOverlay</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class StopOverlay extends ItemizedOverlay<OverlayItem>
{

    private List<OverlayItem> mOverlays = new LinkedList<OverlayItem>();

    /**
     * 
     * @param defaultMarker
     */
    public StopOverlay(Drawable defaultMarker)
    {
        super(boundCenterBottom(defaultMarker));
    }

    /**
     * 
     * @param overlay
     */
    public void addOverlay(OverlayItem overlay)
    {
        mOverlays.add(overlay);
        populate();
    }

    /**
     * 
     * @param i
     */
    @Override
    protected OverlayItem createItem(int i)
    {
        return mOverlays.get(i);
    }

    /**
     * 
     */
    @Override
    public int size()
    {
        return mOverlays.size();
    }

}
