package com.example.rabinovich.schoolbus.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rabinovich.schoolbus.Database.BusViewModel;
import com.example.rabinovich.schoolbus.Database.StopViewModel;
import com.example.rabinovich.schoolbus.Database.StudentViewModel;
import com.example.rabinovich.schoolbus.Database.TripStudentViewModel;
import com.example.rabinovich.schoolbus.Database.TripViewModel;
import com.example.rabinovich.schoolbus.Database.User;
import com.example.rabinovich.schoolbus.Database.UserViewModel;
import com.example.rabinovich.schoolbus.Fragments.AdminDriverFragment;
import com.example.rabinovich.schoolbus.Fragments.AdminStudentFragment;
import com.example.rabinovich.schoolbus.Fragments.AdminUsersFragment;
import com.example.rabinovich.schoolbus.Fragments.BusFragment;
import com.example.rabinovich.schoolbus.Fragments.GuardianStudentFragment;
import com.example.rabinovich.schoolbus.Fragments.StopFragment;
import com.example.rabinovich.schoolbus.Fragments.TripDriverFragment;
import com.example.rabinovich.schoolbus.Fragments.TripFragment;
import com.example.rabinovich.schoolbus.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    SharedPreferences loginPreferences;
    String email;
    String password;
    String firstName;
    String lastName;
    int id;
    boolean isAdmin;
    UserViewModel userViewModel;
    StopViewModel stopViewModel;
    StudentViewModel studentViewModel;
    TripViewModel tripViewModel;
    BusViewModel busViewModel;
    TripStudentViewModel tripStudentViewModel;


    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);

    }

    @Override
    protected void onStart(){
        super.onStart();

        loginPreferences = getSharedPreferences(getString(R.string.shared_preferences_file), MODE_PRIVATE);
        email = loginPreferences.getString("userEmail", null);
        password = loginPreferences.getString("userPassword", null);
        firstName = loginPreferences.getString("userFirstName", null);
        lastName = loginPreferences.getString("userLastName", null);

        id = loginPreferences.getInt("userId", -1);
        isAdmin = loginPreferences.getBoolean("userIsAdmin", false);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        stopViewModel = ViewModelProviders.of(this).get(StopViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        busViewModel = ViewModelProviders.of(this).get(BusViewModel.class);
        tripStudentViewModel = ViewModelProviders.of(this).get(TripStudentViewModel.class);
        if(id == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 48);
        } else {
            LoadCurrentUser();
        }
    }

    private void LoadCurrentUser() {
        userViewModel.getUserByCredentials(email, password).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                current_user = user;
                SetupNavigationView();
                LoadUi();
                SetupToolbar();
                SetupNavigationHomeButton();
                SetupDrawerListener();
            }
        });
    }

    private void LoadUi() {
        String user_type = current_user.getUser_type();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        if(user_type.equals(getString(R.string.user_type_admin))){
            navigationView.inflateMenu(R.menu.drawer_view_admin);
        }else if(user_type.equals(getString(R.string.user_type_driver))){
            navigationView.inflateMenu(R.menu.drawer_view_driver);
        }else if(user_type.equals(getString(R.string.user_type_guardian))){
            navigationView.inflateMenu(R.menu.drawer_view_guardian);
        }
    }


    private void SetupDrawerListener() {
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    private void SetupNavigationHomeButton() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void SetupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void SetupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        // close drawer when item is tapped
                        if(id==R.id.nav_trips){
                            TripFragment tripFragment = new TripFragment(tripViewModel, userViewModel, busViewModel, studentViewModel, tripStudentViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, tripFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();

                        }

                        if(id==R.id.nav_driver_trips){
                            TripDriverFragment tripDriverFragment = new TripDriverFragment(tripViewModel, userViewModel, busViewModel, studentViewModel, tripStudentViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, tripDriverFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();

                        }



                        if(id==R.id.nav_buses){
                            BusFragment busFragment = new BusFragment(busViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, busFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();

                        }
                        if(id==R.id.nav_stops){
                            StopFragment stopFragment = new StopFragment(stopViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, stopFragment);
                            transaction.addToBackStack(null);
                            mDrawerLayout.closeDrawers();

                            transaction.commit();
                        }
                        if(id==R.id.nav_students){
                            AdminStudentFragment adminStudentFragment = new AdminStudentFragment(studentViewModel, userViewModel, stopViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, adminStudentFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();
                        }
                        if(id==R.id.nav_my_students){
                            GuardianStudentFragment guardianStudentFragment = new GuardianStudentFragment(studentViewModel, userViewModel, stopViewModel, tripViewModel, tripStudentViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, guardianStudentFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();
                        }
                        if(id==R.id.nav_users){
                            AdminUsersFragment adminMainFragment = new AdminUsersFragment(userViewModel);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container, adminMainFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();

                        }
                        if(id==R.id.nav_log_out){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, 48);
                        }

                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            SharedPreferences.Editor prefEditor = loginPreferences.edit();
            Integer id = data.getExtras().getInt("id");
            prefEditor.putInt("userId", data.getExtras().getInt("id"));
            prefEditor.putString("userFirstName", data.getExtras().getString("first_name"));
            prefEditor.putString("userLastName", data.getExtras().getString("last_name"));
            prefEditor.putString("userEmail", data.getExtras().getString("email"));
            prefEditor.putString("userPassword", data.getExtras().getString("password"));
            prefEditor.putBoolean("userIsAdmin", data.getExtras().getBoolean("is_admin"));
            prefEditor.commit();
            Context context = getApplicationContext();
            CharSequence text = "Login Successful";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }



}
