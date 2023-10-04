package com.example.demoapponboardingsdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.onboarding.android.onboardingsdk.OnboardingSDKActivity;
import com.onboarding.android.onboardingsdk.OnboardingConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    List<String> manifestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            askPermissions();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        UUID uuid = UUID.randomUUID();
        String accountHolderID = uuid.toString();
        Intent intent = new Intent(getApplicationContext(), OnboardingSDKActivity.class);
        String mobileTokenProduction = "";
        String mobileTokenSandbox = "";

        OnboardingConfiguration mOnboardingConfiguration = new OnboardingConfiguration.Builder(accountHolderID, mobileTokenSandbox)
                .setBackgroundColor("#FFFFFF")
                .setFontColor("#111c4e")
                .setFontFamily(OnboardingConfiguration.FontFamily.open_sans)
                .setLogLevel(OnboardingConfiguration.LogLevel.info)
                .setSandboxEnvironment() // Retire essa linha para usar o ambiente de produção
                .build();

        intent.putExtra("settings", mOnboardingConfiguration);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK && data != null){
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("bundle_response", "response: " + data.getStringExtra("exception"));
            }
            else {
                // o usuário fechou a activity
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void askPermissions() throws PackageManager.NameNotFoundException {
        manifestPermissions = Arrays.asList(this.getApplicationContext()
                .getPackageManager()
                .getPackageInfo(this.getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS)
                .requestedPermissions);
        List<String> permissionsToAsk = manifestPermissions;
        ActivityCompat.requestPermissions(this, permissionsToAsk.toArray(new String[0]), 2);
    }
}