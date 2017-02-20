package ru.dictionary.verbs.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Денис on 20.02.2017.
 */

public class WordModel {

    public String english;
    public String transcription;
    List<WordModel> list;

    public void setList(List<String> english, List<String> translate) {
        list = new ArrayList<>();
        for (int i = 0; i < translate.size(); i++) {
            WordModel model = new WordModel();
            model.english = english.get(i);
            model.transcription = translate.get(i);
            list.add(model);
        }
    }

    public List<WordModel> getList() {
        return list;
    }
}
