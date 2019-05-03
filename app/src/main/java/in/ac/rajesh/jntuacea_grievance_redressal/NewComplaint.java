package in.ac.rajesh.jntuacea_grievance_redressal;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NewComplaint extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    EditText e;
    int i;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        sharedPreferences=getSharedPreferences("mypref",MODE_PRIVATE);

        e=findViewById(R.id.complaint);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        myRef=db.getReference("data");


    }

    public void submit(View view) {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        i=Integer.parseInt(sharedPreferences.getString("count",""));
        i++;
        Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
        myRef.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("complaints").child(""+i).setValue(e.getText().toString());

        editor.putString("count",""+i);
        editor.apply();

    }
}
