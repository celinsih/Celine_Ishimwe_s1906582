package com.example.celine_ishimwe_s1906582;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Roadworks extends AppCompatActivity {
    ListView listViewRss;
    ArrayList<Item> events;
    Object roadworkItem;
    EventAdapter eventAdapter;
    ArrayList<String> location;
    private Item item;
    private String text;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_incidents_layover);
        location = new ArrayList<String>();
        new ProcessInBackground().execute();
        listViewRss = findViewById(R.id.roadworklist);
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(events, this);
        listViewRss.setAdapter(eventAdapter);
        back = findViewById(R.id.back);
        Spinner filterDropdown =findViewById(R.id.filterDropdown);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.Filter, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        filterDropdown.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

    }
    public InputStream getInputStream(URL url){
        try {
            return url.openConnection().getInputStream();
        }catch (IOException e){
            return null;
        }
    }
    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String Status()  {
        String status = "";
        try {

            Date dateStart =  new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(item.getEventStart());
            Date  dateEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(item.getEventEnd());
            assert dateStart != null;
            if(dateStart.before(new Date())){

                assert dateEnd != null;
                if(dateEnd.before(new Date())){

                    status = "Past";
                }else{
                    status = "Current";
                }

            }else{

                status = "Future";

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return status;
    }

    public  class ProcessInBackground extends AsyncTask<Integer, Integer, Exception>{
        ProgressDialog progressDialog = new ProgressDialog(Roadworks.this);
        Exception exception = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Busy loading data... please wait!");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try {
                URL url= new URL("http://m.highwaysengland.co.uk/feeds/rss/AllEvents.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url),"UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();
                for(int i=0; i<47; i++){
                    eventType = xpp.next();
                }
                while ( eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equalsIgnoreCase("item")){
                                item = new Item();
                            }
                            break;

                        case XmlPullParser.TEXT:
                             text = xpp.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if(xpp.getName().equalsIgnoreCase("title")) {
                                item.setTitle(text);

                            }  else if (xpp.getName().equalsIgnoreCase("category")) {

                                if(item.getCategory() == null){
                                    item.setCategory(text);
                                }
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                item.setDescription(text);
                            }else if(xpp.getName().equalsIgnoreCase("item")) {
                                String status = Status();
                                item.setStatus(status);
                                events.add(item);
                            }else if(xpp.getName().equalsIgnoreCase("road")){
                                item.setRoad(text);
                            }else if(xpp.getName().equalsIgnoreCase("region")) {
                                item.setRegion(text);
                            }else if(xpp.getName().equalsIgnoreCase("latitude")) {
                                item.setLatitude(text);
                            }else if(xpp.getName().equalsIgnoreCase("longitude")) {
                                item.setLongitude(text);
                            }else if(xpp.getName().equalsIgnoreCase("overallStart")) {
                                item.setEventStart(text);
                            }else if(xpp.getName().equalsIgnoreCase("overallEnd")) {
                                item.setEventEnd(text);
                            }
                            break;

                        default:
                            break;
                    }

                    eventType = xpp.next();

                }
            }catch (MalformedURLException e){
                exception = e;

            }catch (XmlPullParserException e){
                exception =e;

            }catch (IOException e){
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayList<Item> list = new ArrayList<>();

            for(Item item: events){

                if(item.getCategory().equals("Road Works")){
                    list.add(item);
                }

            }

            eventAdapter.updateItems(list);
            progressDialog.dismiss();
        }
    }
}