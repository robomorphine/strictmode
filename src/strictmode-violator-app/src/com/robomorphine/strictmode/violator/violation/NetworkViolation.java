package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class NetworkViolation extends ThreadViolation {
    
    public NetworkViolation(Context context) {
        super(context,
              R.drawable.network,
              R.string.network_name, 
              R.string.network_descr);
    }
    
    @Override
    public void violate() {
        try {
            HttpGet get = new HttpGet("http://www.google.com");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            response.getEntity().consumeContent();
            client.getConnectionManager().shutdown();
        } catch(IOException ex) {
            Toast.makeText(getContext(), "Networking error: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
