package ru.dictionary.verbs;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Денис on 28.01.2017.
 */

public class Utils {

    private static List<String[]> readCsv(Context context) {
        List<String[]> questionList = new ArrayList<String[]>();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream csvStream = assetManager.open("Verbs.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                questionList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public static ArrayList<BDModel> getAllData(Context aContext) {
        ArrayList<BDModel> bdModels = new ArrayList<>();
        for (String[] array : readCsv(aContext)) {
            BDModel bdModel = new BDModel();
            bdModel.original = array[0];
            bdModel.transcription = array[1];
            bdModel.pastSimple = array[2];
            bdModel.pastTranscription = array[3];
            bdModel.pastParticipal = array[4];
            bdModel.pastParticipalTranscription = array[5];
            bdModel.russian = array[6];
            bdModel.spanish = array[7];
            bdModel.chines = array[8];
            bdModels.add(bdModel);
        }
        Log.d("MainActivity", "BDModelList " + bdModels.size());
        return bdModels;
    }
}
