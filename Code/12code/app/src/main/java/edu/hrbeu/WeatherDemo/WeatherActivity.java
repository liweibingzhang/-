package edu.hrbeu.WeatherDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import edu.hrbeu.WeatherDemo.DB.DBAdapter;
import edu.hrbeu.WeatherDemo.Service.WeatherService;
import edu.hrbeu.WeatherDemo.Weather.Weather;

public class WeatherActivity extends Activity {

	final static int MENU_START_SERVICE = Menu.FIRST;
	final static int MENU_STOP_SERVICE = Menu.FIRST + 1;
	final static int MENU_REFRESH = Menu.FIRST + 2;
	final static int MENU_QUIT = Menu.FIRST + 3;

	private DBAdapter dbAdapter;
	private int TIME = 5000;  
	
	int[] DWeatherArray = { R.drawable.d00, R.drawable.d01, R.drawable.d02,
			R.drawable.d03, R.drawable.d04, R.drawable.d05, R.drawable.d06,
			R.drawable.d07, R.drawable.d08, R.drawable.d09, R.drawable.d10,
			R.drawable.d11, R.drawable.d12, R.drawable.d13, R.drawable.d14,
			R.drawable.d15, R.drawable.d16, R.drawable.d17, R.drawable.d18,
			R.drawable.d19, R.drawable.d20, R.drawable.d21, R.drawable.d22,
			R.drawable.d23, R.drawable.d24, R.drawable.d25, R.drawable.d26,
			R.drawable.d27, R.drawable.d28, R.drawable.d29, R.drawable.d30,
			R.drawable.d31, R.drawable.d53 };
	
	String[] windDirect={"无持续风向","东北风","东风","东南风","南风","西南风","西风","西北风","北风","旋转风"};
	String[] windPower={"微风","3-4级","4-5级","5-6级","6-7级","7-8级","8-9级","9-10级","10-11级","11-12级"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_weather);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.LoadConfig();
		
		final Intent serviceIntent = new Intent(this, WeatherService.class);
		startService(serviceIntent);
		
		 handler.postDelayed(runnable, 5000); //每隔1s执行  

	}
	
    Handler handler = new Handler();  
    Runnable runnable = new Runnable() {  
  
        @Override  
        public void run() {  
            // handler自带方法实现定时器  
            try {  
                
                RefreshWeatherData();
                System.out.println("do...");  
                handler.postDelayed(this, TIME);  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
                System.out.println("exception...");  
            }  
        }  
    }; 
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.add(0, MENU_START_SERVICE, 0, "启动服务");
		menu.add(0, MENU_STOP_SERVICE, 1, "停止服务");
		menu.add(0, MENU_REFRESH, 2, "刷新");
		menu.add(0, MENU_QUIT, 3, "退出");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Intent serviceIntent = new Intent(this, WeatherService.class);
		switch (item.getItemId()) {
		case MENU_REFRESH:
			RefreshWeatherData();
			return true;
		case MENU_START_SERVICE:
			startService(serviceIntent);
			return true;
		case MENU_STOP_SERVICE:
			stopService(serviceIntent);
			return true;
		case MENU_QUIT:
			finish();
			break;
		}
		return false;
	}

	private void RefreshWeatherData() {

		// (0) 当前温度
		TextView currentCondition = (TextView) findViewById(R.id.tab_weather_current_condition);
		TextView currentWind = (TextView) findViewById(R.id.tab_weather_current_wind);
		ImageView currentImage = (ImageView) findViewById(R.id.tab_weather_current_image);
		TextView currentCity = (TextView) findViewById(R.id.tab_weather_current_city);

		String msgCondition = "";
		msgCondition += "Temperature：" + Weather.current_temperature + ", ";
		// msgCondition += Weather.current_humidity ;
		currentCondition.setText(msgCondition);

		currentWind.setText(windDirect[Integer.parseInt(Weather.current_windD)] + ", "+windPower[Integer.parseInt(Weather.current_windP)] + ", "
				+ Weather.date);
		
		 currentImage.setBackgroundDrawable(getResources().getDrawable(
					DWeatherArray[Integer.parseInt(Weather.current_weather)]));
		currentCity.setText(Weather.city);

		// (1) 预报：第1天
		TextView forcastD1Date = (TextView) findViewById(R.id.tab_weather_d1_date);
		ImageView forcastD1Image = (ImageView) findViewById(R.id.tab_weather_d1_image);
		TextView forcastD1Temperature = (TextView) findViewById(R.id.tab_weather_d1_temperature);

		forcastD1Date.setText("明天");

		
		forcastD1Image.setBackgroundDrawable(getResources().getDrawable(
				DWeatherArray[Integer.parseInt(Weather.weatherD[1])]));
		
		String msgD1Temperature = Weather.temperatureD[1] + "/"
				+ Weather.temperatureN[1];
		forcastD1Temperature.setText(msgD1Temperature);

		// (2) 预报：第2天
		TextView forcastD2Date = (TextView) findViewById(R.id.tab_weather_d2_date);
		ImageView forcastD2Image = (ImageView) findViewById(R.id.tab_weather_d2_image);
		TextView forcastD2Temperature = (TextView) findViewById(R.id.tab_weather_d2_temperature);

		forcastD2Date.setText("后天");
		forcastD2Image.setBackgroundDrawable(getResources().getDrawable(
				DWeatherArray[Integer.parseInt(Weather.weatherD[2])]));
		
		String msgD2Temperature = Weather.temperatureD[2] + "/"
				+ Weather.temperatureN[2];
		forcastD2Temperature.setText(msgD2Temperature);


	}

}
