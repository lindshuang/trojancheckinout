package cs310.trojancheckinout.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import cs310.trojancheckinout.CheckIn;
import cs310.trojancheckinout.R;
import cs310.trojancheckinout.ShowAllBuildingsActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Bundle bundle = getActivity().getIntent().getExtras();
        String currEmail = "";
        if(bundle != null) {
            currEmail =  bundle.getString("email");
        }
        final Button tempButton = root.findViewById(R.id.button);

        String finalCurrEmail = currEmail;
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tempActivityIntent = new Intent(getActivity(), CheckIn.class);
                tempActivityIntent.putExtra("email", finalCurrEmail);
                startActivity(tempActivityIntent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        return root;
    }
}