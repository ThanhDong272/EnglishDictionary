package com.project.android.exampledictionary.navigation_drawer;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.adapter.RecyclerViewAdapterFavorite;
import com.project.android.exampledictionary.database.DatabaseHelper;
import com.project.android.exampledictionary.database.LoadDatabaseAsync;
import com.project.android.exampledictionary.model.Favorite;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    String enWord;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;

    ArrayList<Favorite> favoriteList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterFavorite favoriteAdapter;

    RelativeLayout emptyFavorite;
    Cursor cursorFavorite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_favorite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("FAVORITES");

        myDbHelper = new DatabaseHelper(getContext());

        if (myDbHelper.checkDatabase()) {
            openDatabase();
        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(getContext());
            task.execute();
        }

        emptyFavorite = getActivity().findViewById(R.id.empty_favorite);

        //RecyclerView
        recyclerView = getActivity().findViewById(R.id.recycler_view_favorite);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        fetch_favorite();
    }

    private static void openDatabase() {
        try {
            myDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetch_favorite() {
        favoriteList = new ArrayList<>();
        favoriteAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoriteAdapter);

        Favorite favorite;

        if (databaseOpened) {
            cursorFavorite = myDbHelper.getFavorite();
            if (cursorFavorite.moveToFirst()) {
                do {
                    favorite = new Favorite(cursorFavorite.getString(cursorFavorite.getColumnIndex("word")), R.drawable.ic_delete);
                    favoriteList.add(favorite);
                } while (cursorFavorite.moveToNext());
            }


            if (favoriteAdapter.getItemCount() == 0) {
                emptyFavorite.setVisibility(View.VISIBLE);
            } else {
                emptyFavorite.setVisibility(View.GONE);
            }
        }

        favoriteAdapter.setOnItemClickListener(new RecyclerViewAdapterFavorite.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                favoriteList.remove(position);
                favoriteAdapter.notifyItemRemoved(position);
                myDbHelper.deleteFavorite(enWord);
                favoriteAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Favorite Word Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch_favorite();
    }
}
