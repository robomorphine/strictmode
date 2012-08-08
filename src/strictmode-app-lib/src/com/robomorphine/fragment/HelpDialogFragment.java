package com.robomorphine.fragment;

import com.robomorphine.strictmode.app.lib.R;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpDialogFragment extends DialogFragment {

    public final static String HTML_RESOURCE_ID_KEY = "resourcesId"; 
    
    public static HelpDialogFragment createFragment(int helpRawResId) {
        Bundle args = new Bundle();
        args.putInt(HTML_RESOURCE_ID_KEY, helpRawResId);
        
        HelpDialogFragment fragment = new HelpDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int resId = 0;
        
        Bundle bundle = getArguments();
        if (bundle != null) {
            resId = bundle.getInt(HTML_RESOURCE_ID_KEY, -1);
        }
        
        LayoutInflater inflater = LayoutInflater.from(getActivity());        
        View view = inflater.inflate(R.layout.fragment_help, null);        
        final WebView webView = (WebView)view.findViewById(R.id.webview);
        webView.setBackgroundColor(getDefaultBackgroundColor());
        disableHardwareAccelaration(webView);
        
        if (resId != 0) {
            webView.loadData(fromRawResource(resId), "text/html", Encoding.UTF_16.name());
        }
                        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getText(R.string.help));
        builder.setView(view);
        builder.setPositiveButton(getString(android.R.string.ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        return builder.create();
    }
    
    private int getDefaultBackgroundColor() {
        TypedValue value = new TypedValue();
        Theme theme = getActivity().getTheme();
        theme.resolveAttribute(android.R.attr.colorBackground, value, true);
        return value.data;
    }
    
    @TargetApi(11)
    private void disableHardwareAccelaration(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
    
    private String fromRawResource(int resId) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream in = getResources().openRawResource(resId);
            BufferedReader read = new BufferedReader(new InputStreamReader(in));            
            
            String line = null;
            while ((line = read.readLine()) != null) {
                builder.append(line);
            }              
        } catch (IOException ex) {
            //ignore
        }
        return builder.toString(); 
    }
}
