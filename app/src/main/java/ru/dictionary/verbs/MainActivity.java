package ru.dictionary.verbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
                startActivity(new Intent(this, TranslateActivity.class));
                break;
            case R.id.btn_espanol:
                startActivity(new Intent(this, TranslateActivity.class));
                break;
            case R.id.btn_russian:
                startActivity(new Intent(this, TranslateActivity.class));
                break;
            case R.id.btn_chines:
                startActivity(new Intent(this, TranslateActivity.class));
                break;
        }
    }
}
