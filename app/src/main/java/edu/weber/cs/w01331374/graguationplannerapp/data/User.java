package edu.weber.cs.w01331374.graguationplannerapp.data;

public class User {
    private  String email;
    private String degree;
    private String name;
    private String major;

    public User() {

    }
    public User(String email) {
    this.email= email;
    }

    public User(String email, String name,String degree, String major) {
        this.email = email;
        this.degree = degree;
        this.major = major;
        this.name=name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getName() {
        return name;
    }
}
