package com.htetznaing.cocmm2;


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

public class language extends Activity implements B4AActivity{
	public static language mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.cocmm2", "com.htetznaing.cocmm2.language");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (language).");
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
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.cocmm2", "com.htetznaing.cocmm2.language");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.cocmm2.language", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (language) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (language) Resume **");
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
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return language.class;
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
        BA.LogInfo("** Activity (language) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (language) Resume **");
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
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _t = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public static int _theme_value = 0;
public anywheresoftware.b4a.object.XmlLayoutBuilder _res = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b2 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b2bg = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _bg = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _interstitial = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _abg = null;
public com.htetznaing.cocmm2.main _main = null;
public com.htetznaing.cocmm2.font _font = null;
public com.htetznaing.cocmm2.allinone _allinone = null;
public com.htetznaing.cocmm2.about _about = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _ab_click() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub ab_Click";
 //BA.debugLineNum = 140;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 141;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 25;BA.debugLine="Activity.Title = \"COC MM Language\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("COC MM Language"));
 //BA.debugLineNum = 26;BA.debugLine="abg.Initialize(LoadBitmap(File.DirAssets,\"bg.jpg";
mostCurrent._abg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 27;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 29;BA.debugLine="interstitial.Initialize(\"interstitial\",\"ca-app-pu";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"interstitial","ca-app-pub-4173348573252986/3801251754");
 //BA.debugLineNum = 30;BA.debugLine="interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 32;BA.debugLine="banner.Initialize(\"banner\",\"ca-app-pub-4173348573";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"banner","ca-app-pub-4173348573252986/8371052153");
 //BA.debugLineNum = 33;BA.debugLine="banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 34;BA.debugLine="Activity.AddView(banner,0%x,100%y - 50dip,100%x,5";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 36;BA.debugLine="t.Initialize(\"t\",500)";
_t.Initialize(processBA,"t",(long) (500));
 //BA.debugLineNum = 37;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 38;BA.debugLine="t1.Initialize(\"t1\",30000)";
_t1.Initialize(processBA,"t1",(long) (30000));
 //BA.debugLineNum = 39;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 41;BA.debugLine="bg.Initialize(LoadBitmap(File.DirAssets,\"language";
mostCurrent._bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"language.png").getObject()));
 //BA.debugLineNum = 42;BA.debugLine="imv.Initialize(\"imv\")";
mostCurrent._imv.Initialize(mostCurrent.activityBA,"imv");
 //BA.debugLineNum = 43;BA.debugLine="imv.Background = bg";
mostCurrent._imv.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bg.getObject()));
 //BA.debugLineNum = 44;BA.debugLine="Activity.AddView(imv,2%x,3%y,96%x,40%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (96),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 45;BA.debugLine="b1bg.Initialize(LoadBitmap(File.DirAssets,\"install";
mostCurrent._b1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"install.png").getObject()));
 //BA.debugLineNum = 46;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 47;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b1bg.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="b2bg.Initialize(LoadBitmap(File.DirAssets,\"restore";
mostCurrent._b2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"restore.png").getObject()));
 //BA.debugLineNum = 50;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 51;BA.debugLine="b2.Background = b2bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b2bg.getObject()));
 //BA.debugLineNum = 53;BA.debugLine="Activity.AddView(b1,20%x,50%y,60%x,12%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 54;BA.debugLine="Activity.AddView(b2,20%x,(b1.Top+b1.Height)+ 5%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 55;BA.debugLine="Activity.AddMenuItem3(\"Share App\",\"share\",LoadBit";
mostCurrent._activity.AddMenuItem3(BA.ObjectToCharSequence("Share App"),"share",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 56;BA.debugLine="Activity.AddMenuItem3(\"Change Themes\",\"ct\",LoadBi";
mostCurrent._activity.AddMenuItem3(BA.ObjectToCharSequence("Change Themes"),"ct",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"theme.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 57;BA.debugLine="Activity.AddMenuItem3(\"About App\",\"ab\",LoadBitmap(";
mostCurrent._activity.AddMenuItem3(BA.ObjectToCharSequence("About App"),"ab",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"about.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
int _i = 0;
 //BA.debugLineNum = 153;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 154;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 155;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 156;BA.debugLine="i = Msgbox2(\"What do you Install?\",\"Attention!\",\"";
_i = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("What do you Install?"),BA.ObjectToCharSequence("Attention!"),"Zawgyi","","Unicode",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 157;BA.debugLine="If i = DialogResponse.POSITIVE Then";
if (_i==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 158;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 159;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 160;BA.debugLine="If	File.Exists(File.DirRootExternal,\"texts.csv";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"texts.csv")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 161;BA.debugLine="File.Copy(File.DirAssets,\"texts.csv\",File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"texts.csv",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"texts.csv");
 };
 //BA.debugLineNum = 165;BA.debugLine="ml.RootCmd(\"mount -o rw,remount /data\",\"\",Null,Nu";
mostCurrent._ml.RootCmd("mount -o rw,remount /data","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="ml.RootCmd(\"cp -r \"&File.DirRootExternal&\"/texts.";
mostCurrent._ml.RootCmd("cp -r "+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/texts.csv data/data/com.supercell.clashofclans/update/csv/texts.csv","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 167;BA.debugLine="ml.chmod(\"/data/data/com.supercell.clashofclans/u";
mostCurrent._ml.chmod("/data/data/com.supercell.clashofclans/update/csv/texts.csv",BA.NumberToString(666));
 //BA.debugLineNum = 168;BA.debugLine="File.Delete(File.DirRootExternal,\"texts.csv\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"texts.csv");
 //BA.debugLineNum = 169;BA.debugLine="Msgbox(\"COC Myanmar Language Installed!\" & CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("COC Myanmar Language Installed!"+anywheresoftware.b4a.keywords.Common.CRLF+"Now, you can using with Myanmar Language"),BA.ObjectToCharSequence("Completed"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 172;BA.debugLine="Msgbox(\"Your Device not have Root Access\",\"Attent";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your Device not have Root Access"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 177;BA.debugLine="If i = DialogResponse.NEGATIVE Then";
if (_i==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 178;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 179;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 180;BA.debugLine="If	File.Exists(File.DirRootExternal,\"Utexts.cs";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Utexts.csv")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 181;BA.debugLine="File.Copy(File.DirAssets,\"Utexts.csv\",File.DirRoo";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Utexts.csv",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Utexts.csv");
 };
 //BA.debugLineNum = 185;BA.debugLine="ml.RootCmd(\"mount -o rw,remount /data\",\"\",Null,Nu";
mostCurrent._ml.RootCmd("mount -o rw,remount /data","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 186;BA.debugLine="ml.RootCmd(\"cp -r \"&File.DirRootExternal&\"/Utexts";
mostCurrent._ml.RootCmd("cp -r "+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Utexts.csv data/data/com.supercell.clashofclans/update/csv/texts.csv","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 187;BA.debugLine="ml.chmod(\"/data/data/com.supercell.clashofclans/u";
mostCurrent._ml.chmod("/data/data/com.supercell.clashofclans/update/csv/texts.csv",BA.NumberToString(666));
 //BA.debugLineNum = 188;BA.debugLine="File.Delete(File.DirRootExternal,\"Utexts.csv\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Utexts.csv");
 //BA.debugLineNum = 189;BA.debugLine="Msgbox(\"COC Myanmar Language Installed!\" & CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("COC Myanmar Language Installed!"+anywheresoftware.b4a.keywords.Common.CRLF+"Now, you can using with Myanmar Language"),BA.ObjectToCharSequence("Completed"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 192;BA.debugLine="Msgbox(\"Your Device not have Root Access\",\"Attent";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your Device not have Root Access"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 195;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
 //BA.debugLineNum = 197;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 198;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 199;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 201;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 202;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 203;BA.debugLine="ml.rm(\"data/data/com.supercell.clashofclans/updat";
mostCurrent._ml.rm("data/data/com.supercell.clashofclans/update/csv/texts.csv");
 //BA.debugLineNum = 204;BA.debugLine="Msgbox(\"Original Restore Finished\" & CRLF & \"You";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Original Restore Finished"+anywheresoftware.b4a.keywords.Common.CRLF+"You can install again!"),BA.ObjectToCharSequence("Completed!"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 206;BA.debugLine="Msgbox(\"Your Device not have Root Access\",\"Attent";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your Device not have Root Access"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
return "";
}
public static String  _ct_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _lis = null;
int _idd_int = 0;
com.maximus.id.id _idd = null;
 //BA.debugLineNum = 74;BA.debugLine="Sub ct_Click";
 //BA.debugLineNum = 75;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 76;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 77;BA.debugLine="Dim idd_int As Int";
_idd_int = 0;
 //BA.debugLineNum = 78;BA.debugLine="Dim idd As id";
_idd = new com.maximus.id.id();
 //BA.debugLineNum = 79;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 80;BA.debugLine="lis.AddAll(Array As String(\"Holo\",\"Holo Light\",\"H";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"}));
 //BA.debugLineNum = 81;BA.debugLine="idd_int = idd.InputList1(lis,\"Choose Themes!\")";
_idd_int = _idd.InputList1(_lis,"Choose Themes!",mostCurrent.activityBA);
 //BA.debugLineNum = 82;BA.debugLine="If idd_int = 0 Then";
if (_idd_int==0) { 
 //BA.debugLineNum = 83;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo"));
 };
 //BA.debugLineNum = 86;BA.debugLine="If idd_int = 1 Then";
if (_idd_int==1) { 
 //BA.debugLineNum = 87;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light"));
 };
 //BA.debugLineNum = 90;BA.debugLine="If idd_int = 2 Then";
if (_idd_int==2) { 
 //BA.debugLineNum = 91;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 94;BA.debugLine="If idd_int = 3 Then";
if (_idd_int==3) { 
 //BA.debugLineNum = 95;BA.debugLine="SetTheme(16973829)";
_settheme((int) (16973829));
 };
 //BA.debugLineNum = 98;BA.debugLine="If idd_int = 4 Then";
if (_idd_int==4) { 
 //BA.debugLineNum = 99;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material"));
 };
 //BA.debugLineNum = 102;BA.debugLine="If idd_int = 5 Then";
if (_idd_int==5) { 
 //BA.debugLineNum = 103;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light"));
 };
 //BA.debugLineNum = 106;BA.debugLine="If idd_int = 6 Then";
if (_idd_int==6) { 
 //BA.debugLineNum = 107;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 110;BA.debugLine="If idd_int = 7 Then";
if (_idd_int==7) { 
 //BA.debugLineNum = 111;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent"));
 };
 //BA.debugLineNum = 114;BA.debugLine="If idd_int = 8 Then";
if (_idd_int==8) { 
 //BA.debugLineNum = 115;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent.NoTitleBar"));
 };
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 13;BA.debugLine="Dim b1,b2 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim b1bg,b2bg As BitmapDrawable";
mostCurrent._b1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 15;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 16;BA.debugLine="Dim imv As ImageView";
mostCurrent._imv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim bg As BitmapDrawable";
mostCurrent._bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 18;BA.debugLine="Dim banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim interstitial As InterstitialAd";
mostCurrent._interstitial = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 21;BA.debugLine="Dim abg As BitmapDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adclosed() throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub interstitial_AdClosed";
 //BA.debugLineNum = 219;BA.debugLine="interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim t,t1 As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 8;BA.debugLine="Dim Theme_Value As Int";
_theme_value = 0;
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Private Sub SetTheme (Theme As Int)";
 //BA.debugLineNum = 121;BA.debugLine="If Theme = 0 Then";
if (_theme==0) { 
 //BA.debugLineNum = 122;BA.debugLine="ToastMessageShow(\"Theme not available.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Theme not available."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 123;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 125;BA.debugLine="If Theme = Theme_Value Then Return";
if (_theme==_theme_value) { 
if (true) return "";};
 //BA.debugLineNum = 126;BA.debugLine="Theme_Value = Theme";
_theme_value = _theme;
 //BA.debugLineNum = 127;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 128;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,language.getObject());
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 60;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 61;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 62;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 64;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 65;BA.debugLine="copy.setText(\"COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား";
_copy.setText(mostCurrent.activityBA,"COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား? ၊ COC ကိုျမန္မာဘာသာျဖင့္ အသုံးျပဳခ်င္ပါသလား? ရန္သူစခန္းေတြမွာသြားတိုက္တဲ့အခါ ေထာင္ေခ်ာက္ေတြ ဗုံးေတြ၊ Teslas ႀကိဳတင္ေတြ႕ျမင္ခ်င္ပါသလား? ဒါေတြအားလုံးကို #COC_Myanmar_Tool တစ္ခုတည္းနဲ႔တင္လုပ္ေဆာင္နိုင္ပါၿပီ။ Root ေဖာက္ထားတဲ့မည္သည့္ Android ဖုန္းမဆိုအထက္ပါလုပ္ေဆာင္ခ်က္ေတြကို #COC_Myanmar_Tool ကထည့္သြင္းေပးနိုင္ပါတယ္။ Play Store ကေနအလြယ္တကူေဒါင္းယူရရွိနိုင္ပါတယ္။ Download Free at Google Play Store: https://play.google.com/store/apps/details?id=com.htetznaing.cocmmtool2 သို႔မဟုတ္မိမိတို႔ဖုန္းထဲက App Store တခုခုကေနလည္း ေဒါင္းနိုင္ပါတယ္။ ေဒါင္းဖို႔အဆင္မေျပသူမ်ားကေတာ့ ဒီမွာ သြားေဒါင္းနိုင္ပါတယ္ > http://www.htetznaing.com/COCMyanmarTool <");
 //BA.debugLineNum = 66;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 67;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 68;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 69;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("#COC_Myanmar_Tool"));
 //BA.debugLineNum = 70;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 71;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 145;BA.debugLine="If	interstitial.Ready Then interstitial.Show Else";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();}
else {
mostCurrent._interstitial.LoadAd();};
 //BA.debugLineNum = 146;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 150;BA.debugLine="If	interstitial.Ready Then interstitial.Show Else";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();}
else {
mostCurrent._interstitial.LoadAd();};
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
}
