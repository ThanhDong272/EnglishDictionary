package com.project.android.exampledictionary.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.SendEmail;
import com.project.android.exampledictionary.database.DatabaseFeedbackHelper;

public class FeedbackFragment extends Fragment {

    DatabaseFeedbackHelper databaseFeedbackHelper;

    private EditText mEditTextEmail, mEditTextSubject, mEditTextMessage;
    private Button mButtonSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_feedback, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("FEEDBACK");

        databaseFeedbackHelper = new DatabaseFeedbackHelper(getContext());

        mEditTextEmail = getActivity().findViewById(R.id.editText_your_email);
        mEditTextSubject = getActivity().findViewById(R.id.editText_subject);
        mEditTextMessage = getActivity().findViewById(R.id.editText_improve);

        mButtonSubmit = getActivity().findViewById(R.id.btn_submit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get  input data from view
                String mEmail = mEditTextEmail.getText().toString();
                String mSubject = mEditTextSubject.getText().toString();
                String mMessage = mEditTextMessage.getText().toString();
                new SendEmail(getContext(), mEmail, mSubject, mMessage).execute();

                boolean isInserted = databaseFeedbackHelper.insertData(mEditTextEmail.getText().toString(), mEditTextSubject.getText().toString(), mEditTextMessage.getText().toString());

                if (isInserted) {

                } else {
                    Toast.makeText(getContext(), "Message not Sent", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
