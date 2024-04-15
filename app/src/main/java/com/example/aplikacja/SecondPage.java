package com.example.aplikacja;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class SecondPage extends AppCompatActivity {
    private EditText sourceLanguage;
    private TextView translatedLanguage;
    private MaterialButton SourceChooseLanguageButton;
    private MaterialButton TranslatedLanguageButton;
    private MaterialButton TranslationButton;
    private TranslatorOptions TranslatorOptions;
    private Translator Translator;
    private ArrayList<ModelLanguage>languageArrayList;
    private static final String TAG = "MAIN_TAG";
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "Select language";
    private String destinationLanguageTitle = "Select language";
    private String destinationLanguageCode = "ur";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Zmiana tła
        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        ImageButton button = findViewById(R.id.prev_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondPage.this,MainActivity.class);
                startActivity(intent);
            }

        });

        ImageButton changeLanguageButton = findViewById(R.id.change_language);
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapLanguages();
            }
        });

        sourceLanguage = findViewById(R.id.SourceLanguage);
        translatedLanguage =findViewById(R.id.TranslatedLanguage);
        SourceChooseLanguageButton = findViewById(R.id.SourceLanguageButton);
        TranslatedLanguageButton = findViewById(R.id.TranslatedLanguageButton);
        TranslationButton = findViewById(R.id.Translation_button);
        loadLanguages();
        
        SourceChooseLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLanguageChose();

            }
        });
        TranslatedLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });
        TranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });



    }
    private String sourceLanguageText = " ";
    private void validateData() {
        sourceLanguageText = sourceLanguage.getText().toString().trim();
        Log.d(TAG, "validateData: sourceLanguageText"+ sourceLanguageText );
        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this,"Enter text to translate...", Toast.LENGTH_SHORT).show();
        } else if (sourceLanguageTitle.equals("Select language")) {
            Toast.makeText(this, "Please select source language.", Toast.LENGTH_SHORT).show();
        } else {
            startTranslations();
        }
    }

    private void startTranslations() {

        TranslatorOptions = new TranslatorOptions.Builder().setSourceLanguage(sourceLanguageCode).setTargetLanguage(destinationLanguageCode).build();
        Translator = Translation.getClient(TranslatorOptions);
        DownloadConditions downloadConditions = new DownloadConditions.Builder().requireWifi().build();
        Translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"On success: model ready, starting translate");
                Translator.translate(sourceLanguageText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String translatedText) {
                        Log.d(TAG, "onSuccess: TranslatedText"+ translatedText);
                        translatedLanguage.setText(translatedText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                        Toast.makeText(SecondPage.this ,"Failed due to"+ e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                });

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                        Toast.makeText(SecondPage.this ,"Failed due to"+ e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                });
    }

    private void sourceLanguageChose(){
        PopupMenu popupMenu = new PopupMenu(this, SourceChooseLanguageButton);
        for(int i=0;i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int  position = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).LanguageCode;
                sourceLanguageTitle = languageArrayList.get(position).LanguageTitle;
                SourceChooseLanguageButton.setText(sourceLanguageTitle);
                //show in logs
                Log.d(TAG, "onMenuItemClick: sourceCode" + sourceLanguageCode);
                Log.d(TAG, "onMenuItemClick: titleCode" + sourceLanguageTitle);
                return false;
            }
        });
    }
    private void destinationLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this, TranslatedLanguageButton  );
        for(int i=0;i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int  position = item.getItemId();

                destinationLanguageCode = languageArrayList.get(position).LanguageCode;
                destinationLanguageTitle = languageArrayList.get(position).LanguageTitle;
                TranslatedLanguageButton.setText(destinationLanguageTitle);
                //show in logs
                Log.d(TAG, "onMenuItemClick: destinationLanguageCode" + destinationLanguageCode);
                Log.d(TAG, "onMenuItemClick: destinationLanguageTitle" + destinationLanguageTitle);

                return false;
            }
        });

    }


    private void swapLanguages() {
        String temp = sourceLanguage.getText().toString();
        sourceLanguage.setText(translatedLanguage.getText().toString());
        translatedLanguage.setText(temp);

        String tempCode = sourceLanguageCode;
        sourceLanguageCode = destinationLanguageCode;
        destinationLanguageCode = tempCode;

        int tempId = SourceChooseLanguageButton.getId();
        SourceChooseLanguageButton.setId(TranslatedLanguageButton.getId());
        TranslatedLanguageButton.setId(tempId);

        MaterialButton tempButton = SourceChooseLanguageButton;
        SourceChooseLanguageButton = TranslatedLanguageButton;
        TranslatedLanguageButton = tempButton;

        SourceChooseLanguageButton.setText(sourceLanguageTitle);
        TranslatedLanguageButton.setText(destinationLanguageTitle);
    }


    private void loadLanguages() {
        languageArrayList=new ArrayList<>();
        List<String>languageCodeList = TranslateLanguage.getAllLanguages();
        for(String LanguageCode:languageCodeList){
            String LanguageTitle = new Locale(LanguageCode).getDisplayLanguage();
            Log.d(TAG,"loadLanguages: LanguageCode "+ LanguageCode);
            Log.d(TAG,"loadLanguages: LanguageTitle "+ LanguageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(LanguageCode,LanguageTitle);
            languageArrayList.add(modelLanguage);
        }
    }

}