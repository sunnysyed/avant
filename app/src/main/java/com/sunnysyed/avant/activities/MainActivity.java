package com.sunnysyed.avant.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunnysyed.avant.Avant;
import com.sunnysyed.avant.R;
import com.sunnysyed.avant.api.AvantApi;
import com.sunnysyed.avant.api.UserSingleton;
import com.sunnysyed.avant.api.model.LoanApplication;
import com.sunnysyed.avant.api.model.LoanApplicationAttachment;
import com.sunnysyed.avant.api.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ACProgressFlower dialog;

    TextView mNameView, mEmailView;
    private ListView mListView;
    private LinearLayout mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNameView = (TextView) findViewById(R.id.name);
        mEmailView = (TextView) findViewById(R.id.email);

        mListView = (ListView) findViewById(R.id.listview);

        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.TRANSPARENT).build();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();
        if (UserSingleton.getInstance().userModel == null){
            AvantApi.get().getProfile(UserSingleton.getInstance().accessToken).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()){
                        UserSingleton.getInstance().userModel = response.body();
                        updateUi();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }else {
            updateUi();
            dialog.dismiss();
        }
    }

    public void updateUi(){
        mNameView.setText(UserSingleton.getInstance().userModel.getProfile().getFirstName() + " " + UserSingleton.getInstance().userModel.getProfile().getLastName());
        mEmailView.setText(UserSingleton.getInstance().userModel.getProfile().getEmail());
        if (UserSingleton.getInstance().userModel.getLoanApplications() == null || UserSingleton.getInstance().userModel.getLoanApplications().size() ==0 ){
            final ImageView logo = (ImageView)findViewById(R.id.logo);

            Runnable action = new Runnable() {
                @Override
                public void run() {
                    Interpolator i = new Interpolator() {
                        @Override
                        public float getInterpolation(float input) {
                            float x = input < 1 / 3f ? 2 * input : (1 + input) / 2;
                            return (float) Math.sin(x * Math.PI);
                        }
                    };
                    ScaleAnimation anim = new ScaleAnimation(1, 1.2f, 1, 1.2f, logo.getWidth() / 2, logo.getHeight() / 2);
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.setDuration(750);
                    anim.setInterpolator(i);
                    logo.startAnimation(anim);
                }
            };
            logo.post(action);
            mEmptyView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            mListView.setAdapter(new LoanApplicationAdapter(UserSingleton.getInstance().userModel.getLoanApplications()));
            mEmptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.logout) {
            UserSingleton.getInstance().logout();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.nothing);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  class LoanApplicationAdapter extends BaseAdapter {

        ArrayList<LoanApplication> mLoanApplications;
        public LoanApplicationAdapter (List<LoanApplication> applications) {
            mLoanApplications = new ArrayList<>();
            mLoanApplications.addAll(applications);
        }
        @Override
        public int getCount() {
            return mLoanApplications.size();
        }

        @Override
        public LoanApplication getItem(int position) {
            return mLoanApplications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mLoanApplications.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater)MainActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.loan_application_item, parent, false);
            }
            TextView application_type = (TextView) convertView.findViewById(R.id.application_type);
            application_type.setText(getItem(position).getLoanApplicationType());

            TextView application_id = (TextView) convertView.findViewById(R.id.application_id);
            application_id.setText("Loan Application ID: " + getItem(position).getId());
            List<LoanApplicationAttachment> mLoanApplicationAttachment = getItem(position).getLoanApplicationAttachments();
            ImageView iv = (ImageView) convertView.findViewById(R.id.image);
            if (mLoanApplicationAttachment != null && mLoanApplicationAttachment.size() > 0){
                ImageLoader.getInstance().displayImage(mLoanApplicationAttachment.get(mLoanApplicationAttachment.size() -1).getImageUrl(), iv);
            }
            else {
                iv.setImageResource(R.drawable.logo);
            }
            return convertView;
        }
    }
}
