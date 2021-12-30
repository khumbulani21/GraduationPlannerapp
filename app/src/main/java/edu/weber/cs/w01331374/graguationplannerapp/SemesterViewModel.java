package edu.weber.cs.w01331374.graguationplannerapp;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;


public class SemesterViewModel extends ViewModel {
    private MutableLiveData<List<Semester>> semesters;
String planName;
    public LiveData<List<Semester>> getSemesters(String planName) {
        this.planName=planName;
        if (semesters == null) {
            semesters = new MutableLiveData<List<Semester>>();

            readPlans();
        }
        return semesters;
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseSemesters;




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
        List<Semester> sems= new ArrayList<>();


        //planS = new MutableLiveData<List<Plan>>();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
       // databasePlans= FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans");
        databaseSemesters = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(planName).child("semesters");
        databaseSemesters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sems.clear();
                System.out.println("Semester LIST 0ppppppppppppppppppppppppppppppppppppppppppppppppppppppppp00000000000000000000000000000"+snapshot);
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Semester sem=snapshot1.getValue(Semester.class);
                    sems.add(sem);
                }

                semesters.setValue(sems);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
