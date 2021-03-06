package com.example.celine_ishimwe_s1906582;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter{

    List<Item> itemList;
    Activity activity;
    public EventAdapter(List<Item> list, Activity activity){
        this.itemList = list;
        this.activity = activity;

    }

    public void updateItems(ArrayList<Item> items){
        Log.d("Event Adapter", String.valueOf(items.size()));

        itemList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.listview, viewGroup, false);

        ImageView imageView = convertView.findViewById(R.id.status);
        TextView roadworkName = convertView.findViewById(R.id.roadworkName);
        TextView road = convertView.findViewById(R.id.road);
        TextView location = convertView.findViewById(R.id.location);
        TextView date = convertView.findViewById(R.id.date);

        Item item = getItem(i);
        String title = item.getTitle();
        String period = item.getStatus();
        String rName = item.getCategory();
        if(period.equals("Current")){
            imageView.setImageResource(R.drawable.currentrw);
        }else if(period.equals("Future")){
            imageView.setImageResource(R.drawable.futurerw);
        }else{
            imageView.setImageResource(R.drawable.pastrw);
        }
        if(rName.equals("RoadOrCarriagewayOrLaneManagement") || rName.equals("Broken down vehicle")){
            roadworkName.setText(rName.substring(0,12));
        }else {
            roadworkName.setText(item.getCategory());
        }
        road.setText(item.getRoad());
        location.setText(title.substring(0, title.indexOf("|")));
        date.setText(item.getEventStart());


        return convertView;
    }
}
