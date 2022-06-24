package com.example.calculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HistoryModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> history;

    public LiveData<ArrayList<String>> getHistory() {
        if (history == null) {
            history = new MutableLiveData<>();
            history.setValue(new ArrayList<>());
        }
        return history;
    }
    public void addHistory(String equation) {
        ArrayList<String> h = history.getValue();
        h.add(equation);
        history.setValue(h);
    }
    public void clearHistory() {
        history.setValue(new ArrayList<>());
    }
}
