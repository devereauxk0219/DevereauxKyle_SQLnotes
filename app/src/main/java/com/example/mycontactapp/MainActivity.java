package com.example.mycontactapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editAddress = findViewById(R.id.editText_address);

        myDb = new DatabaseHelper( this);
        Log.d( "MyContactApp", "Main Activity: instantiated DatabaseHelper");
    }

    public void addData(View view)
    {
        System.out.println(editName.getText().toString());
        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());

        if(isInserted == true)
        {
            Toast.makeText( MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText( MainActivity.this, "Failed - contact not inserted", Toast.LENGTH_LONG).show();
        }

    }

    public void viewData(View view) //connected with GETCONTACT button
    {
        Cursor res = myDb.getAllData();

        if(res.getCount() == 0) {
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            //append res column 0, ... to the buffer ... see Stringbuffer and cursor's api's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone: " + res.getString(2) + "\n");
            buffer.append("Address: " + res.getString(3) + "\n");
        }

        showMessage("Data", buffer.toString());
    }

    public void searchData(View view)
    {
        Cursor res = myDb.getAllData();

        if(res.getCount() == 0) {
            showMessage("Error", "No data found in database");
            return;
        }

        String[] searchParams = new String[3];
        searchParams[0] = editName.getText().toString();
        searchParams[1] = editPhone.getText().toString();
        searchParams[2] = editAddress.getText().toString();

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())   //CHECK if LOGICAL, probably isnt.
        {
            boolean ifAdd = true;
            for(int i = 0; i<3; i++)
            {
                if(searchParams[i] != null && !searchParams[i].equals(res.getString(i+1))) {
                    ifAdd = false;
                    break;
                }
            }
            if(ifAdd) {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Name: " + res.getString(1) + "\n");
                buffer.append("Phone: " + res.getString(2) + "\n");
                buffer.append("Address: " + res.getString(3) + "\n\n");
            }
        }

        showMessage("Data", buffer.toString());

    }

    public void showMessage(String title, String message)
    {
        //put more degugging stuff in here
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
