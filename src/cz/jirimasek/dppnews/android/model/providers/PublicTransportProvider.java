package cz.jirimasek.dppnews.android.model.providers;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.jirimasek.dppnews.android.model.RestApiClient;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * <code>PublicTransportProvider</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class PublicTransportProvider extends ContentProvider
{

    public static final Uri CONTENT_URI = Uri.parse("content://cz.jirimasek.dppnews.android.provider.publictransportprovider");

    private static final int STOP_ALL_ROWS = 1;
    private static final int STOP_SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(
                "cz.jirimasek.dppnews.android.provider.publictransportprovider",
                "stops", STOP_ALL_ROWS);
        uriMatcher.addURI(
                "cz.jirimasek.dppnews.android.provider.publictransportprovider",
                "stops/#", STOP_SINGLE_ROW);
    }

    private RestApiClient client;

    @Override
    public boolean onCreate()
    {
        client = new RestApiClient();

        return true;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case STOP_SINGLE_ROW:
                return "vnd.dppnews.cursor.dir/stops";

            case STOP_ALL_ROWS:
                return "vnd.dppnews.cursor.item/stops";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder)
    {
        if (selectionArgs.length != 0 && selectionArgs.length != 2)
        {
            throw new IllegalArgumentException("There must be set both coordinates or none of them.");
        }
        
        switch (uriMatcher.match(uri))
        {
            case STOP_SINGLE_ROW:
                return null;

            case STOP_ALL_ROWS:

                StringBuilder query = new StringBuilder();
                
                query.append("http://dpp-stops.appspot.com/api/v1/stops");
                
                if (selectionArgs.length == 2) 
                {
                    query.append("?lat=");
                    query.append(selectionArgs[0]);
                    query.append("&lng=");
                    query.append(selectionArgs[1]);
                    query.append("&dist=350");
                }

                String response = client.callWebErvice(query.toString());

                try
                {
                    JSONObject resp = new JSONObject(response);

                    JSONArray stops = resp.getJSONArray("stops");

                    String[] columnNames = new String[] { "_id", "name", 
                            "latitude", "longitude", "distance", "transport",
                            "lines" };

                    MatrixCursor cursor = new MatrixCursor(columnNames);

                    for (int i = 0; i < stops.length(); i++)
                    {
                        JSONObject stop = stops.getJSONObject(i);

                        String name = stop.getString("name");
                        JSONArray platforms = stop.getJSONArray("platforms");
                        
                        for (int j = 0 ; j < platforms.length() ; j++)
                        {
                            JSONObject platform = platforms.getJSONObject(j);

                            String latitude = platform.getString("latitude");
                            String longitude = platform.getString("longitude");
                            String distance = platform.getString("distance");
                            String transport = platform.getString("transport");
                            String lines = platform.getString("lines");
                            
                            String id = String.format("%s, %s", latitude, longitude);

                            String[] inc = new String[] { id, name, latitude, longitude, distance, transport, lines };

                            cursor.addRow(inc);
                        }
                    }

                    return cursor;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return null;

            default:

                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
