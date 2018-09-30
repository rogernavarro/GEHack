package com.slane.client.ge.flasherpasajero;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class frmSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_splash);

        AppCompatTextView txt1 = (AppCompatTextView) findViewById(R.id.txt1);
        txt1.setGravity(Gravity.CENTER);

        AppCompatTextView txt2 = (AppCompatTextView) findViewById(R.id.txt2);
        txt2.setGravity(Gravity.CENTER);

        AppCompatTextView txt3 = (AppCompatTextView) findViewById(R.id.txt3);
        txt3.setGravity(Gravity.CENTER);

        AppCompatTextView txt4 = (AppCompatTextView) findViewById(R.id.txt4);
        txt4.setGravity(Gravity.CENTER);

        Button mEmailSignInButton = (Button) findViewById(R.id.btnsigin);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sig = new Intent(frmSplash.this, frmLogin.class);
                startActivityForResult(sig, 500);
            }
        });
    }

}
