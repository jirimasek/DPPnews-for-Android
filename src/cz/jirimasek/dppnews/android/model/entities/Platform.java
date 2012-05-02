package cz.jirimasek.dppnews.android.model.entities;

import java.util.HashSet;
import java.util.Set;

import cz.jirimasek.dppnews.android.commons.StringUtils;
import cz.jirimasek.dppnews.android.commons.enums.Transport;

/**
 * <code>Platform</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class Platform
{

    private int latitude;
    private int longitude;
    private Transport transport;
    private Set<String> lines;
    
    /**
     * 
     * @return
     */
    public int getLatitude()
    {
        return latitude;
    }
    
    /**
     * 
     * @param latitude
     */
    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
    }
    
    /**
     * 
     * @return
     */
    public int getLongitude()
    {
        return longitude;
    }
    
    /**
     * 
     * @param longitude
     */
    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
    }
    
    /**
     * 
     * @return
     */
    public Transport getTransport()
    {
        return transport;
    }
    
    /**
     * s
     * @param transport
     */
    public void setTransport(Transport transport)
    {
        this.transport = transport;
    }
    
    /**
     * 
     * @return
     */
    public Set<String> getLines()
    {
        if (lines == null)
        {
            lines = new HashSet<String>();
        }
        
        return lines;
    }
    
    /**
     * 
     * @param lines
     */
    public void setLines(Set<String> lines)
    {
        this.lines = lines;
    }
    
    /**
     * 
     * @param line
     */
    public void addLine(String line)
    {
        if (!StringUtils.isNullOrEmpty(line))
        {
            Set<String> set = getLines();
            
            set.add(line);
        }
    }
}
