package com.app.fixee.myapplication.adminTickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.models.Moderator;
import com.app.fixee.myapplication.models.Ticket;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

public class FullTicketActivity extends MvpActivity<IFullTicketView, IFullTicketPresenter> implements IFullTicketView {

    private TextView name;
    private TextView description;
    private TextView status;
    private TextView builder;
    private TextView feedback;
    private ImageView imageView;
    private Button btnSetAction;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private ProgressDialog progressDialog;
    private TextView textNone;
    private Ticket ticket;
    private int accessLevel;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_ticket);

        initViews();
        getTicketInfo();
        getData();
        setData();
    }

    // INIT ALL BASE VIEWS
    private void initViews() {

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        status = findViewById(R.id.status);
        builder = findViewById(R.id.builder);
        feedback = findViewById(R.id.feedback);
        imageView = findViewById(R.id.imageView);

        progressBar = findViewById(R.id.progress_bar);
        constraintLayout = findViewById(R.id.content);
        textNone = findViewById(R.id.info_none);

        btnSetAction = findViewById(R.id.btn_action);
    }

    private void getTicketInfo() {
        Intent intent = getIntent();
        ticket = new Ticket(intent.getStringExtra("name"), intent.getStringExtra("description"), intent.getStringExtra("photo"), intent.getStringExtra("date"), intent.getStringExtra("builder"), intent.getStringExtra("builderUid"), intent.getStringExtra("docId"), intent.getStringExtra("feedback"), intent.getStringExtra("status"));
        accessLevel = intent.getIntExtra("accessLevel", 0);
        position = intent.getIntExtra("position", 0);
    }

    private void getData() {
        switch (accessLevel) {
            case 2:
                hideProgressBar();
                hideTextNone();
                showContent();

                btnSetAction.setText(R.string.set_status);

                if (ticket.getStatus().equals(getString(R.string.status_4))) {
                    btnSetAction.setEnabled(false);
                    btnSetAction.setVisibility(View.GONE);
                } else {
                    btnSetAction.setOnClickListener(view -> createDialogStatus());
                }
                break;

            case 3:
                btnSetAction.setText(R.string.set_builder);

                if (ticket.getStatus().equals(getString(R.string.status_4))) {
                    hideProgressBar();
                    hideTextNone();
                    showContent();

                    btnSetAction.setEnabled(false);
                    btnSetAction.setVisibility(View.GONE);
                } else {
                    getPresenter().getAllModerator(FullTicketActivity.this);
                }
                break;

            default:
                break;
        }
    }

    private void setData() {
        name.setText(ticket.getName());
        description.setText(ticket.getDescription());
        status.setText(ticket.getStatus());
        builder.setText(ticket.getBuilder());

        if (!ticket.getFeedback().isEmpty()) {
            feedback.setVisibility(View.VISIBLE);
            feedback.setText(getString(R.string.feedback_name, ticket.getFeedback()));
        }

        if (!ticket.getPhoto().isEmpty()) {
            imageView.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(ticket.getPhoto())
                    .into(imageView);
        }
    }

    private void createDialogStatus() {
        String[] status = new String[3];
        status[0] = getString(R.string.status_2);
        status[1] = getString(R.string.status_3);
        status[2] = getString(R.string.status_4);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(status, (dialogInterface, i) -> getPresenter().setStatus(ticket.getDocId(), i + 2, FullTicketActivity.this));
        builder.show();
    }

    @Override
    public void setModerators(List<Moderator> moderators) {
        btnSetAction.setOnClickListener(view -> {

            String[] builders = new String[moderators.size()];

            for (int i = 0; i < moderators.size(); i++) {
                builders[i] = moderators.get(i).getName();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(builders, (dialogInterface, i) -> getPresenter().setBuilder(ticket.getDocId(), moderators.get(i).getName(), moderators.get(i).getUid(), FullTicketActivity.this));
            builder.show();
        });
    }

    @Override
    public void updateBuilder(String builderName) {
        builder.setText(builderName);
        status.setText(getString(R.string.status_2));

        Intent intent = new Intent();
        intent.putExtra("name", ticket.getName());
        intent.putExtra("description", ticket.getDescription());
        intent.putExtra("photo", ticket.getPhoto());
        intent.putExtra("date", ticket.getDate());
        intent.putExtra("builder", builderName);
        intent.putExtra("builderUid", ticket.getBuilderUid());
        intent.putExtra("docId", ticket.getDocId());
        intent.putExtra("feedback", ticket.getFeedback());
        intent.putExtra("status", getString(R.string.status_2));
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void updateStatus(String statusStr) {
        status.setText(statusStr);

        Intent intent = new Intent();
        intent.putExtra("name", ticket.getName());
        intent.putExtra("description", ticket.getDescription());
        intent.putExtra("photo", ticket.getPhoto());
        intent.putExtra("date", ticket.getDate());
        intent.putExtra("builder", ticket.getBuilder());
        intent.putExtra("builderUid", ticket.getBuilderUid());
        intent.putExtra("docId", ticket.getDocId());
        intent.putExtra("feedback", ticket.getFeedback());
        intent.putExtra("status", statusStr);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void notModerators() {
        btnSetAction.setOnClickListener(view -> snackError(getString(R.string.builders_not_found)));
    }

    // SHOW SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(btnSetAction, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideContent() {
        constraintLayout.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        constraintLayout.setVisibility(View.VISIBLE);
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

    // HIDE TEXT CENTER
    @Override
    public void hideTextNone() {
        textNone.setText("");
        textNone.setVisibility(View.GONE);

    }

    // SET TEXT CENTER
    @Override
    public void setTextNone(String text) {
        textNone.setText(text);
        textNone.setVisibility(View.VISIBLE);
    }

    // HIDE PROGRESS BAR
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public IFullTicketPresenter createPresenter() {
        return new FullTicketPresenter();
    }
}
