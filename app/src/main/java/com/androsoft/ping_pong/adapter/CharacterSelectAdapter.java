package com.androsoft.ping_pong.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.constant.BundleTags;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.fragment.characterselect.CharacterSelectFragment;
import com.androsoft.ping_pong.fragment.characterselect.viewholder.CharacterSelectViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CharacterSelectAdapter extends RecyclerView.Adapter<CharacterSelectViewHolder> {
    ArrayList<Character> characters;
    CharacterSelectFragment characterSelectFragment;
    public CharacterSelectAdapter(ArrayList<Character> characters, CharacterSelectFragment characterSelectFragment){
        this.characters = characters;
        this.characterSelectFragment = characterSelectFragment;
    }

    @NonNull
    @NotNull
    @Override
    public CharacterSelectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CharacterSelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character_tab, null));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CharacterSelectViewHolder holder, int position) {
        Character character = characters.get(position);
        Context context = holder.itemView.getContext();
        holder.getShipImageView().setImageDrawable(ContextCompat.getDrawable(context, character.getCharacterImage()));
        holder.getShipTitle().setText(context.getString(character.getTitle()));
        holder.getShipDescription().setText(context.getString(character.getDescription()));
        holder.getShipLayout().setOnClickListener(v -> {
            String ipAddress = characterSelectFragment.getIpAddress();
            Network network = new Network(ipAddress);
            try {
                network.createConnectedThread().sendCharacterInformation(position);
            } catch (Exception e) {
                Log.wtf("error connecting",e.getMessage());
                //AlertDialog.Builder ab = new AlertDialog.Builder(holder.itemView.getContext());
            }
            characterSelectFragment.setPlayerType(position);
            if(characterSelectFragment.getEnemyType() != -1){
                characterSelectFragment.navigateGameScreen();
            }

            //Navigation.findNavController(holder.itemView).navigate(R.id.gameScreenNavigate, gameScreenBundle);
        });
    }

    @Override
    public int getItemCount() {
        return this.characters.size();
    }
}
