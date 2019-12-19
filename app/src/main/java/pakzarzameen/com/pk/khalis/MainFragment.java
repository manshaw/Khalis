package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    Button add_order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        add_order = (Button) view.findViewById(R.id.add_new);
        add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(getContext(), NewOrderActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });
        return view;
    }
}
