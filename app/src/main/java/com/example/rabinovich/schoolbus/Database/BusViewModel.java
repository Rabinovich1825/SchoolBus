package com.example.rabinovich.schoolbus.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class BusViewModel extends AndroidViewModel {

    private BusRepository mRepository;

    public BusViewModel(Application application){
        super(application);
        mRepository = new BusRepository(application);
    }

    public LiveData<List<Bus>> getAllBuses(){return mRepository.getAllBuses();}
    public LiveData<Bus> getBusById(int bus_id){return mRepository.getBusById(bus_id);}
    public LiveData<Bus> getBusByDriverId(int driver_id){return mRepository.getBusByDriverId(driver_id);}
    public void insert(Bus bus){mRepository.insert(bus);}
}
