package com.robomorphine.strictmode.adapter;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.violation.ViolationException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViolationStacktraceAdapter extends BaseAdapter {
    
    private final static int EXCEPTION_MESSAGE_TYPE = 0;
    private final static int STACKTRACE_ELEMENT_TYPE = 1;
    private final static int VIEW_TYPE_COUNT = 2;
    
    private final LayoutInflater mInflater;
    private List<Object> mItems;
     
    public ViolationStacktraceAdapter(Context context, ViolationException exception) {
        mInflater = LayoutInflater.from(context);
        setException(exception);
    }
    
    private void setException(ViolationException exception) {
        ArrayList<Object> items = new ArrayList<Object>();
        while(exception != null) {
            items.add(exception);
            StackTraceElement [] elements = exception.getStackTrace();
            if(elements != null) {
                for(StackTraceElement element : elements) {
                    items.add(element);
                }
            }
            exception = (ViolationException)exception.getCause();
        }
        mItems = items;
    }
    
    @Override
    public int getCount() {
        return mItems.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
    
    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof ViolationException) {
            return EXCEPTION_MESSAGE_TYPE;
        } else if(getItem(position) instanceof StackTraceElement) {
            return STACKTRACE_ELEMENT_TYPE;
        }
        throw new IllegalStateException("Invalid view type.");
    }
    
    private View newView(int position, ViewGroup parent) {
        View view = null;
        switch(getItemViewType(position)) {
            case EXCEPTION_MESSAGE_TYPE:
                view = mInflater.inflate(R.layout.violation_stacktrace_message_item, parent, false);
                break;
            case STACKTRACE_ELEMENT_TYPE:
                view = mInflater.inflate(R.layout.violation_stacktrace_entry_item, parent, false); 
                break;
            default:
                throw new IllegalStateException("Invalid view type.");
        }
        return view;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        int viewType = getItemViewType(position);
        if(view == null) {
            view = newView(position, parent);
        }
        
        switch(viewType) {
            case EXCEPTION_MESSAGE_TYPE:
                bindExceptionMessageView((ViolationException)getItem(position), view);
                break;
            case STACKTRACE_ELEMENT_TYPE:
                bindStacktraceEntryView((StackTraceElement)getItem(position), view);
                break;
        }
        
        return view;
    }
    
    private void bindExceptionMessageView(ViolationException exception, View view) {
        TextView exceptionName = (TextView)view.findViewById(R.id.exception_name);
        TextView exceptionMsg = (TextView)view.findViewById(R.id.exception_msg);
        exceptionName.setText(exception.getClassName());
        exceptionMsg.setText(exception.getMessage());
    }
    
    private void bindStacktraceEntryView(StackTraceElement element, View view) {
        TextView classname = (TextView)view.findViewById(R.id.classname);
        TextView method = (TextView)view.findViewById(R.id.method);
        TextView location = (TextView)view.findViewById(R.id.location);
        
        classname.setText(element.getClassName());
        method.setText(element.getMethodName() + "()");
        
        int line = element.getLineNumber();
        if(line == -2) {
            location.setText("Native code");    
        } else if(line == -1) {
            location.setText("Unknown source");
        } else {
            location.setText("at " + element.getFileName() + ":" + element.getLineNumber());
        }
        
    }
    
    

}
