package yasis.apps.sasi;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

///$conn = mysqli_connect("localhost","root","","");
//$sql = "";
public class activityLogin extends AppCompatActivity {
    EditText username,password;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Login(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if(username.getText().toString().isEmpty()){
            username.setError("Please enter username");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Please enter password");
            progressBar.setVisibility(View.GONE);
            return;
        }
        mAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString())
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           startActivity(new Intent(activityLogin.this, activityAttend.class));
                       }else{
                           new AlertDialog.Builder(activityLogin.this)
                                   .setTitle("Login Error")
                                   .setMessage("Please try again")
                                   .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {

                                       }
                                   })
                                   .show();
                       }
                       progressBar.setVisibility(View.GONE);
                   }
               });

    }

}
