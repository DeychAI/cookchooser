package com.deych.cookchooser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.MainActivity;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 14.12.2015.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content);

        if (App.get(this).getUserComponent() != null) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            finish();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, new LoginFragment())
                        .commit();
            }
        }
    }
}
