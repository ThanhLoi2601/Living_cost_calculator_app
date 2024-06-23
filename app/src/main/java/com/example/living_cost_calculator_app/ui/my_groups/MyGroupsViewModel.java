package com.example.living_cost_calculator_app.ui.my_groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyGroupsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyGroupsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my groups fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}