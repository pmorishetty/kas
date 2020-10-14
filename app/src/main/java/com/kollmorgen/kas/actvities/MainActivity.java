package com.kollmorgen.kas.actvities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kollmorgen.kas.R;
import com.kollmorgen.kas.service.RetrofitInstance;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      final android.widget.Button loginBtn = findViewById(R.id.btnLogin);
      loginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            EditText ipAddressText = findViewById(R.id.editTextIPAddress);

            Boolean status = RetrofitInstance.setIpAddress(ipAddressText.getText().toString());

            if (status) {

               Intent intent = new Intent(MainActivity.this, AxisOverview.class);
               startActivity(intent);

            } else {
               Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
}
