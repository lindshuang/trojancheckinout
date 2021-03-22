package cs310.trojancheckinout.ui.dashboard;

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

import cs310.trojancheckinout.ProfileActivity;
import cs310.trojancheckinout.R;
import cs310.trojancheckinout.ShowAllBuildingsActivity;
import cs310.trojancheckinout.sharedData;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button profileButton = root.findViewById(R.id.profile_button);
        Bundle bundle = getActivity().getIntent().getExtras();
        final String currEmail = bundle.getString("email");

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedData.setData("SAL");
                Intent profileActivityIntent = new Intent(getActivity(), ProfileActivity.class);
                profileActivityIntent.putExtra("email", currEmail);
                startActivity(profileActivityIntent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        return root;

    }
}