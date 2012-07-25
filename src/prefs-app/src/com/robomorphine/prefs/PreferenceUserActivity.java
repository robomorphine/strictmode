
package com.robomorphine.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Map;

public class PreferenceUserActivity extends Activity {
    private EditText m_text;

    final static private String s_prefName = "test";
    final static private String s_key = "key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_text = (EditText) findViewById(R.id.text);
        
        SharedPreferences prefs = getSharedPreferences("_has_set_default_values", Context.MODE_PRIVATE);
        Map<String, ?> map = prefs.getAll();
        Log.e("YO", map.toString());
    }

    public void onUse(View v) {
        PreferenceUser user = new PreferenceUser(this);
        user.use(this);
    }

    public void onStore(View v) {
        SharedPreferences prefs = getSharedPreferences(s_prefName, Context.MODE_PRIVATE);
        prefs.edit().putString(s_key, m_text.getText().toString()).commit();
    }

    public void onRetrieve(View v) {
        SharedPreferences prefs = getSharedPreferences(s_prefName, Context.MODE_PRIVATE);
        String value = prefs.getString(s_key, "");
        m_text.setText(value);
    }

    public void onDelete(View v) {
        File prefFile = new File(getFilesDir() + "/"  + ".." + "/" +
                                  "shared_prefs" + "/" + s_prefName + ".xml");

        if (!prefFile.delete() && prefFile.exists()) {
            Toast.makeText(this, "Failed to delete preference file!", Toast.LENGTH_LONG).show();
        }
    }
}
