package pakzarzameen.com.pk.khalis;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class AddActivity extends AppCompatActivity {

    WeekdaysPicker weekday_pick;
    private static final String[] TIMES = new String[]{
            "Daily", "Weekly", "Monthly", "Custom Days"
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
    public boolean onetime;
    public boolean schedule_done = false;
    public boolean custom_pick = false;
    public boolean day_specified = true;
    List<String> Days = new ArrayList<>();
    DatabaseReference mDatabase;
    String user;
    List<Integer> days_selected = new ArrayList<>();
    public static final String[] Weekdays = {"None", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        user = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabase = FirebaseDatabase.getInstance().getReference("/Test/User/" + user);
        Calendar currCalendar = Calendar.getInstance();
        contract = (FbContract) getIntent().getSerializableExtra("Firebase");

        weekday_pick = (WeekdaysPicker) findViewById(R.id.weekdays);
        timepick = (EditText) findViewById(R.id.timepicker);
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
                mTimePicker = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        AddActivity.this.hour = selectedHour;
                        AddActivity.this.min = selectedMinute;
                        if (selectedHour > 12)
                            timepick.setText(selectedHour - 12 + ":" + selectedMinute + " pm");
                        else
                            timepick.setText(selectedHour + ":" + selectedMinute + " am");
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIMES);
        schedule_spinner = (BetterSpinner) findViewById(R.id.spinner2);
        schedule_spinner.setAdapter(adapter);
        schedule_spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (schedule_spinner.getText().toString().trim().equals("Custom Days")) {
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

        if (contract.getOrderType().equals("OneTime"))
            onetime = true;
        else
            onetime = false;

        if (onetime) {
            schedule_spinner.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timepick.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
            start_text.setText("Specify time and date when you want your order to be delivered");
            timepick.setGravity(View.TEXT_ALIGNMENT_CENTER);
            timepick.setLayoutParams(params);
        }

    }


    public void startNextActivity(View view) {
        if (isValid()) {
            makeContract();
            Intent nextActivity = new Intent(this, MainActivity.class);
            nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextActivity);
            finish();
        }
    }

    private void makeContract() {

        String epoch = year1 + "-" + (month1 + 1) + "-" + day1 + "T" + hour + ":" + min + ":" + "00+0000";
        DateTime dateTimeInUtc = new DateTime(epoch);
        dateTimeInUtc.toDateTime(DateTimeZone.UTC);
        Long epochtime = (dateTimeInUtc.getMillis());
        contract.setTimeStamp(epochtime);
        if (!onetime)
            contract.setScheduleType(schedule_spinner.getText().toString().trim());

        if (custom_pick)
            contract.setDays(weekday_pick.getSelectedDaysText());

        mDatabase.push().setValue(contract);
    }

    private boolean isValid() {
        if (onetime && timepick.getText().toString().trim().length() > 0)
            return true;
        if (timepick.getText().toString().trim().length() > 0 && schedule_done && day_specified)
            return true;
        return false;
    }
}

