package edu.weber.cs.w01331374.graguationplannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;
import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseViewHolder>  {
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
public CourseRecyclerAdapter(Context context, List<Course> courseList)
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
public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_view,parent,false);
        //System.out.println("inside on create view holder pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp ");
        CourseViewHolder courseViewHolder= new CourseViewHolder(view);
        return courseViewHolder;
        }

@Override
public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course=courseList.get(position);
    String courseCode="";
        if(course!=null)
        {
        holder.item=course;
        String name=course.getName();
              courseCode=course.getCourse_code();
        holder.tv1.setText(" :"+name);
        holder.tv2.setText(courseCode);
        }
    String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
    // Plans child plan name semester name

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(planName).child("semesters").child(semName+" "+semYear).child("Courses").child(courseCode);
    holder.imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


         // delete course
            deleteCourse(databaseRef);


        }
    });
        //   System.out.println("inside bind yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy "+planList.size());

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
public class CourseViewHolder extends RecyclerView.ViewHolder
{

    public View itemRoot;
    public TextView tv1,tv2;
    public Course item;
    public ImageButton imageButton;
    public CourseViewHolder( View view){

        super(view);
        //   System.out.println("*************************************************************** inside Plan View");
        itemRoot=view;

        tv1=itemRoot.findViewById(R.id.txtCourse);
        tv2=itemRoot.findViewById(R.id.txtCCode);
        imageButton=itemRoot.findViewById(R.id.imgDeleteCourse);
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

}
