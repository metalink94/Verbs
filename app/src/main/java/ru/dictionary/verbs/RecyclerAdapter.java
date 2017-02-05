package ru.dictionary.verbs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Денис on 28.01.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<BDModel> mDataset;
    private Context mContext;
    private int mExtra;

    public RecyclerAdapter(Context aContext, int intExtra) {
        mContext = aContext;
        mExtra = intExtra;
        mDataset = new ArrayList<>();
    }

    public RecyclerAdapter(Context context, List<BDModel> mData, int intExtra) {
        mContext = context;
        mDataset = mData;
        mExtra = intExtra;
    }

    public void addItem(int position, BDModel item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(BDModel item) {
        mDataset.add(item);
        notifyDataSetChanged();
    }

    public void removeList() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    public List<BDModel> getList() {
        return mDataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTranslate;
        public LinearLayout rowTranslate;

        public ViewHolder(View v) {
            super(v);
            mTranslate = (TextView) v.findViewById(R.id.translate);
            rowTranslate = (LinearLayout) v.findViewById(R.id.row_translate);
        }
    }

    public RecyclerAdapter(Context aContext, List<BDModel> myDataset) {
        mDataset = myDataset;
        mContext = aContext;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BDModel item = mDataset.get(position);
        holder.rowTranslate.removeAllViews();
        holder.mTranslate.setText(setTranslate(item));
        ArrayList<String> strings = new ArrayList<>();
        strings.add(item.original);
        strings.addAll(Arrays.asList(item.pastSimple.split("\\s*,\\s*")));
        strings.addAll(Arrays.asList(item.pastParticipal.split("\\s*,\\s*")));
        ArrayList<String> transcription = new ArrayList<>();
        transcription.add(item.transcription);
        transcription.addAll(Arrays.asList(item.pastTranscription.split("\\s*,\\s*")));
        transcription.addAll(Arrays.asList(item.pastParticipalTranscription.split("\\s*,\\s*")));
        for (LinearLayout layout : setLayouts(strings, transcription)) {
            holder.rowTranslate.addView(layout);
        }
    }

    private TextView setTextView(final String english, final String transcription) {
        final TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) Utils.convertDpToPixel(2f,mContext);
        params.rightMargin = (int) Utils.convertDpToPixel(2f,mContext);
        textView.setLayoutParams(params);
        textView.setText(english);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().equals(english)) {
                    textView.setText(transcription);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.transcription_color));
                } else {
                    textView.setText(english);
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }
        });
        textView.setTextSize(15f);
        textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_transparent));
        textView.setPadding((int) Utils.convertDpToPixel(5f,mContext),(int) Utils.convertDpToPixel(4f,mContext),
                (int) Utils.convertDpToPixel(5f,mContext), (int) Utils.convertDpToPixel(4f,mContext));
        return textView;
    }

    private ArrayList<LinearLayout> setLayouts(ArrayList<String> english, ArrayList<String> transcription) {
        ArrayList<LinearLayout> linearLayouts = new ArrayList<>();
        ArrayList<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < transcription.size(); i++) {
            textViews.add(setTextView(english.get(i), transcription.get(i)));
        }
        int size = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        LinearLayout layout1 = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        layout1.setLayoutParams(params);
        layout1.setGravity(Gravity.CENTER);
        for (TextView textView : textViews) {
            textView.measure(0, 0);
            size = (int) (size + textView.getMeasuredWidth() + Utils.convertDpToPixel(4f,mContext));
            if (size < metrics.widthPixels) {
                layout.addView(textView);
            } else {
                layout1.addView(textView);
            }
        }
        linearLayouts.add(layout);
        if (layout1.getChildCount() > 0) {
            linearLayouts.add(layout1);
        }
        return linearLayouts;
    }

    private String setTranslate(BDModel item) {
        String translate = "";
        switch (mExtra) {
            case TranslateActivity.RUSSIAN:
                translate = item.russian;
                break;
            case TranslateActivity.CHINES:
                translate = item.chines;
                break;
            case TranslateActivity.ENGLISH:
                translate = item.russian;
                break;
            case TranslateActivity.SPANISH:
                translate = item.spanish;
                break;
        }
        return translate;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
