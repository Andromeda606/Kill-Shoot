package com.androsoft.killshot.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.androsoft.killshot.R;
import com.androsoft.killshot.connection.BattleInterface;
import com.androsoft.killshot.connection.StreamInterface;
import com.androsoft.killshot.connection.network.Network;
import com.androsoft.killshot.connection.network.NetworkConnectedThread;
import com.androsoft.killshot.constant.BundleTags;
import com.androsoft.killshot.databinding.FragmentNetworkSelectBinding;
import com.androsoft.killshot.dialog.CustomDialog;
import com.androsoft.killshot.dialog.InfoDialog;
import com.androsoft.killshot.util.DeviceUtil;

import java.util.HashMap;

public class NetworkSelectFragment extends Fragment {
    public NetworkSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void navigateCharacterSelect(String ipAddress) {

        requireActivity().runOnUiThread(() -> {
            Bundle bundle = new Bundle();
            bundle.putString(BundleTags.IP_ADDRESS, ipAddress);
            Navigation.findNavController(requireView()).navigate(R.id.action_NetworkSelectFragment_to_CharacterSelectFragment, bundle);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNetworkSelectBinding binding = FragmentNetworkSelectBinding.bind(inflater.inflate(R.layout.fragment_network_select, container, false));
        binding.yourIpAddressText.setText(String.format(getString(R.string.local_ip_address), DeviceUtil.getLocalIpAddress()));
        NetworkConnectedThread.setOnBattleInit(new BattleInterface.OnBattleInit() {
            @Override
            public void onRequest(String ipAddress) {
                requireActivity().runOnUiThread(() -> {
                    Network network = new Network(ipAddress);
                    StreamInterface streamInterface;
                    try {
                        streamInterface = network.createConnectedThread();
                    } catch (Exception e) {
                        CustomDialog.showConnectionErrorDialog(requireContext());
                        return;
                    }

                    new CustomDialog(requireContext())
                            .setMessage("Düşman Savaş Teklifi gönderdi! IP Adresi: " + ipAddress)
                            .setPositiveButton("KABUL ET", (dialog, which) -> {
                                streamInterface.acceptBattle();
                                navigateCharacterSelect(ipAddress);
                            })
                            .setNeutralButton("REDDET", (dialog, which) -> streamInterface.rejectBattle())
                            .show();
                });
            }

            @Override
            public void catchProcess(String ipAddress, Boolean status) {
                if (status) {
                    navigateCharacterSelect(ipAddress);
                    return;
                }
                requireActivity().runOnUiThread(() -> new CustomDialog(requireContext())
                        .setTitle("Uyarı")
                        .setMessage("Düşman savaş teklifini reddetti")
                        .setPositiveButton("tamam", null)
                        .show());

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
        binding.iconHelp.setOnClickListener(v ->
                new InfoDialog(requireContext())
                        .setHyperlinks(new HashMap<String, String>() {{
                            put("Spaceship icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/spaceship");
                            put("Spaceship icons created by photo3idea_studio - Flaticon", "https://www.flaticon.com/free-icons/spaceship");
                            put("Bullet point icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/bullet-point");
                            put("Munitions icons created by Smashicons - Flaticon", "https://www.flaticon.com/free-icons/munitions");
                            put("Bullet icons created by Freepik - Flaticon", "https://www.flaticon.com/free-icons/bullet");
                            put("Spaceship icons created by Freepik - Flaticon 2", "https://www.flaticon.com/free-icons/spaceship");
                            put("Spaceship icons created by Freepik - Flaticon 3", "https://www.flaticon.com/free-icons/spaceship");
                        }})
                        .setTitle("Yardım")
                        .setPositiveButton("Kapat", null)
                        .show()
        );
        return binding.getRoot();
    }
}