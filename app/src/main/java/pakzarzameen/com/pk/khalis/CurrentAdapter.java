package pakzarzameen.com.pk.khalis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.ContactViewHolder> {

    private List<FbContract> DataList;
    private List<String> key_list;

    public CurrentAdapter(List<FbContract> contactList, List<String> contactKey) {
        this.DataList = contactList;
        this.key_list = contactKey;
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        FbContract ci = DataList.get(i);
        final String key = key_list.get(i);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM,yyyy \n  h:mm aaa");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String normalTime = sdf.format(new java.util.Date(ci.getTimeStamp()));
        contactViewHolder.order_type.setText(ci.getOrderType());
        contactViewHolder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                        .setIcon(R.drawable.ic_close_black_24dp)
                        .setTitle("Delete Order")
                        .setMessage("Are you sure you want to delete the order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(key);
                                    Intent intent = new Intent(view.getContext(),CurrentOrderActivity.class);
                                    view.getContext().startActivity(intent);
                                    ((Activity)view.getContext()).finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });


        if (ci.getOrderType().equals("One-Time")) {
            contactViewHolder.schedule_type.setVisibility(View.GONE);
            contactViewHolder.customdays.setVisibility(View.GONE);
        } else {
            contactViewHolder.schedule_type.setText(ci.getScheduleType());
            if (ci.getScheduleType().equals("Custom Days")) {
                contactViewHolder.customdays.setVisibility(View.VISIBLE);
                contactViewHolder.customdays.setText(TextUtils.join(",", ci.getDays()));
            } else
                contactViewHolder.customdays.setVisibility(View.GONE);
        }
        contactViewHolder.milk.setText(ci.getMilkQuantity() + "Kg");
        contactViewHolder.yogurt.setText(ci.getYogurtQuantity() + "Kg");
        contactViewHolder.time.setText(normalTime);
    }


    public void deleteItem(String key){
        DatabaseReference mDatabaseReference;
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("/Test/User/"+currentuser+"/Current Orders").child(key);
        mDatabaseReference.removeValue();

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_orders, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView milk;
        protected TextView yogurt;
        protected TextView order_type, schedule_type;
        protected TextView customdays;
        protected TextView time;
        protected ImageButton cross;

        public ContactViewHolder(View v) {
            super(v);
            milk = (TextView) v.findViewById(R.id.tvMilkquant);
            yogurt = (TextView) v.findViewById(R.id.tvYogurt_quant);
            order_type = (TextView) v.findViewById(R.id.tvOrderTitle);
            schedule_type = (TextView) v.findViewById(R.id.tvScheduletype);
            customdays = (TextView) v.findViewById(R.id.tvCustomdays);
            time = (TextView) v.findViewById(R.id.tvTime);
            cross = (ImageButton) v.findViewById(R.id.delete_data);
        }
    }
}

