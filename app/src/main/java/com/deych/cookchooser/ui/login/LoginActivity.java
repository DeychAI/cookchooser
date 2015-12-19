package com.deych.cookchooser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.ui.MainActivity;

import javax.inject.Inject;

/**
 * Created by deigo on 14.12.2015.
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getAppComponent().inject(this);
        
        if (mPreferences.getUserId() != 0 && !TextUtils.isEmpty(mPreferences.getUserToken())) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.layout_content);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new LoginFragment())
                    .commit();
        }
    }
}
