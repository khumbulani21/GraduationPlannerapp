package edu.weber.cs.w01331374.graguationplannerapp.data;


import java.io.Serializable;


public class Course implements Serializable {
    private   String name;
    private   String course_code;

    public Course() {

    }

    public Course( String course_code, String name ) {
        this.name = name;
        this.course_code = course_code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }
    @Override
    public String toString() {
        return  name+" "+course_code ;
    }


}
