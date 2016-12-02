package com.htetznaing.cocmmtool;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.cocmmtool", "com.htetznaing.cocmmtool.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.cocmmtool", "com.htetznaing.cocmmtool.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.cocmmtool.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _t = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public static int _theme_value = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b5 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b2bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b3bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b4bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b5bg = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb1 = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _abg = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _interstitial = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wv = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bstart = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bsbg = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _res = null;
public com.htetznaing.cocmmtool.font _font = null;
public com.htetznaing.cocmmtool.language _language = null;
public com.htetznaing.cocmmtool.traps _traps = null;
public com.htetznaing.cocmmtool.allinone _allinone = null;
public com.htetznaing.cocmmtool.about _about = null;
public com.htetznaing.cocmmtool.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (font.mostCurrent != null);
vis = vis | (language.mostCurrent != null);
vis = vis | (traps.mostCurrent != null);
vis = vis | (allinone.mostCurrent != null);
vis = vis | (about.mostCurrent != null);
return vis;}
public static String  _ab_click() throws Exception{
 //BA.debugLineNum = 221;BA.debugLine="Sub ab_Click";
 //BA.debugLineNum = 222;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 223;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 225;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 42;BA.debugLine="interstitial.Initialize(\"interstitial\",\"ca-app-pu";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"interstitial","ca-app-pub-4173348573252986/6359500554");
 //BA.debugLineNum = 43;BA.debugLine="interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 //BA.debugLineNum = 45;BA.debugLine="banner.Initialize(\"banner\",\"ca-app-pub-4173348573";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"banner","ca-app-pub-4173348573252986/9452567751");
 //BA.debugLineNum = 46;BA.debugLine="banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 47;BA.debugLine="Activity.AddView(banner,0%x,0%y,100%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 48;BA.debugLine="t.Initialize(\"t\",500)";
_t.Initialize(processBA,"t",(long) (500));
 //BA.debugLineNum = 49;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 50;BA.debugLine="t1.Initialize(\"t1\",15000)";
_t1.Initialize(processBA,"t1",(long) (15000));
 //BA.debugLineNum = 51;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 54;BA.debugLine="lb.Initialize(\"lb\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"lb");
 //BA.debugLineNum = 55;BA.debugLine="lb.Text = \"Developed By Khun Htetz Naing\"";
mostCurrent._lb.setText((Object)("Developed By Khun Htetz Naing"));
 //BA.debugLineNum = 56;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 57;BA.debugLine="Activity.AddView(lb,0%x,95%y,100%x,5%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (95),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 59;BA.debugLine="abg.Initialize(LoadBitmap(File.DirAssets,\"bgtwo.j";
mostCurrent._abg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bgtwo.jpg").getObject()));
 //BA.debugLineNum = 60;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 61;BA.debugLine="b1bg.Initialize(LoadBitmap(File.DirAssets,\"root.p";
mostCurrent._b1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"root.png").getObject()));
 //BA.debugLineNum = 62;BA.debugLine="b2bg.Initialize(LoadBitmap(File.DirAssets,\"zawgyi";
mostCurrent._b2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"zawgyi.png").getObject()));
 //BA.debugLineNum = 63;BA.debugLine="b3bg.Initialize(LoadBitmap(File.DirAssets,\"mmlg.p";
mostCurrent._b3bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mmlg.png").getObject()));
 //BA.debugLineNum = 64;BA.debugLine="b4bg.Initialize(LoadBitmap(File.DirAssets,\"trap.p";
mostCurrent._b4bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"trap.png").getObject()));
 //BA.debugLineNum = 65;BA.debugLine="b5bg.Initialize(LoadBitmap(File.DirAssets,\"all.pn";
mostCurrent._b5bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"all.png").getObject()));
 //BA.debugLineNum = 66;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 67;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b1bg.getObject()));
 //BA.debugLineNum = 68;BA.debugLine="lb1.Initialize(\"\")";
mostCurrent._lb1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 69;BA.debugLine="lb1.Text = \"Check Root Access!\"";
mostCurrent._lb1.setText((Object)("Check Root Access!"));
 //BA.debugLineNum = 71;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 72;BA.debugLine="b2.Background = b2bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b2bg.getObject()));
 //BA.debugLineNum = 74;BA.debugLine="b3.Initialize(\"b3\")";
mostCurrent._b3.Initialize(mostCurrent.activityBA,"b3");
 //BA.debugLineNum = 75;BA.debugLine="b3.Background = b3bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b3bg.getObject()));
 //BA.debugLineNum = 77;BA.debugLine="b4.Initialize(\"b4\")";
mostCurrent._b4.Initialize(mostCurrent.activityBA,"b4");
 //BA.debugLineNum = 78;BA.debugLine="b4.Background = b4bg";
mostCurrent._b4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b4bg.getObject()));
 //BA.debugLineNum = 80;BA.debugLine="b5.Initialize(\"b5\")";
mostCurrent._b5.Initialize(mostCurrent.activityBA,"b5");
 //BA.debugLineNum = 81;BA.debugLine="b5.Background = b5bg";
mostCurrent._b5.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b5bg.getObject()));
 //BA.debugLineNum = 83;BA.debugLine="Activity.addview(b1,30%x,10%y,40%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 84;BA.debugLine="Activity.AddView(b2,10%x,(b1.Top+b1.Height)+5%y,3";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 85;BA.debugLine="Activity.AddView(b3,54%x,(b1.Top+b1.Height)+5%y,3";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (54),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 86;BA.debugLine="Activity.AddView(b4,10%x,(b3.Top+b3.Height)+5%y,3";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._b3.getTop()+mostCurrent._b3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 87;BA.debugLine="Activity.AddView(b5,54%x,(b3.Top+b3.Height)+5%y,3";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b5.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (54),mostCurrent.activityBA),(int) ((mostCurrent._b3.getTop()+mostCurrent._b3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 89;BA.debugLine="Activity.AddView(lb1,30%x,(b1.Top+b1.Height),40%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 90;BA.debugLine="lb1.Gravity = Gravity.CENTER";
mostCurrent._lb1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 91;BA.debugLine="Activity.AddMenuItem3(\"Share App\",\"share\",LoadBit";
mostCurrent._activity.AddMenuItem3("Share App","share",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="Activity.AddMenuItem3(\"Change Themes\",\"ct\",LoadBi";
mostCurrent._activity.AddMenuItem3("Change Themes","ct",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"theme.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 93;BA.debugLine="Activity.AddMenuItem3(\"About App\",\"ab\",LoadBitmap(";
mostCurrent._activity.AddMenuItem3("About App","ab",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"about.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 95;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 96;BA.debugLine="Dim wv As WebView";
mostCurrent._wv = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 97;BA.debugLine="wv.Initialize(\"wv\")";
mostCurrent._wv.Initialize(mostCurrent.activityBA,"wv");
 //BA.debugLineNum = 98;BA.debugLine="wv.LoadUrl(\"file:///android_asset/sp.html\")";
mostCurrent._wv.LoadUrl("file:///android_asset/sp.html");
 //BA.debugLineNum = 99;BA.debugLine="wv.Visible=True";
mostCurrent._wv.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 100;BA.debugLine="wv.ZoomEnabled = False";
mostCurrent._wv.setZoomEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 102;BA.debugLine="bsbg.Initialize(Colors.Blue,15)";
mostCurrent._bsbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Blue,(int) (15));
 //BA.debugLineNum = 103;BA.debugLine="bstart.Initialize(\"bstart\")";
mostCurrent._bstart.Initialize(mostCurrent.activityBA,"bstart");
 //BA.debugLineNum = 104;BA.debugLine="bstart.Text = \">> Start >>\"";
mostCurrent._bstart.setText((Object)(">> Start >>"));
 //BA.debugLineNum = 105;BA.debugLine="bstart.Background = bsbg";
mostCurrent._bstart.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bsbg.getObject()));
 //BA.debugLineNum = 106;BA.debugLine="Activity.AddView(wv,0%x,0%y,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._wv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(bstart,60%x,87%y,35%x,8%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bstart.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (87),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 109;BA.debugLine="b1.Visible = False";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 110;BA.debugLine="b2.Visible = False";
mostCurrent._b2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 111;BA.debugLine="b3.Visible = False";
mostCurrent._b3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 112;BA.debugLine="b4.Visible = False";
mostCurrent._b4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 113;BA.debugLine="b5.Visible = False";
mostCurrent._b5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 291;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 287;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 289;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
 //BA.debugLineNum = 247;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 248;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 249;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 251;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 252;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 253;BA.debugLine="Msgbox(\"Congratulations!\" & CRLF & \"Your Device";
anywheresoftware.b4a.keywords.Common.Msgbox("Congratulations!"+anywheresoftware.b4a.keywords.Common.CRLF+"Your Device is Rooted","Attention!",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 255;BA.debugLine="Msgbox(\"Error! Your Device is\" & CRLF & \"not ha";
anywheresoftware.b4a.keywords.Common.Msgbox("Error! Your Device is"+anywheresoftware.b4a.keywords.Common.CRLF+"not have Root Access","Attention!",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
 //BA.debugLineNum = 259;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 260;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 261;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 263;BA.debugLine="StartActivity(Font)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._font.getObject()));
 //BA.debugLineNum = 264;BA.debugLine="End Sub";
return "";
}
public static String  _b3_click() throws Exception{
 //BA.debugLineNum = 266;BA.debugLine="Sub b3_Click";
 //BA.debugLineNum = 267;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 268;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 270;BA.debugLine="StartActivity(Language)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._language.getObject()));
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
return "";
}
public static String  _b4_click() throws Exception{
 //BA.debugLineNum = 273;BA.debugLine="Sub b4_Click";
 //BA.debugLineNum = 274;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 275;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 277;BA.debugLine="StartActivity(Traps)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._traps.getObject()));
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public static String  _b5_click() throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Sub b5_CLick";
 //BA.debugLineNum = 281;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 282;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 284;BA.debugLine="StartActivity(AllinOne)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._allinone.getObject()));
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
return "";
}
public static String  _bstart_click() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub bstart_Click";
 //BA.debugLineNum = 126;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 127;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 129;BA.debugLine="wv.Visible = False";
mostCurrent._wv.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 130;BA.debugLine="b1.Visible = True";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 131;BA.debugLine="b2.Visible = True";
mostCurrent._b2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="b3.Visible = True";
mostCurrent._b3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 133;BA.debugLine="b4.Visible = True";
mostCurrent._b4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 134;BA.debugLine="b5.Visible = True";
mostCurrent._b5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 135;BA.debugLine="bstart.Visible = False";
mostCurrent._bstart.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _ct_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _lis = null;
int _idd_int = 0;
com.maximus.id.id _idd = null;
 //BA.debugLineNum = 154;BA.debugLine="Sub ct_Click";
 //BA.debugLineNum = 155;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 156;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 158;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 159;BA.debugLine="Dim idd_int As Int";
_idd_int = 0;
 //BA.debugLineNum = 160;BA.debugLine="Dim idd As id";
_idd = new com.maximus.id.id();
 //BA.debugLineNum = 161;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 162;BA.debugLine="lis.AddAll(Array As String(\"Holo\",\"Holo Light\",\"H";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"}));
 //BA.debugLineNum = 163;BA.debugLine="idd_int = idd.InputList1(lis,\"Choose Themes!\")";
_idd_int = _idd.InputList1(_lis,"Choose Themes!",mostCurrent.activityBA);
 //BA.debugLineNum = 164;BA.debugLine="If idd_int = 0 Then";
if (_idd_int==0) { 
 //BA.debugLineNum = 165;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo"));
 };
 //BA.debugLineNum = 168;BA.debugLine="If idd_int = 1 Then";
if (_idd_int==1) { 
 //BA.debugLineNum = 169;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light"));
 };
 //BA.debugLineNum = 172;BA.debugLine="If idd_int = 2 Then";
if (_idd_int==2) { 
 //BA.debugLineNum = 173;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 176;BA.debugLine="If idd_int = 3 Then";
if (_idd_int==3) { 
 //BA.debugLineNum = 177;BA.debugLine="SetTheme(16973829)";
_settheme((int) (16973829));
 };
 //BA.debugLineNum = 180;BA.debugLine="If idd_int = 4 Then";
if (_idd_int==4) { 
 //BA.debugLineNum = 181;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material"));
 };
 //BA.debugLineNum = 184;BA.debugLine="If idd_int = 5 Then";
if (_idd_int==5) { 
 //BA.debugLineNum = 185;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light"));
 };
 //BA.debugLineNum = 188;BA.debugLine="If idd_int = 6 Then";
if (_idd_int==6) { 
 //BA.debugLineNum = 189;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 192;BA.debugLine="If idd_int = 7 Then";
if (_idd_int==7) { 
 //BA.debugLineNum = 193;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent"));
 };
 //BA.debugLineNum = 196;BA.debugLine="If idd_int = 8 Then";
if (_idd_int==8) { 
 //BA.debugLineNum = 197;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent.NoTitleBar"));
 };
 //BA.debugLineNum = 200;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Dim b1,b2,b3,b4,b5 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b4 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim b1bg,b2bg,b3bg,b4bg,b5bg As BitmapDrawable";
mostCurrent._b1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b3bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b4bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b5bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 27;BA.debugLine="Dim lb1 As Label";
mostCurrent._lb1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 29;BA.debugLine="Dim abg As BitmapDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 30;BA.debugLine="Dim lb As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim interstitial As mwAdmobInterstitial";
mostCurrent._interstitial = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 33;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 34;BA.debugLine="Dim wv As WebView";
mostCurrent._wv = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim bstart As Button";
mostCurrent._bstart = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim bsbg As ColorDrawable";
mostCurrent._bsbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 37;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _lb_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _ii = null;
 //BA.debugLineNum = 117;BA.debugLine="Sub lb_Click";
 //BA.debugLineNum = 118;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 119;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 121;BA.debugLine="Dim ii As PhoneIntents";
_ii = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 122;BA.debugLine="StartActivity(ii.OpenBrowser(\"https://www.fb.com/";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ii.OpenBrowser("https://www.fb.com/Khun.Htetz.Naing")));
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
font._process_globals();
language._process_globals();
traps._process_globals();
allinone._process_globals();
about._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim t As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 20;BA.debugLine="Dim t1 As Timer";
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 21;BA.debugLine="Dim Theme_Value As Int";
_theme_value = 0;
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 202;BA.debugLine="Private Sub SetTheme (Theme As Int)";
 //BA.debugLineNum = 203;BA.debugLine="If Theme = 0 Then";
if (_theme==0) { 
 //BA.debugLineNum = 204;BA.debugLine="ToastMessageShow(\"Theme not available.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Theme not available.",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 207;BA.debugLine="If Theme = Theme_Value Then Return";
if (_theme==_theme_value) { 
if (true) return "";};
 //BA.debugLineNum = 208;BA.debugLine="Theme_Value = Theme";
_theme_value = _theme;
 //BA.debugLineNum = 209;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 210;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,main.getObject());
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 138;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 139;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 140;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 142;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 144;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 145;BA.debugLine="copy.setText(\"COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား?";
_copy.setText(mostCurrent.activityBA,"COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား? ၊ COC ကိုျမန္မာဘာသာျဖင့္ အသုံးျပဳခ်င္ပါသလား? ရန္သူစခန္းေတြမွာသြားတိုက္တဲ့အခါ ေထာင္ေခ်ာက္ေတြ ဗုံးေတြ၊ Teslas ႀကိဳတင္ေတြ႕ျမင္ခ်င္ပါသလား? ဒါေတြအားလုံးကို #COC_Myanmar_Tool တစ္ခုတည္းနဲ႔တင္လုပ္ေဆာင္နိုင္ပါၿပီ။ Root ေဖာက္ထားတဲ့မည္သည့္ Android ဖုန္းမဆိုအထက္ပါလုပ္ေဆာင္ခ်က္ေတြကို #COC_Myanmar_Tool ကထည့္သြင္းေပးနိုင္ပါတယ္။ Play Store ကေနအလြယ္တကူေဒါင္းယူရရွိနိုင္ပါတယ္။ Download Free at Google Play Store: https://play.google.com/store/apps/details?id=com.htetznaing.cocmmtool သို႔မဟုတ္မိမိတို႔ဖုန္းထဲက App Store တခုခုကေနလည္း ေဒါင္းနိုင္ပါတယ္။ ေဒါင္းဖို႔အဆင္မေျပသူမ်ားကေတာ့ ဒီမွာ သြားေဒါင္းနိုင္ပါတယ္ > http://ht3tzn4ing.blogspot.com/2016/12/COCMyanmarTool.html <");
 //BA.debugLineNum = 146;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 147;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 148;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 149;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJEC";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("#COC_Myanmar_Tool"));
 //BA.debugLineNum = 150;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\"";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 151;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 228;BA.debugLine="If	interstitial.Status=interstitial.Status_AdReady";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 229;BA.debugLine="interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 231;BA.debugLine="If interstitial.Status=interstitial.Status_Dismiss";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 232;BA.debugLine="interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 234;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 237;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 238;BA.debugLine="If	interstitial.Status=interstitial.Status_AdReady";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 239;BA.debugLine="interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 241;BA.debugLine="If interstitial.Status=interstitial.Status_Dismiss";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 242;BA.debugLine="interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 244;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
return "";
}
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
}
