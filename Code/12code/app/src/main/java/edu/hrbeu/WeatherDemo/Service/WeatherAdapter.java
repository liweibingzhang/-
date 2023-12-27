package edu.hrbeu.WeatherDemo.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.hrbeu.WeatherDemo.DB.Config;
import edu.hrbeu.WeatherDemo.Weather.Weather;

public class WeatherAdapter {

	public static void GetWeatherData() throws IOException, Throwable {

		String areaid = Config.CityName;
		String type = "forecast_v";
		String appid = "Please input your appid";//���й������������appid
		String appid_six = "Please input";//���й������������appid��ǰ��λ
		String private_key = "Please input your key";//���й������������key

		Date dt = new Date();// �������Ҫ��ʽ,��ֱ����dt,dt���ǵ�ǰϵͳʱ��
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// ������ʾ��ʽ
		String nowTime = df.format(dt);// ��DateFormat��format()������dt�л�ȡ����yyyy/MM/dd
										// HH:mm:ss��ʽ��ʾ201506051830

		// ��Ҫ���ܵ�����
		String public_key = "http://open.weather.com.cn/data/?areaid=" + areaid
				+ "&type=" + type + "&date=" + nowTime + "&appid=" + appid;
		// ��Կ
		String key = EncodeUtil.standardURLEncoder(public_key, private_key);
		String url = "http://open.weather.com.cn/data/?areaid=" + areaid
				+ "&type=" + type + "&date=" + nowTime + "&appid=" + appid_six
				+ "&key=" + key;

		String response = null;
		// ��ȡ����
		try {
			// ����һ��Ĭ�ϵ�HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// ����һ��GET����
			HttpGet request = new HttpGet(url);
			// ����GET���󣬲�����Ӧ����ת�����ַ���
			response = httpclient.execute(request, new BasicResponseHandler());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response);
		getWeather(response);
		
	}

	public static void getWeather(String strResult) {

		try {
			// /����
			JSONObject jsonObject;
			// String a = new String(strResult, "UTF-8");
			jsonObject = new JSONObject(strResult);

			JSONObject c = jsonObject.getJSONObject("c");
			JSONObject f = jsonObject.getJSONObject("f");

			// Weather weather = new Weather();
			Weather.city = c.getString("c3");

			// ///////////////////////////////////////////////////////
			byte[] converttoBytes = Weather.city.getBytes("ISO-8859-1");
			String s1 = new String(converttoBytes);
			// System.out.println(s1);
			Weather.city = s1;
			// ////////////////////////////////////////////////////////////

			Date dt = new Date();// �������Ҫ��ʽ,��ֱ����dt,dt���ǵ�ǰϵͳʱ��
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// ������ʾ��ʽ
			String nowTime = df.format(dt);// ��DateFormat��format()������dt�л�ȡ����yyyy/MM/dd
											// HH:mm:ss��ʽ��ʾ201506051830
			Weather.date=f.getString("f0");
					
			int hh = dt.getHours();
			Boolean dayflag = true;
			if (hh > 18 || hh < 8)
				dayflag = false;
			// ////////////////////////////////////////////////////

			JSONArray f1 = f.getJSONArray("f1");

			for (int i = 0; i < f1.length(); i++) {

				JSONObject jsob = (JSONObject) f1.get(i);
				Weather.weatherD[i] = jsob.getString("fa");
				Weather.temperatureD[i] = jsob.getString("fc");
				Weather.windDD[i] = jsob.getString("fe");
				Weather.windPD[i] = jsob.getString("fg");
				
				Weather.weatherN[i] = jsob.getString("fb");
				Weather.temperatureN[i] = jsob.getString("fd");
				Weather.windDN[i] = jsob.getString("ff");
				Weather.windPN[i] = jsob.getString("fh");

			}
			
			if (dayflag)// ����
			{
				Weather.current_weather = Weather.weatherD[0];
				Weather.current_temperature =Weather.temperatureD[0];
				Weather.current_windD = Weather.windDD[0];
				Weather.current_windP = Weather.windPD[0];
			} else {
				Weather.current_weather = Weather.weatherN[0];
				Weather.current_temperature =Weather.temperatureN[0];
				Weather.current_windD = Weather.windDN[0];
				Weather.current_windP = Weather.windPN[0];
			}

			// return Weather;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return null;
	}


}
