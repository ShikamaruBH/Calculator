package com.example.calculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScreenModel extends ViewModel {

    private MutableLiveData<String> text;
    public LiveData<String> getString() {
        if (text == null) {
            text = new MutableLiveData<>();
            text.setValue("0");
        }
        return text;
    }

    public void addString(String str) {
        if ("0".equals(text.getValue())) {
            text.setValue(str);
        } else {
            text.setValue(text.getValue() + str);
        }
    }
    public void removeLast() {
        String s = text.getValue();
        if (text.getValue().length() == 1) {
            text.setValue("0");
        } else if (text.getValue().length() > 0) {
            text.setValue(s.substring(0, s.length() - 1));
        }
    }
    public void clear() {
        text.setValue("");
    }
}
