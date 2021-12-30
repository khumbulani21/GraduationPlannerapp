package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;


public class CourseListFragment extends DialogFragment {

    private View root;
    private  HelperInteface helper;

    FragmentManager fm;

    String pName;
    String sYear;
    List<Course> courseList;
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String uid;
    String semName;
    DatabaseReference databaseCourses;
    public CourseListFragment() {
        // Required empty public constructor
    }
    public void setsYear(String sYear)
    {
        this.sYear=sYear;
    }
    public void setPlanName(String pName)
    {
        this.pName=pName;
    }

    public void setSemesterName(String semName)
    {
        this.semName=semName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_Dialog_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root=inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm=getParentFragmentManager();



        requireDialog().getWindow().setWindowAnimations(R.style.Theme_GraguationPlannerApp_DialogAnimation);
        Toolbar toolbar=view.findViewById(R.id.toolBarCourseList);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.courselistdialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.signOut:return true;
                    case R.id.addCourse:
                        //open available courses page
                        helper.addCourse(pName,semName,sYear);

                        return true;
                    default:return  false;

                }

            }
        });


        courseList = new ArrayList<>();
        Context context=getContext();

        sharedPreferences=context.getSharedPreferences("planData", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        uid=sharedPreferences.getString("uid","");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            //add plan name
          //  databaseCourses = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(pName).child("semesters").child(semName);

            recyclerView = root.findViewById(R.id.rvCourseList);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CourseRecyclerAdapter(getContext(), new ArrayList<>());
adapter.setPlanName(pName);
adapter.setSemName(semName);
adapter.setSemYear(sYear);

            recyclerView.setAdapter(adapter);

            recyclerView.setHasFixedSize(false);

            new ViewModelProvider(this)
                    .get(CourseViewModel.class).getCourses(pName,semName,sYear)
                    .observe(this, new Observer<List<Course>>() {
                        @Override
                        public void onChanged(@Nullable List<Course> courses) {
                            if (courses != null) {
                                adapter.setPlanList(courses);

                            }
                        }
                    });
        }


    }
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {

            helper = (HelperInteface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " Must implement CourseList HelperInteface ");
        }
    }
    public void process(String pName,String sName ,String year ) {
        if (helper != null)
            helper.addCourse(  pName,  sName,year);
    }


    public interface HelperInteface {
        public void addCourse(String pName,String sName,String year);
    }
}