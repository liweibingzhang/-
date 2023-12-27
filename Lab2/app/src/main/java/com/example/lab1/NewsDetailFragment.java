package com.example.lab1;

import android.view.View;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsDetailFragment extends Fragment {

    private TextView newsDetailText;

    public NewsDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        newsDetailText = view.findViewById(R.id.newsDetailText);
        return view;
    }

    public void updateNewsContent(String content) {
        newsDetailText.setText(content);
    }
}
