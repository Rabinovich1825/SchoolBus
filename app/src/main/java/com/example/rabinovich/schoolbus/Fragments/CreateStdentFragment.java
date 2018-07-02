package com.example.rabinovich.schoolbus.Fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.example.rabinovich.schoolbus.Database.Stop;
import com.example.rabinovich.schoolbus.Database.StopViewModel;
import com.example.rabinovich.schoolbus.Database.StudentViewModel;
import com.example.rabinovich.schoolbus.Database.User;
import com.example.rabinovich.schoolbus.Database.UserViewModel;
import com.example.rabinovich.schoolbus.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CreateStdentFragment extends Fragment {
    private StudentViewModel studentViewModel;
    private UserViewModel userViewModel;
    private StopViewModel stopViewModel;

    private ArrayList<String> mGuardians;
    private ArrayList<String> mStops;
    
    private AutoCompleteTextView studentNameText;
    private AutoCompleteTextView studentLastNameText;
    private AutoCompleteTextView studentAgeText;
    private AutoCompleteTextView studentClassroomText;
    private AutoCompleteTextView studentRutText;
    private AutoCompleteTextView studentContactPhoneText;
    private Spinner studentGuardianSpinner;
    private Spinner studentStopSpinner;

    public CreateStdentFragment(StudentViewModel studentViewModel, UserViewModel userViewModel, StopViewModel stopViewModel) {
        // Required empty public constructor
        this.studentViewModel = studentViewModel;
        this.userViewModel = userViewModel;
        this.stopViewModel = stopViewModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_stdent, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studentNameText = (AutoCompleteTextView) getView().findViewById(R.id.student_name_text);
        studentLastNameText = (AutoCompleteTextView) getView().findViewById(R.id.student_last_name_text);
        studentAgeText = (AutoCompleteTextView) getView().findViewById(R.id.student_age_text);
        studentClassroomText = (AutoCompleteTextView) getView().findViewById(R.id.student_classroom_text);
        studentRutText = (AutoCompleteTextView) getView().findViewById(R.id.student_rut_text);
        studentContactPhoneText = (AutoCompleteTextView) getView().findViewById(R.id.student_contact_phone_text);
        studentGuardianSpinner = (Spinner) getView().findViewById(R.id.guardian_spinner);
        studentStopSpinner = (Spinner) getView().findViewById(R.id.stop_spinner);
        final Context context = getContext();

        userViewModel.getUsersByUserType(getString(R.string.user_type_guardian)).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                mGuardians = new ArrayList<String>();
                for (Iterator<User> user = users.iterator(); user.hasNext();){
                    User this_user = user.next();
                    mGuardians.add(this_user.getFirst_name() + " " + this_user.getLast_name());
                }
                SetupGuardianSpinner(mGuardians, context);
            }
        });

        stopViewModel.getAllStops().observe(this, new Observer<List<Stop>>() {
            @Override
            public void onChanged(@Nullable List<Stop> stops) {
                mStops = new ArrayList<String>();
                for (Iterator<Stop> stop = stops.iterator(); stop.hasNext();){
                    Stop this_stop = stop.next();
                    mStops.add(this_stop.getStreet() + " " + Integer.toString(this_stop.getNumeration()) + " " + this_stop.getComuna());
                }
                SetupStopSpinner(mStops, context);
            }
        });
    }

    public void SetupGuardianSpinner(ArrayList<String> users, Context context){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentGuardianSpinner.setAdapter(adapter);
    }

    public void SetupStopSpinner(ArrayList<String> stops, Context context){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, stops);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentGuardianSpinner.setAdapter(adapter);
    }
}
