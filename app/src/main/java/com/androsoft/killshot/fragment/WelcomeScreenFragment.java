package com.androsoft.killshot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.androsoft.killshot.R;
import com.androsoft.killshot.databinding.FragmentWelcomeScreenBinding;
import com.androsoft.killshot.dialog.InfoDialog;

import java.util.HashMap;

public class WelcomeScreenFragment extends Fragment {

    public WelcomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        FragmentWelcomeScreenBinding binding = FragmentWelcomeScreenBinding.bind(view);
        binding.buttonContributed.setOnClickListener(v -> new InfoDialog(requireContext())
                .setHyperlinks(new HashMap<String, String>() {{
                    put("Spaceship icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/spaceship");
                    put("Spaceship icons created by photo3idea_studio - Flaticon", "https://www.flaticon.com/free-icons/spaceship");
                    put("Bullet point icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/bullet-point");
                    put("Munitions icons created by Smashicons - Flaticon", "https://www.flaticon.com/free-icons/munitions");
                    put("Bullet icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/bullet");
                    put("Spaceship icons created by Freepik - Flaticon 2", "https://www.flaticon.com/free-icons/spaceship");
                    put("Spaceship icons created by Freepik - Flaticon 3", "https://www.flaticon.com/free-icons/spaceship");
                }})
                .setTitle(getString(R.string.help))
                .setPositiveButton(getString(R.string.close), null)
                .show());
        binding.buttonClose.setOnClickListener(v -> requireActivity().finishAffinity());
        binding.buttonStart.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_WelcomeScreenFragment_to_NetworkSelectFragment));
        return view;
    }
}