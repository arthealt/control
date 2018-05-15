package com.app.fixee.myapplication.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.add.AddActivity;
import com.app.fixee.myapplication.adminTickets.FullTicketActivity;
import com.app.fixee.myapplication.callbacks.OnResponseClickTicket;
import com.app.fixee.myapplication.login.LoginActivity;
import com.app.fixee.myapplication.manage.ManageActivity;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpActivity<IMainView, IMainPresenter> implements IMainView, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int MENU_MANAGE = 1;
    private static final int MENU_LOGOUT = 2;

    private TextView textNone;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private RecyclerView recyclerViewContent;
    private String newsError = "";
    private String ticketsError = "";
    private int accessLevel = 0;
    private BottomNavigationView bottomNavigationView;
    private ProgressDialog progressDialog;
    private List<Ticket> ticketsAll = new ArrayList<>();
    private List<News> newsAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getPresenter().getNews(MainActivity.this);
        getPresenter().getTickets(MainActivity.this);
    }

    // INIT ALL BASE VIEWS
    private void initViews() {

        recyclerViewContent = findViewById(R.id.recycler_tickets_user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewContent.getContext(), linearLayoutManager.getOrientation());

        recyclerViewContent.setLayoutManager(linearLayoutManager);
        recyclerViewContent.addItemDecoration(dividerItemDecoration);

        textNone = findViewById(R.id.info_none);
        progressBar = findViewById(R.id.progress_bar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    // SET NEWS
    public void setItemNews() {

        recyclerViewContent.setAdapter(new RecyclerViewNews(newsAll, accessLevel, this::createDialogSetPosting));

        switch (newsError) {
            case "-":
                hideProgressBar();
                hideTextNone();
                showFab();
                break;

            case "":
                hideFab();
                showProgressBar();
                break;

            default:
                if (newsError.equals(getString(R.string.empty_news))) {
                    showFab();
                } else {
                    hideFab();
                }
                setTextNone(newsError);
                break;
        }
    }

    // SET TICKETS
    public void setItemTickets() {

        recyclerViewContent.setAdapter(new RecyclerViewTickets(ticketsAll, accessLevel, new OnResponseClickTicket() {
            @Override
            public void onSuccess(Ticket ticket, int position) {
                openTicketActivity(ticket, position);
            }

            @Override
            public void onSuccessFeedback(String docId) {
                createWriteFeedback(docId);
            }
        }));

        switch (ticketsError) {
            case "-user":
                hideProgressBar();
                hideTextNone();
                showFab();
                break;

            case "-":
                hideFab();
                hideProgressBar();
                hideTextNone();
                break;

            case "":
                hideFab();
                showProgressBar();
                break;

            default:
                if (ticketsError.equals(getString(R.string.empty_tickets))) {
                    showFab();
                } else {
                    hideFab();
                }
                setTextNone(ticketsError);
                break;
        }
    }

    // CREATE DIALOG POST NEWS
    private void createWriteFeedback(String docId) {

        View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.input_add_feedback, findViewById(android.R.id.content), false);
        final EditText editFeedback = viewInflated.findViewById(R.id.feedback);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getString(R.string.feedback_title));
        builder.setView(viewInflated);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.send), (dialogInterface, i) -> { });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            if (!editFeedback.getText().toString().trim().isEmpty()) {
                editFeedback.setError(null);
                getPresenter().addFeedback(docId, editFeedback.getText().toString().trim(), MainActivity.this);
                dialog.dismiss();
            } else {
                editFeedback.setError(getString(R.string.error_fill));
            }
        });
    }

    // CREATE DIALOG POST NEWS
    private void createDialogSetPosting(String docId, int position) {

        String[] publish = {getString(R.string.publish)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(publish, (dialogInterface, i) -> getPresenter().publishNews(docId, position, MainActivity.this));
        builder.show();
    }

    // OPEN MANAGE ACCOUNT ACTIVITY
    private void openManageActivity() {
        startActivity(new Intent(this, ManageActivity.class));
    }

    // OPEN FULL TICKET ACTIVITY
    private void openTicketActivity(Ticket ticket, int position) {
        Intent intent = new Intent(this, FullTicketActivity.class);
        intent.putExtra("name", ticket.getName());
        intent.putExtra("description", ticket.getDescription());
        intent.putExtra("photo", ticket.getPhoto());
        intent.putExtra("date", ticket.getDate());
        intent.putExtra("builder", ticket.getBuilder());
        intent.putExtra("builderUid", ticket.getBuilderUid());
        intent.putExtra("docId", ticket.getDocId());
        intent.putExtra("feedback", ticket.getFeedback());
        intent.putExtra("status", ticket.getStatus());
        intent.putExtra("accessLevel", accessLevel);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
    }

    // OPEN CREATE  ACTIVITY
    private void openCreateActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("itemMenu", getCurrentSelectedItemBottomMenu());
        startActivityForResult(intent, 2);
    }

    // SHOW FAB
    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    // HIDE FAB
    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    // GET CURRENT SELECTED ITEM BOTTOM MENU
    private int getCurrentSelectedItemBottomMenu() {

        switch (bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).toString()) {
            case "Новости":
                return 1;

            case "Заявки":
                return 2;

            default:
                return 0;
        }
    }

    // CLICK'S
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                openCreateActivity();
                break;
        }
    }

    // CLICK'S BOTTOM MENU
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_news:
                setItemNews();
                break;

            case R.id.action_tickets:
                setItemTickets();
                break;
        }
        return true;
    }

    // FOR OPTIONS MENU
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();
        if (accessLevel == 3) {
            menu.add(0, MENU_MANAGE, Menu.NONE, R.string.admin_menu);
        }
        menu.add(0, MENU_LOGOUT, Menu.NONE, R.string.log_out);

        return super.onPrepareOptionsMenu(menu);
    }

    // FOR OPTIONS MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_MANAGE:
                openManageActivity();
                break;

            case MENU_LOGOUT:
                getPresenter().logOut();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREATE PRESENTER
    @NonNull
    @Override
    public IMainPresenter createPresenter() {
        return new MainPresenter();
    }

    // OPEN LOGIN ACTIVITY
    @Override
    public void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    // UPDATE NEWS
    @Override
    public void updateNews(int position) {
        newsAll.get(position).setStatus(2);
        recyclerViewContent.getAdapter().notifyItemChanged(position);
    }

    // FOR UPDATE EDIT ITEM
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 1:

                    int position = data.getIntExtra("position", 0);
                    Ticket ticket = new Ticket(data.getStringExtra("name"), data.getStringExtra("description"), data.getStringExtra("photo"), data.getStringExtra("date"), data.getStringExtra("builder"), data.getStringExtra("builderUid"), data.getStringExtra("docId"), data.getStringExtra("feedback"), data.getStringExtra("status"));

                    ticketsAll.set(position, ticket);
                    recyclerViewContent.getAdapter().notifyItemChanged(position);
                    break;

                case 2:
                    switch (data.getIntExtra("currentItem", 0)) {
                        case 1:
                            addNewsOne(data);
                            break;

                        case 2:
                            addTicket(data);
                            break;

                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    // ADD NEW TICKET
    private void addTicket(Intent data) {
        hideTextNone();
        ticketsAll.add(new Ticket(data.getStringExtra("name"), data.getStringExtra("description"), data.getStringExtra("photo"), data.getStringExtra("date"), data.getStringExtra("builder"), data.getStringExtra("builderUid"), data.getStringExtra("docId"), data.getStringExtra("feedback"), data.getStringExtra("status")));
        recyclerViewContent.getAdapter().notifyItemChanged(ticketsAll.size());
        recyclerViewContent.scrollToPosition(ticketsAll.size() - 1);
    }

    // ADD NEW NEWS
    private void addNewsOne(Intent data) {
        hideTextNone();
        newsAll.add(new News(data.getStringExtra("title"), data.getStringExtra("description"), data.getStringExtra("date"), data.getStringExtra("photo"), data.getStringExtra("docId"), data.getStringExtra("author"), data.getIntExtra("status", 0)));
        recyclerViewContent.getAdapter().notifyItemChanged(newsAll.size());
        recyclerViewContent.scrollToPosition(newsAll.size() - 1);
    }

    @Override
    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;

        if (accessLevel == 3) {
            invalidateOptionsMenu();
        }
    }

    // GET ALL TICKET'S
    @Override
    public void addTickets(List<Ticket> tickets) {
        ticketsAll.addAll(tickets);
        recyclerViewContent.getAdapter().notifyDataSetChanged();
    }

    // GET ALL NEWS
    @Override
    public void addNews(List<News> news) {
        newsAll.addAll(news);
        recyclerViewContent.getAdapter().notifyDataSetChanged();
    }

    // SET NEWS ERROR
    @Override
    public void setNewsErr(String error) {
        newsError = error;
    }

    // SET TICKETS ERROR
    @Override
    public void setTicketsErr(String error) {
        ticketsError = error;
    }

    // LOAD CURRENT TAB BOTTOM MENU
    @Override
    public void loadCurrentItemMenu() {

        switch (getCurrentSelectedItemBottomMenu()) {
            case 1:
                setItemNews();
                break;

            case 2:
                setItemTickets();
                break;

            default:
                break;
        }
    }

    // SHOW SNACK ERROR
    @Override
    public void snackError(String error) {
        Snackbar.make(fab, error, Snackbar.LENGTH_LONG).show();
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

    // SHOW PROGRESS BAR
    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // HIDE PROGRESS BAR
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
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
}
