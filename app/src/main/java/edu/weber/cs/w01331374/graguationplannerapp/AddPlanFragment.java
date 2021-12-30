package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;


public class AddPlanFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
private Spinner spinner;


private String semester;
private Button btnSavePlan;
private EditText etPlanName;
private View root;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private LoadAddSemester mcallback;
    FirebaseDatabase db;
    DatabaseReference databasePlans;
    String uid;
public AddPlanFragment() {
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
        // Inflate the layout for this fragment
        return root=inflater.inflate(R.layout.fragment_app_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireDialog().getWindow().setWindowAnimations(R.style.Theme_GraguationPlannerApp_DialogAnimation);
        Context context= getContext();
        sharedPreferences=context.getSharedPreferences("planData", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        uid=     sharedPreferences.getString("uid","");
db=FirebaseDatabase.getInstance();
        Toolbar toolbar=view.findViewById(R.id.toolbarAddPlan);

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
                    case R.id.signOut:return true;
                    default:return  false;

                }

            }
        });



        etPlanName= root.findViewById(R.id.etPlanName);
        btnSavePlan=root.findViewById(R.id.btnSavePlan);


        btnSavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String planName=etPlanName.getText().toString();

                //from drop downz


                //save plan to database and take user back to page to add semesters
                if(!planName.equalsIgnoreCase(""))
                {


                 Snackbar.make(view, "To do plan saved ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                 String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // save plan
                    //generates new key
                    String key=db.getReference("Users").child(formatEmail(email)).push().getKey();

                addPlan(formatEmail(email),key,planName);
                 mcallback.loadAddSemester(planName);
                 dismiss();
                 //add function to open the other dialog here
                }
                else
                {
                    Snackbar.make(view, "Enter a plan name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        semester=(String)parent.getItemAtPosition(position);
      Toast.makeText(getContext(),""+parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if(mcallback==null)
        mcallback=(LoadAddSemester)activity;
    }

    public void addPlan(String email,String uid,String name)
    {
        Plan plan= new Plan( name);

        //save plan
        databasePlans= db.getReference("Users").child(email).child("Plans");
        Map<String, Object> plans = new HashMap<>();
        plans.put(name,plan);

        databasePlans.updateChildren(plans).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public String formatEmail(String email){
        String newEmail="";
        String temp[]= email.split("\\.");
        for(String s:temp)
        {
            newEmail=newEmail+s;
        }

        return newEmail;
    }
    public interface LoadAddSemester{
    public void loadAddSemester(String planName);
    }
}