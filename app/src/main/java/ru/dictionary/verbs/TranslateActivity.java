package ru.dictionary.verbs;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Денис on 22.01.2017.
 */

public class TranslateActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    public static final int ENGLISH = 0;
    public static final int RUSSIAN = 1;
    public static final int CHINES = 2;
    public static final String KEY = "translate_key";
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
        setViewPager();
        setRecyclerView();
        setUiPageViewController();
    }

    private void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter= new RecyclerAdapter(this, getIntent().getIntExtra(KEY, 0));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setVisibility(View.GONE);
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
                default:
                    translate = model.russian;
                    break;
            }
            if (model.original.toLowerCase().startsWith(search.toLowerCase()) || translate.toLowerCase().startsWith(search.toLowerCase())) {
                    mRecyclerAdapter.addItem(model);
            }
        }
    }

    private void setViewPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        pager.setAdapter(new CustomPagerAdapter());
        pager.setOnPageChangeListener(this);
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
                    findViewById(R.id.viewpager).setVisibility(View.GONE);
                    pager_indicator.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mSearchFilter = mVerb.getText().toString();
                search(mSearchFilter);
                saveText(mVerb.getText().toString());
            }
        });
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
        }
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
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN){
                        descriptionFirst.setText(getString(R.string.russian_1));
                    } else {
                        descriptionFirst.setText(getString(R.string.chn_1));
                    }
                    break;
                case 1:
                    TextView descriptionSecond = (TextView) findViewById(R.id.description_second);
                    if (getIntent().getIntExtra(KEY, 0) == ENGLISH) {
                        descriptionSecond.setText(getString(R.string.english_2));
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN){
                        descriptionSecond.setText(getString(R.string.russian_2));
                    } else {
                        descriptionSecond.setText(getString(R.string.chn_2));
                    }
                    break;
                case 2:
                    TextView descriptionThird = (TextView) findViewById(R.id.description_third);
                    if (getIntent().getIntExtra(KEY, 0) == ENGLISH) {
                        descriptionThird.setText(getString(R.string.english_3));
                    } else if (getIntent().getIntExtra(KEY, 0) == RUSSIAN){
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
