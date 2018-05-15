package com.app.fixee.myapplication.manage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.models.User;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends MvpActivity<IManageView, IManagePresenter> implements IManageView {

    private TextView textNone;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<User> usersAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        initViews();
        getPresenter().getAllUsers(ManageActivity.this);
    }

    // INIT ALL BASE VIEWS
    private void initViews() {

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        textNone = findViewById(R.id.info_none);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void createDialogAccessLevel(String name, String docId, int position) {

        String[] levels = new String[3];
        levels[0] = getString(R.string.access_level_1);
        levels[1] = getString(R.string.access_level_2);
        levels[2] = getString(R.string.access_level_3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.set_user, name));
        builder.setItems(levels, (dialogInterface, i) -> getPresenter().updateUser(docId, i + 1, position, ManageActivity.this));
        builder.show();
    }

    @NonNull
    @Override
    public IManagePresenter createPresenter() {
        return new ManagePresenter();
    }

    // LOAD ALL USERS
    @Override
    public void loadUsers(List<User> users) {
        usersAll.addAll(users);
        recyclerView.setAdapter(new RecyclerViewUsers(usersAll, this::createDialogAccessLevel));
    }

    // UPDATE USER
    @Override
    public void updateUser(String accessLevel, int position) {
        usersAll.get(position).setAccessLevel(accessLevel);
        recyclerView.getAdapter().notifyItemChanged(position);
    }

    // SHOW SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show();
    }

    // HIDE TEXT CENTER
    @Override
    public void hideTextNone() {
        textNone.setText("");
        textNone.setVisibility(View.GONE);
    }

    // SET TEXT CENTER
    @Override
    public void setTextNone(String text) {
        hideProgressBar();
        textNone.setText(text);
        textNone.setVisibility(View.VISIBLE);
    }

    // HIDE PROGRESS BAR
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    // SHOW PROGRESS DIALOG
    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, R.style.RedProgressDialog);
            progressDialog.setMessage(getString(R.string.text_wait));
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
