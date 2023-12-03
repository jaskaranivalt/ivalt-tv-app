package com.lunatech.ivalt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.lunatech.ivalt.Adopter.MoveClassAdopter;
import com.lunatech.ivalt.Model.Moves;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView trendingRecyclerView;
    private RecyclerView newlyUploadedRecyclerView;
    private MoveClassAdopter moveClassAdopter;
    private ArrayList<Moves> movesArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initMoves(); // Initialize the moves list

        recyclerView = view.findViewById(R.id.live_recycler_view);
        newlyUploadedRecyclerView = view.findViewById(R.id.newly_uploaded_recycler_view);
        trendingRecyclerView = view.findViewById(R.id.trending_recycler_view);

        setupRecyclerView(recyclerView);
        setupRecyclerView(newlyUploadedRecyclerView);
        setupRecyclerView(trendingRecyclerView);

        return view;
    }

    private void initMoves() {
        for (int i = 0; i < 10; i++) {
            movesArrayList.add(new Moves("moveName", "moveType", "moveCategory", "movePower", "moveAccuracy"));
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        moveClassAdopter = new MoveClassAdopter(getContext(), movesArrayList);
        recyclerView.setAdapter(moveClassAdopter);
    }
}
