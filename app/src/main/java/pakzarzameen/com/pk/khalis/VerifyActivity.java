package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;

public class VerifyActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";
    private EditText etDigit1;
    private EditText etDigit2;
    private EditText etDigit3;
    private EditText etDigit4;
    private EditText etDigit5;
    private EditText etDigit6;
    private FloatingActionButton btnContinue;
    private Button btnResend;
    private String strPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String VID;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    String user;
    private static final String PACAKGE_NAME = "pk.com.pakzarzameen.khalis";
    SharedPreferences prefs = null;
    private TextView heading_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        strPhoneNumber = getIntent().getStringExtra("Number");
        mAuth = FirebaseAuth.getInstance();
        btnResend = findViewById(R.id.button3);
        setupUI();
        prefs = getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean("SignedIn", true).commit();
        setButtonContinueClickbleOrNot();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                if (new AppLanguageManager(VerifyActivity.this).getAppLanguage().equals("ar")) {
                    Toast.makeText(VerifyActivity.this, "آپ کا رجسٹریشن کامیاب ہے", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VerifyActivity.this, "Verification Successful", Toast.LENGTH_LONG).show();
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(VerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(VerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mResendToken = token;
                VID = verificationId;

            }
        };
        startPhoneNumberVerification(strPhoneNumber);


    }

    private void startNextActivity() {
        user = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabase = FirebaseDatabase.getInstance().getReference("/Test/User/" + user);
        prefs.edit().putString("Type", "user").commit();
        mDatabase.setValue("");
        prefs.edit().putBoolean("TypeSpecified", true).commit();
        prefs.edit().putString("PhoneNumber", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).commit();
        Intent nextActivity = new Intent(this, MainActivity.class);
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextActivity);
        finish();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                30, TimeUnit.SECONDS, this, mCallbacks);
    }

    private void setupUI() {
        heading_verify = (TextView) findViewById(R.id.textView2);
        if (new AppLanguageManager(VerifyActivity.this).getAppLanguage().equals("ar")) {
            heading_verify.setText("ہم نے آپ کو ایک 6 عددی کوڈ بھیجا ہے");
            btnResend.setText("دوبارہ بھیجین");
        } else {
            btnResend.setText("Resend");
            heading_verify.setText("We sent you a 6-digit code");
        }
        btnContinue = findViewById(R.id.button2);
        btnResend.setEnabled(false);
        btnResend.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnResend.setEnabled(true);
            }
        }, 30000);

        etDigit1 = (EditText) findViewById(R.id.etDigit1);
        etDigit2 = (EditText) findViewById(R.id.etDigit2);
        etDigit3 = (EditText) findViewById(R.id.etDigit3);
        etDigit4 = (EditText) findViewById(R.id.etDigit4);
        etDigit5 = (EditText) findViewById(R.id.etDigit5);
        etDigit6 = (EditText) findViewById(R.id.etDigit6);

        etDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit2.requestFocus();
                }
            }
        });
        etDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit3.requestFocus();
                } else {
                    etDigit1.requestFocus();
                }
            }
        });
        etDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit4.requestFocus();
                } else {
                    etDigit2.requestFocus();
                }
            }
        });
        etDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit5.requestFocus();
                } else {
                    etDigit3.requestFocus();
                }
            }
        });
        etDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit6.requestFocus();
                } else {
                    etDigit4.requestFocus();
                }
            }
        });
        etDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                } else {
                    etDigit5.requestFocus();
                }
            }
        });

        etDigit1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                } else {
                    if (etDigit1.getText().toString().trim().length() == 1) {
                        etDigit2.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit2.getText().toString().trim().length() == 0)
                        etDigit1.requestFocus();
                } else {
                    if (etDigit2.getText().toString().trim().length() == 1) {
                        etDigit3.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit3.getText().toString().trim().length() == 0)
                        etDigit2.requestFocus();
                } else {
                    if (etDigit3.getText().toString().trim().length() == 1) {
                        etDigit4.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit4.getText().toString().trim().length() == 0)
                        etDigit3.requestFocus();
                } else {
                    if (etDigit4.getText().toString().trim().length() == 1) {
                        etDigit5.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit5.getText().toString().trim().length() == 0)
                        etDigit4.requestFocus();
                } else {
                    if (etDigit5.getText().toString().trim().length() == 1) {
                        etDigit6.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit6.getText().toString().trim().length() == 0)
                        etDigit5.requestFocus();
                }
                return false;
            }
        });

    }

    private boolean validate() {
        if (TextUtils.isEmpty(etDigit1.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(etDigit2.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(etDigit3.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(etDigit4.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(etDigit5.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(etDigit6.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private void setButtonContinueClickbleOrNot() {
        if (!validate()) {
            btnContinue.setAlpha(.5f);
            btnContinue.setClickable(false);
        } else {
            btnContinue.setAlpha(1.0f);
            btnContinue.setClickable(true);
        }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            startNextActivity();
                            // ...
                        } else {
                            Toast.makeText(VerifyActivity.this, "Sign in failed!", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyActivity.this, "Invalid Code Entered", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void resend_msg(View view) {
        if (mResendToken != null)
            resendVerificationCode(strPhoneNumber, mResendToken);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        btnResend.setEnabled(false);
        btnResend.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnResend.setEnabled(true);
            }
        }, 30000);
    }


    public void startNextActivity(View view) {
        if (validate()) {
            String code = etDigit1.getText().toString().trim() + etDigit2.getText().toString().trim() + etDigit3.getText().toString().trim() + etDigit4.getText().toString().trim() + etDigit5.getText().toString().trim() + etDigit6.getText().toString().trim();
            verifyPhoneNumberWithCode(VID, code);
        }
    }
}

