package edu.weber.cs.w01331374.graguationplannerapp.data;

import java.io.Serializable;
import java.util.List;

public class Semester implements Serializable {

    private String name;
    private String year;
   // private List<Course> courses;
    private  int semesterId;
    public Semester(){

    }
    public Semester(String name, String year, List<Course> courses) {
        this.name = name;
        this.year = year;
      //  this.courses = courses;
    }
    public Semester(String name, String year ) {
        this.name = name;
        this.year = year;
       // this.courses = null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

        public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

//    public List<Course> getCourses() {
//        return courses;
//    }

//    public void setCourses(List<Course> courses) {
//        this.courses = courses;
//    }

    @Override
    public String toString() {
        return   name +  " "+ year;
    }
}
