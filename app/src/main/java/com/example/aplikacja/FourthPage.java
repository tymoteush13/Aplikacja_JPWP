package com.example.aplikacja;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Random;

public class FourthPage extends AppCompatActivity {

    private Button btn_check;
    private TextView tv_word;
    private EditText ed_translation;
    private DataBaseHelper dataBaseHelper;
    private int randomNumber;
    private static final String TAG = "MAIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fourth_page);
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


        btn_check = findViewById(R.id.checkBtn);
        tv_word = findViewById(R.id.tvWord);
        ed_translation = findViewById(R.id.etTranslation);


        dataBaseHelper = new DataBaseHelper(this);

        List<ModelFlashcards> flashcards = dataBaseHelper.getEveryone();

        showFlashcard(flashcards);



        ImageButton button = findViewById(R.id.prev_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourthPage.this,MainActivity.class);
                startActivity(intent);
            }

        });



        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String checkTranslation = ed_translation.getText().toString();
                    String checkWord = flashcards.get(randomNumber).getTranslation();
                    Log.d(TAG,"List of Flashcard is not empty");
                    if (checkTranslation.equals(checkWord)){

                        Toast.makeText(FourthPage.this,"Correct answear", LENGTH_SHORT).show();

                        flashcards.remove(randomNumber);

                        showFlashcard(flashcards);
                        ed_translation.setText("");
                        Log.d(TAG,"Correct answear to flashacard "+checkWord);

                    }else{
                        Toast.makeText(FourthPage.this,"Wrong answear", LENGTH_SHORT).show();
                        showFlashcard(flashcards);
                        ed_translation.setText("");
                        Log.d(TAG,"Wrong answear to flashacard "+checkWord);

                    }
                }catch (Exception e){
                    Log.e(TAG, "onFailure: ",e );
                }



            }
        });




    }

    private void showFlashcard(List<ModelFlashcards> flashcards){
        int listSize = flashcards.size();
        Random random = new Random();


        if(!flashcards.isEmpty()){
            randomNumber = random.nextInt(listSize);
            ModelFlashcards flashcardModel = flashcards.get(randomNumber);
            tv_word.setText(flashcardModel.getWord());
            Log.d(TAG,"Correct show flashcard");
        }else{
            tv_word.setText("You need to create flashcards!");
            Log.d(TAG,"List of Flashcard is not empty");
        }

    }

}