package com.example.comp_3000_myweekly_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton login_button = (MaterialButton) findViewById(R.id.login_button);
        MaterialButton signup_button = (MaterialButton) findViewById(R.id.create_account_button);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        login_button.setOnClickListener(v -> handleLoginDialog());

        signup_button.setOnClickListener(v -> handleSignupDialog());
    }

    private void handleLoginDialog() {

        View view = getLayoutInflater().inflate(R.layout.activity_main, null);

        Button login_button = view.findViewById(R.id.login_button);
        EditText username = view.findViewById(R.id.username_entry);
        EditText password = view.findViewById(R.id.password_entry);

        HashMap<String, String> map = new HashMap<>();
        map.put("username", username.getText().toString());
        map.put("password", password.getText().toString());

        Call<LoginResult> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                if (response.code() == 200) {

                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                    LoginResult result = response.body();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this );
                    builder1.setTitle(result.getUsername());
                    builder1.setMessage((result.getUsername()));

                    builder1.show();

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));

                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    };


    private void handleSignupDialog() {

        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button signup_button = view.findViewById(R.id.signup_button);
        EditText username = view.findViewById(R.id.username_entry_s);
        EditText password = view.findViewById(R.id.password_entry_s);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();
                map.put("username", username.getText().toString());
                map.put("password", password.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(MainActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(MainActivity.this, "Username Taken", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}