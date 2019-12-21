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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pakzarzameen.com.pk.khalis.Utils.FbContract;

public class PreviousOrderActivity extends AppCompatActivity {
    String currentuser;
    ProgressBar mProgressbar;
    private DatabaseReference mDatabaseReference;
    private FbContract session = null;
    List<FbContract> session_List;
    RecyclerView recList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_orders);
        recList = (RecyclerView) findViewById(R.id.cardList2);
        mProgressbar = (ProgressBar) findViewById(R.id.progressBar3);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        session_List = new ArrayList<>();
        getsession();
    }

    private void getsession() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query LastSession = mDatabaseReference.child("/Test/User/"+currentuser + "/Previous Orders").orderByKey();  //gets us last seven sessions
        LastSession.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                DataSnapshot snp = dataSnapshot;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {    //traverse through last seven sessions, utimately mLastsession = lastSession in database
                    session = snap.getValue(FbContract.class);
                    assert session != null;
                    session_List.add(session);
                }
                mProgressbar.setVisibility(View.GONE);
                i++;
                PreviousAdapter rv = new PreviousAdapter(session_List);
                recList.setAdapter(rv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //  progressDialog.dismiss();
        });

    }
}
