package com.example.rabinovich.schoolbus.Fragments;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rabinovich.schoolbus.Database.UserViewModel;
import com.example.rabinovich.schoolbus.Database.User;
import com.example.rabinovich.schoolbus.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ShowUserFragment extends Fragment {
    UserViewModel userViewModel;
    String show_id;
    Integer current_id;
    TextView id_show;
    EditText name_show;
    EditText last_show;
    EditText email_show;
    EditText type_show;
    User current_user;

    public ShowUserFragment(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_show_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id_show = (TextView) getView().findViewById(R.id.id_view);
        name_show = (EditText) getView().findViewById(R.id.edit_name);
        last_show = (EditText) getView().findViewById(R.id.last_name_edit);
        email_show = (EditText) getView().findViewById(R.id.edit_email);
        type_show = (EditText) getView().findViewById(R.id.edit_type);

        show_id = getArguments().getString("Id");
        current_id = Integer.parseInt(show_id);
        userViewModel.getUserById(current_id).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                id_show.setText(show_id);
                name_show.setText(user.getFirst_name());
                last_show.setText(user.getLast_name());
                email_show.setText(user.getEmail());
                type_show.setText(user.getUser_type());
            }
        });





    }

}
