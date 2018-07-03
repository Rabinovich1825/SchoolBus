package com.example.rabinovich.schoolbus.Fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rabinovich.schoolbus.Database.Bus;
import com.example.rabinovich.schoolbus.Database.BusViewModel;
import com.example.rabinovich.schoolbus.Database.StudentViewModel;
import com.example.rabinovich.schoolbus.Database.Trip;
import com.example.rabinovich.schoolbus.Database.TripStudent;
import com.example.rabinovich.schoolbus.Database.TripStudentViewModel;
import com.example.rabinovich.schoolbus.Database.TripViewModel;
import com.example.rabinovich.schoolbus.Database.User;
import com.example.rabinovich.schoolbus.Database.UserViewModel;
import com.example.rabinovich.schoolbus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TripShowFragment extends Fragment {
TripViewModel tripViewModel;
TextView trip_id;
CalendarView date_trip;
Spinner driverSpinner;
Spinner busSpinner;
String date;
private List<User> users;
private List<Bus> buses;
private BusViewModel busViewModel;
private UserViewModel userViewModel;
private StudentViewModel studentViewModel;
private TripStudentViewModel tripStudentViewModel;

    public TripShowFragment(TripViewModel tripViewModel, UserViewModel userViewModel, BusViewModel busViewModel,StudentViewModel studentViewModel, TripStudentViewModel tripStudentViewModel) {
        this.tripViewModel=tripViewModel;
        this.busViewModel = busViewModel;
        this.userViewModel = userViewModel;
        this.studentViewModel = studentViewModel;
        this.tripStudentViewModel = tripStudentViewModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trip_id = (TextView) getView().findViewById(R.id.trip_id_edit);
        driverSpinner = (Spinner) getView().findViewById(R.id.driver_list_edit);
        busSpinner = (Spinner) getView().findViewById(R.id.bus_list_edit);
        date_trip = (CalendarView) getView().findViewById(R.id.date_trip_edit);
        date = "01-01-1900";
        date_trip.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date= String.valueOf(dayOfMonth) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
            }
        });

        Integer show_id = getArguments().getInt("Id");
        final String current_id = String.valueOf(show_id);
        tripViewModel.getTripById(show_id).observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip otrip) {
                //declaras en este espacio todo lo que hagas utilizando el trip obtenido
                trip_id.setText(current_id);
                final Trip trip = otrip;
                final Context context = getContext();


                userViewModel.getUsersByUserType("driver").observe(getActivity(), new Observer<List<User>>() {
                    @Override
                    public void onChanged(@Nullable List<User> ousers) {
                        users = ousers;
                        List<String> userStrings = new ArrayList<>();
                        for (User user:users) {
                            userStrings.add(user.getFirst_name() + " " + user.getLast_name());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, userStrings);
                        driverSpinner.setAdapter(adapter);
                        busSpinner.setSelection(GetCurrentSelectedUserPosition(users, trip.getDriverId()));
                    }
                });

                busViewModel.getAllBuses().observe(getActivity(), new Observer<List<Bus>>() {
                    @Override
                    public void onChanged(@Nullable List<Bus> obuses) {
                        buses = obuses;
                        List<String> busStrings = new ArrayList<>();
                        for (Bus bus:buses) {
                            busStrings.add(bus.getPlate());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, busStrings);
                        busSpinner.setAdapter(adapter);
                        busSpinner.setSelection(GetCurrentSelectedBusPosition(buses, trip.getBusId()));
                    }
                });
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String str_date = trip.getDate();
                long l_date = 0;
                try {
                    l_date = sdf.parse(str_date).getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date_trip.setDate(l_date);




                Button mAddStundentsButton = (Button) getView().findViewById(R.id.add_students_button);
                mAddStundentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenTripStudentFragment(trip);
                    }
                });
                Button mEditButton = (Button) getView().findViewById(R.id.Edit_trip);
                mEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Edit(trip);
                    }
                });

                Button mDeleteButton = (Button) getView().findViewById(R.id.Delete_trip);
                mDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Delete(trip);
                    }
                });
            }
        });


    }

    private int GetCurrentSelectedUserPosition(List<User> users, int id){
        int position = 0;
        for (User user: users) {
            if(user.getId() == id){
                position = users.indexOf(user);
                break;
            }
        }
        return position;
    }

    private int GetCurrentSelectedBusPosition(List<Bus> buses, int id){
        int position = 0;
        for (Bus bus: buses) {
            if(bus.getId() == id){
                position = buses.indexOf(bus);
                break;
            }
        }
        return position;
    }


    public void OpenTripStudentFragment(Trip trip){
        TripStudentFragment tripStudentFragment = new TripStudentFragment(trip, studentViewModel, tripStudentViewModel);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, tripStudentFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void Edit(Trip trip){
        trip.setDriverId(users.get((int)driverSpinner.getSelectedItemId()).getId());
        trip.setBusId(buses.get((int)busSpinner.getSelectedItemId()).getId());
        trip.setDate(date);

        tripViewModel.update(trip);

        getActivity().getSupportFragmentManager().popBackStack();

    }


    private void Delete(Trip trip){
        tripViewModel.delete(trip);
        getActivity().getSupportFragmentManager().popBackStack();


    }



}
