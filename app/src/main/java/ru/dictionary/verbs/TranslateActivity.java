package ru.dictionary.verbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Денис on 22.01.2017.
 */

public class TranslateActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    public static final int ENGLISH = 0;
    public static final int RUSSIAN = 1;
    public static final int CHINES = 2;
    public static final String KEY = "translate_key";
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transalete);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new CustomPagerAdapter());
        pager.setOnPageChangeListener(this);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        setUiPageViewController();
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
