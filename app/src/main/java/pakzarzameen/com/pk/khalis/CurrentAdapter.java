package pakzarzameen.com.pk.khalis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;
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
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, final int i) {
        final AppLanguageManager appLanguageManager = new AppLanguageManager(contactViewHolder.card.getContext());
        final FbContract ci = DataList.get(i);
        final String key = key_list.get(i);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM,yyyy \n  h:mm aaa");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String normalTime = sdf.format(new java.util.Date(ci.getTimeStamp()));
        if (disabledelete(ci.getTimeStamp()))
            contactViewHolder.cross.setVisibility(View.GONE);
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            if (ci.getOrderType().equals("One-Time")) {
                contactViewHolder.order_type.setText("ایک وقت کا آرڈر");
            } else {
                contactViewHolder.order_type.setText("شیڈول آرڈر");
            }
        } else {
            contactViewHolder.order_type.setText(ci.getOrderType());
        }
        contactViewHolder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (appLanguageManager.getAppLanguage().equals("ar")) {
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setTitle("آرڈر ختم کریں")
                            .setMessage("کیا آپ واقعی آرڈر کو حذف کرنا چاہتے ہیں؟")
                            .setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(key);
                                    Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                    view.getContext().startActivity(intent);
                                    ((Activity) view.getContext()).finish();
                                }
                            })
                            .setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                } else {
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setTitle("Delete Order")
                            .setMessage("Are you sure you want to delete the order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(key);
                                    Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                    view.getContext().startActivity(intent);
                                    ((Activity) view.getContext()).finish();
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
            }
        });

// Below is code for pausing and resuming
        if (ci.getOrderType().equals("Schedule")) {
            if (ci.getStatus().equals("Resumed")) {
                contactViewHolder.pause.setBackgroundResource(R.drawable.ic_pause_white);
                contactViewHolder.pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (appLanguageManager.getAppLanguage().equals("ar")) {
                            final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                                    .setIcon(R.drawable.ic_pause_black_24dp)
                                    .setTitle("عارضی وقفہ")
                                    .setMessage("کیا آپ واقعی آرڈر کو روکنا چاہتے ہیں؟")
                                    .setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DataList.get(i).setStatus("Paused");
                                            pause(key, "Paused");
                                            Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                            view.getContext().startActivity(intent);
                                            ((Activity) view.getContext()).finish();
                                        }
                                    })
                                    .setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .show();
                        } else {
                            final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                                    .setIcon(R.drawable.ic_pause_black_24dp)
                                    .setTitle("Pause Order")
                                    .setMessage("Are you sure you want to pause the order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DataList.get(i).setStatus("Paused");
                                            pause(key, "Paused");
                                            Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                            view.getContext().startActivity(intent);
                                            ((Activity) view.getContext()).finish();
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
                    }
                });
            } else {
                contactViewHolder.card.setFocusable(false);
                contactViewHolder.pause.setBackgroundResource(R.drawable.ic_play_white);
                contactViewHolder.pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (appLanguageManager.getAppLanguage().equals("ar")) {
                            final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                                    .setIcon(R.drawable.ic_play_arrow_black_24dp)
                                    .setTitle("دوبارہ شروع کریں")
                                    .setMessage("کیا آپ واقعی آرڈر دوبارہ شروع کرنا چاہتے ہیں؟")
                                    .setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            pause(key, "Resumed");
                                            Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                            view.getContext().startActivity(intent);
                                            ((Activity) view.getContext()).finish();
                                        }
                                    })
                                    .setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .show();
                        } else {
                            final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext())
                                    .setIcon(R.drawable.ic_play_arrow_black_24dp)
                                    .setTitle("Resume Order")
                                    .setMessage("Are you sure you want to resume the order?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            pause(key, "Resumed");
                                            Intent intent = new Intent(view.getContext(), CurrentOrderActivity.class);
                                            view.getContext().startActivity(intent);
                                            ((Activity) view.getContext()).finish();
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
                    }
                });
            }
        }


        if (ci.getOrderType().equals("One-Time")) {
            contactViewHolder.schedule_type.setVisibility(View.GONE);
            contactViewHolder.customdays.setVisibility(View.GONE);
        } else {
            contactViewHolder.schedule_type.setText(ci.getScheduleType());
            if (ci.getScheduleType().equals("Custom Days") || ci.getScheduleType().equals("حسب ضرورت دن")) {
                contactViewHolder.customdays.setVisibility(View.VISIBLE);
                contactViewHolder.customdays.setText(TextUtils.join(",", ci.getDays()));
            } else
                contactViewHolder.customdays.setVisibility(View.GONE);
        }
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            contactViewHolder.buffaloMilk.setText("Buffalo (" + ci.getBuffaloMilkQuantity() + ")کلو ");
            contactViewHolder.cowMilk.setText("Cow (" + ci.getCowMilkQuantity() + ")کلو ");
            contactViewHolder.yogurt.setText("Yogurt (" + ci.getYogurtQuantity() + ")کلو ");
            contactViewHolder.butter.setText("Butter (" + ci.getButterQuantity() + ")کلو ");
            contactViewHolder.ghee.setText("Ghee (" + ci.getGheeQuantity() + ")کلو ");
        } else {
            contactViewHolder.buffaloMilk.setText("Buffalo (" + ci.getBuffaloMilkQuantity() + " liter)");
            contactViewHolder.cowMilk.setText("Cow (" + ci.getCowMilkQuantity() + " liter)");
            contactViewHolder.yogurt.setText("Yogurt (" + ci.getYogurtQuantity() + " kg)");
            contactViewHolder.butter.setText("Butter (" + ci.getButterQuantity() + " kg)");
            contactViewHolder.ghee.setText("Ghee (" + ci.getGheeQuantity() + " kg)");
        }
        contactViewHolder.time.setText(normalTime);
    }


    public void deleteItem(String key) {
        DatabaseReference mDatabaseReference;
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("/Test/User/" + currentuser + "/Current Orders").child(key);
        mDatabaseReference.removeValue();

    }

    public void pause(String key, String status) {
        DatabaseReference mDatabaseReference;
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("/Test/User/" + currentuser + "/Current Orders/" + key + "/status");
        mDatabaseReference.setValue(status);
    }

    public boolean disabledelete(long timestamp) {
        Date now = Calendar.getInstance().getTime();
        long difference;
        if (timestamp > now.getTime()) {
            difference = (timestamp - now.getTime()) / 1000l / 60l;
            if (difference < 40)
                return true;
        }
        return false;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        AppLanguageManager appLanguageManager = new AppLanguageManager(viewGroup.getContext());
        View itemView;
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_orders_ar, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_orders, viewGroup, false);
        }
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView buffaloMilk, cowMilk;
        protected TextView yogurt, butter, ghee;
        protected TextView order_type, schedule_type;
        protected TextView customdays;
        protected TextView time;
        protected ImageButton cross;
        protected ImageButton pause;
        protected CardView card;

        public ContactViewHolder(View v) {
            super(v);
            buffaloMilk = (TextView) v.findViewById(R.id.tvBM);
            cowMilk = (TextView) v.findViewById(R.id.tvCM);
            yogurt = (TextView) v.findViewById(R.id.tvY);
            butter = (TextView) v.findViewById(R.id.tvB);
            ghee = (TextView) v.findViewById(R.id.tvG);
            order_type = (TextView) v.findViewById(R.id.tvOrderTitle);
            schedule_type = (TextView) v.findViewById(R.id.tvScheduletype);
            customdays = (TextView) v.findViewById(R.id.tvCustomdays);
            time = (TextView) v.findViewById(R.id.tvTime);
            cross = (ImageButton) v.findViewById(R.id.delete_data);
            pause = (ImageButton) v.findViewById(R.id.pause_data);
            card = (CardView) v.findViewById(R.id.card_view);
        }
    }
}

