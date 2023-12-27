// AdminActivity.java

package com.leaf.Secondhand;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.Secondhand.adapter.MyCommodityAdapter;
import com.leaf.Secondhand.bean.Commodity;
import com.leaf.Secondhand.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ListView lvAllCommodities;
    List<Commodity> allCommodities = new ArrayList<>();
    CommodityDbHelper dbHelper;
    MyCommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lvAllCommodities = findViewById(R.id.lv_all_commodities);
        adapter = new MyCommodityAdapter(getApplicationContext());
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
        allCommodities = dbHelper.readAllCommodities(); // Retrieve all user-published items
        adapter.setData(allCommodities);
        lvAllCommodities.setAdapter(adapter);

        // Long press to delete item
        lvAllCommodities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("提示:").setMessage("确认删除此商品项吗?").setIcon(R.drawable.icon_user).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Commodity commodity = (Commodity) adapter.getItem(position);
                        dbHelper.deleteMyCommodity(commodity.getTitle(),commodity.getDescription(),commodity.getPrice());
                        Toast.makeText(AdminActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        allCommodities = dbHelper.readAllCommodities();
                        adapter.setData(allCommodities);
                        lvAllCommodities.setAdapter(adapter);
                    }
                }).show();
                return false;
            }
        });
        //退出登录按钮点击事件
        Button btnLogOut = findViewById(R.id.btn_Logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("提示:").setMessage("确认退出系统吗?").setIcon(R.drawable.icon_user).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //跳转到登录界面
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }


}
