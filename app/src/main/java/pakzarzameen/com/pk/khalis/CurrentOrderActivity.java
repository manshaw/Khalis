package pakzarzameen.com.pk.khalis;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class CurrentOrderActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FbContract session = null;
    List<FbContract> session_List;
    List<String> key_list;
    String currentuser;
    ProgressBar mProgressbar;
    RecyclerView recList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_orders);
        recList = (RecyclerView) findViewById(R.id.cardList);
        mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        session_List = new ArrayList<>();
        key_list = new ArrayList<>();
        getsession();
    }


    private void getsession() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query LastSession = mDatabaseReference.child("/Test/User/"+currentuser + "/Current Orders").orderByKey();  //gets us last seven sessions
        LastSession.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                DataSnapshot snp = dataSnapshot;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {    //traverse through last seven sessions, utimately mLastsession = lastSession in database
                    session = snap.getValue(FbContract.class);
                    assert session != null;
                    session_List.add(session);
                    key_list.add(snap.getKey());
                }
                mProgressbar.setVisibility(View.GONE);
                i++;
                checkForPrevious();
                CurrentAdapter rv = new CurrentAdapter(session_List, key_list);
                recList.setAdapter(rv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //  progressDialog.dismiss();
        });

    }

    private void checkForPrevious(){



        for(int i=0;i<session_List.size();i++)
        {
            if(session_List.get(i).getOrderType().equals("One-Time")) {
                if (session_List.get(i).getPayment().equals("Paid")) {
                    pushToPrevious(session_List.get(i));
                    DatabaseReference mDatabaseReference;
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("/Test/User/" + currentuser + "/Current Orders").child(key_list.get(i));
                    mDatabaseReference.removeValue();
                    session_List.remove(i);
                }
            }
        }
    }

    private void pushToPrevious(FbContract contract){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("/Test/User/"+currentuser+"/Previous Orders");
        mDatabaseReference.push().setValue(contract);
    }
}
