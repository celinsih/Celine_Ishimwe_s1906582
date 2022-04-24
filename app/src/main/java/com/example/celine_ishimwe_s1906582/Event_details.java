package com.example.celine_ishimwe_s1906582;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Event_details extends AppCompatActivity {

    TextView status;
    TextView title;
    TextView region;
    TextView description;
    TextView event_start;
    TextView event_end;
    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_layout);

        Intent intent = getIntent();
        status = findViewById(R.id.status_);
        title =findViewById(R.id.title);
        region = findViewById(R.id.region);
        description = findViewById(R.id.description);
        event_end = findViewById(R.id.eventEnd);
        event_start = findViewById(R.id.eventStart);
        back =findViewById(R.id.back);
        if(intent != null){
            String Status = intent.getStringExtra("status");
            status.setText(Status);
            String Title = intent.getStringExtra("road work");
            title.setText(Title);
            String Locat = intent.getStringExtra("region");
            region.setText(Locat);
            String Descr = intent.getStringExtra("location");
            description.setText(Descr);
            String start = intent.getStringExtra("event start");
            event_start.setText(start);
            String end = intent.getStringExtra("event end");
            event_end.setText(end);
        }

    }

    public void back(View view) {
        finish();
    }
}