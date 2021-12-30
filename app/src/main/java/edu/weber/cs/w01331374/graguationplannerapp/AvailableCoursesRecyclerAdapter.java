package edu.weber.cs.w01331374.graguationplannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;
import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;
import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;

public class AvailableCoursesRecyclerAdapter extends RecyclerView.Adapter<AvailableCoursesRecyclerAdapter.AvailableCoursesViewHolder>  {
    Context context;


    private List<Course> courseList;

    private String planName;
    private String semName;
    private String semYear;
    public void setPlanName(String planName){
        this.planName= planName;
    }
    public void setSemName(String semName){
        this.semName= semName;
    }
    public void setSemYear(String semYear){
        this.semYear= semYear;
    }
    public AvailableCoursesRecyclerAdapter(Context context, List<Course> courseList)
    {

        clear();
        this.courseList =courseList;
        notifyDataSetChanged();
        this.context=context;
    }


    public void setPlanList(List<Course> list)
    {
        //planList.clear();
        clear();
        courseList.addAll(list);

//tells the recycler adapter that data has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvailableCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.availablecourse_view,parent,false);
        //System.out.println("inside on create view holder pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp ");
        AvailableCoursesViewHolder courseViewHolder= new AvailableCoursesViewHolder(view);
        return courseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableCoursesViewHolder holder, int position) {
        Course course=courseList.get(position);
        String courseCode="";
        if(course!=null)
        {
            holder.item=course;
            String name=course.getName();
            courseCode=course.getCourse_code();
            holder.tv2.setText(" :"+name);
            holder.tv1.setText(courseCode);
        }
        //String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//add course to course list
            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                // Plans child plan name semester name
                System.out.println("dljf;assssssssssssskkffffffffffffffffff "+email);
                System.out.println(" planName  dljf;assssssssssssskkffffffffffffffffff "+planName);
                System.out.println("semName  dljf;assssssssssssskkffffffffffffffffff "+semName);
                System.out.println(" dljf;assssssssssssskkffffffffffffffffff "+semYear);
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(planName).child("semesters").child(semName+" "+semYear).child("Courses");
                addCourse(databaseRef,   course,  v);

            }
        });
    }

    private void deleteCourse(DatabaseReference databaseRef) {

        databaseRef.removeValue();
    }

    //how many viewholders exist
    @Override
    public int getItemCount()
    {
        System.out.println("size of list llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll "+courseList.size());
        return courseList.size();
    }




    //ViewHolders hold ui of an individual item in the list
    public class AvailableCoursesViewHolder extends RecyclerView.ViewHolder
    {

        public View itemRoot;
        public TextView tv1,tv2;
        public Course item;
        public ImageButton imageButton;
        public AvailableCoursesViewHolder( View view){

            super(view);
            //   System.out.println("*************************************************************** inside Plan View");
            itemRoot=view;

            tv1=itemRoot.findViewById(R.id.txtACCode);
            tv2=itemRoot.findViewById(R.id.txtACName);
            imageButton=itemRoot.findViewById(R.id.imgAddAv);
        }

    }
    public void clear()
    {
        if(courseList!=null)
        {
            for(Course c:courseList)
            {
                System.out.println(c);
            }
            courseList.clear();
            System.out.println("*************************************************************** List cleared");
        }

    }
    public void addCourse(DatabaseReference db, Course course,View view)
    {

       // databaseSemester = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Plans").child(name).child("semesters").child(semester.getName()+" "+semester.getYear()).child("Courses");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(course.getCourse_code() , course);
        db.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    Snackbar.make(view, "save successful ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

}
