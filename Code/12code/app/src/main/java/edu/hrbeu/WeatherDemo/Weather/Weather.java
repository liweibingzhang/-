package edu.hrbeu.WeatherDemo.Weather;


public class Weather {

	public static String city;// c3
	public static String province;// c7
	public static String date;// f0

	public static String current_weather;
	
	public static String current_temperature;
	public static String current_windD;

	public static String current_windP;
	
	public static String[] weatherD = new String[3];// fa
	public static String[] weatherN = new String[3];// fb
	
	public static String[] temperatureD = new String[3];// fc
	public static String[] temperatureN = new String[3];// fd
	public static String[] humidity = new String[3];
	public static String[] windDD = new String[3];// fe
	public static String[] windDN = new String[3];// ff

	public static String[] windPD = new String[3];// fg
	public static String[] windPN = new String[3];// fh
	
	public static String[] sunrise = new String[3];// fi
	public static String[] sundown = new String[3];// 
	
	public static String GetSmsMsgD(){
		String msg = "";
		msg += city + "£¬";
		msg += weatherD[0] + ", " + temperatureD[0]+". ";
//		msg += day[0].day_of_week+", " + day[0].condition + ", " + 
//				day[0].high + "/" + day[0].low; 
		return msg;
	}
	public static String GetSmsMsgN(){
		String msg = "";
		msg += city + "£¬";
		msg += weatherN[0] + ", " + temperatureN[0]+". ";
//		msg += day[0].day_of_week+", " + day[0].condition + ", " + 
//				day[0].high + "/" + day[0].low; 
		return msg;
	}

}
