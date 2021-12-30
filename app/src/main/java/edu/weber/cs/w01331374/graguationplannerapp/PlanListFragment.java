package edu.weber.cs.w01331374.graguationplannerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01331374.graguationplannerapp.data.Course;
import edu.weber.cs.w01331374.graguationplannerapp.data.Plan;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<Plan> planList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String uid;
    DatabaseReference databasePlans;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PlanRecyclerAdapter adapter;
    private View root;
    private HelperInteface helper;
     Button btnLogout;
    public PlanListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanListFragment newInstance(String param1, String param2) {
        PlanListFragment fragment = new PlanListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root =inflater.inflate(R.layout.fragment_plan_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        planList= new ArrayList<>();
        Context context=getContext();

        sharedPreferences=context.getSharedPreferences("planData", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        uid=sharedPreferences.getString("uid","");
if(FirebaseAuth.getInstance().getCurrentUser()!=null){
    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    databasePlans = FirebaseDatabase.getInstance().getReference("Users").child(formatEmail(email)).child("Plans");

    recyclerView = root.findViewById(R.id.rvPlans);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     adapter = new PlanRecyclerAdapter(getContext(), new ArrayList<>());
     adapter.setPlanListFragment(this);

    recyclerView.setAdapter(adapter);

    recyclerView.setHasFixedSize(false);

    new ViewModelProvider(this)
            .get(PlanViewModel.class).getPlans()
            .observe(this, new Observer<List<Plan>>() {
                @Override
                public void onChanged(@Nullable List<Plan> plans) {
                    if(plans !=null)
                    {
                        adapter.setPlanList(plans);

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
            throw new ClassCastException(activity.toString() + " Must implement HelperInteface ");
        }
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
    public List<Plan> readPlans()
    {

        databasePlans.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Plan plan=snapshot1.getValue(Plan.class);

                    planList.add(plan);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return planList;
    }
//opens semester associated with clicked plan
    public void process(String pName) {
        if (helper != null)
            helper.showPlan(pName);
    }


    public interface HelperInteface {
        public void showPlan(String pName);

    }
}