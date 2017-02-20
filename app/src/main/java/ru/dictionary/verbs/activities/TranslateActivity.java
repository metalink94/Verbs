package ru.dictionary.verbs.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.dictionary.verbs.R;
import ru.dictionary.verbs.models.BDModel;
import ru.dictionary.verbs.utils.Utils;
import ru.dictionary.verbs.utils.adapters.RecyclerAdapter;

import static ru.dictionary.verbs.utils.Utils.getVersion;

/**
 * Created by Денис on 22.01.2017.
 */

public class TranslateActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final int ENGLISH = 0;
    public static final int RUSSIAN = 1;
    public static final int CHINES = 2;
    public static final int SPANISH = 3;
    public static final String KEY = "translate_key";
    public static final String SAVED_FIRST = "SAVED_FIRST";
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private EditText mVerb;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private String mSearchFilter;
    private List<BDModel> mData;
    private SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transalete);
        mData = Utils.getAllData(this);
        setEditText();
        setRecyclerView();
        if (getFirst()) {
            setViewPager();
            setUiPageViewController();
        }
        findViewById(R.id.sendButton).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
    }

    private void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new RecyclerAdapter(this, getIntent().getIntExtra(KEY, 0));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        if (getFirst()) {
            mRecyclerView.setVisibility(View.GONE);
        }
        mRecyclerAdapter.addItems(mData);
        findViewById(R.id.removeText).setOnClickListener(this);
    }

    private void search(String search) {
        if (mRecyclerAdapter.getList() != null && mRecyclerAdapter.getList().size() > 0) {
            mRecyclerAdapter.removeList();
        }
        for (BDModel model : mData) {
            String translate;
            switch (getIntent().getIntExtra(KEY, 0)) {
                case RUSSIAN:
                    translate = model.russian;
                    break;
                case CHINES:
                    translate = model.chines;
                    break;
                case SPANISH:
                    translate = model.spanish;
                    break;
                default:
                    translate = model.russian;
                    break;
            }
            if (model.original.toLowerCase().startsWith(search.toLowerCase()) || translate.toLowerCase().startsWith(search.toLowerCase())) {
                mRecyclerAdapter.addItem(model);
            }
            if (search.length() > 0 && mRecyclerAdapter.getList().size() == 0) {
                showSorryMessage();
            } else {
                closeSorryMessage();
            }
        }
    }

    private void showSorryMessage() {
        findViewById(R.id.sorryLayout).setVisibility(View.VISIBLE);
        TextView sorryMessage = (TextView) findViewById(R.id.sorrMessage);
        TextView descriptionMessage = (TextView) findViewById(R.id.sorryDesription);
        String sorry = "";
        String description = "";
        switch (getIntent().getIntExtra(KEY, 0)) {
            case RUSSIAN:
                sorry = getString(R.string.sorryMessageRus);
                description = getString(R.string.descriptionMessageRus);
                break;
            case ENGLISH:
                sorry = getString(R.string.sorryMessageEng);
                description = getString(R.string.descriptionMessageEng);
                break;
            case CHINES:
                sorry = getString(R.string.sorryMessageChn);
                description = getString(R.string.descriptionMessageChn);
                break;
            case SPANISH:
                sorry = getString(R.string.sorryMessageRus);
                description = getString(R.string.descriptionMessageRus);
                break;
        }
        sorryMessage.setText(sorry);
        descriptionMessage.setText(description);
    }

    private void closeSorryMessage() {
        findViewById(R.id.sorryLayout).setVisibility(View.GONE);
    }

    private void setViewPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        pager.setAdapter(new CustomPagerAdapter());
        pager.setOnPageChangeListener(this);
        saveFirstOpen();
    }

    private void setEditText() {
        mVerb = (EditText) findViewById(R.id.ed_verb);
        mVerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mVerb.getHint().equals(getString(R.string.hint_verb))) {
                    mVerb.setText(mVerb.getHint());
                    mVerb.setSelection(mVerb.getHint().length());
                }
            }
        });
        loadText();
        mVerb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mVerb.getText().length() > 0) {
                    if (!getFirst() && pager_indicator != null) {
                        findViewById(R.id.viewpager).setVisibility(View.GONE);
                        pager_indicator.setVisibility(View.GONE);
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.removeText).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.removeText).setVisibility(View.INVISIBLE);
                }
                mSearchFilter = mVerb.getText().toString();
                search(mSearchFilter);
                saveText(mVerb.getText().toString());
            }
        });
    }

    void saveFirstOpen() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(SAVED_FIRST, false);
        ed.commit();
    }

    boolean getFirst() {
        sPref = getPreferences(MODE_PRIVATE);
        return sPref.getBoolean(SAVED_FIRST, true);
    }

    void saveText(String text) {
        if (text.length() == 0) {
            mVerb.setHint(getString(R.string.hint_verb));
        }
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, text);
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        if (savedText.equals("")) {
            mVerb.setHint(getString(R.string.hint_verb));
        } else {
            mVerb.setHint(savedText);
        }
    }

    private void setUiPageViewController() {

        dotsCount = 3;
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transcription:
                TextView transcription = (TextView) findViewById(R.id.transcription);
                transcription.setText(getString(R.string.transcription));
                transcription.setTextColor(ContextCompat.getColor(this, R.color.transcription_color));
                break;
            case R.id.removeText:
                mVerb.setText("");
                break;
            case R.id.sendButton:
                sendMail();
                break;
            case R.id.about:
                startActivity(new Intent(TranslateActivity.this, AboutActivity.class));
        }
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supportMail));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectMail_notFound));
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n%s %s", getString(R.string.not_found, mVerb.getText().toString()), getString(R.string.android), getVersion()));

        startActivity(Intent.createChooser(intent, getString(R.string.sendMail)));
    }

    class CustomPagerAdapter extends PagerAdapter {

        private static final int COUNT = 3;
        private int mLayouts[] = {R.layout.viewpager_first_page, R.layout.viewpager_second_page, R.layout.viewpager_third_page};

        public CustomPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(TranslateActivity.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(mLayouts[position], container, false);
            container.addView(layout);
            switch (position) {
                case 0:
                    TextView descriptionFirst = (TextView) findViewById(R.id.description_first);
                    if (getIntent().getIntExtra(KEY, 0) == ENGLISH) {
                        descriptionFirst.setText(getString(R.string.english_1));
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN) {
                        descriptionFirst.setText(getString(R.string.russian_1));
                    } else {
                        descriptionFirst.setText(getString(R.string.chn_1));
                    }
                    break;
                case 1:
                    TextView descriptionSecond = (TextView) findViewById(R.id.description_second);
                    if (getIntent().getIntExtra(KEY, 0) == ENGLISH) {
                        descriptionSecond.setText(getString(R.string.english_2));
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN) {
                        descriptionSecond.setText(getString(R.string.russian_2));
                    } else {
                        descriptionSecond.setText(getString(R.string.chn_2));
                    }
                    break;
                case 2:
                    TextView descriptionThird = (TextView) findViewById(R.id.description_third);
                    if (getIntent().getIntExtra(KEY, 0) == ENGLISH) {
                        descriptionThird.setText(getString(R.string.english_3));
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN) {
                        descriptionThird.setText(getString(R.string.russian_3));
                    } else {
                        descriptionThird.setText(getString(R.string.chn_3));
                    }
                    findViewById(R.id.transcription).setOnClickListener(TranslateActivity.this);
                    break;
            }
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
