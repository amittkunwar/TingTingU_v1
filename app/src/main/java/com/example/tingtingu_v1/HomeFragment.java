package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private int currentSlide = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the ViewPager2
        viewPager2 = view.findViewById(R.id.imageSlider);

        // Set up image list
        List<Integer> imageList = new ArrayList<>();
        // Add your images here
        imageList.add(R.drawable.slide3);
        imageList.add(R.drawable.slide6);

        // Set up the adapter
        SliderAdapter adapter = new SliderAdapter(imageList);
        viewPager2.setAdapter(adapter);

        // Auto slide functionality
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentSlide = position;
                sliderHandler.removeCallbacks(slideRunnable);
                sliderHandler.postDelayed(slideRunnable, 2000); // Slide every 2 seconds
            }
        });

        // Find the survey icon and set click listener
        ImageView surveyIcon = view.findViewById(R.id.survey);  // Your survey icon
        surveyIcon.setOnClickListener(v -> openWebFragment());

        // Find the create profile section and set click listener
        ImageView createProfileIcon = view.findViewById(R.id.add_user);  // Your create profile icon
        createProfileIcon.setOnClickListener(v -> openCreateProfileFragment());

        // Find the invite user icon and set click listener to navigate to activity_refer
        ImageView inviteUserIcon = view.findViewById(R.id.invite_user);
        inviteUserIcon.setOnClickListener(v -> openReferActivity());

        // Find the message icon (msg) and set click listener to navigate to main_msg fragment
        ImageView msgIcon = view.findViewById(R.id.msg);  // Your message icon
        msgIcon.setOnClickListener(v -> openMainMsgFragment()); // Call the method to open main_msg fragment

        return view; // Return the inflated view
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentSlide == viewPager2.getAdapter().getItemCount() - 1) {
                currentSlide = 0;
            } else {
                currentSlide++;
            }
            viewPager2.setCurrentItem(currentSlide, true);
        }
    };

    // Method to open CreateProfileFragment
    private void openCreateProfileFragment() {
        Fragment createProfileFragment = new CreateProfileFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, createProfileFragment); // Ensure you replace the correct container
        transaction.addToBackStack(null); // Optional: add to back stack
        transaction.commit();
    }

    // Method to open WebFragment
    private void openWebFragment() {
        Fragment webFragment = new WebFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, webFragment); // Ensure you replace the correct container
        transaction.addToBackStack(null); // Optional: add to back stack
        transaction.commit();
    }

    // Method to navigate to the Refer Activity
    private void openReferActivity() {
        Intent intent = new Intent(getActivity(), Refer.class); // Replace ReferActivity with the actual class name of your refer activity
        startActivity(intent);
    }

    // Method to open main_msg Fragment
    private void openMainMsgFragment() {
        Fragment mainMsgFragment = new main_msg();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainMsgFragment); // Ensure you replace the correct container
        transaction.addToBackStack(null); // Optional: add to back stack
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(slideRunnable, 2000); // Resume auto sliding
    }
}
