package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;
import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;

public class AvailableCoursesFragment extends DialogFragment {


    private View root;
    private TextView lblSemYr;
    private TextView lblSem;
    private String planName;
    private String startingSem;
    private TextView txtPName;
    private String year;
    DatabaseReference databaseSemester;
    Button btnSaveCourses;
    private EditText etCourseCode;
    private EditText etCourseName;
    private RecyclerView recyclerView;

    private AvailableCoursesRecyclerAdapter adapter;


    public AvailableCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_Dialog_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return root=inflater.inflate(R.layout.fragment_available_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireDialog().getWindow().setWindowAnimations(R.style.Theme_GraguationPlannerApp_DialogAnimation);
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        txtPName=root.findViewById(R.id.txtPlan);
        lblSem=root.findViewById(R.id.txtSemName);
        lblSemYr=root.findViewById(R.id.txtSemYr);
        txtPName.setText(planName);
        lblSem.setText(startingSem);
        lblSemYr.setText(year);

        Toolbar toolbar=view.findViewById(R.id.toolbarACourses);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.fullscreendialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.logOut:
                        FirebaseAuth.getInstance().signOut();
                        return true;

                    default:return  false;

                }

            }
        });
        recyclerView = root.findViewById(R.id.rvAcourses);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailableCoursesRecyclerAdapter(getContext(), new ArrayList<>());
        adapter.setPlanName(planName);
        adapter.setSemName(startingSem);
        adapter.setSemYear(year);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        new ViewModelProvider(this)
                .get(AvailableCoursesViewModel.class).getCourses(planName,startingSem,year)
                .observe(this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(@Nullable List<Course> courses) {
                        if (courses != null) {
                            adapter.setPlanList(courses);
                         }
                    }
                });



    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setData(String planName,String startingSemester,String year) {
        this.year=year;
        this.planName=planName;
        this.startingSem=startingSemester;


    }

    public void addCourse(String uid,String name,Semester semester,Course course)
    {
        databaseSemester = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Plans").child(name).child("semesters").child(semester.getName()+" "+semester.getYear()).child("Courses");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(course.getCourse_code() , course);
        databaseSemester.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    Snackbar.make(root, "save successful ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }

}