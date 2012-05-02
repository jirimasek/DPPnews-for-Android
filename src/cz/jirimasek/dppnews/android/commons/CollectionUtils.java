package cz.jirimasek.dppnews.android.commons;

import java.util.Collection;

/**
 * <code>CollecitonUtils</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class CollectionUtils
{

    /**
     * 
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(Collection collection)
    {
        if (collection == null || collection.isEmpty())
        {
            return true;
        }
        
        return false;
    }
}
