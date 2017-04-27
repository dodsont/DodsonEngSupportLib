package com.dodsoneng.support;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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
    public static void appLaunched(Context mContext, String app_pname, String name, int launches) {
    	
    	app_name = name;
    	market_link = app_pname;

        DodsonEng de = new DodsonEng(mContext,false);


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
        if (launch_count >= launches) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                if (de.isNetworkAvailable()) {
                    showRateDialog(mContext, editor);
                }
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

        new MaterialDialog.Builder(mContext)
                .title("Rate " + app_name)
                .content("If you enjoy using " + app_name + ", please take a moment to rate it.\n\nThank you for your support! God Bless!")
                .positiveText("No Thanks")
                .neutralText("Remind Me Later")
                .negativeText("Rate Now")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (editor != null) {
                            editor.putBoolean("dontshowagain_noThanks", true);
                            editor.commit();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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