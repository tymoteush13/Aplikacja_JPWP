package com.example.aplikacja;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;



public class SecondPage extends AppCompatActivity {
    private EditText InputText;
    private TextView OutputText;
    private MaterialButton InputLanguageButton;
    private MaterialButton OutputLanguageButton;
    private Translator Translator;
    private ArrayList<ModelLanguage>languageArrayList;
    private static final String TAG = "MAIN_TAG";
    private String InputLanguageCode;
    private String InputLanguageTitle;
    private String OutputLanguageTitle;
    private String OutputLanguageCode;
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

        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        ImageButton button = findViewById(R.id.prev_button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(SecondPage.this,MainActivity.class);
            startActivity(intent);
        });


        InputText = findViewById(R.id.InputText);
        OutputText =findViewById(R.id.OutputText);
        InputLanguageButton = findViewById(R.id.InputLanguageButton);
        OutputLanguageButton = findViewById(R.id.OutputLanguageButton);
        MaterialButton translationButton = findViewById(R.id.TranslationButton);

        loadLanguages();
        InputLanguageButton.setOnClickListener(v -> selectSourceLanguage());
        OutputLanguageButton.setOnClickListener(v -> selectDestinationLanguage());
        translationButton.setOnClickListener(v -> validateData());


    }
    private String sourceLanguageText = " ";
    private void validateData() {
        sourceLanguageText = InputText.getText().toString().trim();
        Log.d(TAG, "validateData: sourceLanguageText"+ sourceLanguageText );
        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this,"Enter text to translate...", Toast.LENGTH_SHORT).show();
        } else if (InputLanguageTitle == null) {
            Toast.makeText(this, "Please select input language.", Toast.LENGTH_SHORT).show();
        }else if (OutputLanguageTitle == null) {
            Toast.makeText(this, "Please select output language.", Toast.LENGTH_SHORT).show();
        } else {
            startTranslations();
        }
    }

    private void startTranslations() {

        com.google.mlkit.nl.translate.TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(InputLanguageCode).setTargetLanguage(OutputLanguageCode).build();
        Translator = Translation.getClient(translatorOptions);
        DownloadConditions downloadConditions = new DownloadConditions.Builder().requireWifi().build();
        Translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(unused -> {
            Log.d(TAG,"On success: model ready, starting translate");
            Translator.translate(sourceLanguageText).addOnSuccessListener(translatedText -> {
                Log.d(TAG, "onSuccess: TranslatedText"+ translatedText);
                OutputText.setText(translatedText);
                OutputText.setTextColor(getResources().getColor(android.R.color.black));
            }).addOnFailureListener(e -> {
                Log.e(TAG, "onFailure: ",e );
                Toast.makeText(SecondPage.this ,"Failed due to"+ e.getMessage(),Toast.LENGTH_SHORT ).show();
            });

        })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ",e );
                    Toast.makeText(SecondPage.this ,"Failed due to"+ e.getMessage(),Toast.LENGTH_SHORT ).show();
                });
    }

    private void selectSourceLanguage(){
        PopupMenu popupMenu = new PopupMenu(this, InputLanguageButton);
        languageArrayList.sort(Comparator.comparing(ModelLanguage::getLanguageTitle));
        for(int i=0;i<languageArrayList.size();i++){

            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int  position = item.getItemId();

            InputLanguageCode = languageArrayList.get(position).LanguageCode;
            InputLanguageTitle = languageArrayList.get(position).LanguageTitle;
            InputLanguageButton.setText(InputLanguageTitle);

            Log.d(TAG, "onMenuItemClick: sourceCode" + InputLanguageCode);
            Log.d(TAG, "onMenuItemClick: titleCode" + InputLanguageTitle);
            return false;
        });
    }
    private void selectDestinationLanguage(){
        PopupMenu popupMenu = new PopupMenu(this, OutputLanguageButton  );
        for(int i=0;i<languageArrayList.size();i++){
            if (InputLanguageCode != null && InputLanguageCode.equals(languageArrayList.get(i).LanguageCode)) {
                continue;
            }
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int  position = item.getItemId();

            OutputLanguageCode = languageArrayList.get(position).LanguageCode;
            OutputLanguageTitle = languageArrayList.get(position).LanguageTitle;
            OutputLanguageButton.setText(OutputLanguageTitle);

            Log.d(TAG, "onMenuItemClick: destinationLanguageCode" + OutputLanguageCode);
            Log.d(TAG, "onMenuItemClick: destinationLanguageTitle" + OutputLanguageTitle);

            return false;
        });

    }


    private void loadLanguages() {
        languageArrayList=new ArrayList<>();
        List<String>languageCodeList = TranslateLanguage.getAllLanguages();
        Locale polishLocale = new Locale("pl");
        for(String LanguageCode:languageCodeList){
            String LanguageTitle = new Locale(LanguageCode).getDisplayLanguage(polishLocale);

            Log.d(TAG,"loadLanguages: LanguageCode "+ LanguageCode);
            Log.d(TAG,"loadLanguages: LanguageTitle "+ LanguageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(LanguageCode,LanguageTitle);
            languageArrayList.add(modelLanguage);
        }
    }

}