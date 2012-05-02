package cz.jirimasek.dppnews.android.model.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cz.jirimasek.dppnews.android.commons.CollectionUtils;


/**
 * <code>Stop</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class Stop
{

    private String name;
    private Collection<Platform> platforms;
    private Set<String> lines;
    
    /**
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 
     * @return
     */
    public Collection<Platform> getPlatforms()
    {
        if (platforms == null)
        {
            platforms = new LinkedList<Platform>();
        }
        
        return platforms;
    }
    
    /**
     * 
     * @param platforms
     */
    public void setPlatforms(Collection<Platform> platforms)
    {
        this.platforms = platforms;
    }
    
    /**
     * 
     * @param platform
     */
    public void addPlaftorm(Platform platform)
    {
        if (platform != null)
        {
            Collection<Platform> collection = getPlatforms();
            
            collection.add(platform);
            
            if (!CollectionUtils.isNullOrEmpty(platform.getLines()))
            {
                Set<String> set = getLines();
                
                set.addAll(platform.getLines());
            }
        }
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
}
