package com.app.fixee.myapplication.add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

public class AddActivity extends MvpActivity<IAddView, IAddPresenter> implements IAddView, View.OnClickListener {

    private EditText name;
    private EditText description;
    private Button addPhoto;
    private ImageView imageView;
    private String linkPhoto = "";
    private Uri uriPhoto;
    private int currentItem = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initViews();
        getInfo();

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable("uri") != null) {
                setPhoto(savedInstanceState.getParcelable("uri"));
                setTextButton();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("uri", uriPhoto);
    }

    // INIT ALL BASE VIEWS
    private void initViews() {
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        addPhoto = findViewById(R.id.btn_add_photo);
        Button create = findViewById(R.id.btn_create);

        addPhoto.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    // GET INFO FROM INTENT
    private void getInfo() {
        currentItem = getIntent().getIntExtra("itemMenu", 0);
    }

    // CLICK'S
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_photo:
                openPickerPhoto();
                break;

            case R.id.btn_create:
                if (!validate(name, description)) return;

                switch (currentItem) {
                    case 1:
                        getPresenter().addNews(name.getText().toString().trim(), description.getText().toString().trim(), linkPhoto, AddActivity.this);
                        break;

                    case 2:
                        getPresenter().addTicket(name.getText().toString().trim(), description.getText().toString().trim(), linkPhoto, AddActivity.this);
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
    }

    // OPEN PICKER FOR UPLOAD ON SERVER
    private void openPickerPhoto() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, 1);
    }

    // CHECK FIELDS
    private boolean validate(EditText name, EditText description) {

        boolean valid = true;

        if (name.getText().toString().trim().isEmpty()) {
            name.setError(getString(R.string.error_fill));
            valid = false;
        } else {
            name.setError(null);
        }

        if (description.getText().toString().trim().isEmpty()) {
            description.setError(getString(R.string.error_fill));
            valid = false;
        } else {
            description.setError(null);
        }

        return valid;
    }

    // SET TEXT BUTTON PHOTO
    private void setTextButton() {
        addPhoto.setText(R.string.set_photo);
    }

    // SET PHOTO
    private void setPhoto(Uri uri) {
        uriPhoto = uri;

        Glide.with(this)
                .load(uri)
                .into(imageView);
    }

    // FOR UPLOAD PHOTO
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            getPresenter().uploadPhoto(data.getData());
            setPhoto(data.getData());
            setTextButton();
        }
    }

    // SEND RESULT
    @Override
    public void onSend() {
        setResult(RESULT_OK);
        finish();
    }

    // ADD PHOTO URL
    @Override
    public void addPhotoUrl(String link) {
        linkPhoto = link;
    }

    // ADD TICKET FROM MAIN
    @Override
    public void addTicket(Ticket ticket) {

        Intent intent = new Intent();
        intent.putExtra("name", ticket.getName());
        intent.putExtra("description", ticket.getDescription());
        intent.putExtra("photo", ticket.getPhoto());
        intent.putExtra("date", ticket.getDate());
        intent.putExtra("builder", ticket.getBuilder());
        intent.putExtra("builderUid", ticket.getBuilderUid());
        intent.putExtra("docId", ticket.getDocId());
        intent.putExtra("feedback", ticket.getFeedback());
        intent.putExtra("status", ticket.getStatus());
        intent.putExtra("currentItem", currentItem);
        setResult(RESULT_OK, intent);

        finish();
    }

    // ADD NEWS FROM MAIN
    @Override
    public void addNews(News news) {

        Intent intent = new Intent();
        intent.putExtra("title", news.getTitle());
        intent.putExtra("description", news.getDescription());
        intent.putExtra("date", news.getDate());
        intent.putExtra("photo", news.getPhoto());
        intent.putExtra("docId", news.getDocId());
        intent.putExtra("author", news.getAuthor());
        intent.putExtra("status", news.getStatus());
        intent.putExtra("currentItem", currentItem);
        setResult(RESULT_OK, intent);

        finish();
    }

    // SHOW SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(name, error, Snackbar.LENGTH_LONG).show();
    }

    // SHOW PROGRESS DIALOG
    @Override
    public void showProgressDialog() {
        hideKeyboard();
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

    // HIDE KEYBOARD
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // CREATE PRESENTER
    @NonNull
    @Override
    public IAddPresenter createPresenter() {
        return new AddPresenter();
    }
}
