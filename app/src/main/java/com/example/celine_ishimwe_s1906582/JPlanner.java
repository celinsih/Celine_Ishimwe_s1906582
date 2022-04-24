package com.example.celine_ishimwe_s1906582;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JPlanner extends AppCompatActivity {
    ListView listViewRss;
    ArrayList<Item> events;
    EventAdapter eventAdapter;
    private Item item;
    private String text;
    ProgressDialog progressDialog;
    EditText et_source, journey_date;
    ImageButton display_journey;
    String sDate;
    String sSource;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jplanner);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we load the data...");
        listViewRss = findViewById(R.id.listview);
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(events, this);
       /// listViewRss.setAdapter(eventAdapter);
        listViewRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Road works", "Item is clicked");
                Intent intent = new Intent(JPlanner.this, Event_details.class);
                Item clickedItem = eventAdapter.getItem(i);
                intent.putExtra("status", clickedItem.getStatus());
                intent.putExtra("road work", clickedItem.getCategory());
                intent.putExtra("location", clickedItem.getTitle());
                intent.putExtra("region", clickedItem.getRegion());
                intent.putExtra("event start", clickedItem.getEventStart());
                intent.putExtra("event end", clickedItem.getEventEnd());

                startActivity(intent);
            }
        });
        et_source = findViewById(R.id.et_source);
        journey_date = findViewById(R.id.Joourney_date);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        journey_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(JPlanner.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        display_journey = findViewById(R.id.display_journey);
        display_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 sSource = et_source.getText().toString().trim();
                 sDate = journey_date.getText().toString().toLowerCase();

                if( sSource.equals("") || sDate.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter both location and date", Toast.LENGTH_SHORT).show();
                } else{
                        new ProcessInBackground().execute();

                }

                try {
                    Date sdf = new SimpleDateFormat("MM/dd/yy").parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        journey_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    private ArrayList<Item> DisplayJourney( String sDate, String sSource, ArrayList<Item> list) throws ParseException {
        progressDialog.show();
        ArrayList<Item> filtered = new ArrayList<>();



        for(Item item: list){
            String road  = item.getRoad().toLowerCase();

            Date start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.getEventStart());
            Date end = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.getEventEnd());
            Date sdf = new SimpleDateFormat("MM/dd/yy").parse(sDate);

            //put all the date in same format
            SimpleDateFormat  checkIfEqual = new SimpleDateFormat("yyyyMMdd");
            String startF = checkIfEqual.format(start);
            String endF = checkIfEqual.format(end);
            String userF = checkIfEqual.format(sdf);

            boolean isRoad = false;
            if(road.equals(sSource)){
                isRoad = true;
            }

            if( isRoad  && (startF.equals(userF)|| endF.equals(userF)) ){
                filtered.add(item);
            } else if( isRoad && (sdf.after(start) && sdf.before(end))){
                filtered.add(item);
            }

        }


        return filtered;
    }
    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public InputStream getInputStream(URL url){
        try {
            return url.openConnection().getInputStream();
        }catch (IOException e){
            return null;
        }
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

    public  class ProcessInBackground extends AsyncTask<Integer, Integer, Exception> {
        Exception exception = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                String category = item.getCategory();

                if(category.equals("Road Works")){
                    list.add(item);
                }

            }

            ArrayList<Item> filtered = new ArrayList<>();
            try {
                filtered =  DisplayJourney(sDate, sSource, list);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d("incident", String.valueOf(filtered.size()));
            eventAdapter.updateItems(filtered);
            progressDialog.dismiss();
        }



    }
}