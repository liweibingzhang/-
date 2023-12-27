package com.example.lab1;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        // 获取编辑文本框和确认按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final EditText editTextDirectory = view.findViewById(R.id.editTextDirectory);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnGoToNews = view.findViewById(R.id.btnGoToNews);
        
        // 添加点击事件处理，发送广播
        btnGoToNews.setOnClickListener(v -> {
            String directoryNumberStr = editTextDirectory.getText().toString();
            try {
                int directoryNumber = Integer.parseInt(directoryNumberStr);
                // 创建一个广播Intent并传递目录数字
                Intent intent = new Intent("com.example.lab1.DIRECTORY_ACTION");
                Log.d("NavigateToNews", "NewsList directoryNumber: " + directoryNumber);
                intent.putExtra("directoryNumber", directoryNumber);
                requireActivity().sendBroadcast(intent);
            } catch (NumberFormatException e) {
                // 处理无效输入
                Toast.makeText(getContext(), "请输入有效的目录数字", Toast.LENGTH_SHORT).show();
            }
        });

        newsList.setOnItemClickListener((parent, view1, position, id) -> {
            if (listener != null) {
                listener.onNewsItemSelected(position);
            }
        });
        return view;
    }

    public interface OnNewsItemSelectedListener {
        void onCreate(Bundle savedInstanceState);

        void onNewsItemSelected(int newsNumber);
    }

    public void setOnNewsItemSelectedListener(OnNewsItemSelectedListener listener) {
        this.listener = listener;
    }
}

