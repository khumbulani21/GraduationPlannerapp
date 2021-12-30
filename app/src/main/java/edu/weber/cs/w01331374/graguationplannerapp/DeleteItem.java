package edu.weber.cs.w01331374.graguationplannerapp;

import com.google.firebase.database.DatabaseReference;

public class DeleteItem {
    public static void delete(DatabaseReference databaseReference)
    {
        databaseReference.removeValue();
    }
}
