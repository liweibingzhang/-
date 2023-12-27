package edu.hrbeu.WeatherDemo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import edu.hrbeu.WeatherDemo.Service.WeatherService;

public class WeatherDemo extends TabActivity {
    /** Called when the activity is first created. */
	final static int MENU_START_SERVICE = Menu.FIRST;
	final static int MENU_STOP_SERVICE = Menu.FIRST + 1;
	final static int MENU_REFRESH = Menu.FIRST + 2;
	final static int MENU_QUIT = Menu.FIRST + 3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TabHost tabHost = getTabHost();   
        tabHost.addTab(tabHost.newTabSpec("TAB1").
        		setIndicator("天气预报",getResources().getDrawable(R.drawable.tab_weather)).
        		setContent(new Intent(this, WeatherActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("TAB2").
        		setIndicator("历史数据",getResources().getDrawable(R.drawable.tab_history)).
        		setContent(new Intent(this, HistoryActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("TAB3").
        		setIndicator("系统设置",getResources().getDrawable(R.drawable.tab_setup)).
        		setContent(new Intent(this, SetupActivity.class)));
    }
    
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
		//	RefreshWeatherData();
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
}