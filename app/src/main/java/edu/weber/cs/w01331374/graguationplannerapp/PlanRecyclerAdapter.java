package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder>  {
    Context context;
    PlanListFragment planListFragment;

    private List<Plan> planList;


    public PlanRecyclerAdapter(Context context,List<Plan> planList)
    {

        clear();
        this.planList =planList;
        notifyDataSetChanged();
        this.context=context;
    }


    public void setPlanList(List<Plan> list)
    {

        clear();
        planList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_view,parent,false);
        PlanViewHolder planViewHolder= new PlanViewHolder(view);
        return planViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan=planList.get(position);

        if(plan!=null)
        {
            holder.item=plan;
            String name=plan.getName();
            holder.tv1.setText(name);
        }
        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open semester list
               planListFragment.process( plan.getName());

            }
        });

        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
         DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FormatEmail.formatEmail(email)).child("Plans").child(plan.getName()) ;
        holder.imgDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteItem.delete(databaseRef);
            }
        });

    }

    //how many viewholders exist
    @Override
    public int getItemCount()
    {

        return planList.size();
    }


    public void setPlanListFragment( PlanListFragment planListFragment )
    {
        this.planListFragment=planListFragment;

    }

    //ViewHolders hold ui of an individual item in the list
    public class PlanViewHolder extends RecyclerView.ViewHolder
    {

        public View itemRoot;
        public TextView tv1;
        public Plan item;
        public ImageButton imgDeletePlan;
        public PlanViewHolder( View view){
            super(view);
            itemRoot=view;
            imgDeletePlan=itemRoot.findViewById(R.id.imgDeletePlan);
            tv1=itemRoot.findViewById(R.id.txtPla);
         }

    }
public void clear()
{
    if(planList!=null)
    {
        for(Plan p:planList)
        {
            System.out.println(p);
        }
        planList.clear();

    }

}

}
