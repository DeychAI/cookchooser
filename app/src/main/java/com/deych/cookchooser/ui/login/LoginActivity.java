package com.deych.cookchooser.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deych.cookchooser.R;

/**
 * Created by deigo on 14.12.2015.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new LoginFragment())
                    .commit();
        }
    }
}
