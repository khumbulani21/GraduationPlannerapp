package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;


public class AddSemester extends DialogFragment implements AdapterView.OnItemSelectedListener {


    private View root;
    private RadioGroup rgroup;
    private RadioButton radioSpring;
    private RadioButton radioSummer;
    private RadioButton radioFall;
    private Button btnSaveSem;

    private List<String> years;
    private  String semName;
    private Spinner spinnerYear;
    private LoadAddCourses mcallback;
    DatabaseReference databaseSemester;
    private String semester;
    TextView planName;
    private String name;

    public AddSemester() {
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
        return root=inflater.inflate(R.layout.fragment_add_semester, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireDialog().getWindow().setWindowAnimations(R.style.Theme_GraguationPlannerApp_DialogAnimation);
        radioSpring=root.findViewById(R.id.radioSpring);
        radioSummer=root.findViewById(R.id.radioSummer);
        radioFall=root.findViewById(R.id.radioFall);
        rgroup=root.findViewById(R.id.rgroupSemesters);
        planName=root.findViewById(R.id.txtpname);
        btnSaveSem=root.findViewById(R.id.btnSaveSem);
        setLabel();
        radioFall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semName="fall";
            }
        });
        radioSpring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semName="spring";
            }
        });
        radioSummer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semName="summer";
            }
        });



        Toolbar toolbar=view.findViewById(R.id.toolbarAddSemester);

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
                    case R.id.signOut:
                        FirebaseAuth.getInstance().signOut();
                        return true;
                    default:return  false;

                }

            }
        });
        Date today= new Date();
        Calendar cal= Calendar.getInstance();
        cal.setTime(today);
        int yr=cal.get(Calendar.YEAR);


        years=new ArrayList<>() ;

        int startingYear=yr-6;
        for(int i=0;i<12;i++)
        {
            years.add(String.valueOf(startingYear+i));
        }


        spinnerYear = (Spinner) root.findViewById(R.id.spinnerYear) ;
        spinnerYear.setOnItemSelectedListener(this);
        ArrayAdapter adapter2 = new ArrayAdapter(getContext(),R.layout.spinner_layout,R.id.txtYear,years);
// Specify the layout to use when the list of choices appears

// Apply the adapter to the spinner
        spinnerYear.setAdapter(adapter2);

        btnSaveSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName=planName.getText().toString();


                String year=spinnerYear.getSelectedItem().toString();
                //save plan to database and take user back to page to add semesters
                if(!pName.equalsIgnoreCase("")&&  !semName.equalsIgnoreCase(""))
                {
                    Semester semester= new Semester(semName,year);

                    Snackbar.make(view, "To do plan saved ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    addSemester(FormatEmail.formatEmail(email),pName,semester);
               mcallback.loadAddCourses(pName,semester.getName(),semester.getYear());
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

    public void addSemester(String uid,String name,Semester semester)
    {

        databaseSemester = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Plans").child(name).child("semesters");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( semester.getName()+" "+semester.getYear(), semester);
        databaseSemester.updateChildren(childUpdates);
//        databaseSemester.setValue(semester.getName()+" "+semester.getYear()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//                    Snackbar.make(root, "save successful ", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            }
//        });
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
            mcallback=(LoadAddCourses)activity;

    }
public void setPlanName(String name)
{
    this.name=name;

}
public void setLabel(){

        planName.setText(name);

}
    public interface LoadAddCourses{
        public void loadAddCourses(String planName,String semester,String year);
    }

}