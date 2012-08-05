package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class AboutDialogFragment extends DialogFragment {
    
    private String getVersion() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
            return pi.versionName + " ( " + pi.versionCode + " )";
        } catch(NameNotFoundException ex) {
            return "<unknown>";
        }
    }
    
    private CharSequence linkify(String text, String link) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        URLSpan span = new URLSpan(link);
        
        builder.append(text);
        builder.setSpan(span, 0, builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }
    
    private CharSequence linkify(int textResId, int linkResId) {
        return linkify(getString(textResId), getString(linkResId));
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_about, null);
        
        TextView version = (TextView)view.findViewById(R.id.version);        
        TextView author = (TextView)view.findViewById(R.id.author);
        TextView project = (TextView)view.findViewById(R.id.project);
        TextView license = (TextView)view.findViewById(R.id.license);
        TextView repo = (TextView)view.findViewById(R.id.repo);
        
        author.setMovementMethod(LinkMovementMethod.getInstance());
        project.setMovementMethod(LinkMovementMethod.getInstance());
        license.setMovementMethod(LinkMovementMethod.getInstance());        
        repo.setMovementMethod(LinkMovementMethod.getInstance());
        
        version.setText(getVersion());        
        author.setText(linkify(R.string.author, R.string.author_link));        
        project.setText(linkify(R.string.project, R.string.project_link));
        license.setText(linkify(R.string.license, R.string.license_link));        
        repo.setText(linkify(R.string.repo, R.string.repo_link));        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());                
        builder.setTitle(R.string.about);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        
        return builder.create();
    }

}
