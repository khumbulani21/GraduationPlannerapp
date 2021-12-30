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

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<List<Course>> courses;
    private String pName;
    private String sName;
    private String year;

    public LiveData<List<Course>> getCourses(String pName, String sName,String year) {
this.pName=pName;
this.sName=sName;
this.year=year;
        if (courses == null) {
            courses = new MutableLiveData<List<Course>>();

            readPlans();
        }
        return courses;
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseCourses;




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
        List<Course> coursesList= new ArrayList<>();


        //planS = new MutableLiveData<List<Plan>>();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        // databasePlans= FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans");
        databaseCourses = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(pName).child("semesters").child(sName+" "+year).child("Courses");
        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList.clear();
                System.out.println("Semester LIST 0ppppppppppppppppppppppppppppppppppppppppppppppppppppppppp00000000000000000000000000000"+snapshot);
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Course course=snapshot1.getValue(Course.class);
                    coursesList.add(course);
                }

                courses.setValue(coursesList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
