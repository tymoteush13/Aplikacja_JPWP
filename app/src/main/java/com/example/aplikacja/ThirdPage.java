package com.example.aplikacja;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThirdPage extends AppCompatActivity {

    private Button btn_add,btn_start;
    private EditText et_word, et_translation;
    private ListView lv_FlashcardList;
    private ArrayAdapter customerArrayAdapter;
    private DataBaseHelper dataBaseHelper;
    private static final String TAG = "MAIN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tło
        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();



        btn_add = findViewById(R.id.addBtn);
        btn_start = findViewById(R.id.startBtn);

        et_word = findViewById(R.id.edWord);
        et_translation = findViewById(R.id.edTranslation);

        lv_FlashcardList = findViewById(R.id.rvFlashcard);

        dataBaseHelper = new DataBaseHelper(ThirdPage.this);

        showAllFlashcards(dataBaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ModelFlashcards modelFlashcards;

                try {
                    modelFlashcards = new ModelFlashcards(et_word.getText().toString(),et_translation.getText().toString(),-1);
                    Log.d(TAG,"Correct create ModelFlascards");
                }catch (Exception e){
                    modelFlashcards = new ModelFlashcards(null,null,-1);
                    Log.e(TAG, "onFailure: ",e );
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(ThirdPage.this);

                boolean success = dataBaseHelper.addOne(modelFlashcards);

                showAllFlashcards(dataBaseHelper);

            }
        });

        lv_FlashcardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ModelFlashcards clickedCustomer = (ModelFlashcards) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomer);
                showAllFlashcards(dataBaseHelper);
                return true;
            }
        });



        ImageButton button = findViewById(R.id.prev_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdPage.this,MainActivity.class);
                startActivity(intent);
            }

        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdPage.this,FourthPage.class);
                startActivity(intent);
            }
        });


    }


    private void showAllFlashcards(DataBaseHelper dataBaseHelper) {
        customerArrayAdapter = new ArrayAdapter<ModelFlashcards>(ThirdPage.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
        lv_FlashcardList.setAdapter(customerArrayAdapter);
        Log.d(TAG,"Correct showing all Flascards");
    }
}