package com.example.celine_ishimwe_s1906582;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
        import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result = "";
    private String url1="";
    private Button rIncidents;
    private Button jPlanner;
    private Button rWorks;
    private Button search;
    // All events
    private String urlSource="http://m.highwaysengland.co.uk/feeds/rss/AllEvents.xml";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout title_container = findViewById(R.id.title_Container);
        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);
        LinearLayout button_container = findViewById(R.id.button_container);
         rIncidents = (Button) findViewById(R.id.buttonInc);
        jPlanner = (Button) findViewById(R.id.buttonJPlanner);
        LinearLayout button_container2 = findViewById(R.id.button_container2);
         rWorks =(Button) findViewById(R.id.rWorks);
         search = (Button) findViewById(R.id.search);
        TextView footerMess = findViewById(R.id.footerText);
        rWorks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoadWorks();
            }
        });
        rIncidents.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoadIncidents();
            }
        });
        jPlanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openJPlanner();
            }
        });
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

//        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
//        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
//        startButton = (Button)findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);
//        Log.e("MyTag","after startButton");
        // More Code goes here
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchBydate.class);
        startActivity(intent);
    }

    private void openJPlanner() {
        Intent intent = new Intent(this, JPlanner.class);
        startActivity(intent);
    }

    public void openRoadWorks(){
        Intent intent = new Intent(this, Roadworks.class);
        startActivity(intent);
    }
    public void openRoadIncidents(){
        Intent intent = new Intent(this, RoadIncidents.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
    }




}