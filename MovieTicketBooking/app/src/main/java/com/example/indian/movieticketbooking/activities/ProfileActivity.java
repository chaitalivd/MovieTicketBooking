package com.example.indian.movieticketbooking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    EditText userName,currentPwd, newPwd, confirmPwd;
    Spinner userCity;
    Button editProfile, changePassword, savePassword;
    DataBaseHandler dataBaseHandler;
    String errMsg = "Field is Empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final LinearLayout changePwdLyt = (LinearLayout)findViewById(R.id.change_password_lyt);
        changePwdLyt.setVisibility(View.GONE);
        userName = (EditText)findViewById(R.id.username_field);
        userCity = (Spinner)findViewById(R.id.city_list_box);
        editProfile = (Button)findViewById(R.id.edit_profile_btn);
        changePassword = (Button)findViewById(R.id.change_password_btn);
        savePassword = (Button)findViewById(R.id.confirm_pin_change_btn);
        setUserDetails();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editProfile.getText().equals("EDIT PROFILE")){
                    disableOrEnableAllFields(true);
                    editProfile.setText("SAVE CHANGES");
                }
                else if(editProfile.getText().equals("SAVE CHANGES")){
                    if(userName.length() == 0){
                        userName.setError(errMsg);
                    }
                    else{
                        String changeName = userName.getText().toString();
                        String changeGender = userCity.getSelectedItem().toString();
                        dataBaseHandler = new DataBaseHandler(ProfileActivity.this);
                        dataBaseHandler = dataBaseHandler.open();
                        SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
                        String getUserName = sp.getString("userName",null);
                        dataBaseHandler.update(getUserName,changeName,changeGender);
                        Toast.makeText(ProfileActivity.this,"UserName and City Updated!!", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userName",changeName);
                        editor.commit();
                        setUserDetails();
                    }
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwdLyt.setVisibility(View.VISIBLE);
            }
        });

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPwd = (EditText)findViewById(R.id.old_password_field);
                newPwd = (EditText)findViewById(R.id.new_password_field);
                confirmPwd = (EditText)findViewById(R.id.confirm_new_pwd_field);
                String etCurrentPwd = currentPwd.getText().toString();
                String etNewPwd = newPwd.getText().toString();
                String etConfirmPwd = confirmPwd.getText().toString();
                dataBaseHandler = new DataBaseHandler(ProfileActivity.this);
                dataBaseHandler = dataBaseHandler.open();
                SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
                String getUserName = sp.getString("userName",null);
                if(etCurrentPwd.length() == 0){
                    currentPwd.setError(errMsg);
                }
                else if(etNewPwd.length() == 0){
                    newPwd.setError(errMsg);
                }
                else if(etConfirmPwd.length() == 0){
                    confirmPwd.setError(errMsg);
                }
                else if(!etNewPwd.equals(etConfirmPwd)){
                    Toast.makeText(ProfileActivity.this,"New Password and Confirm Password mismatch",Toast.LENGTH_SHORT).show();
                }
                else{
                    String dbPwd = dataBaseHandler.getSingleEntry(getUserName);
                    if(dbPwd == null || !dbPwd.equals(etCurrentPwd)){
                        Toast.makeText(ProfileActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                    }
                    else if(dbPwd.equals(etCurrentPwd)){
                        dataBaseHandler.updatePwd(getUserName,etNewPwd);
                        Toast.makeText(ProfileActivity.this,"Password Updated!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setUserDetails() {
        SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
        String getUserName = sp.getString("userName",null);
        if(getUserName!=null){
            dataBaseHandler = new DataBaseHandler(this);
            dataBaseHandler = dataBaseHandler.open();
            ArrayList<String> userDetails = dataBaseHandler.getUserDetails(getUserName);
            if(userDetails.size()>0){
                String etUserName = userDetails.get(0);
                String etPassword = userDetails.get(1);
                String etCity = userDetails.get(2);
                userName.setText(etUserName);
                if(etCity.equals("Bangalore")){
                    userCity.setSelection(0);
                }
                else if(etCity.equals("Chennai")){
                    userCity.setSelection(1);
                }
                disableOrEnableAllFields(false);
                editProfile.setText("EDIT PROFILE");
            }
        }
    }

    private void disableOrEnableAllFields(boolean areFieldsToBeEnabled){
        userName.setEnabled(areFieldsToBeEnabled);
        userCity.setEnabled(areFieldsToBeEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler = dataBaseHandler.open();
    }
}
