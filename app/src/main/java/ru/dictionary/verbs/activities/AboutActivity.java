package ru.dictionary.verbs.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ru.dictionary.verbs.R;
import ru.dictionary.verbs.utils.Utils;

/**
 * Created by Денис on 20.02.2017.
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mSendDesMail;
    TextView mSendDevMail;
    TextView mMainSite;
    TextView mDesSite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setViews();
    }

    private void setViews() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.change_language).setOnClickListener(this);
        findViewById(R.id.sendMail_about).setOnClickListener(this);
        mSendDesMail = (TextView) findViewById(R.id.send_mail_des);
        mSendDesMail.setOnClickListener(this);
        mSendDevMail = (TextView) findViewById(R.id.send_mail_dev);
        mSendDevMail.setOnClickListener(this);
        mMainSite = (TextView) findViewById(R.id.main_site);
        mMainSite.setOnClickListener(this);
        mDesSite = (TextView) findViewById(R.id.site_des);
        mDesSite.setOnClickListener(this);
    }

    @Override
    public void onClick(View aView) {
        switch (aView.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.change_language:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.send_mail_des:
                Utils.sendMail(this, mSendDesMail.getText().toString(),null);
                break;
            case R.id.send_mail_dev:
                Utils.sendMail(this, mSendDevMail.getText().toString(),null);
                break;
            case R.id.main_site:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s%s",getString(R.string.http), mMainSite.getText().toString()))));
                break;
            case R.id.site_des:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s%s",getString(R.string.http), mDesSite.getText().toString()))));
                break;
            case R.id.sendMail_about:
                Utils.sendMail(this, null, null);
        }
    }
}
