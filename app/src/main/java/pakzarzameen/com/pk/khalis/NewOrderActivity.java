package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.weiwangcn.betterspinner.library.BetterSpinner;

import net.igenius.customcheckbox.CustomCheckBox;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class NewOrderActivity extends AppCompatActivity {
    private CustomCheckBox order_one, schedule_order;
    private FbContract contract = new FbContract();
    private Boolean one_time = false, schedule_time = false;
    Float quan_buffalo = 0f, quan_cow = 0f, quan_yogurt = 0f, quan_butter = 0f, quan_ghee = 0f;
    String quan_butter_detail, quan_ghee_detail;
    Button add;
    BetterSpinner items, quantity;
    Integer serial = 1;
    Boolean pass = Boolean.FALSE;
    Float Total = 0f;

    private static final String[] ITEMS = new String[]{
            "Buffalo Milk", "Cow Milk", "Yogurt", "Butter", "Desi Ghee"
    };
    private static final String[] PACKING = new String[]{
            "1 liter"
    };
    private static final String[] PACKING_BUTTER = new String[]{
            "0.25 kg", "0.5 kg", "1 kg", "1.25 kg", "1.5 kg", "1.75 kg", "2 kg", "2.25 kg", "2.5 kg", "2.75 kg", "3 kg"
    };
    private static final String[] PACKING_GHEE = new String[]{
            "0.5 kg", "1 kg", "1.5 kg", "2 kg", "2.5 kg", "3 kg", "3.5 kg", "4 kg", "4.5 kg", "5 kg"
    };
    private static final String[] PACKING_MILK = new String[]{
            "1 liter", "2 liter", "3 liter", "4 liter", "5 liter", "6 liter", "7 liter", "8 liter", "9 liter", "10 liter"
    };

    private static final String[] PACKING_YOGURT = new String[]{
            "1 kg", "2 kg", "3 kg", "4 kg", "5 kg", "6 kg", "7 kg", "8 kg", "9 kg", "10 kg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new AppLanguageManager(NewOrderActivity.this).getAppLanguage().equals("ar")) {
            setContentView(R.layout.activity_order_new_ar);
        } else {
            setContentView(R.layout.activity_order_new);
        }
        order_one = (CustomCheckBox) findViewById(R.id.order_one);
        schedule_order = (CustomCheckBox) findViewById(R.id.schedule);
        order_one.setChecked(false, true);
        schedule_order.setChecked(false, true);
        add = (Button) findViewById(R.id.add);
        items = (BetterSpinner) findViewById(R.id.items);
        quantity = (BetterSpinner) findViewById(R.id.quantity);
        ArrayAdapter<String> adapter_items = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ITEMS);
        final ArrayAdapter<String> adapter_packng_butter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PACKING_BUTTER);
        final ArrayAdapter<String> adapter_packng_ghee = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PACKING_GHEE);
        final ArrayAdapter<String> adapter_packing_milk = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PACKING_MILK);
        final ArrayAdapter<String> adapter_packing_yogurt = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PACKING_YOGURT);
        items.setAdapter(adapter_items);
        quantity.setAdapter(adapter_packing_milk);
        contract.setButterQuantity(0f);
        contract.setCowMilkQuantity(0f);
        contract.setYogurtQuantity(0f);
        contract.setButterQuantity(0f);
        contract.setGheeQuantity(0f);
        contract.setAddress("");
        contract.setOrderType("");
        contract.setButterDetail("");
        contract.setGheeDetail("");
        contract.setTimeStamp(0l);

        items.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("Buffalo Milk") || s.toString().equals("Cow Milk")) {
                    quantity.setText(null);
                    quantity.setAdapter(adapter_packing_milk);
                } else if (s.toString().equals("Yogurt")) {
                    quantity.setText(null);
                    quantity.setAdapter(adapter_packing_yogurt);
                } else if (s.toString().equals("Butter")) {
                    quantity.setText(null);
                    quantity.setAdapter(adapter_packng_butter);
                } else if (s.toString().equals("Desi Ghee")) {
                    quantity.setText(null);
                    quantity.setAdapter(adapter_packng_ghee);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    pass = Boolean.TRUE;
                    if (items.getText().toString().equals("Yogurt")) {
                        quan_yogurt = Float.parseFloat(quantity.getText().toString().split("\\s+")[0]);
                        totalPayment(quan_yogurt, 140);
                    } else if (items.getText().toString().equals("Buffalo Milk")) {
                        quan_buffalo = Float.parseFloat(quantity.getText().toString().split("\\s+")[0]);
                        totalPayment(quan_buffalo, 120);
                    } else if (items.getText().toString().equals("Cow Milk")) {
                        quan_cow = Float.parseFloat(quantity.getText().toString().split("\\s+")[0]);
                        totalPayment(quan_cow, 120);
                    } else if (items.getText().toString().equals("Butter")) {
                        quan_butter = Float.parseFloat(quantity.getText().toString().split("\\s+")[0]);
                        totalPayment(quan_butter, 1200);
                    } else if (items.getText().toString().equals("Desi Ghee")) {
                        quan_ghee = Float.parseFloat(quantity.getText().toString().split("\\s+")[0]);
                        totalPayment(quan_ghee, 1900);
                    }
                    addRow(serial, items.getText().toString(), quantity.getText().toString());
                    serial = serial + 1;
                }
            }
        });

        order_one.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if (order_one.isChecked() == true && schedule_order.isChecked() == true) {
                    schedule_order.setChecked(false, true);
                    schedule_time = false;
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
                    schedule_time = true;
            }
        });

    }

    public Boolean valid() {
        if (TextUtils.isEmpty(items.getText())) {
            items.setError("Item is required!");
            return Boolean.FALSE;
        } else if (TextUtils.isEmpty(quantity.getText())) {
            quantity.setError("Quantity is required!");
            return Boolean.FALSE;
        } else {
            quantity.setError(null);
            items.setError(null);
            return Boolean.TRUE;
        }
    }


    public void startNextActivity(View view) {
        if (pass) {
            makeContract();
            Intent nextActivity = new Intent(this, AddActivity.class);
            nextActivity.putExtra("Firebase", contract);
            nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextActivity);
            finish();
        } else {
            if (new AppLanguageManager(NewOrderActivity.this).getAppLanguage().equals("ar")) {
                Toast.makeText(this, "براہ کرم پہلے کچھ مصنوعات شامل کریں", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kindly add some products first", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void makeContract() {
        contract.setBuffaloMilkQuantity(quan_buffalo);
        contract.setCowMilkQuantity(quan_cow);
        contract.setYogurtQuantity(quan_yogurt);
        contract.setButterQuantity(quan_butter);
        contract.setGheeQuantity(quan_ghee);
        contract.setGheeDetail(quan_ghee_detail);
        contract.setButterDetail(quan_butter_detail);
        if (one_time)
            contract.setOrderType("One-Time");
        if (schedule_time)
            contract.setOrderType("Schedule");
    }


    public void addRow(Integer serial, String name, String quan) {
        TextView tip = (TextView) findViewById(R.id.tip);
        tip.setVisibility(View.GONE);
        TableLayout table = (TableLayout) findViewById(R.id.items_table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(getDrawable(R.drawable.cell));
        LinearLayout item = new LinearLayout(this);
        item.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 7));
        item.setGravity(Gravity.CENTER);
        item.setOrientation(LinearLayout.HORIZONTAL);
        TextView tb_serial = new TextView(this);
        tb_serial.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tb_serial.setText("#" + serial.toString());
        item.addView(tb_serial);

        TextView tb_item = new TextView(this);
        tb_item.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3));
        tb_item.setText(name);
        item.addView(tb_item);

        TextView tb_quantity = new TextView(this);
        tb_quantity.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tb_quantity.setText(quan);
        item.addView(tb_quantity);

        tr.addView(item);
        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void totalPayment(Float quan, Integer price) {
        TextView total = (TextView) findViewById(R.id.total);
//        Total = Total + (quan_buffalo * 120) + (quan_butter * 1200) + (quan_cow * 120) + (quan_yogurt * 140) + (quan_ghee * 1900);
        Total = Total + (quan * price);
        contract.setTotal(Total);
        if (new AppLanguageManager(NewOrderActivity.this).getAppLanguage().equals("ar")) {
            total.setText(" Rs." + Total + "کل ادائیگی ");
        } else {
            total.setText("Total Payment: " + Total + " Rs.");
        }
    }
}
