package pakzarzameen.com.pk.khalis;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class AddActivity extends AppCompatActivity {

    WeekdaysPicker weekday_pick;
    private static final String[] TIMES = new String[]{
            "Daily", "Weekly", "Monthly", "Custom Days"
    };
    private static final String[] TIMES_AR = new String[]{
            "روزانہ", "ہفتہ وار", "ماہانہ", "حسب ضرورت دن"
    };
    BetterSpinner schedule_spinner;
    EditText timepick;
    TextView start_text;
    FbContract contract;
    private int year1;
    private int month1;
    private int day1;
    public int hour, min;
    public Long time;
    public String address_value;
    public boolean onetime;
    public boolean schedule_done = false;
    public boolean custom_pick = false;
    public boolean day_specified = true;
    List<String> Days = new ArrayList<>();
    DatabaseReference mDatabase;
    String user;
    Boolean address_given = false;
    List<Integer> days_selected = new ArrayList<>();
    SharedPreferences prefs = null;
    private static final String PACAKGE_NAME = "pk.com.pakzarzameen.khalis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        user = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabase = FirebaseDatabase.getInstance().getReference("/Test/User/" + user + "/Current Orders");
        Calendar currCalendar = Calendar.getInstance();
        contract = (FbContract) getIntent().getSerializableExtra("Firebase");
        prefs = getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);
        weekday_pick = (WeekdaysPicker) findViewById(R.id.weekdays);
        timepick = (EditText) findViewById(R.id.timepicker);
        schedule_spinner = (BetterSpinner) findViewById(R.id.spinner2);
        if (new AppLanguageManager(AddActivity.this).getAppLanguage().equals("ar")) {
            timepick.setText("وقت مقرر کریں");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, TIMES_AR);
            schedule_spinner.setHint("شیڈول کریں");
            schedule_spinner.setAdapter(adapter);
        } else {
            timepick.setText("Specify Time");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, TIMES);
            schedule_spinner.setHint("Schedule Days");
            schedule_spinner.setAdapter(adapter);
        }
        start_text = (TextView) findViewById(R.id.start_text);

        weekday_pick.setSelectedDays(days_selected);
        weekday_pick.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
            @Override
            public void onChange(View view, int i, List<Integer> list) {
                if (custom_pick)
                    if (list.size() == 0) {
                        day_specified = false;
                    } else {
                        day_specified = true;
                    }
                else
                    day_specified = true;
            }
        });

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerExample);
        year1 = currCalendar.get(Calendar.YEAR);
        month1 = currCalendar.get(Calendar.MONTH);
        day1 = currCalendar.get(Calendar.DAY_OF_MONTH);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePicker.init(year1, month1, day1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                year1 = year;
                month1 = month;
                day1 = day;
            }
        });


        //timepick.setEnabled(false);
        timepick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        AddActivity.this.hour = selectedHour;
                        AddActivity.this.min = selectedMinute;

                        if (selectedHour > 12)
                            timepick.setText(selectedHour - 12 + ":" + selectedMinute + " pm");
                        else if (selectedHour == 0)
                            timepick.setText(12 + ":" + selectedMinute + " am");
                        else if (selectedHour < 12)
                            timepick.setText(selectedHour + ":" + selectedMinute + " am");
                        else if (selectedHour == 12)
                            timepick.setText(selectedHour + ":" + selectedMinute + " pm");
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();

            }
        });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, TIMES);
//        schedule_spinner = (BetterSpinner) findViewById(R.id.spinner2);
//        schedule_spinner.setAdapter(adapter);
        schedule_spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (schedule_spinner.getText().toString().trim().equals("Custom Days") || schedule_spinner.getText().toString().trim().equals("حسب ضرورت دن")) {
                    weekday_pick.setVisibility(View.VISIBLE);
                    custom_pick = true;
                } else {
                    weekday_pick.setVisibility(View.GONE);
                    custom_pick = false;
                }
                schedule_done = true;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (contract.getOrderType().equals("One-Time"))
            onetime = true;
        if (contract.getOrderType().equals("Schedule"))
            onetime = false;

        if (onetime) {
            schedule_spinner.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timepick.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
            if (new AppLanguageManager(AddActivity.this).getAppLanguage().equals("ar")) {
                start_text.setText("وقت اور تاریخ بتائیں جب آپ چاہتے ہیں کہ آپ کا آرڈر پہنچ جائے");
            } else {
                start_text.setText("Specify time and date when you want your order to be delivered");
            }
            timepick.setGravity(View.TEXT_ALIGNMENT_CENTER);
            timepick.setLayoutParams(params);
        } else {
            if (new AppLanguageManager(AddActivity.this).getAppLanguage().equals("ar")) {
                start_text.setText("آرڈر کے آغاز کی تاریخ کا انتخاب کریں");
            } else {
                start_text.setText("Selet Start From Date");
            }
        }

    }


    public void btn_done(View view) {
        if (isValid()) {
            openAddressDialog();
        }
    }

    private void startNextActivity() {

        if (address_given) {
            makeContract();
            Intent nextActivity = new Intent(this, MainActivity.class);
            nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextActivity);
            finish();
        }

    }

    private void openAddressDialog() {
        final EditText text = new EditText(AddActivity.this);
        if (prefs.contains("Address")) {
            text.setText(prefs.getString("Address", "Address"));
        } else {
            text.setHint("Address");
        }
        text.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        text.setFocusable(false);
        text.setFocusableInTouchMode(true);
        AlertDialog alertDialog1 = new AlertDialog.Builder(AddActivity.this)
                .setIcon(R.drawable.ic_add_location_black_24dp)
                .setTitle("Address")
                .setView(text)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (text.getText().toString().trim().length() > 0) {
                            address_given = true;
                            contract.setAddress(text.getText().toString().trim());
                            startNextActivity();
                        } else
                            Toast.makeText(AddActivity.this, "Kindly specify address for delivery", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void makeContract() {
        String epoch = year1 + "-" + (month1 + 1) + "-" + day1 + "T" + hour + ":" + min + ":" + "00+0000";
        DateTime dateTimeInUtc = new DateTime(epoch, DateTimeZone.UTC);
        Long epochtime = (dateTimeInUtc.getMillis());
//        Date localtime = new Date(epochtime);
//        SimpleDateFormat sdf = new SimpleDateFormat();
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date gmtTime = new Date(sdf.format(localtime));
        contract.setTimeStamp(epochtime);
        contract.setStatus("Resumed");
        contract.setName(prefs.getString("Name", "John"));
        contract.setPayment("UnPaid");
        if (!onetime)
            contract.setScheduleType(schedule_spinner.getText().toString().trim());

        if (custom_pick)
            contract.setDays(weekday_pick.getSelectedDaysText());

        mDatabase.push().setValue(contract);
    }

    private boolean isValid() {
        if (onetime && timepick.getText().toString().trim().length() > 0)
            return true;
        if (timepick.getText().toString().trim().length() > 0 && schedule_done && day_specified) {
            if (custom_pick)
                if (weekday_pick.getSelectedDays().size() > 0)
                    return true;
        }
        if (timepick.getText().toString().trim().length() == 0) {
            timepick.setError("You need to enter time");
            return false;
        }

        if (!schedule_done) {
            schedule_spinner.setError("Please specify schedule");
            return false;
        }
        return true;
    }
}

