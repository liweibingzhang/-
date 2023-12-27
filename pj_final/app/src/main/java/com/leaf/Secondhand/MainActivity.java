package com.leaf.Secondhand;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.Secondhand.adapter.AllCommodityAdapter;
import com.leaf.Secondhand.bean.Commodity;
import com.leaf.Secondhand.util.CommodityDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvAllCommodity;
    List<Commodity> allCommodities = new ArrayList<>();
    ImageButton ibLearning, ibElectronic, ibDaily, ibSports;

    CommodityDbHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        // 在返回到该界面时重新获取数据并更新界面
        // 清空适配器数据
        dbHelper = new CommodityDbHelper(MainActivity.this, CommodityDbHelper.DB_NAME, null, 1);
        adapter = new AllCommodityAdapter(MainActivity.this);
        allCommodities = dbHelper.readAllCommodities();
        adapter.clear();
        adapter.setData(allCommodities);
        lvAllCommodity.setAdapter(adapter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvAllCommodity = findViewById(R.id.lv_all_commodity);
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
        adapter = new AllCommodityAdapter(getApplicationContext());
        allCommodities = dbHelper.readAllCommodities();
        adapter.setData(allCommodities);
        lvAllCommodity.setAdapter(adapter);
        final Bundle bundle = this.getIntent().getExtras();
        final TextView tvStuNumber = findViewById(R.id.tv_student_number);
        String str = "";
        if (bundle != null) {
            str = "欢迎" + bundle.getString("username") + ",你好!";
        }
        tvStuNumber.setText(str);
        //当前登录的学生账号
        final String stuNum = tvStuNumber.getText().toString().substring(2, tvStuNumber.getText().length() - 4);
        ImageButton IbAddProduct = findViewById(R.id.ib_add_product);
        //跳转到添加物品界面
        IbAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCommodityActivity.class);
                if (bundle != null) {
                    //获取学生学号
                    bundle.putString("user_id", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        ImageButton IbPersonalCenter = findViewById(R.id.ib_personal_center);
        //跳转到个人中心界面
        IbPersonalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                if (bundle != null) {
                    //获取学生学号
                    bundle.putString("username1", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("position", position);
                    bundle1.putString("title", commodity.getTitle());
                    bundle1.putString("description", commodity.getDescription());
                    bundle1.putFloat("price", commodity.getPrice());
                    bundle1.putString("phone", commodity.getPhone());
                    bundle1.putString("stuId", stuNum);

                    Intent intent = new Intent(MainActivity.this, ReviewCommodityActivity.class);
                    intent.putExtras(bundle1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putByteArray("picture", commodity.getPicture());
                    intent.putExtras(bundle2);
                    startActivity(intent);
                } catch (Exception e) {
                    // 在这里处理异常
                    e.printStackTrace();
                    Log.e("YourTag", "Exception occurred: " + e.getMessage(), e);
                    // 可以添加一些错误处理的逻辑，例如弹出提示等
                    Toast.makeText(MainActivity.this, "发生异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 为 ListView 添加长按事件监听器
        lvAllCommodity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // 弹出确认购买的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认购买");
                builder.setMessage("是否确认购买该商品？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确认购买，执行删除商品的操作
                        Commodity commodity = allCommodities.get(position);
                        // 执行删除商品的操作，你需要根据具体情况实现
                        dbHelper.deleteMyCommodity(commodity.getTitle(), commodity.getDescription(), commodity.getPrice());
                        // 提示用户
                        Toast.makeText(MainActivity.this, "商品购买成功！", Toast.LENGTH_SHORT).show();
                        allCommodities = dbHelper.readAllCommodities();
                        adapter.setData(allCommodities);
                        lvAllCommodity.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消购买，不执行任何操作
                    }
                });
                builder.show();
                return true; // 返回 true 表示消费了长按事件
            }
        });
        //点击不同的类别,显示不同的商品信息
        ibLearning = findViewById(R.id.ib_learning_use);
        ibElectronic = findViewById(R.id.ib_electric_product);
        ibDaily = findViewById(R.id.ib_daily_use);
        ibSports = findViewById(R.id.ib_sports_good);
        final Bundle bundle2 = new Bundle();
        //学习用品
        ibLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 1);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //电子用品
        ibElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 2);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //生活用品
        ibDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 3);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //体育用品
        ibSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 4);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
    }
    private String saveBitmapToFile(byte[] picture) {
        // 将图片数据保存到文件
        File imagesDir = new File(getExternalCacheDir(), "images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        String fileName = "image_" + System.currentTimeMillis() + ".png";
        File imageFile = new File(imagesDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(picture);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }


}