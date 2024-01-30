package com.pac.imonline.activity.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pac.imonline.R;
import com.pac.imonline.activity.Community;
import com.pac.imonline.activity.CommunityAdapter;
import com.pac.imonline.activity.CommunityDao;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CommunityListFragment extends Fragment {

    private static final int REQUEST_CREATE_COMMUNITY = 1;
    private static final int RESULT_COMMUNITY_CREATED = 3;

    private RecyclerView communityRecyclerView;
    private CommunityAdapter communityAdapter;
    private List<Community> communityList;
    private CommunityDao communityDao;
    private AppDatabase appDatabase;

    public CommunityListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community_list, container, false);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.botton_home:
                    startActivity(new Intent(requireContext(), MainActivity.class));
                    requireActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    requireActivity().finish();
                    return true;

            }

            return false;
        });

        communityRecyclerView = view.findViewById(R.id.communityRecyclerView);
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        communityList = new ArrayList<>();
        communityAdapter = new CommunityAdapter(communityList);
        communityRecyclerView.setAdapter(communityAdapter);

        appDatabase = AppDatabase.getAppDatabase(requireContext());
        communityDao = appDatabase.getCommunityDao();

        communityDao.getAllCommunities().observe(getViewLifecycleOwner(), new Observer<List<Community>>() {
            @Override
            public void onChanged(List<Community> communities) {
                communityList.clear();
                communityList.addAll(communities);
                communityAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void onCreateCommunityClicked(View view) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CreateCommunityFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CREATE_COMMUNITY && resultCode == RESULT_COMMUNITY_CREATED && data != null) {
            Community community = data.getParcelableExtra("community");
            if (community != null) {
                communityList.add(community);
                communityAdapter.notifyItemInserted(communityList.size() - 1);
            }
        }
    }
}
