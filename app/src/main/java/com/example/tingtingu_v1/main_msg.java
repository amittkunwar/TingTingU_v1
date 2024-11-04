package com.example.tingtingu_v1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class main_msg extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_msg, container, false);

        // Initialize TabLayout and ViewPager2
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.viewpager);

        // Set up ViewPager2 with Adapter
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new chats(), "Chats");
        vpAdapter.addFragment(new groupchats(), "GroupChats");
        vpAdapter.addFragment(new settings_msg(), "Settings");
        viewPager2.setAdapter(vpAdapter);

        // Use TabLayoutMediator to link TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chats");
                    break;
                case 1:
                    tab.setText("GroupChats");
                    break;
                case 2:
                    tab.setText("Settings");
                    break;
            }
        }).attach();

        return view;
    }
}
