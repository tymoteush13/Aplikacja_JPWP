package com.example.aplikacja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView showCountTextView;
    Button count_button;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Aplikacja);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button = findViewById(R.id.SecondActivitybutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondPage.class);
                startActivity(intent);
            }

        });
           Button toast = findViewById(R.id.toast_button);
        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(MainActivity.this, "Hello toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
        showCountTextView = findViewById(R.id.numbers);
        count_button = findViewById(R.id.count_button);
        count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                showCountTextView.setText(String.format(getString(R.string.clicked_d_times), count));
            }
        });



    }



}