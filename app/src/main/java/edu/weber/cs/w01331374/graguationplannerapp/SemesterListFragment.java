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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;


public class SemesterListFragment extends DialogFragment {

    private View root;
    private HelperInteface helper;
    List<Semester> semesterList;
    private RecyclerView recyclerView;
    private AddSemester addSemester;
    private SemesterRecyclerAdapter adapter;
    SharedPreferences sharedPreferences;
    private ShowAddSemester showAddSemester;
    ShowCoursesList showCList;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String uid;
    String planName;
    String sName;
    DatabaseReference databasePlans;
    private TextView txtSemListPName;
    private ImageButton imgSemCourse;

    public SemesterListFragment() {
        // Required empty public constructor
    }
public void setPlanName(String planName)
{
    this.planName=planName;
}
    public void setAddSemesterFragment( AddSemester addSemester  )
    {
        this.addSemester=addSemester;
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
        return root=inflater.inflate(R.layout.fragment_semester_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requireDialog().getWindow().setWindowAnimations(R.style.Theme_GraguationPlannerApp_DialogAnimation);
        Toolbar toolbar=view.findViewById(R.id.toolBarSemList);
        txtSemListPName= root.findViewById(R.id.txtSemListPName);
        txtSemListPName.setText(planName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.semesterlistdialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.signOut:return true;
                    case R.id.addSemester:
//interface
                        showAddSemester.showAddSemester(planName);
                        //open semester page
                        return true;
                    default:return  false;

                }

            }
        });

        semesterList= new ArrayList<>();
        Context context=getContext();

        sharedPreferences=context.getSharedPreferences("planData", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        uid=sharedPreferences.getString("uid","");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            //add plan name
            databasePlans = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(planName).child("Semesters");

            recyclerView = root.findViewById(R.id.rvSemList);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new SemesterRecyclerAdapter(getContext(), new ArrayList<>());
            adapter.setSemesterListFragment(this);
adapter.setPlanName(planName);

            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(false);

                    new ViewModelProvider(this)
                    .get(SemesterViewModel.class).getSemesters(planName)
                    .observe(this, new Observer<List<Semester>>() {
                        @Override
                        public void onChanged(@Nullable List<Semester> semesters) {
                            if (semesters != null) {
                                adapter.setPlanList(semesters);
                                System.out.println("PLAN LIST 0ppppppppppppppppppppppppppppppppppppppppppppppppppppppppp00000000000000000000000000000" + semesters);
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
            showAddSemester=(ShowAddSemester)activity;
                showCList= (ShowCoursesList)activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " Must implement HelperInteface ");
            }
        }
    public void process(Plan plan) {
        if (helper != null)
            helper.showSemesters(plan);

    }


    public interface HelperInteface {
        public void showSemesters(Plan plan);
    }


    public void addSem(String planName)
    {
        if(showAddSemester!=null)
            showAddSemester.showAddSemester(planName);

    }
    public interface ShowAddSemester{
        public void showAddSemester(String planName);
    }
    public void showCourses(String pName,String sName,String sYear) {
        if (showCList != null)
            showCList.showCoursesList(pName,sName,sYear);
    }


    public interface ShowCoursesList {
        public void showCoursesList(String pName,String sName,String sYear);
    }
}