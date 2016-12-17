package com.grayditch.netarea.presentation.views.mainactivity.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.grayditch.netarea.R;
import com.grayditch.netarea.presentation.App;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gerard on 9/04/16.
 */
public class LoginFragment extends Fragment implements LoginView {

    @Inject
    LoginPresenter presenter;

    private OnLoginInteractionListener mListener;
    private ProgressDialog progress;

    @Bind(R.id.username_edittext)
    EditText username;
    @Bind(R.id.password_edittext)
    EditText password;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(getContext()).inject(this);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);
        this.presenter.onCreate(this);
        return view;
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Fetching qualifications");
        progress.setMessage("loading...");
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.dismiss();
    }

    @Override
    public void navigateToQualifications() {
        if (mListener != null)
            mListener.onLoginFinished();
    }


    @OnClick(R.id.login_button)
    public void onClick() {
        Log.v("LoginFragment", "On Click Login");
        presenter.login(username.getText().toString(), password.getText().toString());
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLoginInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface OnLoginInteractionListener {
        void onLoginFinished();
    }
}
