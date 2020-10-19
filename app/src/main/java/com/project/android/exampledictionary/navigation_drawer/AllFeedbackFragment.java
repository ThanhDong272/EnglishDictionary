package com.project.android.exampledictionary.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.adapter.FeedbackAdapter;
import com.project.android.exampledictionary.database.DatabaseFeedbackHelper;
import com.project.android.exampledictionary.model.Feedback;

import java.util.ArrayList;

public class AllFeedbackFragment extends Fragment {

    DatabaseFeedbackHelper databaseFeedbackHelper;
    EditText editTextDelete;
    Button buttonDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_view_feedback, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("ALL FEEDBACK");

        databaseFeedbackHelper = new DatabaseFeedbackHelper(getContext());

        editTextDelete = getActivity().findViewById(R.id.editText_delete);
        buttonDelete = getActivity().findViewById(R.id.btn_delete);

        fillListview();
        deleteData();
    }

    public void fillListview() {
        ListView myListview = getActivity().findViewById(R.id.list_item);
        DatabaseFeedbackHelper dbhelper = new DatabaseFeedbackHelper(getContext());

        ArrayList<Feedback> feedbacksList = dbhelper.getAllData();

        FeedbackAdapter feedbackAdapter = new FeedbackAdapter(feedbacksList, getContext());
        myListview.setAdapter(feedbackAdapter);
    }

    public void deleteData() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = databaseFeedbackHelper.deleteData(editTextDelete.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(getContext(), "Feedback Deleted", Toast.LENGTH_LONG).show();
                    fillListview();
                } else {
                    Toast.makeText(getContext(), "Feedback not Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
