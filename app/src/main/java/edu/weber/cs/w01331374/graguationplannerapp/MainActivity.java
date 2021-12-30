package edu.weber.cs.w01331374.graguationplannerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.User;

public class MainActivity extends AppCompatActivity implements AddPlanFragment.LoadAddSemester, AddSemester.LoadAddCourses, PlanListFragment.HelperInteface ,SemesterListFragment.HelperInteface,SemesterListFragment.ShowAddSemester,SemesterListFragment.ShowCoursesList,CourseListFragment.HelperInteface{
private FragmentManager fm;
    private FirebaseAuth mAuth;
    String uid;
    private static final int RC_SIGN_IN = 100;
    FirebaseDatabase db;
    DatabaseReference dbUsers;
    DatabaseReference databasePlans;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.fullscreendialog);
        setSupportActionBar(toolbar);
        fm=getSupportFragmentManager();

    //authentication
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
            // Create and launch sign-in intent
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN);
        }

        sharedPreferences=getSharedPreferences("planData",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

          db = FirebaseDatabase.getInstance();
        dbUsers = db.getReference("Users");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
         AddPlanFragment addPlanFragment= new AddPlanFragment();
         addPlanFragment.show(fm,"addplan");
            }
        });
        //getAllUsers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),"User sign in  result code "+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);


            if (resultCode == RESULT_OK) {
                //editor.putBoolean("LoginStatus",true);
                // Successfully signed in
                  uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                  editor.putString("uid",uid);
                  editor.commit();
                Toast.makeText(getApplicationContext(),"User sign in  successful "+resultCode, Toast.LENGTH_SHORT).show();
                if(response.isNewUser()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String key=FormatEmail.formatEmail(email);
                 dbUsers = db.getReference("Users").child(uid);
                User newUser=new User(email);
                Map<String, User> users = new HashMap<>();
                users.put("user", new User(email));
                dbUsers.setValue(users);}
            } else {
                //editor.putBoolean("LoginStatus",false);
            }
        }
    }

    @Override
    public void loadAddSemester(String  planName) {
        AddSemester addSemester = new AddSemester();
        addSemester.show(fm,"addSemester");
        addSemester.setPlanName(planName);

        //addCoursesFragment.setData(planName);
    }



    @Override
    public void loadAddCourses(String planName,String semester,String year) {
        AvailableCoursesFragment availableCoursesFragment= new AvailableCoursesFragment();
        availableCoursesFragment.setData(planName,semester,year);
        availableCoursesFragment.show(fm,"aCourses");

    }

    public  List<Plan> getAllUsers( )
    {

        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        databasePlans= FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans");
        return readPlans();
    }

    public List<Plan> readPlans()
    {
      List<Plan>  plans=new ArrayList<>();
        databasePlans.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Plan plan=snapshot1.getValue(Plan.class);

                    plans.add(plan);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return plans;
    }

    @Override
    public void showPlan(String plan) {
        SemesterListFragment semesterListFragment= new SemesterListFragment();
        semesterListFragment.show(fm,"semList");
        semesterListFragment.setPlanName(plan);
    }



    @Override
    public void showSemesters(Plan plan) {
// load courses
    }

    @Override
    public void addCourse(String pName, String sName,String year) {
        AvailableCoursesFragment availableCoursesFragment= new AvailableCoursesFragment();
        availableCoursesFragment.setData(pName,sName,year);
        availableCoursesFragment.show(fm,"aCourses");

    }

    @Override
    public void showAddSemester(String planName) {
        AddSemester addSemester= new AddSemester();
        addSemester.setPlanName(planName);
        addSemester.show(fm,"addSem");

    }

    @Override
    public void showCoursesList(String pName,String semName,String sYear) {
        CourseListFragment courseList= new CourseListFragment();
        courseList.setPlanName(pName);
        courseList.setSemesterName(semName);
        courseList.setsYear(sYear);
        courseList.show(fm,"courseList");
    }


}