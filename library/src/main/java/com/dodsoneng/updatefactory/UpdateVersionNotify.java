package com.dodsoneng.updatefactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

public class UpdateVersionNotify extends UpdateVersion {

	public UpdateVersionNotify(){
		
	}
	
	@Override
	public void NotifyUser() {
        /*********** Create notification ***********/
        
        final NotificationManager mgr=
            (NotificationManager)getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
        Notification note=new Notification(getImage(),
                                                        "New Version of Bible Trivia!",
                                                        System.currentTimeMillis());
         
        // This pending intent will open after notification click
        PendingIntent i=PendingIntent.getActivity(getContext(), 0,
        		new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl())),
                                                0);
         
        note.setLatestEventInfo(getContext(), "Bible Trivia",
                                "Click to down the new Bible Trivia Version", i);
         
        //After uncomment this line you will see number of notification arrived
        note.number=2;
        mgr.notify(1337, note);
        
    }
}
