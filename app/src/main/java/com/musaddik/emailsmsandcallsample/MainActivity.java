package com.musaddik.emailsmsandcallsample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button sendEmail = findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                sendEmail();
            }
        });

        Button smsButton=findViewById(R.id.btn_launch_sms);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });
        Button dialerButton = findViewById(R.id.btn_launch_call);
        dialerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialer();
            }
        });
        Button callButton = findViewById(R.id.btn_call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.CALL_PHONE}, 1);
                }else {

                    callNumber();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed

                    callNumber();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    protected void sendEmail(){
        Log.i("Send email","");
        String[] TO ={""};
        String[] CC ={""};
        Intent emailIntent =new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Email message goes here");
        try{
            startActivity(Intent.createChooser(emailIntent,"Send mail..."));
            finish();
            Log.d("mail sent","");
        }catch(android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this,"There is no email client installed.",Toast.LENGTH_SHORT).show();
        }
    }

    private String getPhoneNumber(){
        EditText etPhoneNumber= findViewById(R.id.txt_number);
        return etPhoneNumber.getText().toString();
    }
    private void callNumber() {
        // Note that this ACTION_CALL requires permission
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+getPhoneNumber()));
        startActivity(callIntent);
    }
    private void launchDialer()
    {
        // No permisison needed
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+getPhoneNumber()));
        startActivity(callIntent);
    }
    private void sendSMSMessage() {
        try {
            Uri uri = Uri.parse("smsto:"+getPhoneNumber());
            // No permisison needed
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            // Set the message to be sent
            smsIntent.putExtra("sms_body", "This form my sms app");
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this,
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    }

