package com.example.celine_ishimwe_s1906582;

import static com.example.celine_ishimwe_s1906582.R.color.whiteGrey;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.net.URLConnection;

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
        rIncidents.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoadIncidents();
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
    public void openRoadIncidents(){
        Intent intent = new Intent(this, RoadIncidents.class);
        startActivity(intent);
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();

    } //

    @Override
    public void onClick(View v)
    {
        Log.e("MyTag","in onClick");
        startProgress();
        Log.e("MyTag","after startProgress");
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

    }


}