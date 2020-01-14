package pakzarzameen.com.pk.khalis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    SharedPreferences prefs = null;
    private FragmentTransaction fragmentTransaction;
    private static final String PACAKGE_NAME = "pk.com.pakzarzameen.khalis";
    EditText name, address;
    Button update;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);
    }

    private void openNextFragment(Fragment fragment) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLanguageManager appLanguageManager = new AppLanguageManager(getContext());
        View view;
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            view = inflater.inflate(R.layout.fragment_profile_ar, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        name = (EditText) view.findViewById(R.id.etFullName);
        address = (EditText) view.findViewById(R.id.etAddress);
        update = (Button) view.findViewById(R.id.bUpdateProfile);
        if (prefs.contains("Name"))
            name.setText(prefs.getString("Name", "Name"));

        if (prefs.contains("Address"))
            address.setText(prefs.getString("Address", "Address(Optional)"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().length() > 0) {
                    prefs.edit().putString("Name", name.getText().toString().trim()).commit();
                    if (address.getText().toString().trim().length() > 0) {
                        prefs.edit().putString("Address", address.getText().toString().trim()).commit();
                    }
                    prefs.edit().putBoolean("ProfileSpecified", true).commit();
                    Toast.makeText(getContext(), "Successfully Updated!", Toast.LENGTH_LONG).show();
                    getActivity().recreate();
                }
            }
        });
        return view;
    }


}
