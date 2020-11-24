package com.nectarinfotel.data.adapter;

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

import com.nectarinfotel.R;
import com.nectarinfotel.data.jsonmodel.DetailInfo;

import java.util.ArrayList;

public class SiteAutoCompleteAdapter extends BaseAdapter implements Filterable {

    public static ArrayList<DetailInfo> Sitelist;
    private ArrayList<DetailInfo> mOriginalValues;
    private ArrayList<DetailInfo> filter;
    private ArrayFilter mFilter;
    Context context;

    public SiteAutoCompleteAdapter(Context context, ArrayList<DetailInfo> callerlist) {
        Log.d("test", "Site list in adapter : " + callerlist.size());
//        this.Sitelist = callerlist;
        mOriginalValues = new ArrayList<DetailInfo>(callerlist);
        this.context = context;
        Sitelist = new ArrayList<DetailInfo>();
        for(int i = 0; i < callerlist.size(); i++)
        {
            DetailInfo rowItem = callerlist.get(i);
            Sitelist.add(rowItem);
        }

    }

    @Override
    public int getCount() {
        Log.d("Sitelist getcount", "" + Sitelist.size());
        return Sitelist.size();
    }

    @Override
    public String getItem(int position) {
        return Sitelist.get(position).getSite_name();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.caller_list_item, null);
        TextView callername = view.findViewById(R.id.caller_name);
        callername.setText(Sitelist.get(i).getSite_name());
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
                    mOriginalValues = new ArrayList<DetailInfo>(Sitelist);
                }
            }

            if (prefix == null || prefix.length() == 0) {

                Log.d("Sitelist perfilterin","....");

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

                    String item = values.get(i).getSite_name();
                    String item_id = values.get(i).getSite_id();
                    String sitecode = values.get(i).getSite_code();
                    String province = values.get(i).getProvince();
                    String parent_site = values.get(i).getParent_site();
                    if (item.toLowerCase().contains(prefixString)) {
                        info = new DetailInfo();
                        info.setSite_name(item);
                        info.setSite_id(item_id);
                        info.setSite_code(sitecode);
                        info.setProvince(province);
                        info.setParent_site(parent_site);
                        newValues.add(info);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
                Log.d("filter", "" + newValues.size());
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                 Sitelist = new ArrayList<DetailInfo>();
                Sitelist = (ArrayList<DetailInfo>) results.values;
            } else {
                Sitelist = new ArrayList<DetailInfo>();
            }
            Log.d("Sitelist publish result", "" + results.values);
            Log.d("Sitelist publish result", "" + Sitelist.size());
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}