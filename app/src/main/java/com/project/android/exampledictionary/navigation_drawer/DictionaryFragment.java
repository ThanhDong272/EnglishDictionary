package com.project.android.exampledictionary.navigation_drawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.activity.WordMeaningActivity;
import com.project.android.exampledictionary.adapter.RecyclerViewAdapterHistory;
import com.project.android.exampledictionary.database.DatabaseHelper;
import com.project.android.exampledictionary.database.LoadDatabaseAsync;
import com.project.android.exampledictionary.model.History;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryFragment extends Fragment {

    SearchView search;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;

    SimpleCursorAdapter suggestionAdapter;

    ArrayList<History> historyList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;

    RelativeLayout emptyHistory;
    Cursor cursorHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_dictionary, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("DICTIONARY");

        search = (SearchView) getActivity().findViewById(R.id.search_view);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconified(false);
            }
        });

        myDbHelper = new DatabaseHelper(getContext());

        if (myDbHelper.checkDatabase()) {
            openDatabase();
        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(getContext());
            task.execute();
        }

        //setup SimpleCursorAdapter
        final String[] from = new String[]{"en_word"};
        final int[] to = new int[]{R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(getContext(), R.layout.suggestion_row, null, from, to, 0) {
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }
        };

        search.setSuggestionsAdapter(suggestionAdapter);

        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                //Add clicked text to search box
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word, false);

                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(getContext(), WordMeaningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word", clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = search.getQuery().toString();

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(text);

                if (m.matches()) {
                    Cursor c = myDbHelper.getMeaning(text);

                    if (c.getCount() == 0) {
                        showAlertDialog();
                    } else {
                        //search.setQuery("",false);
                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(getContext(), WordMeaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    showAlertDialog();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                //Give Suggestion list margins
                search.setIconifiedByDefault(false);

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(s);

                if (m.matches()) {
                    Cursor cursorSuggestion = myDbHelper.getSuggestions(s);
                    suggestionAdapter.changeCursor(cursorSuggestion);
                }
                return false;
            }
        });


        emptyHistory = (RelativeLayout) getActivity().findViewById(R.id.empty_history);

        //RecyclerView
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        fetch_history();
    }

    public static void openDatabase() {
        try {
            myDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetch_history() {
        historyList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdapterHistory(getContext(), historyList);
        recyclerView.setAdapter(historyAdapter);

        History h;

        if (databaseOpened) {
            cursorHistory = myDbHelper.getHistory();
            if (cursorHistory.moveToFirst()) {
                do {
                    h = new History(cursorHistory.getString(cursorHistory.getColumnIndex("word")));
                    historyList.add(h);
                } while (cursorHistory.moveToNext());
            }

            historyAdapter.notifyDataSetChanged();

            if (historyAdapter.getItemCount() == 0) {
                emptyHistory.setVisibility(View.VISIBLE);
            } else {
                emptyHistory.setVisibility(View.GONE);
            }
        }
    }

    private void showAlertDialog() {
        search.setQuery("", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setTitle("Word Not Found");
        builder.setMessage("Please Search Again");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                search.clearFocus();
            }
        });

        AlertDialog dialog = builder.create();
        //display dialog
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch_history();
    }
}
