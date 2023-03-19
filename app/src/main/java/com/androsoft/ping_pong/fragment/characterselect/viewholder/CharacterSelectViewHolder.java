package com.androsoft.ping_pong.fragment.characterselect.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.databinding.ItemCharacterTabBinding;

public class CharacterSelectViewHolder extends RecyclerView.ViewHolder {
    ItemCharacterTabBinding binding;
    public CharacterSelectViewHolder(final View itemView) {
        super(itemView);

        this.binding = ItemCharacterTabBinding.bind(itemView);
    }

    public ImageView getShipImageView(){
        return binding.shipImage;
    }

    public TextView getShipTitle(){
        return binding.titleText;
    }

    public TextView getShipDescription(){
        return binding.descriptionText;
    }

    public View getShipLayout(){
        return binding.shipLayout;
    }
}