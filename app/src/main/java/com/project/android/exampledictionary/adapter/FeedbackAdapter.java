package com.project.android.exampledictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.model.Feedback;

import java.util.ArrayList;

public class FeedbackAdapter extends BaseAdapter {
    private ArrayList<Feedback> feedbacksList;
    private Context context;

    public FeedbackAdapter(ArrayList<Feedback> list, Context context) {
        this.feedbacksList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.feedbacksList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.feedbacksList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.item_list_view_feedback, null);

            holder = new ViewHolder();
            holder.id = convertView.findViewById(R.id.txt_id);
            holder.email = convertView.findViewById(R.id.txt_email);
            holder.subject = convertView.findViewById(R.id.txt_subject);
            holder.message = convertView.findViewById(R.id.txt_message);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Feedback feedback = feedbacksList.get(position);
        holder.id.setText(feedback.getFeedBackID());
        holder.email.setText(feedback.getEmail());
        holder.subject.setText(feedback.getSubject());
        holder.message.setText(feedback.getMessage());

        return convertView;
    }

    private static class ViewHolder {
        public TextView id;
        public TextView email;
        public TextView subject;
        public TextView message;
    }
}
