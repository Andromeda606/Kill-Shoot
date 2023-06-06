package com.androsoft.killshot.fragment.characterselect.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.androsoft.killshot.databinding.ItemCharacterTabBinding;

public class CharacterSelectViewHolder extends RecyclerView.ViewHolder {
    ItemCharacterTabBinding binding;
    public CharacterSelectViewHolder(final View itemView) {
        super(itemView);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

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