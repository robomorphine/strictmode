package com.robomorphine.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUser {
    private final Context m_ctx;

    public PreferenceUser(Context ctx) {
        m_ctx = ctx;
    }

    public void use(Activity activity) {
        /* apply defaults from xml resource for DefaultSharedPreferences */
        PreferenceManager.setDefaultValues(m_ctx, R.xml.preferences, true);

        /* apply defaults from xml resource for custom preferences */
        PreferenceManager.setDefaultValues(m_ctx, "custom_default_pref1", Context.MODE_PRIVATE,
                R.xml.preferences, true);

        PreferenceManager.setDefaultValues(m_ctx, "custom_default_pref2", Context.MODE_PRIVATE,
                R.xml.preferences, true);

        /* default preferences */
        use(PreferenceManager.getDefaultSharedPreferences(m_ctx));

        /* custom preferences */
        use(m_ctx.getSharedPreferences("custom_pref1", Context.MODE_PRIVATE));
        use(m_ctx.getSharedPreferences("custom_pref2", Context.MODE_PRIVATE));
        use(m_ctx.getSharedPreferences("world_private", Context.MODE_PRIVATE));
        use(m_ctx.getSharedPreferences("world_r", Context.MODE_WORLD_READABLE));
        use(m_ctx.getSharedPreferences("world_w", Context.MODE_WORLD_WRITEABLE));
        use(m_ctx.getSharedPreferences("world_rw", Context.MODE_WORLD_READABLE
                | Context.MODE_WORLD_WRITEABLE));

        /* activity preferences */
        if (activity != null) {
            use(activity.getPreferences(Context.MODE_PRIVATE));
        }
    }

    public void use(SharedPreferences prefs) {
        Editor editor = prefs.edit();
        editor.putBoolean("boolValue", true);
        editor.putInt("intValue", 111);
        editor.putLong("longValue", 22222222);
        editor.putFloat("floatValue", 11.1f);
        editor.putString("stringValue", "value");
        editor.commit();
    }
}
