package com.app.fixee.myapplication.register;

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
import com.app.fixee.myapplication.login.LoginActivity;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import static android.util.Patterns.EMAIL_ADDRESS;

public class RegisterActivity extends MvpActivity<IRegisterView, IRegisterPresenter> implements IRegisterView, View.OnClickListener {

    private Button btnGoLogin;
    private ProgressDialog progressDialog;

    private String name;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    // INIT BASE ALL VIEWS
    private void initViews() {

        // HIDE TOOLBAR
        getSupportActionBar().hide();

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        btnGoLogin = findViewById(R.id.btn_go_login);
        btnGoLogin.setOnClickListener(this);
    }

    // CREATE PRESENTER
    @NonNull
    @Override
    public IRegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (!validate()) break;
                showProgressDialog();
                getPresenter().register(name, login, password, RegisterActivity.this);
                break;
            case R.id.btn_go_login:
                goLogin();
                break;
        }
    }

    // CHECK FIELDS
    private boolean validate() {

        boolean valid = true;

        EditText inputName = findViewById(R.id.input_register_name);
        EditText inputLogin = findViewById(R.id.input_register_email);
        EditText inputPassword = findViewById(R.id.input_register_password);

        name = inputName.getText().toString().trim();
        login = inputLogin.getText().toString().trim();
        password = inputPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError(getString(R.string.error_name));
            valid = false;
        } else {
            inputName.setError(null);
        }

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

    // GO ACTIVITY LOGIN
    private void goLogin() {
        startActivity(new Intent(this, LoginActivity.class));
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

    // SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(btnGoLogin, error, Snackbar.LENGTH_LONG).show();
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
