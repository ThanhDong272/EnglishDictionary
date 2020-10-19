package com.project.android.exampledictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.activity.WordMeaningActivity;
import com.project.android.exampledictionary.model.Favorite;

import java.util.ArrayList;

public class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerViewAdapterFavorite.FavoriteViewHolder> {
    private ArrayList<Favorite> mFavoriteList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewWord;
        public ImageView mDeleteImage;

        public FavoriteViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextViewWord = itemView.findViewById(R.id.en_word);
            mDeleteImage = itemView.findViewById(R.id.icon_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String text = mFavoriteList.get(position).getWord();

                    Intent intent = new Intent(mContext, WordMeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word", text);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapterFavorite(Context context, ArrayList<Favorite> favoriteList) {
        this.mContext = context;
        this.mFavoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_layout, parent, false);
        FavoriteViewHolder favoriteViewHolder = new FavoriteViewHolder(v, mListener);
        return favoriteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite currentFavorite = mFavoriteList.get(position);

        holder.mDeleteImage.setImageResource(currentFavorite.getImageDelete());
        holder.mTextViewWord.setText(currentFavorite.getWord());
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

}
