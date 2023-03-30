package com.androsoft.ping_pong.fragment.characterselect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.adapter.CharacterSelectAdapter;
import com.androsoft.ping_pong.connection.BattleInterface;
import com.androsoft.ping_pong.connection.network.NetworkConnectedThread;
import com.androsoft.ping_pong.constant.BundleTags;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.databinding.FragmentCharacterSelectBinding;

import java.util.ArrayList;

/**
 * Karakter seçme işleminde yapılacaklar; <br>
 * Kullanıcı eğer karakteri seçtiyse karşı tarafa seçildiğine dair bir istek gönderilecek.<br>
 * Karşı taraf bu isteği alırsa kendisi de karakter seçti mi diye kontrol edecek eğer seçmediyse<br>seçmesini bekleyecek ve ekrana "Karşı oyuncu karakter seçti" yazacağız
 * Karakteri seçtiyse direkt oyuna gidecek fakat karşı tarafın da oyuna gelmesi gerektiğinden onu da bekleyeceğiz.
 */
public class CharacterSelectFragment extends Fragment {
    FragmentCharacterSelectBinding binding;
    boolean isUsed = false;
    int enemyType = -1, playerType = -1;

    public CharacterSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void navigateGameScreen() {
        Bundle bundle = new Bundle(requireArguments());
        bundle.putString(BundleTags.CHARACTER_TYPE, getPlayerType() + "");
        bundle.putString(BundleTags.ENEMY_TYPE, getEnemyType() + "");
        Navigation.findNavController(requireView()).navigate(R.id.action_CharacterSelectFragment_to_GameFragment, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharacterSelectBinding.inflate(inflater);
        ArrayList<Character> characters = new ArrayList<>();
        Character character = Character.getCharacter(0, null);
        for (int i = 1; character != null; i++) {
            characters.add(character);
            character = Character.getCharacter(i, null);
        }
        binding.characterList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.characterList.setAdapter(new CharacterSelectAdapter(characters, this));
        NetworkConnectedThread.setOnBattleInit(new BattleInterface.OnBattleInit() {
            @Override
            public void characterSelected(String ipAddress, int characterType) {
                if (isUsed()) {
                    navigateGameScreen();
                    return;
                }
                setEnemyType(characterType);
            }
        });
        return binding.getRoot();
    }

    public String getIpAddress() {
        return requireArguments().getString(BundleTags.IP_ADDRESS);
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setEnemyType(int enemyType) {
        this.enemyType = enemyType;
        requireActivity().runOnUiThread(() -> binding.enemyStatus.setText("Düşman karakteri seçti: " + enemyType));
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}