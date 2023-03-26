package com.androsoft.ping_pong.fragment.characterselect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.adapter.CharacterSelectAdapter;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.databinding.FragmentCharacterSelectBinding;
import com.androsoft.ping_pong.util.DeviceUtil;

import java.util.ArrayList;

public class CharacterSelectFragment extends Fragment {
    FragmentCharacterSelectBinding binding;
    public CharacterSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharacterSelectBinding.inflate(inflater);
        ArrayList<Character> characters = new ArrayList<>();
        for (Character.Type characterType : Character.Type.values()) {
            characters.add(Character.convertTypeToCharacter(characterType));
        }
        binding.characterList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.characterList.setAdapter(new CharacterSelectAdapter(characters, this));

        return binding.getRoot();
    }

    public String getIpAddress(){
        return binding.editTextTextPersonName.getText().toString();
    }
}