package com.dodsoneng.updatefactory;

import android.content.Context;

public abstract class UpdateVersion {
	private String version;
	private String message;
	private boolean force;
	private Context context;
	private int image;
	private String url;
	
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Boolean getForce() { return force; }
	public void setForce(Boolean force) { this.force = force; }
	
	public Context getContext() { return context; }
	public void setContext(Context context) { this.context = context; }
	
	public int getImage() { return image; }
	public void setImage(int image) { this.image = image; }
	
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	

	public abstract void NotifyUser();
}
