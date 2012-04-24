package cz.jirimasek.dppnews.android.provider;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.jirimasek.dppnews.android.model.IncidentClient;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

/**
 * <code>IncidentProvider</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class IncidentProvider extends ContentProvider
{

    public static final Uri CONTENT_URI = Uri.parse("content://cz.jirimasek.dppnews.android.provider.incidentprovider");

    private static final int INCIDENT_ALL_ROWS = 1;
    private static final int INCIDENT_SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(
                "cz.jirimasek.dppnews.android.provider.incidentprovider",
                "incident", INCIDENT_ALL_ROWS);
        uriMatcher.addURI(
                "cz.jirimasek.dppnews.android.provider.incidentprovider",
                "incident/#", INCIDENT_SINGLE_ROW);
    }

    private IncidentClient client;

    @Override
    public boolean onCreate()
    {
        client = new IncidentClient();

        return true;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case INCIDENT_SINGLE_ROW:
                return "vnd.dppnews.cursor.dir/incident";

            case INCIDENT_ALL_ROWS:
                return "vnd.dppnews.cursor.item/incident";

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
        switch (uriMatcher.match(uri))
        {
            case INCIDENT_SINGLE_ROW:
                return null;

            case INCIDENT_ALL_ROWS:

                String response = client
                        .callWebErvice("http://dpp-news.appspot.com/api/v2/incidents");

                try
                {
                    JSONObject resp = new JSONObject(response);

                    JSONArray incidents = resp.getJSONArray("incidents");

                    Log.i(IncidentProvider.class.getName(),
                            "Number of entries " + incidents.length());

                    /*
                     * {
                     * "about":"http://www.dpp.cz/mimoradne-udalosti/1203251546"
                     * , "events":"Dopravní nehoda",
                     * "lines":[{"number":"17","transport"
                     * :"Tram"},{"number":"18","transport":"Tram"}],
                     * "origin":"2012-03-25T15:46:00Z",
                     * "renew":"2012-03-25T15:59:00Z",
                     * "stretch":"Národní divadlo - Staroměstská" }
                     */

                    String[] columnNames = new String[] { "_id", "about",
                            "stretch" };

                    MatrixCursor cursor = new MatrixCursor(columnNames);

                    for (int i = 0; i < incidents.length(); i++)
                    {
                        JSONObject incident = incidents.getJSONObject(i);

                        String about = incident.getString("about");
                        String stretch = incident.getString("stretch");

                        int index = about.lastIndexOf("/");

                        String id = about.substring(index + 1);

                        String[] inc = new String[] { id, about, stretch };

                        cursor.addRow(inc);

                        Log.i(IncidentProvider.class.getName(), stretch);
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
