package edu.weber.cs.w01331374.graguationplannerapp;

public class FormatEmail {
    public static String formatEmail(String email){
        String newEmail="";
        String temp[]= email.split("\\.");
        for(String s:temp)
        {
            newEmail=newEmail+s;
        }

        return newEmail;
    }
}
