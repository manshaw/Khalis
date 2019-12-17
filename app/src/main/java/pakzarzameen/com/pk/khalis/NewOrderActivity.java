package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import net.igenius.customcheckbox.CustomCheckBox;

import androidx.appcompat.app.AppCompatActivity;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class NewOrderActivity extends AppCompatActivity {
    CustomCheckBox milk, yogurt, order_one, schedule_order;
    NumberPicker quantity_milk, quantity_yogurt;
    Boolean milk_highligh, yogurt_highlight;
    TextView milk_text, yogurt_text;
    FbContract contract = new FbContract();
    Boolean one_time;
    double quant_m, quant_y;
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__order_new);
        milk = (CustomCheckBox) findViewById(R.id.box_milk);
        yogurt = (CustomCheckBox) findViewById(R.id.box_yogurt);
        quantity_milk = (NumberPicker) findViewById(R.id.number_picker);
        quantity_yogurt = (NumberPicker) findViewById(R.id.number_picker2);
        order_one = (CustomCheckBox) findViewById(R.id.order_one);
        schedule_order = (CustomCheckBox) findViewById(R.id.schedule);
        milk_text = (TextView) findViewById(R.id.milk_text);
        yogurt_text = (TextView) findViewById(R.id.yogurt_text);
        address = (EditText) findViewById(R.id.addresstext);
        order_one.setChecked(false, true);
        schedule_order.setChecked(false, true);
        yogurt.setChecked(false, true);
        milk.setChecked(false, true);

        contract.setMilkQuantity(0);
        contract.setYogurtQuantity(0);
        contract.setAddress("");
        contract.setOrderType("");
        contract.setScheduleType("");
        contract.setTimeStamp(0l);

        quantity_yogurt.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                quant_y = (double) newVal;
            }
        });
        quantity_milk.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                quant_m = (double) newVal;
            }
        });

        milk.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (milk.isChecked()) {
                    milk_highligh = true;
                    milk_text.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    quantity_milk.setDividerColor(getResources().getColor(R.color.colorAccent));
                } else {
                    milk_highligh = false;
                    milk_text.setHintTextColor(getResources().getColor(R.color.label_text));
                    quantity_milk.setDividerColor(getResources().getColor(R.color.label_text));
                }
            }
        });
        yogurt.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (yogurt.isChecked()) {
                    yogurt_highlight = true;
                    yogurt_text.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    quantity_yogurt.setDividerColor(getResources().getColor(R.color.colorAccent));
                } else {
                    yogurt_highlight = false;
                    yogurt_text.setHintTextColor(getResources().getColor(R.color.label_text));
                    quantity_yogurt.setDividerColor(getResources().getColor(R.color.label_text));
                }
            }
        });
        order_one.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (order_one.isChecked() == true && schedule_order.isChecked() == true) {
                    schedule_order.setChecked(false, true);
                }
                if (order_one.isChecked())
                    one_time = true;
            }
        });
        schedule_order.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (order_one.isChecked() == true && schedule_order.isChecked() == true) {
                    order_one.setChecked(false, true);
                    one_time = false;
                }
                if (schedule_order.isChecked())
                    one_time = false;
            }
        });
    }

    public boolean isValid() {
        if ((milk.isChecked() || yogurt.isChecked()) && (order_one.isChecked() || schedule_order.isChecked()) && address.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

    public void startNextActivity(View view) {
        if (isValid()) {
            makeContract();
            Intent nextActivity = new Intent(this, AddActivity.class);
            nextActivity.putExtra("Firebase", contract);
            nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextActivity);
            finish();
        }
    }

    public void makeContract() {
        if (milk.isChecked()) {
            if (quant_m > 1)
                contract.setMilkQuantity(quant_m);
            else
                contract.setMilkQuantity(1);
        }
        if (yogurt.isChecked()) {
            if (quant_y > 1)
                contract.setYogurtQuantity(quant_m);
            else
                contract.setYogurtQuantity(1);
        }
        if (one_time)
            contract.setOrderType("OneTime");
        else
            contract.setOrderType("ScheduleOrder");
        contract.setAddress(address.getText().toString().trim());
    }


}
