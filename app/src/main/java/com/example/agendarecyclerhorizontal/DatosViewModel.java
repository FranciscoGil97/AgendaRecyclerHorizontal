package com.example.agendarecyclerhorizontal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class DatosViewModel extends ViewModel {
    private MutableLiveData<Usuario> selected;

    public void setData( Usuario item) {
        if (selected==null) selected=new MutableLiveData< Usuario>();
        selected.setValue(item);
    }

    public LiveData< Usuario> getData() {
        if (selected!= null) setData(selected.getValue());
        else selected=new MutableLiveData< Usuario>();
        return selected;
    }
}
