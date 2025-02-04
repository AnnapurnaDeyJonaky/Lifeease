package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthArticlesActivity extends AppCompatActivity {

    private String[][] health_details =
            {
                    {"Walking Daily","","","","Click More Details"},
                    {"Benefits of a Balanced Diet","","","","Click More Details"},
                    {"Mental Health Awareness","","","","Click More Details"},
                    {"The Impact of Hydration on Health","","","","Click More Details"},
                    {"Stop Smoking","","","","Click More Details"}

            };

    private int[] images = {
            R.drawable.health1,
            R.drawable.health2,
            R.drawable.health3,
            R.drawable.health4,
            R.drawable.health5
    };

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        lst = findViewById(R.id.listViewHealthArticles);


        Button btnBackHealthArticles = findViewById(R.id.backButtonHealthArticles);
        btnBackHealthArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthArticlesActivity.this,ProductsDisplay.class);
                startActivity(intent);
            }
        });

        list = new ArrayList();

        for (int i=0; i<health_details.length;i++)
        {
            item= new HashMap<String,String>();
            item.put("line1", health_details[i][0]);
            item.put("line2", health_details[i][1]);
            item.put("line3", health_details[i][2]);
            item.put("line4", health_details[i][3]);
            item.put("line5", health_details[i][4]);
            list.add( item );

        }

        sa = new SimpleAdapter(this,list,
                R.layout.multi_lines,
                new String[] {"line1", "line2", "line3", "line4", "line5" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});

        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(HealthArticlesActivity.this,HealthArticlesDetailsActivity.class);
                intent.putExtra("text1",health_details[i][0]);
                intent.putExtra("text2",images[i]);

                startActivity(intent);
            }
        });




    }
}