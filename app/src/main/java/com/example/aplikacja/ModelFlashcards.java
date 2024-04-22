package com.example.aplikacja;

public class ModelFlashcards {

    private int id;
    private String word;
    private String translation;

    public ModelFlashcards(String word, String translation, int id) {
        this.word = word;
        this.translation = translation;
        this.id = id;
    }

    public ModelFlashcards() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }


    @Override
    public String toString() {
        return
                word + " - " + translation;
    }
}
