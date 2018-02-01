package com.example.indian.movieticketbooking.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private int STORAGE_PERMISSION_CODE = 23;
    ImageView regProfileImg;
    String selectedCity, picturePath;
    String errMsg = "Field is Empty";
    DataBaseHandler dataBaseHandler;
    Button signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
        String checkLogged = sp.getString("userName",null);
        if(checkLogged != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("userName",checkLogged);
            editor.commit();
            launchHomeActivity();
        }
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        signUp = (Button)findViewById(R.id.register_btn);
        signIn = (Button)findViewById(R.id.signIn_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler = dataBaseHandler.open();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog();
            }
        });
    }

    private void validateUser() {
        EditText userName = (EditText)findViewById(R.id.username_et);
        EditText password = (EditText)findViewById(R.id.password_et);

        String logUserName = userName.getText().toString();
        String logPassword = password.getText().toString();

        if(logUserName.length() == 0){
            userName.setError(errMsg);
        }
        else if(logPassword.length() == 0){
            password.setError(errMsg);
        }
        else if(!logUserName.isEmpty()){
            dataBaseHandler = dataBaseHandler.open();
            String storedPassword = dataBaseHandler.getSingleEntry(logUserName);
            if(storedPassword.equals("Empty")){
                Toast.makeText(LoginActivity.this, "Please SignUp", Toast.LENGTH_SHORT).show();
            }
            else{
                if(storedPassword.equals(logPassword)){
                    Toast.makeText(LoginActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("myPref",MODE_PRIVATE).edit();
                    editor.putString("userName",logUserName);
                    editor.commit();
                    launchHomeActivity();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("json_category","movies.json");
        intent.putExtra("category_title","movie list fetched Successfully");
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    private void registerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_signup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        regProfileImg = (ImageView)dialog.findViewById(R.id.profilePic_iv);
        final EditText regUserName = (EditText)dialog.findViewById(R.id.regUsername_et);
        final EditText regPassword = (EditText)dialog.findViewById(R.id.regPwd_et);
        final EditText regConfirmPassword = (EditText)dialog.findViewById(R.id.regConfPwd_et);
        Spinner regCity = (Spinner)dialog.findViewById(R.id.city_sp);
        Button regSignUp = (Button)dialog.findViewById(R.id.signUp_btn);

        regProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        final String[] spCity = {"Bangalore", "Chennai"};
        ArrayAdapter<String> aa = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, spCity);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regCity.setAdapter(aa);
        regCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = spCity[position].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        regSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbUserName = regUserName.getText().toString();
                String dbPassword = regPassword.getText().toString();
                String dbConfirmPassword = regConfirmPassword.getText().toString();

                if(dbUserName.length() == 0){
                    regUserName.setError(errMsg);
                }
                else if(dbPassword.length() == 0){
                    regPassword.setError(errMsg);
                }
                else if(dbConfirmPassword.length() == 0){
                    regConfirmPassword.setError(errMsg);
                }
                else if(picturePath == null){
                    Toast.makeText(LoginActivity.this, "Please insert an image!", Toast.LENGTH_SHORT).show();
                }
                else if(dbPassword.equals(dbConfirmPassword)){
                    try{
                        FileInputStream fis = new FileInputStream(picturePath);
                        byte[] image = new byte[fis.available()];
                        fis.read(image);
                        dataBaseHandler = dataBaseHandler.open();
                        dataBaseHandler.insert(dbUserName,dbPassword,selectedCity,image);
                        fis.close();
                        Toast.makeText(LoginActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Password and Confirm Password mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                regProfileImg.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(LoginActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
