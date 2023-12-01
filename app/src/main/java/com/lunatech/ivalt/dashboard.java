package com.lunatech.ivalt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lunatech.ivalt.Adopter.MoveClassAdopter;
import com.lunatech.ivalt.Model.Moves;

import java.util.ArrayList;


public class dashboard extends Fragment {


    RecyclerView recyclerView;
    RecyclerView trendingRecyclerView;
    RecyclerView newlyUploadedRecyclerView;
    MoveClassAdopter moveClassAdopter;
    ArrayList<Moves> movesArrayList = new ArrayList<>(10);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, null);
        for (int i = 0; i < 10; i++) {
            movesArrayList.add(new Moves("moveName", "moveType", "moveCategory", "movePower", "moveAccuracy"));
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.live_recycler_view);
        newlyUploadedRecyclerView = (RecyclerView) view.findViewById(R.id.newly_uploaded_recycler_view);
        trendingRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recycler_view);


        moveClassAdopter = new MoveClassAdopter(getContext(), movesArrayList);
        recyclerView.setAdapter(moveClassAdopter);
        newlyUploadedRecyclerView.setAdapter(moveClassAdopter);
        trendingRecyclerView.setAdapter(moveClassAdopter);



        return view;
    }
}