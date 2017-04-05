Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	Dim t,t1 As Timer
	Dim Theme_Value As Int
End Sub

Sub Globals
	Dim res As XmlLayoutBuilder
Dim b1,b2 As Button
Dim b1bg,b2bg As BitmapDrawable
Dim ml As MLfiles
Dim imv As ImageView
Dim bg As BitmapDrawable
Dim banner As AdView
Dim p As Phone
Dim interstitial As InterstitialAd
Dim abg As BitmapDrawable
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.Title = "All In One"
	p.SetScreenOrientation(1)
	abg.Initialize(LoadBitmap(File.DirAssets,"bg.jpg"))
	Activity.Background = abg
	
	interstitial.Initialize("interstitial","ca-app-pub-4173348573252986/3801251754")
	interstitial.LoadAd
	
	banner.Initialize("banner","ca-app-pub-4173348573252986/8371052153")
	banner.LoadAd
	Activity.AddView(banner,0%x,100%y - 50dip,100%x,50dip)
	
	t.Initialize("t",500)
	t.Enabled = False
	t1.Initialize("t1",30000)
	t1.Enabled = True
	
	bg.Initialize(LoadBitmap(File.DirAssets,"allinone.png"))
	imv.Initialize("imv")
	imv.Background = bg
	Activity.AddView(imv,2%x,3%y,96%x,40%y)
b1bg.Initialize(LoadBitmap(File.DirAssets,"install.png"))
b1.Initialize("b1")
b1.Background = b1bg

b2bg.Initialize(LoadBitmap(File.DirAssets,"restore.png"))
b2.Initialize("b2")
b2.Background = b2bg

Activity.AddView(b1,20%x,50%y,60%x,12%y)
Activity.AddView(b2,20%x,(b1.Top+b1.Height)+ 5%y,60%x,12%y)
	Activity.AddMenuItem3("Share App","share",LoadBitmap(File.DirAssets,"share.png"),True)
	Activity.AddMenuItem3("Change Themes","ct",LoadBitmap(File.DirAssets,"theme.png"),True)
Activity.AddMenuItem3("About App","ab",LoadBitmap(File.DirAssets,"about.png"),True)
End Sub

Sub share_Click
		t.Enabled = True
	Dim ShareIt As Intent
	Dim copy As BClipboard
	copy.clrText
	copy.setText("COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား? ၊ COC ကိုျမန္မာဘာသာျဖင့္ အသုံးျပဳခ်င္ပါသလား? ရန္သူစခန္းေတြမွာသြားတိုက္တဲ့အခါ ေထာင္ေခ်ာက္ေတြ ဗုံးေတြ၊ Teslas ႀကိဳတင္ေတြ႕ျမင္ခ်င္ပါသလား? ဒါေတြအားလုံးကို #COC_Myanmar_Tool တစ္ခုတည္းနဲ႔တင္လုပ္ေဆာင္နိုင္ပါၿပီ။ Root ေဖာက္ထားတဲ့မည္သည့္ Android ဖုန္းမဆိုအထက္ပါလုပ္ေဆာင္ခ်က္ေတြကို #COC_Myanmar_Tool ကထည့္သြင္းေပးနိုင္ပါတယ္။ Play Store ကေနအလြယ္တကူေဒါင္းယူရရွိနိုင္ပါတယ္။ Download Free at Google Play Store: https://play.google.com/store/apps/details?id=com.htetznaing.cocmmtool2 သို႔မဟုတ္မိမိတို႔ဖုန္းထဲက App Store တခုခုကေနလည္း ေဒါင္းနိုင္ပါတယ္။ ေဒါင္းဖို႔အဆင္မေျပသူမ်ားကေတာ့ ဒီမွာ သြားေဒါင္းနိုင္ပါတယ္ > http://www.htetznaing.com/COCMyanmarTool <")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","#COC_Myanmar_Tool")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub

Sub ct_Click
t.Enabled = True
	Dim lis As List
	Dim idd_int As Int
	Dim idd As id
	lis.Initialize
	lis.AddAll(Array As String("Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"))
	idd_int = idd.InputList1(lis,"Choose Themes!")
	If idd_int = 0 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo"))
	End If
	
	If idd_int = 1 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo.Light"))
	End If
	
	If idd_int = 2 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo.Light.DarkActionBar"))
	End If
	
	If idd_int = 3 Then
		SetTheme(16973829)
	End If
	
	If idd_int = 4 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material"))
	End If
	
	If idd_int = 5 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material.Light"))
	End If
	
	If idd_int = 6 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material.Light.DarkActionBar"))
	End If
	
If idd_int = 7 Then
	SetTheme(res.GetResourceId("style", "android:style/Theme.Translucent"))
End If

If idd_int = 8 Then
	SetTheme(res.GetResourceId("style", "android:style/Theme.Translucent.NoTitleBar"))
End If

End Sub

Private Sub SetTheme (Theme As Int)
	If Theme = 0 Then
		ToastMessageShow("Theme not available.", False)
		Return
	End If
	If Theme = Theme_Value Then Return
	Theme_Value = Theme		
	Activity.Finish
	StartActivity(Me)		
End Sub


#if java
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
#end if

Sub ab_Click
		t.Enabled = True
	StartActivity(About)
End Sub

Sub t_Tick
	If	interstitial.Ready Then interstitial.Show Else interstitial.LoadAd
t.Enabled = False
End Sub

Sub t1_Tick
	If	interstitial.Ready Then interstitial.Show Else interstitial.LoadAd
End Sub

Sub b1_Click
		t.Enabled = True
	Dim i As Int
	i = Msgbox2("What do you want to Install?","Attention!","Zawgyi","","Unicode",Null)
	If i = DialogResponse.POSITIVE Then
	ml.GetRoot
	If ml.HaveRoot Then
		'Copy
    If	File.Exists(File.DirRootExternal,"DroidSans.ttf") = False Then
	File.Copy(File.DirAssets,"DroidSans.ttf",File.DirRootExternal,"DroidSans.ttf")
	End If
	
	 If	File.Exists(File.DirRootExternal,"texts.csv") = False Then
	File.Copy(File.DirAssets,"texts.csv",File.DirRootExternal,"texts.csv")
	End If
	
	'Backup
	ml.RootCmd("mount -o rw,remount /system","",Null,Null,False)
			ml.RootCmd("mount -o rw,remount /data","",Null,Null,False)
			ml.mv("/system/fonts/DroidSans.ttf","/system/fonts/DroidSans.ttf.bak")
			ml.mv("/system/fonts/Roboto-Regular.ttf","/system/fonts/Roboto-Regular.ttf.bak")
			ml.mv("/system/fonts/Roboto-Bold.ttf","/system/fonts/Roboto-Bold.ttf.bak")
			ml.mv("/system/fonts/DroidSans-Bold.ttf","/system/fonts/DroidSans-Bold.ttf.bak")
	
			'Install
			ml.RootCmd("cp -r "&File.DirRootExternal&"/DroidSans.ttf system/fonts/Roboto-Bold.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/DroidSans.ttf system/fonts/Roboto-Regular.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/DroidSans.ttf system/fonts/DroidSans.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/DroidSans.ttf system/fonts/DroidSans-Bold.ttf","",Null,Null,False)
	ml.RootCmd("cp -r "&File.DirRootExternal&"/texts.csv data/data/com.supercell.clashofclans/update/csv/texts.csv","",Null,Null,False)
	
			'Permission
			ml.chmod("/system/fonts/DroidSans.ttf",644)
			ml.chmod("/system/fonts/DroidSans-Bold.ttf",644)
			ml.chmod("/system/fonts/Roboto-Bold.ttf",644)
			ml.chmod("/system/fonts/Roboto-Regular.ttf",644)
	ml.chmod("/data/data/com.supercell.clashofclans/update/csv/texts.csv",666)
	
	File.Delete(File.DirRootExternal,"DroidSans.ttf")
	File.Delete(File.DirRootExternal,"texts.csv")
	
	Msgbox("All Myanmar Font+Language+Traps installed!" & CRLF & "Now, Device will be Reboot","Completed")
	ml.RootCmd("reboot","",Null,Null,False)
	Else
	Msgbox("Your Device not have Root Access","Attention!")
	End If
	End If
	
	'Unicode
	If i = DialogResponse.NEGATIVE Then
	ml.GetRoot
	If ml.HaveRoot Then
		'Copy
    If	File.Exists(File.DirRootExternal,"MyanmarPaOh.ttf") = False Then
	File.Copy(File.DirAssets,"MyanmarPaOh.ttf",File.DirRootExternal,"MyanmarPaOh.ttf")
	End If
	
	 If	File.Exists(File.DirRootExternal,"Utexts.csv") = False Then
	File.Copy(File.DirAssets,"Utexts.csv",File.DirRootExternal,"Utexts.csv")
	End If
	
	'Backup
	ml.RootCmd("mount -o rw,remount /system","",Null,Null,False)
			ml.RootCmd("mount -o rw,remount /data","",Null,Null,False)
			ml.mv("/system/fonts/DroidSans.ttf","/system/fonts/DroidSans.ttf.bak")
			ml.mv("/system/fonts/Roboto-Regular.ttf","/system/fonts/Roboto-Regular.ttf.bak")
			ml.mv("/system/fonts/Roboto-Bold.ttf","/system/fonts/Roboto-Bold.ttf.bak")
			ml.mv("/system/fonts/DroidSans-Bold.ttf","/system/fonts/DroidSans-Bold.ttf.bak")
	
			'Install
			ml.RootCmd("cp -r "&File.DirRootExternal&"/MyanmarPaOh.ttf system/fonts/DroidSans.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/MyanmarPaOh.ttf system/fonts/DroidSans-Bold.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/MyanmarPaOh.ttf system/fonts/Roboto-Bold.ttf","",Null,Null,False)
			ml.RootCmd("cp -r "&File.DirRootExternal&"/MyanmarPaOh.ttf system/fonts/Roboto-Regular.ttf","",Null,Null,False)
	ml.RootCmd("cp -r "&File.DirRootExternal&"/Utexts.csv data/data/com.supercell.clashofclans/update/csv/texts.csv","",Null,Null,False)
	
			'Permission
			ml.chmod("/system/fonts/DroidSans.ttf",644)
			ml.chmod("/system/fonts/DroidSans-Bold.ttf",644)
			ml.chmod("/system/fonts/Roboto-Bold.ttf",644)
			ml.chmod("/system/fonts/Roboto-Regular.ttf",644)
	ml.chmod("/data/data/com.supercell.clashofclans/update/csv/texts.csv",666)
	
	File.Delete(File.DirRootExternal,"MyanmarPaOh.ttf")
	File.Delete(File.DirRootExternal,"texts.csv")
	
	Msgbox("All Myanmar Font+Language+Traps installed!" & CRLF & "Now, Device will be Reboot","Completed")
	ml.RootCmd("reboot","",Null,Null,False)
	Else
	Msgbox("Your Device not have Root Access","Attention!")
	End If
	End If
End Sub

Sub b2_Click
		t.Enabled = True
	ml.GetRoot
	If ml.HaveRoot Then
		ml.mv("/system/fonts/DroidSans.ttf.bak","/system/fonts/DroidSans.ttf")
		ml.mv("/system/fonts/DroidSans-Bold.ttf.bak","/system/fonts/DroidSans-Bold.ttf")
		ml.mv("/system/fonts/Roboto-Bold.ttf.bak","/system/fonts/Roboto-Bold.ttf")
		ml.mv("/system/fonts/Roboto-Regular.ttf.bak","/system/fonts/Roboto-Regular.ttf")
	ml.rm("data/data/com.supercell.clashofclans/update/csv/texts.csv")
	
	Msgbox("Original Restore Finished" & CRLF & "Now, Device will be Reboot","Completed!")
	ml.RootCmd("reboot","",Null,Null,False)
	Else
	Msgbox("Your Device not have Root Access","Attention!")
		End If
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub interstitial_AdClosed
	interstitial.LoadAd
End Sub