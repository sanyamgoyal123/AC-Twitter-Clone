package com.app.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText edtSignInEmail, edtSignInPassword;
    private Button btnSignIn2, btnSignUp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Sign In");
       // int color = Integer.parseInt("#1DA1F2".replaceFirst("#", ""), 16);
       // setTitleColor(color);
       // getActionBar().setTitle(Html.fromHtml("<font color='#1DA1F2'>Sign In </font>"));
       // getActionBar().setTitle(Html.fromHtml("<font color='#1DA1F2'>" + getString(R.string.sign_in) + "</font>"));
//        int actionBarTitleId = Resources.getSystem().getIdentifier("Sign In", "id", "android");
//        if(actionBarTitleId > 0) {
//            TextView title = findViewById(actionBarTitleId);
//            if(title != null) {
//                title.setTextColor(Color.parseColor("#1DA1F2"));
//            }
//        }
       // getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));

        edtSignInEmail = findViewById(R.id.edtSignInEmail);
        edtSignInPassword = findViewById(R.id.edtSignInPassword);

        edtSignInPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignIn2);
                }
                return false;
            }
        });
        btnSignIn2 = findViewById(R.id.btnSignIn2);
        btnSignUp2 = findViewById(R.id.btnSignUp2);

        btnSignIn2.setOnClickListener(this);
        btnSignUp2.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSignIn2:
                if (edtSignInEmail.getText().toString().equals("") ||
                        edtSignInPassword.getText().toString().equals("")) {
                    Toasty.info(LogIn.this, "Email and Password is required!", Toast.LENGTH_SHORT, true).show();
                } else {
                    ParseUser.logInInBackground(edtSignInEmail.getText().toString(), edtSignInPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                Toasty.success(LogIn.this, user.getUsername() + " is signed in successfully!",
                                        Toast.LENGTH_SHORT, true).show();
                                transitionToSocialMedia();
                            }
                        }
                    });
                }
                break;
            case R.id.btnSignUp2:
                Intent intent= new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void layoutSignInTapped(View v) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void transitionToSocialMedia() {
        Intent intent = new Intent(LogIn.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}