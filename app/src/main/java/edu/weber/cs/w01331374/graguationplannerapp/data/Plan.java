package edu.weber.cs.w01331374.graguationplannerapp.data;



import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable{
    private String name;
    private String id;
    private List<Semester> semesters;
    public Plan()
    {

    }
    public Plan(String name ,String id)
    {
        this.name=name;
        this.id=id;
    }
    public Plan(String name )
    {
        this.name=name;

    }
    public Plan(String name,Semester startingSemester, List<Semester> semesters,String id)
    {
        this.name=name;
        this.semesters= semesters;
        this.id=id;

    }
    public Plan(String name, List<Semester> semesters)
    {
        this.semesters= semesters;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
