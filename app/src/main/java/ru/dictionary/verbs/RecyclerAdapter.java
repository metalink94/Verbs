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

import java.util.List;

/**
 * Created by Денис on 28.01.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<BDModel> mDataset;
    private Context mContext;

    public RecyclerAdapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTranslate;
        public GridLayout layout;

        public ViewHolder(View v) {
            super(v);
            mTranslate = (TextView) v.findViewById(R.id.translate);
            layout = (GridLayout) v.findViewById(R.id.text_layout);
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

    private TextView getText(String text) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(14f);
        textView.setText(text);
        textView.setTextAppearance(mContext, R.style.ButtonStyle);
        return textView;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layout.setColumnCount(3);
        holder.layout.setRowCount(3);
        holder.layout.addView(getText(mDataset.get(position).original));
        holder.layout.addView(getText(mDataset.get(position).pastParticipal));
        holder.layout.addView(getText(mDataset.get(position).pastSimple));
        holder.mTranslate.setText(mDataset.get(position).russian);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
