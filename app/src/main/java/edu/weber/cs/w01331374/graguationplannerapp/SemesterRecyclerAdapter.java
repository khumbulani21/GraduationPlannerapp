package edu.weber.cs.w01331374.graguationplannerapp;

 

import android.content.Context;
import android.media.Image;
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

import edu.weber.cs.w01331374.graguationplannerapp.data.Semester;

public class SemesterRecyclerAdapter extends RecyclerView.Adapter<SemesterRecyclerAdapter.SemesterViewHolder>  {
    Context context;
    Semester planListFragment;
String planName;
    private List<Semester> semesterList;
SemesterListFragment semesterListFragment;
public void setPlanName(String planName){
    this.planName= planName;
}
    public SemesterRecyclerAdapter(Context context,List<Semester> semesterList)
    {

        clear();
        this.semesterList =semesterList;
        notifyDataSetChanged();
        this.context=context;
    }

    public void setSemesterListFragment(SemesterListFragment semesterListFragment)
    {
        this.semesterListFragment=semesterListFragment;
    }
    public void setPlanList(List<Semester> list)
    {
        //planList.clear();
        clear();
        semesterList.addAll(list);

//tells the recycler adapter that data has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.semester_list_view,parent,false);
        //System.out.println("inside on create view holder pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp ");
        SemesterViewHolder semesterViewHolder = new SemesterViewHolder(view);
        return semesterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester=semesterList.get(position);
        if(semester!=null)
        {
            holder.item=semester;
            String name=semester.getName();
            String year= semester.getYear();
            holder.tv1.setText(name+" "+year);
        }
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        // Plans child plan name semester name
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(planName).child("semesters").child(semester.getName()+" "+semester.getYear());
        holder.imgSemCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteSemester(databaseRef);
        }
        });

        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterListFragment.showCourses( planName,semester.getName(),semester.getYear());
             //open course List
            }
        });

    }

    //how many viewholders exist
    @Override
    public int getItemCount()
    {
        System.out.println("size of semester list llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll "+semesterList.size());
        return semesterList.size();
    }


    //ViewHolders hold ui of an individual item in the list
    public class SemesterViewHolder extends RecyclerView.ViewHolder
    {

        public View itemRoot;
        public TextView tv1;
        public Semester item;
       ImageButton imgSemCourse;
        public SemesterViewHolder(View view){

            super(view);
            //   System.out.println("*************************************************************** inside Plan View");
            itemRoot=view;
            imgSemCourse=itemRoot.findViewById(R.id.imgSemCourse);
            tv1=itemRoot.findViewById(R.id.txtSem);


        }

    }
    public void clear()
    {
        if(semesterList!=null)
        {
            for(Semester p:semesterList)
            {
                System.out.println(p);
            }
            semesterList.clear();
            System.out.println("*************************************************************** List cleared");
        }

    }
    private void deleteSemester(DatabaseReference databaseRef) {

        databaseRef.removeValue();
    }

}
