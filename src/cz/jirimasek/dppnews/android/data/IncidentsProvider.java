package cz.jirimasek.dppnews.android.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Třída <code>IncidentsProvider</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class IncidentsProvider extends ContentProvider
{
    public static final Uri CONTENT_URI = Uri.parse("content://cz.jirimasek.dppnews.android.data.incidentsprovider");
    
    private static final int AUTA_ALLROWS = 1;
    private static final int AUTA_SINGLE_ROW = 2;
    private static final int LODE_ALLROWS = 3;
    private static final int LODE_SINGLE_ROW = 4;
    
    private static final UriMatcher uriMatcher;
    
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        uriMatcher.addURI("cz.jirimasek.dppnews.android.data.incidentsprovider", "auta", AUTA_ALLROWS);
        uriMatcher.addURI("cz.jirimasek.dppnews.android.data.incidentsprovider", "auta/#", AUTA_SINGLE_ROW);
        uriMatcher.addURI("cz.jirimasek.dppnews.android.data.incidentsprovider", "lode", LODE_ALLROWS);
        uriMatcher.addURI("cz.jirimasek.dppnews.android.data.incidentsprovider", "lode/#", LODE_SINGLE_ROW);
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case AUTA_SINGLE_ROW:
                return "vnd.cvut.cursor.dir/auta";
                
            case AUTA_ALLROWS:
                return "vnd.cvut.cursor.item/auta";
                
            case LODE_ALLROWS:
                return "vnd.cvut.cursor.dir/lode";
        
            case LODE_SINGLE_ROW:
                return "vnd.cvut.cursor.item/lode";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
            String arg4)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
