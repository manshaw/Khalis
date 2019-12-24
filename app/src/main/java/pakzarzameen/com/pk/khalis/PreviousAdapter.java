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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import androidx.recyclerview.widget.RecyclerView;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class PreviousAdapter extends RecyclerView.Adapter<PreviousAdapter.ContactViewHolder> {
    private List<FbContract> DataList;


    public PreviousAdapter(List<FbContract> contactList) {
        this.DataList = contactList;
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onBindViewHolder(final PreviousAdapter.ContactViewHolder contactViewHolder, int i) {
        FbContract ci = DataList.get(i);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM,yyyy \n  h:mm aaa");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String normalTime = sdf.format(new java.util.Date(ci.getTimeStamp()));
        contactViewHolder.order_type.setText(ci.getOrderType());
        contactViewHolder.cross.setVisibility(View.GONE);
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


    @Override
    public PreviousAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_orders, viewGroup, false);

        return new PreviousAdapter.ContactViewHolder(itemView);
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


