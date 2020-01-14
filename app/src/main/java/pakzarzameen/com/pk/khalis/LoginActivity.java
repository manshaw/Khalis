package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;
import pakzarzameen.com.pk.khalis.Utils.LoginActivityCheck;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneText;
    CountryCodePicker ccp;
    String number;
    private AlertDialog alertDialog;
    private Configuration configuration;
    private TextView heading_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configuration = getResources().getConfiguration();
        setUpUI();
        if (!LoginActivityCheck.isAlreadyCalled) {
            selectLanguageDialog();
        }
    }

    private void setUpUI() {
        heading_login = (TextView) findViewById(R.id.textView);
        if (new AppLanguageManager(LoginActivity.this).getAppLanguage().equals("ar")) {
            heading_login.setText("برائے کرم اپنا موبائل نمبر درج کریں");
        } else {
            heading_login.setText("Please Enter your Mobile number");
        }
        phoneText = (EditText) findViewById(R.id.phoneText);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);
        ccp.setHintExampleNumberEnabled(true);
    }

    public void startNextActivity(View view) {

        if (ccp.isValidFullNumber()) {
            Intent intent = new Intent(this, VerifyActivity.class);
            intent.putExtra("Number", ccp.getFullNumberWithPlus());
            startActivity(intent);
            finish();
        } else
            Toast.makeText(this, "Enter Valid Phone number", Toast.LENGTH_LONG).show();
    }

    private void selectLanguageDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView;
        String message = "";
        promptsView = li.inflate(R.layout.dialog_select_language, null);
        if (new AppLanguageManager(getApplicationContext()).getAppLanguage().equals("ar")) {
            message = "زبان منتخب کریں";
        } else {
            message = "Please Select Language";
        }
        TextView tvHeading = (TextView) promptsView.findViewById(R.id.tvHeading);
        LinearLayout llLanguageEnglish = (LinearLayout) promptsView.findViewById(R.id.llLanguageEnglish);
        LinearLayout llLanguageUrdu = (LinearLayout) promptsView.findViewById(R.id.llLanguageUrdu);
        tvHeading.setText(message);
        llLanguageEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppLanguageManager(LoginActivity.this).setAppLanguage("en");
                alertDialog.dismiss();
                startSelfActivity();
            }
        });

        llLanguageUrdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppLanguageManager(LoginActivity.this).setAppLanguage("ar");
                alertDialog.dismiss();
                startSelfActivity();
            }
        });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        LoginActivityCheck.isAlreadyCalled = true;
    }

    private void startSelfActivity() {
        Intent selfActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(selfActivityIntent);
        finish();
    }
}
