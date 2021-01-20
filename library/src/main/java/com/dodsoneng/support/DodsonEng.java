package com.dodsoneng.support;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;


/***
 * DodsonEng is a class with general use functions.
 * This class is designed to help make it easier to
 * do common Android things such as:
 * printff() - Print a toast with one line
 * debugPrintff() - Debug specific toast with one line
 * GetRandomNumber() - Get a random number passing in the max
 * isNetworkAvailable() - Check network availability
 * AppInstalledorNot() - Check if an app is installed on the device
 * 
 * @author Thomas Dodson
 *
 */
public class DodsonEng {
	
	private final Context context;
	private boolean debug_on;
	
	
	public DodsonEng(Context ctx, boolean debug_on)
    {
        this.context = ctx;
        this.debug_on = debug_on;
        
    }
	
	/**
	 * This function is used for printing a toast to the screen.
	 * @param String text
	 */
	public void printff (String text)
    {
		int duration = Toast.LENGTH_LONG;
	
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    }
	
	/**
	 * This function will print a toast message for
	 * debugging.  The message will only print if the 
	 * (dodsoneng library) boolean debug_on is set to true.
	 * @param String text
	 */
	public void debugPrintff (String text)
    {
		if (debug_on) {
			int duration = Toast.LENGTH_LONG;
		
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
    }
	
	/**
	 * This function will return a random number from
	 * 0 - the parameter value passed in.
	 * @param max
  	 * @return int rand
  	 */
	public int getRandomNumber(int max)
	{
		 int rand = 0;
		 Random random = new Random();
		 rand = random.nextInt(max);
		 return rand;
	}
	
	/**
	 *  This function is used to check if the phone 
	 *  has a network connection or not to 3G/4G/WiFi 
	 */
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		//String networkType = activeNetworkInfo.getTypeName();
	    return activeNetworkInfo != null;
	}
	
	/***
	 * This function is used to check if an app exists on the current device 
	 * @param uri
	 * @return
	 */
	public boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try
        {
               pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
               app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
               app_installed = false;
        }
        return app_installed ;
	}

	/***
	 * This function is used to check if an emial address is valid
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/***
	 * Get's Screen width so that the two buttons can be the same size
	 * @return width
	 */
	public int ScreenWidth() {
        DisplayMetrics dm=new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        String width=""+dm.widthPixels;
        return Integer.parseInt(width);
    }
}