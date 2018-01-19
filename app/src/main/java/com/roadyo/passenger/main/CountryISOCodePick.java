package com.roadyo.passenger.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ourcabby.passenger.R;


public class CountryISOCodePick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_isocode_pick);

        FragmentManager manager = getSupportFragmentManager();
//        FragmentManager manager;
        FragmentTransaction transaction = manager.beginTransaction();

        CountryPicker picker = new CountryPicker();

        transaction.replace(R.id.home, picker);
        transaction.commit();
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode) {
                String message=dialCode;
                Intent intent=new Intent();
                intent.putExtra("COUNTRY_CODE",message);
                setResult(2,intent);
                finish();//finishing activity
            }
        });
    }

}
