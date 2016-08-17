package com.dodsoneng.support;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/***
 * AppRater will create a prompt to ask the user to rate the app.
 * AppRater will wait until the app has been launched 4 times before
 * it asks the user.  This is done so that they the changes of a good rating
 * are more likely if the user has gone back to the app.
 * 
 * This function will also determine if the app has been installed from
 * Google Play or from the Amazon App Store.  It will direct them to the 
 * proper one automatically.
 * @author Thomas Dodson
 *
 */
public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCHES_UNTIL_PROMPT = 4;
    static String app_name;
    static String market_link;
    
    /***
     * App_launcher will check to see if it is time to prompt
     * the user to rate your app.  The goal is to wait some time
     * to make sure they like the app before asking them to rate it.
     * @param mContext
     * @param app_pname
     * @param name
     */
    public static void appLaunched(Context mContext, String app_pname, String name) {
    	
    	app_name = name;
    	market_link = app_pname;
    	
    	
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { 
        	return ; 
        }
        
        if (prefs.getBoolean("dontshowagain_noThanks", false)) { 
        	return ; 
        }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        
        editor.commit();
    }   
    
    /***
     * showRateDialog will popup the request to the user to rate this app.
     * @param mContext
     * @param editor
     */
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

    	new AlertDialog.Builder(mContext)
        //.setIcon(R.drawable.icon)
        .setTitle("Rate " + app_name)
        .setMessage("If you enjoy using " + app_name + ", please take a moment to rate it.\n\nThank you for your support! God Bless!")
        .setPositiveButton("No Thanks", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked No Thanks so do some stuff */
                	if (editor != null) {
                        editor.putBoolean("dontshowagain_noThanks", true);
                        editor.commit();
                    }
                }
        })
        .setNeutralButton("Remind Me Later", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked Remind Me Later so do some stuff */
                }
        })
        .setNegativeButton("Rate Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked Rate Now so do some stuff */
                	if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                	openMarket(mContext, market_link);
                }
        })
        .show();
    }
    
    /***
     * This function will return the company name of the app store that it was installed from.
     * Ex. Amazon, Google, Unknown
     * @param mContext
     * @param app_pname
     * @return
     */
    public static String appMarket(Context mContext, String app_pname) {
    	String appMarket = "";
    	String installer = mContext.getPackageManager().getInstallerPackageName(mContext.getPackageName());
    	if (installer.equals("com.amazon.venezia")) {
    		appMarket = "Amazon";
    	} else if (installer.equals("com.android.vending")){
    		appMarket = "Google";
    	} else {
    		appMarket = "Unknown";
    	}
    	
    	return appMarket;
    }
    
    /***
     * This function will open the app store to the given app name.
     * The app store is determined by which store the app was installed from.
     * Ex: Google Play, Amazon App Store.
     * @param mContext
     * @param app_pname
     */
    public static void openMarket(Context mContext, String app_pname) {
    	if (appMarket(mContext, app_pname).equals("Amazon")) {
    		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + app_pname)));
    	} else {
    		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_pname)));
    	}
    }
}