package com.example.matemo.week14app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edUsername, edPassword;
    Button btnLogin;
    TextView tvHasil;
    ArrayList<User> arrUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvHasil = findViewById(R.id.tvHasil);
        arrUser = new ArrayList<User>();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edUsername.getText().toString().equals("") && !edPassword.getText().toString().equals(""))
                {
                    fetchUser();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Inputan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginProcess()
    {
        String url = "http://192.168.42.84/myappdb/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tvHasil.setText(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int statusCode = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvHasil.setText("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", edUsername.getText().toString());
                params.put("password", edPassword.getText().toString());
                return params;
            }
        };

    }

    private void fetchUser()
    {
        String url = "http://192.168.42.84/myappdb/fetchUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tvHasil.setText(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int statusCode = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    if(statusCode == 1)
                    {
                        arrUser.clear();
                        String userdata = jsonObject.getString("dataUser");
                        JSONArray jsonArray = new JSONArray(userdata);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            User user = new User(obj.getString("username"), obj.getString("password"), obj.getString("name"));
                            arrUser.add(user);
                        }
                        Toast.makeText(MainActivity.this, arrUser.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvHasil.setText("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", edUsername.getText().toString());
                params.put("password", edPassword.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
