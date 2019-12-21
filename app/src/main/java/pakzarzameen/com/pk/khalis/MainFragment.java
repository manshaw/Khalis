package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    LinearLayout curr,prev,new_order;
    ProgressBar mProgressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        curr = (LinearLayout) view.findViewById(R.id.llCurrent);
        prev = (LinearLayout) view.findViewById(R.id.llPrevious);
        new_order = (LinearLayout) view.findViewById(R.id.llNew);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar2);

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

    @Override
    public void onResume() {
        super.onResume();
        mProgressbar.setVisibility(View.GONE);
    }
}
