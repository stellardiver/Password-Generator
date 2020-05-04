package com.androidapp.pass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {
    private Random Randomizer = new Random();

    //Keep counter to show ad in every 5 shakes
    public int counter = 0;

    //Shake threshold for detector
    public static final int SHAKE_THRESHOLD = 800;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    //Ui components
    public Button btnGenerate, btnCopy;
    public TextView lblGeneratedPassword;
    public ImageView btnSettings;

    //Preferences to read password options
    SharedPreferences prefs = null;
    public int PasswordLenght = 8;
    public boolean isUpperCaseEnabled, isLowerCaseEnabled, isSymbolsEnabled, isNumbersEnabled;
    public ConstraintLayout generatedPassLayout;


    //Supported character arrays
    public String NUMBERS = "0123456789";
    public String AlphaUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String AlphaLowerCase = "abcdefghijklmnopqrstuvwxyz";
    public String Symbols = "._-?:$#@+/,";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        buttonClickListeners();
        detectShake();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

        generatedPassLayout = (ConstraintLayout) findViewById(R.id.generatedPassLayout);
        generatedPassLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    public void initUI(){

        /**
         * Buttons
         */
        btnGenerate = (Button)findViewById(R.id.btnGenerate);
        btnCopy = (Button)findViewById(R.id.btnCopy);
        lblGeneratedPassword = (TextView)findViewById(R.id.lblPasswordGenerated);
        btnSettings = (ImageView) findViewById(R.id.btnSettings);
        generatedPassLayout = (ConstraintLayout)findViewById(R.id.generatedPassLayout);



        generatedPassLayout.setVisibility(View.INVISIBLE);

        prefs = getSharedPreferences("ioscript.androidapp.passwordgenerator", MODE_PRIVATE);
    }


    /**
     * Carch button clicks
     */
    public void buttonClickListeners(){
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });


        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);
            }
        });


        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password_generator_pass", lblGeneratedPassword.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Deteck the shake of the device
     */
    public void detectShake(){
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            /**
             * DETECT MOTION HERE
             * ACTION:
             *  GENERATE NEW PASSWORD AND ADD TO HISTORY
             * @param count
             */
            @Override
            public void onShake(int count) {

                generatePassword();
            }
        });
    }

    public void  generatePassword(){
        String GENERATED_PASSWORD = "";


        isLowerCaseEnabled = ManageAppPreferences.isLowerCaseEnabled(MainActivity.this);
        isUpperCaseEnabled = ManageAppPreferences.isUpperCaseEnabled(MainActivity.this);
        isSymbolsEnabled = ManageAppPreferences.isSymbolsEnabled(MainActivity.this);
        isNumbersEnabled = ManageAppPreferences.isNumbersEnabled(MainActivity.this);



        String ENTRY_CHARS = "";
        if(isLowerCaseEnabled){
            ENTRY_CHARS = ENTRY_CHARS + AlphaLowerCase;
        }
        if(isUpperCaseEnabled){
            ENTRY_CHARS = ENTRY_CHARS + AlphaUpperCase;
        }
        if(isNumbersEnabled){
            ENTRY_CHARS  = ENTRY_CHARS  + NUMBERS;
        }
        if(isSymbolsEnabled){
            ENTRY_CHARS = ENTRY_CHARS + Symbols;
        }
        if(isSymbolsEnabled == false && isNumbersEnabled == false && isUpperCaseEnabled == false  && isLowerCaseEnabled == false){
            ENTRY_CHARS = ENTRY_CHARS + AlphaUpperCase + AlphaLowerCase+ Symbols + NUMBERS;
        }


        GENERATED_PASSWORD = generatePassword(ENTRY_CHARS);
        vibrateAndroidDevice();
        lblGeneratedPassword.setText(GENERATED_PASSWORD);
        generatedPassLayout.setVisibility(View.VISIBLE);


        /**
         * If app is on first run
         * add your own operation here or remove if you do
         * not want to interact with user in first run.
         */
        if(isDialogShowEnabled()){
            //Write your own first run action.
        }
    }

    public  String generatePassword(String ENTRY_CHARS) {
        String GENERATED = "";
        PasswordLenght =  ManageAppPreferences.getPasswordLength(MainActivity.this);
        for (int index = 0; index < PasswordLenght; index++) {
            int pos = Randomizer.nextInt(ENTRY_CHARS.length());
            GENERATED += ENTRY_CHARS.charAt(pos);
        }
        return GENERATED;
    }



    public void vibrateAndroidDevice(){
       if(ManageAppPreferences.isVibrateEnabled(MainActivity.this)){
           Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
               vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
           }else{
               vibrator.vibrate(500);
           }
       }
    }

    public boolean isDialogShowEnabled(){
        boolean stat = false;
        if (prefs.getBoolean("showdialog", true)) {
            stat = true;
        }else{
            stat = false;
        }
        return stat;
    }


    public void dontShowAgainDialog(){
        prefs.edit().putBoolean("showdialog", false).commit();
    }
}
