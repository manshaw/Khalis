package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneText;
    CountryCodePicker ccp;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpUI();
    }

    private void setUpUI() {
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
}
