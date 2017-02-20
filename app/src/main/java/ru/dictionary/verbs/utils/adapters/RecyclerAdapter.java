package ru.dictionary.verbs.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import ru.dictionary.verbs.R;
import ru.dictionary.verbs.activities.TranslateActivity;
import ru.dictionary.verbs.models.BDModel;
import ru.dictionary.verbs.models.WordModel;
import ru.dictionary.verbs.utils.Utils;

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

    public void addItems(List<BDModel> items) {
        mDataset.addAll(items);
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
        public GridLayout rowTranslate;

        ViewHolder(View v) {
            super(v);
            mTranslate = (TextView) v.findViewById(R.id.translate);
            rowTranslate = (GridLayout) v.findViewById(R.id.row_translate);
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
        holder.rowTranslate.addView(setTextView(item.original, item.transcription));
        WordModel model = new WordModel();
        model.setList(Arrays.asList(item.pastSimple.split("\\s*,\\s*")), Arrays.asList(item.pastTranscription.split("\\s*,\\s*")));
        holder.rowTranslate.addView(setLayout(model.getList()));
        model.setList(Arrays.asList(item.pastParticipal.split("\\s*,\\s*")), Arrays.asList(item.pastParticipalTranscription.split("\\s*,\\s*")));
        holder.rowTranslate.addView(setLayout(model.getList()));
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

    private TextView createTextView(int aMeasuredWidth) {
        TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) Utils.convertDpToPixel(2f, mContext);
            params.rightMargin = (int) Utils.convertDpToPixel(2f, mContext);
            textView.setLayoutParams(params);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        textView.setTextSize(15f);
        textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_transparent));
        textView.setPadding((int) Utils.convertDpToPixel(5f,mContext),(int) Utils.convertDpToPixel(4f,mContext),
                (int) Utils.convertDpToPixel(5f,mContext), (int) Utils.convertDpToPixel(4f,mContext));
        return textView;
    }

    private ArrayList<TextView> setTextViews(List<WordModel> aWordModels) {
        ArrayList<TextView> textViews = new ArrayList<>();
        String biggestWord = "";
        String biggetsTranscription = "";
        ListIterator<WordModel> iterator = aWordModels.listIterator();
        WordModel model = new WordModel();
        while (iterator.hasNext()) {
            WordModel wordModel = iterator.next();
            if (wordModel.transcription.length() > biggetsTranscription.length()) {
                biggetsTranscription = wordModel.transcription;
                biggestWord = wordModel.english;
                model = wordModel;
            }
        }
        aWordModels.remove(model);
        final TextView biggestTextView = createTextView(0);
        biggestTextView.setText(biggestWord);
        biggestTextView.measure(0, 0);
        final String finalBiggestWord = biggestWord;
        final String finalBiggetsTranscription = biggetsTranscription;
        biggestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biggestTextView.getText().equals(finalBiggestWord)) {
                    biggestTextView.setText(finalBiggetsTranscription);
                    biggestTextView.setTextColor(ContextCompat.getColor(mContext, R.color.transcription_color));
                } else {
                    biggestTextView.setText(finalBiggestWord);
                    biggestTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }
        });
        textViews.add(biggestTextView);
        iterator = aWordModels.listIterator();
        while (iterator.hasNext()) {
            final WordModel wordModel = iterator.next();
            final TextView textView = createTextView(biggestTextView.getMeasuredWidth());
            textView.setText(wordModel.english);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getText().equals(wordModel.english)) {
                        textView.setText(wordModel.transcription);
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.transcription_color));
                    } else {
                        textView.setText(wordModel.english);
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    }
                }
            });
            textViews.add(textView);
        }
        return textViews;
    }

    private LinearLayout setLayout(List<WordModel> aWordModels) {
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.addAll(setTextViews(aWordModels));
        /*for (int i = 0; i < transcription.size(); i++) {
            textViews.add(setTextView(english.get(i), transcription.get(i)));
        }*/
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (TextView textView : textViews) {
            textView.measure(0, 0);
            layout.addView(textView);
        }
        return layout;
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
