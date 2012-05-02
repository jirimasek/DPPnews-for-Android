package cz.jirimasek.dppnews.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * <code>RestApiClient</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class RestApiClient
{

    /**
     * 
     * @param serviceURL
     * @return
     */
    public String callWebErvice(String serviceURL)
    {
        HttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet();
        
        try
        {
            // Construct a URI object
            getRequest.setURI(new URI(serviceURL));
        }
        catch (URISyntaxException e)
        {
            Log.e("URISyntaxException", e.toString());
        }
        
        BufferedReader in = null;
        HttpResponse response = null;
        
        try
        {   
            // Execute the request
            response = client.execute(getRequest);
        }
        catch (ClientProtocolException e)
        {
            Log.e("ClientProtocolException", e.toString());
        }
        catch (IOException e)
        {
            Log.e("IO exception", e.toString());
        }
        
        try
        {
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
        }
        catch (IllegalStateException e)
        {
            Log.e("IllegalStateException", e.toString());
        }
        catch (IOException e)
        {
            Log.e("IO exception", e.toString());
        }
        
        StringBuffer buff = new StringBuffer("");
        String line = "";
        
        try
        {
            while ((line = in.readLine()) != null)
            {
                buff.append(line);
            }
        }
        catch (IOException e)
        {
            Log.e("IO exception", e.toString());
            return e.getMessage();
        }
        
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            Log.e("IO exception", e.toString());
        }
        
        return buff.toString();
    }

}
