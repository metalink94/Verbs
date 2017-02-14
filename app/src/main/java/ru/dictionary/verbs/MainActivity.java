package ru.dictionary.verbs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Field;

import static ru.dictionary.verbs.Utils.getVersion;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mEnglish;
    TextView mSpanish;
    TextView mRussian;
    TextView mChina;

    private void setView() {
        mEnglish = (TextView) findViewById(R.id.btn_english);
        mSpanish = (TextView) findViewById(R.id.btn_espanol);
        mRussian = (TextView) findViewById(R.id.btn_russian);
        mChina = (TextView) findViewById(R.id.btn_chines);
        mEnglish.setOnClickListener(this);
        mSpanish.setOnClickListener(this);
        mRussian.setOnClickListener(this);
        mChina.setOnClickListener(this);
        findViewById(R.id.sendMail).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_english:
                startActivity(new Intent(this, TranslateActivity.class).putExtra(TranslateActivity.KEY, TranslateActivity.ENGLISH));
                break;
            case R.id.btn_espanol:
                startActivity(new Intent(this, TranslateActivity.class).putExtra(TranslateActivity.KEY, TranslateActivity.SPANISH));
                break;
            case R.id.btn_russian:
                startActivity(new Intent(this, TranslateActivity.class).putExtra(TranslateActivity.KEY, TranslateActivity.RUSSIAN));
                break;
            case R.id.btn_chines:
                startActivity(new Intent(this, TranslateActivity.class).putExtra(TranslateActivity.KEY, TranslateActivity.CHINES));
                break;
            case R.id.sendMail:
                sendMail();

                break;
        }
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supportMail));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectMail));
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s", getString(R.string.android), getVersion()));

        startActivity(Intent.createChooser(intent, getString(R.string.sendMail)));
    }

}
