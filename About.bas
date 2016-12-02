Type=Activity
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
Dim t,t1 As Timer
Dim Theme_Value As Int
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim su As StringUtils 
	Dim res As XmlLayoutBuilder
	Dim p As PhoneIntents 
	Dim lstOne As ListView 
	Dim abg As BitmapDrawable
	Dim Banner As AdView
	Dim Interstitial As mwAdmobInterstitial
	Dim ph As Phone
End Sub

Sub Activity_Create(FirstTime As Boolean)
		If ph.SdkVersion > 19 Then
	Interstitial.Initialize("interstitial","ca-app-pub-4173348573252986/6359500554")
	Interstitial.LoadAd
	
	Banner.Initialize("banner","ca-app-pub-4173348573252986/3406034153")
	Banner.LoadAd
	Activity.AddView(Banner,0%x,90%y,100%x,10%y)
	
	t.Initialize("t",500)
	t.Enabled = False
	t1.Initialize("t1",15000)
	t1.Enabled = True
	End If
	
	Activity.Title = "About"
	abg.Initialize(LoadBitmap(File.DirAssets,"bg.jpg"))
	Activity.Background = abg
	
	Dim imvLogo As ImageView 
	imvLogo.Initialize ("imv")
	imvLogo.Bitmap = LoadBitmap(File.DirAssets , "icon.png")
	imvLogo.Gravity = Gravity.FILL 
	Activity.AddView ( imvLogo , 50%x - 50dip  , 20dip ,  100dip  ,  100dip )
	
	Dim lblName As  Label 
	Dim bg As ColorDrawable 
	bg.Initialize (Colors.DarkGray , 10dip)
	lblName.Initialize ("lbname")
	lblName.Background = bg
	lblName.Gravity = Gravity.CENTER 
	lblName.Text = "COC Myanmar Tool"
	lblName.TextSize = 13
	lblName.TextColor = Colors.White 
	Activity.AddView (lblName , 100%x / 2 - 90dip , 130dip , 180dip , 50dip)
	lblName.Height = su.MeasureMultilineTextHeight (lblName, lblName.Text ) + 5dip
	
	
	Dim c As ColorDrawable 
	c.Initialize (Colors.White , 10dip )
	lstOne.Initialize ("lstOnes")
	lstOne.Background = c
	lstOne.SingleLineLayout .Label.TextSize = 12
	lstOne.SingleLineLayout .Label .TextColor = Colors.DarkGray 
	lstOne.SingleLineLayout .Label .Gravity = Gravity.CENTER 
	lstOne.SingleLineLayout .ItemHeight = 40dip
	lstOne.AddSingleLine2 ("Translated By : Naung Ye Yint Aung    ", 1)
	lstOne.AddSingleLine2 ("Zawgyi Font Developed By : Kyaw Swar Thwin",3)
	lstOne.AddSingleLine2 ("Unicode Font Developed By : Khon Soe Zaw Thu",8)
	lstOne.AddSingleLine2 ("App Developed By : Khun Htetz Naing",4)
	lstOne.AddSingleLine2 ("Phone : +959776003982    ",6)
	lstOne.AddSingleLine2 ("Facebook : www.facebook.com/Khun.Htetz.Naing   ", 7)
	Activity.AddView ( lstOne, 30dip , 170dip , 100%x -  60dip, 242dip)
	
	Dim lblCredit As Label 
	lblCredit.Initialize ("lblCredit")
	lblCredit.TextColor = Colors.RGB (74,20,140)
	lblCredit.TextSize = 13
	lblCredit.Gravity = Gravity.CENTER 
	lblCredit.Text = "Credits & Thanks All Translator,Designer,Developer!"
	Activity.AddView (lblCredit, 10dip,(lstOne.Top+lstOne.Height)+2%y, 100%x - 20dip, 50dip)
	lblCredit.Height = su.MeasureMultilineTextHeight (lblCredit, lblCredit.Text )
		
	Activity.AddMenuItem3("Share App","share",LoadBitmap(File.DirAssets,"share.png"),True)
	Activity.AddMenuItem3("Change Themes","ct",LoadBitmap(File.DirAssets,"theme.png"),True)
End Sub

Sub share_Click
	If ph.SdkVersion > 19 Then
		t.Enabled = True
	End If
Dim ShareIt As Intent
Dim copy As BClipboard
copy.clrText
copy.setText("COC မွာျမန္မာစာေရးခ်င္ဖတ္ခ်င္ပါသလား? ၊ COC ကိုျမန္မာဘာသာျဖင့္ အသုံးျပဳခ်င္ပါသလား? ရန္သူစခန္းေတြမွာသြားတိုက္တဲ့အခါ ေထာင္ေခ်ာက္ေတြ ဗုံးေတြ၊ Teslas ႀကိဳတင္ေတြ႕ျမင္ခ်င္ပါသလား? ဒါေတြအားလုံးကို #COC_Myanmar_Tool တစ္ခုတည္းနဲ႔တင္လုပ္ေဆာင္နိုင္ပါၿပီ။ Root ေဖာက္ထားတဲ့မည္သည့္ Android ဖုန္းမဆိုအထက္ပါလုပ္ေဆာင္ခ်က္ေတြကို #COC_Myanmar_Tool ကထည့္သြင္းေပးနိုင္ပါတယ္။ Play Store ကေနအလြယ္တကူေဒါင္းယူရရွိနိုင္ပါတယ္။ Download Free at Google Play Store: https://play.google.com/store/apps/details?id=com.htetznaing.cocmmtool သို႔မဟုတ္မိမိတို႔ဖုန္းထဲက App Store တခုခုကေနလည္း ေဒါင္းနိုင္ပါတယ္။ ေဒါင္းဖို႔အဆင္မေျပသူမ်ားကေတာ့ ဒီမွာ သြားေဒါင္းနိုင္ပါတယ္ > http://ht3tzn4ing.blogspot.com/2016/12/COCMyanmarTool.html <")
    ShareIt.Initialize (ShareIt.ACTION_SEND,"")
    ShareIt.SetType ("text/plain")
    ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
    ShareIt.PutExtra ("android.intent.extra.SUBJECT","#COC_Myanmar_Tool")
    ShareIt.WrapAsIntentChooser("Share App Via...")
StartActivity (ShareIt)
End Sub

Sub ct_Click
		If ph.SdkVersion > 19 Then
		t.Enabled = True
	End If
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

 Sub t_Tick
If	Interstitial.Status=Interstitial.Status_AdReadyToShow Then
	Interstitial.Show
	End If
If Interstitial.Status=Interstitial.Status_Dismissed Then
	Interstitial.Show	
End If
t.Enabled = False
End Sub

Sub t1_Tick
If	Interstitial.Status=Interstitial.Status_AdReadyToShow Then
	Interstitial.Show
	End If
If Interstitial.Status=Interstitial.Status_Dismissed Then
	Interstitial.Show	
End If
t1.Enabled = False
End Sub

 Sub imv_Click
 		If ph.SdkVersion > 19 Then
		t.Enabled = True
	End If
 	StartActivity(p.OpenBrowser("https://www.facebook.com/Khun.Htetz.Naing/"))
End Sub

Sub lbname_Click
		If ph.SdkVersion > 19 Then
		t.Enabled = True
	End If
 	StartActivity(p.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration"))
End Sub

Sub lblCredit_Click
		If ph.SdkVersion > 19 Then
		t.Enabled = True
	End If
	StartActivity(p.OpenBrowser ("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration"))
End Sub
Sub Activity_Resume
     
End Sub

Sub Activity_Pause (UserClosed As Boolean)
     
End Sub

Sub lstOnes_ItemClick (Position As Int, Value As Object)
		If ph.SdkVersion > 19 Then
		t.Enabled = True
		End If
     Select Value
	 	Case 6
Dim i As Intent
i.Initialize(i.ACTION_VIEW, "tel:+959776003982")
StartActivity(i)
	 			Case 7
				   StartActivity(p.OpenBrowser ("https://www.facebook.com/Khun.Htetz.Naing/"))
	 End Select
End Sub