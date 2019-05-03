package in.ac.rajesh.jntuacea_grievance_redressal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterDetails extends AppCompatActivity {
    Spinner s;
    EditText name,id,gender,mail;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("data");
        s = findViewById(R.id.userlist);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        gender = findViewById(R.id.Gender);
        mail = findViewById(R.id.email);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains("name")) {
            Intent i=new Intent(this,Grievance.class);
            startActivity(i);
        }

        if (sharedPreferences.contains("number")) {

        }

        else {
            editor.putString("number", ""+firebaseAuth.getCurrentUser().getPhoneNumber());
            editor.apply();

        }
    }

    public void grievance(View view) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains("count")){}
            else{
            editor.putString("count", "0");
            editor.apply();
        }
        Toast.makeText(this, ""+sharedPreferences.getString("count",""), Toast.LENGTH_SHORT).show();

        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("name").setValue(name.getText().toString());
        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("id").setValue(id.getText().toString());
        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("gender").setValue(gender.getText().toString());
        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("mail").setValue(mail.getText().toString());
        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("category").setValue(s.getSelectedItem().toString());

        editor.putString("name", ""+name.getText());
        editor.apply();

        Intent i=new Intent(this,Grievance.class);
        startActivity(i);

    }



}
