package com.example.infaqapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {
    // Initialize variables
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    Button Btn;
    ProgressBar p1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btSignIn=findViewById(R.id.bt_sign_in);
        p1 = findViewById(R.id.progressBar1);
        Btn = findViewById(R.id.button);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent fp=new
                        Intent(MainActivity.this,phone_authentication.class);
                startActivity(fp);
            }
        });
        GoogleSignInOptions googleSignInOptions=new
                GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("1022530724476-g3d64i1jtsdhcsdt714c483ltmmro2gq.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
                ,googleSignInOptions);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            startActivity(new
                    Intent(MainActivity.this,videoimageplayer.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// Check condition
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful())
            {
                String s="Sign in successful";
                displayToast(s);

                try {
                    GoogleSignInAccount
                            googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        p1.setVisibility(View.VISIBLE);
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new
                                        OnCompleteListener<AuthResult>() {
                                            @Override

                                            public void onComplete(@NonNull

                                                                   Task<AuthResult> task) {
// Check condition
                                                if(task.isSuccessful())
                                                {

                                                    startActivity(new
                                                            Intent(MainActivity.this
                                                            ,videoimageplayer.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                    displayToast("");
                                                    FirebaseUser firebaseUser =
                                                            FirebaseAuth.getInstance().getCurrentUser();
                                                    String name =
                                                            firebaseUser.getDisplayName().toString();
                                                    String

                                                            photo="https://firebasestorage.googleapis.com/v0/b/social-media- 853d3.appspot.com/o/istockphoto.jpg?alt=media&token=d96a8857-6c2d-4b51- bc17-192daef26c38";
                                                    userinfo u = new
                                                            userinfo(name,photo);
                                                    forfriends f = new
                                                            forfriends(name,photo,FirebaseAuth.getInstance().getUid());
                                                    FirebaseDatabase.getInstance().getReference().child("users")
                                                            .child(FirebaseAuth.getInstance().getUid()).
                                                            setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void

                                                                onComplete(@NonNull Task<Void> task) {
                                                                    if
                                                                    (task.isSuccessful()) {
                                                                        Toast.makeText(MainActivity.this, "Sign In Successful!",
                                                                                Toast.LENGTH_SHORT).show();

                                                                        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()+"friends")
                                                                                .child(FirebaseAuth.getInstance().getUid()).
                                                                                setValue(f);
                                                                    } else {
                                                                    }
                                                                }
                                                            });
                                                }
                                                else
                                                {

                                                    displayToast("Authentication Failed :"+task.getException()
                                                        .getMessage());
                                                }
                                            }
                                        });
                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}