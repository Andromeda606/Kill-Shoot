package com.androsoft.killshot.fragment;

import android.net.InetAddresses;
import android.os.Build;
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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
            View view = getView();
            if (view == null)
                return; // View null olabilir çünkü navigate edildikten sonra çağrılabilir (127.0.0.1 yüzünden)
            Bundle bundle = new Bundle();
            bundle.putString(BundleTags.IP_ADDRESS, ipAddress);
            try {
                Navigation.findNavController(view).navigate(R.id.action_NetworkSelectFragment_to_CharacterSelectFragment, bundle);
            } catch (Exception e) {
                Log.e("NetworkSelectFragment", "navigateCharacterSelect: ", e);
            }
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
                            .setMessage(String.format(getString(R.string.battle_request), ipAddress))
                            .setPositiveButton(getString(R.string.accept).toUpperCase(), (dialog, which) -> {
                                streamInterface.acceptBattle();
                                navigateCharacterSelect(ipAddress);
                            })
                            .setNeutralButton(getString(R.string.reject).toUpperCase(), (dialog, which) -> streamInterface.rejectBattle())
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
                        .setTitle(getString(R.string.warning).toUpperCase())
                        .setMessage(getString(R.string.rejected_battle_request))
                        .setPositiveButton(getString(R.string.ok).toUpperCase(), null)
                        .show());

            }
        });
        binding.button.setOnClickListener(v -> {
            String ipAddress = binding.ipAddressText.getText().toString();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        InetAddress.getByName(ipAddress);
                    } catch (UnknownHostException e) {
                        requireActivity().runOnUiThread(() -> CustomDialog.showConnectionErrorDialog(requireContext()));
                        return;
                    }
                    Network network = new Network(ipAddress);
                    try {
                        requireActivity().runOnUiThread(() -> network.createConnectedThread().findDevice());
                    } catch (Exception e) {
                        Log.wtf("eerror", e.getMessage());
                    }
                }
            }.start();

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
                        .setTitle(getString(R.string.help))
                        .setPositiveButton(getString(R.string.close), null)
                        .show()
        );
        return binding.getRoot();
    }
}