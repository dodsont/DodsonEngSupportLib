package com.dodsoneng.updatefactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;


public class UpdateVersionPopup extends UpdateVersion {

	public UpdateVersionPopup(){
		
	}
	
	@Override
	public void NotifyUser() {
		
		SharedPreferences prefs = getContext().getSharedPreferences("CheckVersion", 0);
        final SharedPreferences.Editor editor = prefs.edit();
        
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
    	 
        // Setting Dialog Info
        alertDialog.setTitle("New Version");
        alertDialog.setIcon(getImage());
        alertDialog.setMessage(getMessage());


        // Button to download latest version
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (editor != null) {
                    editor.putBoolean("WaitLonger", false);
                    editor.commit();
                }
            	getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl())));
            }
        });		
        // Button to not download the latest version
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (getForce()) {
            		
            	} else {
            		if (editor != null) {
	                    editor.putBoolean("WaitLonger", true);
	                    editor.commit();
	                }
            	}
            }
        });
        	alertDialog.show();
	}

}
