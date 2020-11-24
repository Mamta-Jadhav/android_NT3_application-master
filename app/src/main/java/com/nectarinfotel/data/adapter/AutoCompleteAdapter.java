package com.nectarinfotel.data.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.nectarinfotel.R;
import com.nectarinfotel.data.jsonmodel.DetailInfo;
import com.nectarinfotel.ui.login.LoginActivity;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    public static ArrayList<DetailInfo> callerlist;
    private ArrayList<DetailInfo> mOriginalValues;
    private ArrayList<DetailInfo> filter;
    private ArrayFilter mFilter;
    Context context;

    public AutoCompleteAdapter(Context context, ArrayList<DetailInfo> callerlist,ArrayList<DetailInfo> callerlistfilter) {

       this. callerlist=callerlist;
        mOriginalValues = new ArrayList<DetailInfo>(callerlist);
        this.context=context;
        this.filter=callerlistfilter;

    }

    @Override
    public int getCount() {
        return callerlist.size();
    }

    @Override
    public String getItem(int position) {
        return callerlist.get(position).getName();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.caller_list_item, null);
        final TextView callername=view.findViewById(R.id.caller_name);
        if (LoginActivity.language.equals("Portuguese")) {
            LoginActivity.langTranslater.translate(callerlist.get(i).getName()).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d("testTrans", s);
                    callername.setText(s);
                }
            });
        }else{
            callername.setText(callerlist.get(i).getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<DetailInfo>(callerlist);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<DetailInfo> list = new ArrayList<DetailInfo>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                DetailInfo info = null;
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<DetailInfo> values = mOriginalValues;
                int count = values.size();

                ArrayList<DetailInfo> newValues = new ArrayList<DetailInfo>(count);

                for (int i = 0; i < count; i++) {

                    String item = values.get(i).getName();
                    String item_id = values.get(i).getId();
                    if (item.toLowerCase().contains(prefixString)) {
                         info=new DetailInfo();
                        info.setName(item);
                        info.setId(item_id);
                        newValues.add(info);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
                Log.d("filter",""+newValues.size());
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.values!=null){
               // callerlist = new ArrayList<DetailInfo>();
                callerlist = (ArrayList<DetailInfo>) results.values;
            }else{
                callerlist = new ArrayList<DetailInfo>();
            }
            Log.d("callerlistAdapter",""+callerlist.size());
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
