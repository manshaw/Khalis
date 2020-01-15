package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pakzarzameen.com.pk.khalis.Utils.AppLanguageManager;

public class SettingsFragment extends Fragment {
    private TextView tvHeading;
    private LinearLayout llEnglish, llUrdu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvHeading = (TextView) view.findViewById(R.id.tvHeading);
        llEnglish = (LinearLayout) view.findViewById(R.id.llLanguageEnglish);
        llUrdu = (LinearLayout) view.findViewById(R.id.llLanguageUrdu);
        final AppLanguageManager appLanguageManager = new AppLanguageManager(getContext());
        if (appLanguageManager.getAppLanguage().equals("ar")) {
            tvHeading.setText("براہ کرم زبان منتخب کریں");
        } else {
            tvHeading.setText("Please Select Language");
        }
        llEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appLanguageManager.setAppLanguage("en");
                Intent nextActivity = new Intent(getContext(), MainActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });
        llUrdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appLanguageManager.setAppLanguage("ar");
                Intent nextActivity = new Intent(getContext(), MainActivity.class);
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
            }
        });
        return view;
    }
}
