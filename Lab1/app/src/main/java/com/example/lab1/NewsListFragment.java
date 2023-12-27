package com.example.lab1;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
public class NewsListFragment extends Fragment {

    private OnNewsItemSelectedListener listener;

    public NewsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        ListView newsList = view.findViewById(R.id.newsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new String[]{"新闻1", "新闻2", "新闻3", "新闻4","新闻5"});
        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onNewsItemSelected(position);
                }
            }
        });

        return view;
    }

    public interface OnNewsItemSelectedListener {
        void onCreate(Bundle savedInstanceState);

        void onNewsItemSelected(int position);
    }

    public void setOnNewsItemSelectedListener(OnNewsItemSelectedListener listener) {
        this.listener = listener;
    }
}

