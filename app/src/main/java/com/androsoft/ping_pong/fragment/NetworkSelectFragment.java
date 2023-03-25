package com.androsoft.ping_pong.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.connection.OnBattleInit;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.connection.network.NetworkConnectedThread;
import com.androsoft.ping_pong.databinding.FragmentNetworkSelectBinding;

public class NetworkSelectFragment extends Fragment {
    public NetworkSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void navigateCharacterSelect(String ipAddress){
        Bundle bundle = new Bundle();
        bundle.putString("ipAddress", ipAddress);
        Navigation.findNavController(requireView()).navigate(R.id.action_NetworkSelectFragment_to_CharacterSelectFragment, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNetworkSelectBinding binding = FragmentNetworkSelectBinding.bind(inflater.inflate(R.layout.fragment_network_select, container, false));
        NetworkConnectedThread.setOnBattleInit(new OnBattleInit() {
            @Override
            public void onRequest(String ipAddress) {

            }

            @Override
            public void catchProcess(String ipAddress, Boolean status) {

            }
        });
        binding.button.setOnClickListener(v -> {
            String ipAddress = binding.ipAddressText.getText().toString();
            Network network = new Network(ipAddress);
            try {
                network.createConnectedThread().findDevice();
            } catch (Exception e) {
                Log.wtf("eerror", e.getMessage());
            }
        });
        return binding.getRoot();
    }
}