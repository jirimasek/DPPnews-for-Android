package cz.jirimasek.dppnews.android.view.list;

import java.util.ArrayList;
import java.util.List;

import cz.jirimasek.dppnews.android.R;
import cz.jirimasek.dppnews.android.provider.IncidentProvider;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IncidentListView extends ListActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        String incidentsUri = IncidentProvider.CONTENT_URI + "/incident";
        
        List<String> incidents = new ArrayList<String>();
        
        Cursor c = getContentResolver().query(Uri.parse(incidentsUri), null, null, null, null);
        
        startManagingCursor(c);
        
        if (c.moveToFirst())
        {
            do
            {
                incidents.add(c.getString(2));
            }
            while (c.moveToNext());
        }
        
        stopManagingCursor(c);
        
        
        
        

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, incidents.toArray(new String[incidents.size()])));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
