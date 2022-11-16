package com.movetoplay.screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.movetoplay.R;
import com.movetoplay.pref.Pref;
import com.movetoplay.screens.create_child_profile.SetupProfileActivity;
import com.movetoplay.screens.parent.MainParentActivity;
import com.movetoplay.screens.register.RegisterActivity;
import com.movetoplay.screens.signin.SignInActivity;

import kotlin.Unit;

public class Auth extends AppCompatActivity {
    Button btn_e;
    Button btn_g;
    TextView txt;
    ProgressBar progress;

    private static final String TAG = "GoogleActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private AuthViewModel vm;

    @Override
    protected void onStart() {
        super.onStart();
        if (vm.getAuth().getCurrentUser() != null) vm.getAuth().signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initGoogle();
        initListeners();
    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        vm = new ViewModelProvider(Auth.this).get(AuthViewModel.class);
    }

    private void initListeners() {
        btn_e = findViewById(R.id.email_btn);
        btn_g = findViewById(R.id.google);
        txt = findViewById(R.id.no_acc);
        progress = findViewById(R.id.pb_auth);


        txt.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        btn_g.setOnClickListener(view -> signWithGoogle());
        btn_e.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)));

        vm.getSignViaGoogleResult().observe(this, result -> {
            if (result.containsKey("loading")) {
                progress.setVisibility(View.VISIBLE);
            } else if (result.containsKey("success")) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Вы успешно авторизованы!", Toast.LENGTH_SHORT).show();
                updateUI();
            } else {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(this, result.get("error"), Toast.LENGTH_SHORT).show();
                vm.getAuth().signOut();
            }
        });
    }

    private void signWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.e(TAG, "result success: " + account);
                        String token = account.getIdToken();
                        if (token != null)
                            vm.signViaGoogle(token);
                        else
                            Toast.makeText(this, "Error! Token is null", Toast.LENGTH_SHORT).show();
                    } catch (ApiException e) {
                        Log.w(TAG, "Google sign in failed", e);
                        Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else Log.e(TAG, "result error: " + result.getResultCode());
            });

    private void updateUI() {
        Log.e(TAG, "updateUI: ");
        new IsChildDeviceDialogFragment(this::onBtnClick).show(getSupportFragmentManager(), "is_child_dialog");
    }

    private Unit onBtnClick(Boolean isChild) {
        if (isChild) {
            Pref.INSTANCE.setChild(true);
            startActivity(new Intent(this, SetupProfileActivity.class));
        } else {
            Pref.INSTANCE.setChild(false);
            startActivity(new Intent(this, MainParentActivity.class));
        }
        finishAffinity();
        return null;
    }

}