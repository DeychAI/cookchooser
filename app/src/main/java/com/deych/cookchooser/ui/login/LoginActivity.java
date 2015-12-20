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

    @Inject
    UserModel mUserModel;

    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content);

        App.get(this).getAppComponent().inject(this);

        mSubscription = mUserModel.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    App.get(this).createUserComponent(user);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }, e -> {
                    if (savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.content, new LoginFragment())
                                .commit();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
    }
}
