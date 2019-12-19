package yasis.apps.sasi;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class activityVerification extends AppCompatActivity {
    Button verify;
    EditText code;
    String verifyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);
        code = findViewById(R.id.otpCode);
        verify = findViewById(R.id.btnVerify);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+254" + "793875319",
                10,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //09:56:0F:1C:84:8E:D4:A5:6B:CC:AB:E6:86:DA:05:09:20:95:FA:B0
            //Getting the code sent by SMS
            verify.setEnabled(true);
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(activityVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Toast.makeText(activityVerification.this, "Code delayed please retry", Toast.LENGTH_SHORT).show();
        }
    };


    public void verifyCode(View view) {

    }
}
