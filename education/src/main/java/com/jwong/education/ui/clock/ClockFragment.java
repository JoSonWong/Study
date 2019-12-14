package com.jwong.education.ui.clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jwong.education.R;

public class ClockFragment extends Fragment implements View.OnClickListener {

    private ClockViewModel clockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clock, container, false);
        root.findViewById(R.id.btn_clock).setOnClickListener(this);
        clockViewModel.getCurriculumList().observe(this, curriculumList -> {

        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clock:

                break;
        }
    }
}