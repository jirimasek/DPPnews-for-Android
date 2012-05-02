package cz.jirimasek.dppnews.android.commons;

/**
 * <code>StringUtils</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class StringUtils
{

    /**
     * 
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str)
    {
        if (str == null || str.length() == 0)
        {
            return true;
        }
        
        return false;
    }
}
