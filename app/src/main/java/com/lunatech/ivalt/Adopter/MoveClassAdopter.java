package com.lunatech.ivalt.Adopter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lunatech.ivalt.Model.Moves;
import com.lunatech.ivalt.R;
import com.lunatech.ivalt.ViewMove;

import java.util.ArrayList;

public class MoveClassAdopter extends RecyclerView.Adapter {


    private final Context context;
    private ArrayList<Moves> movesList;

    public MoveClassAdopter(Context context, ArrayList<Moves> movesList) {
        this.context = context;
        this.movesList = movesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.move_card, parent, false);
        return new MoveClassAdopter.Movie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MoveClassAdopter.Movie) holder).Bind(movesList.get(position));

    }

    @Override
    public int getItemCount() {
        return movesList.size();
    }

    class Movie extends RecyclerView.ViewHolder {
        RelativeLayout cardView;
        LinearLayout linearLayout;

        public Movie(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.mainBg);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ViewMove.class));

                }
            });

            linearLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cardView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_focused_bg, null));
                        cardView.setPadding(2, 2, 2, 2);
                    } else {
                        cardView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_normal_bg, null));
                        cardView.setPadding(0, 0, 0, 0);
                    }
                }
            });
        }

        public void Bind(Moves moves) {
        }
    }


}

