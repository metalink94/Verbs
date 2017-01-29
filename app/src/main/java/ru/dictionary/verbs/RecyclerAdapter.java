package ru.dictionary.verbs;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public void addItem(int position, BDModel item ){
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(BDModel item){
        mDataset.add(item);
        notifyDataSetChanged();
    }

    public void removeList() {
        for (int i = 0; i < mDataset.size(); i++) {
            notifyItemRemoved(i);
        }
        mDataset.clear();
    }

    public List<BDModel> getList() {
        return mDataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTranslate;
        public TextView original;
        public TextView pastSimple;
        public TextView pastIdent;

        public ViewHolder(View v) {
            super(v);
            mTranslate = (TextView) v.findViewById(R.id.translate);
            original = (TextView) v.findViewById(R.id.original);
            pastSimple = (TextView) v.findViewById(R.id.pastSimple);
            pastIdent = (TextView) v.findViewById(R.id.pastIndetifal);
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
        }
        holder.original.setText(item.original);
        holder.original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.original.getText().equals(item.original)) {
                    holder.original.setText(item.transcription);
                } else {
                    holder.original.setText(item.original);
                }
            }
        });
        holder.pastSimple.setText(item.pastSimple);
        holder.pastSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pastSimple.getText().equals(item.pastSimple)) {
                    holder.pastSimple.setText(item.pastTranscription);
                } else {
                    holder.pastSimple.setText(item.pastSimple);
                }
            }
        });
        holder.pastIdent.setText(item.pastParticipal);
        holder.pastIdent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.pastIdent.getText().equals(item.pastParticipal)) {
                    holder.pastIdent.setText(item.pastParticipalTranscription);
                } else {
                    holder.pastIdent.setText(item.pastParticipal);
                }
            }
        });
        holder.mTranslate.setText(translate);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
