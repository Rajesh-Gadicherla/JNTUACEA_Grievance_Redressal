package in.ac.rajesh.jntuacea_grievance_redressal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    EditText getotp,num;
    String verificationid;
    public SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        num=(EditText) findViewById(R.id.number);
        getotp=(EditText) findViewById(R.id.otp);
        firebaseAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("mypref",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains("number")){
            Intent i=new Intent(this,EnterDetails.class);
            startActivity(i);
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Toast.makeText(Login.this, "onVerificationCompleted:" + credential, Toast.LENGTH_SHORT).show();

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Toast.makeText(Login.this, "Verification Failed, invalid OTP", Toast.LENGTH_SHORT).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                //mPhoneNumberField.setError("Invalid phone number.");
                // [END_EXCLUDE]

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                //Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                //      Snackbar.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }

            // Show a message and update the UI
            // [START_EXCLUDE]
            //updateUI(STATE_VERIFY_FAILED);
            // [END_EXCLUDE]
        }

        @Override
        public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Toast.makeText(Login.this, "onCodeSent:" + verificationId, Toast.LENGTH_SHORT).show();
            verificationid=verificationId;
            // Save verification ID and resending token so we can use them later
            //mVerificationId = verificationId;
            //mResendToken = token;

            // [START_EXCLUDE]
            // Update UI
            //updateUI(STATE_CODE_SENT);
            // [END_EXCLUDE]
        }
    };
    // [END phone_auth_callbacks]
}



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Login.this,EnterDetails.class);
                            startActivity(i);
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(Login.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }



    public void getotp(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+num.getText().toString(),30, TimeUnit.SECONDS,this,mCallbacks);
            Toast.makeText(this, "Wait for OTP", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "No network connection available.\nPlease connect to Internet and try again...", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder a=new AlertDialog.Builder(Login.this).setTitle("Alert!").setMessage("No network connection available.\nPlease connect to Internet and try again...");
            a.show();
        }

    }

    public void login(View view) {

        try {
            verifyPhoneNumberWithCode(verificationid, getotp.getText().toString());
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }


}
