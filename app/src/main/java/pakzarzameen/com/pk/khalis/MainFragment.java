package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;

public class MainFragment extends Fragment {
    LinearLayout curr, prev, new_order;
    ProgressBar mProgressbar;
    TextView prev_no, curr_no;
    long child_count_prev, child_count_curr;
    String user;
    DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLanguageManager appLanguageManager = new AppLanguageManager(getContext());
        View view;
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            view = inflater.inflate(R.layout.fragment_main_ar, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_main, container, false);
        }
        curr = (LinearLayout) view.findViewById(R.id.llCurrent);
        prev = (LinearLayout) view.findViewById(R.id.llPrevious);
        new_order = (LinearLayout) view.findViewById(R.id.llNew);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar2);
        prev_no = (TextView) view.findViewById(R.id.tvPrevNum);
        curr_no = (TextView) view.findViewById(R.id.tvCurrNum);
        user = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        getSession();
        curr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(getContext(), CurrentOrderActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(getContext(), PreviousOrderActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });

        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressbar.setVisibility(View.VISIBLE);
                Intent nextActivity = new Intent(getContext(), NewOrderActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });
        return view;
    }

    private void getSession() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("/Test/User/" + user + "/Previous Orders");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                child_count_prev = dataSnapshot.getChildrenCount();
                prev_no.setText(Long.toString(child_count_prev));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //  progressDialog.dismiss();
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("/Test/User/" + user + "/Current Orders");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                child_count_curr = dataSnapshot.getChildrenCount();
                curr_no.setText(Long.toString(child_count_curr));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //  progressDialog.dismiss();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgressbar.setVisibility(View.GONE);
    }
}
