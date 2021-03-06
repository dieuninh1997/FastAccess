package com.fastaccess.ui.modules.cloud.restore;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.fastaccess.R;
import com.fastaccess.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kosh on 23 Oct 2016, 9:05 PM
 */

public class RestoreView extends BaseActivity<RestoreMvp.View, RestorePresenter> implements RestoreMvp.View {
    private RestorePresenter presenter;
    private ProgressDialog progressDialog;
    private DatabaseReference database;
    private FirebaseUser user;

    @Override protected int layout() {
        return 0;
    }

    @NonNull @Override protected RestorePresenter getPresenter() {
        if (presenter == null) {
            presenter = RestorePresenter.with(this);
        }
        return presenter;
    }

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override public void onShowProgress() {
        if (!getProgressDialog().isShowing()) getProgressDialog().show();
    }

    @Override public void onHideProgress() {
        if (getProgressDialog().isShowing()) getProgressDialog().dismiss();
    }

    @Override public void onShowMessage(@StringRes int resId) {
        onShowMessage(getString(resId));
    }

    @Override public void onShowMessage(@NonNull String msg) {
        onHideProgress();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override public void finishOnError() {
        finish();
    }

    @Override public void onRestoreCompleted() {
        onHideProgress();
        onShowMessage(R.string.successfully_restored);
        finish();
    }

    @Nullable @Override public FirebaseUser user() {
        if (user == null) user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().onRestore(getDatabase());
    }

    @Override protected void onStop() {
        super.onStop();
        try {
            getDatabase().removeEventListener(getPresenter());
        } catch (Exception ignored) {}
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.restore_in_progress));
        }
        return progressDialog;
    }

    private DatabaseReference getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
}
