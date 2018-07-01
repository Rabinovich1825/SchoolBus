package com.example.rabinovich.schoolbus.Fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.rabinovich.schoolbus.Adapters.UserAdapter;
import com.example.rabinovich.schoolbus.Database.User;
import com.example.rabinovich.schoolbus.Database.UserViewModel;
import com.example.rabinovich.schoolbus.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class AdminUsersFragment extends Fragment {
    UserViewModel userViewModel;
    ListView listView;
    SharedPreferences loginPreferences;
    String email;
    String password;

    @SuppressLint("ValidFragment")
    public AdminUsersFragment(UserViewModel userViewModel) {
        // Required empty public constructor
        this.userViewModel = userViewModel;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginPreferences = this.getContext().getSharedPreferences(getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        email = loginPreferences.getString("userEmail", null);
        password = loginPreferences.getString("userPassword", null);

        userViewModel.getUserByCredentials(email, password).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (!user.getUser_type().equals(getString(R.string.user_type_admin))){
                    view.findViewById(R.id.button4).setVisibility(View.GONE);
                }
            }
        });

        userViewModel.getAllUsers().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                listView = view.findViewById(R.id.user_list_view);
                UserAdapter adapter = new UserAdapter(users, getContext());
                listView.setAdapter(adapter);
            }
        });
    }

}
