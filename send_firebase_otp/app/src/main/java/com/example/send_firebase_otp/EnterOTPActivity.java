package com.example.send_firebase_otp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOTPActivity extends AppCompatActivity {
    private EditText ed_otp;
    private Button bt_otp;

    private TextView tv_otp;
    private String mPhoneNumber;
    private String mVeritifiId;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otpactivity);

        getDataIntent();
        sendTitleToobar();
        mAuth = FirebaseAuth.getInstance();

        initView();
        bt_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ed_otp.getText().toString().trim();
                onClickSendOTPCode(str);
            }

        });

        tv_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnClicksendOtpAgain();
            }
        });
    }

    private void getDataIntent() {
        mPhoneNumber = getIntent().getStringExtra("phone_number");
        mVeritifiId = getIntent().getStringExtra("verificationId");
    }


    private void initView() {
        ed_otp = findViewById(R.id.ed_otp);
        bt_otp = findViewById(R.id.bt_otp);
        tv_otp = findViewById(R.id.tv_otp);
    }

    private void sendTitleToobar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter OTP code");
        }
    }

    private void OnClicksendOtpAgain() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mforceResendingToken)
                        // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "VerificationFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVeritifiId = verificationId;
                                mforceResendingToken = forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void onClickSendOTPCode(String str) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVeritifiId, str);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            gotoMainActivity(user.getPhoneNumber());
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void gotoMainActivity(String phoneNumber) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }
}