package com.app.fixee.myapplication.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.fixee.myapplication.main.MainActivity;
import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.register.RegisterActivity;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends MvpActivity<ILoginView, ILoginPresenter> implements ILoginView, View.OnClickListener {

    // BUTTONS FOR AUTH
    Button btnLogin;
    Button btnGoRegister;

    String login;
    String password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    // INIT BASE ALL VIEWS
    private void initViews() {

        // HIDE TOOLBAR
        getSupportActionBar().hide();

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        btnGoRegister = findViewById(R.id.btn_go_register);
        btnGoRegister.setOnClickListener(this);
    }

    // CREATE PRESENTER
    @NonNull
    @Override
    public ILoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    // CLICK'S
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                if (!validate()) break;
                showProgressDialog();
                getPresenter().login(login, password, LoginActivity.this);
                break;
            case R.id.btn_go_register:
                goRegister();
                break;
        }
    }

    // CHECK FIELDS
    private boolean validate() {

        boolean valid = true;

        EditText inputLogin = findViewById(R.id.input_login_email);
        EditText inputPassword = findViewById(R.id.input_login_password);

        login = inputLogin.getText().toString().trim();
        password = inputPassword.getText().toString();

        if (login.isEmpty() || !EMAIL_ADDRESS.matcher(login).matches()) {
            inputLogin.setError(getString(R.string.error_login));
            valid = false;
        } else {
            inputLogin.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError(getString(R.string.error_password));
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    // GO ACTIVITY REGISTER
    private void goRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    // HIDE KEYBOARD
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // SHOW SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(btnGoRegister, error, Snackbar.LENGTH_LONG).show();
    }

    // GO ACTIVITY MAIN
    @Override
    public void goMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // SHOW PROGRESS DIALOG
    @Override
    public void showProgressDialog() {
        hideKeyboard();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, R.style.RedProgressDialog);
            progressDialog.setMessage(getString(R.string.text_auth));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    // HIDE PROGRESS DIALOG
    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
