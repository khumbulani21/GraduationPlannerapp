package edu.weber.cs.w01331374.graguationplannerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;

public class PlanViewModel extends  ViewModel {
    private MutableLiveData<List<Plan>> planS;

    public LiveData<List<Plan>> getPlans() {
        if (planS == null) {
            planS = new MutableLiveData<List<Plan>>();

            readPlans();
        }
        return planS;
    }

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        DatabaseReference databasePlans;




//    public String formatEmail(String email){
//        String newEmail="";
//        String temp[]= email.split("\\.");
//        for(String s:temp)
//        {
//            newEmail=newEmail+s;
//        }
//
//        return newEmail;
//    }
    public void readPlans()
    {
       List<Plan> plans= new ArrayList<>();


       //planS = new MutableLiveData<List<Plan>>();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        databasePlans= FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans");

        databasePlans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plans.clear();

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Plan plan=snapshot1.getValue(Plan.class);
                    plans.add(plan);
                }

                planS.setValue(plans);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

 
}
