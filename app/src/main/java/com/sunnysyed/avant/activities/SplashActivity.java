package com.sunnysyed.avant.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.sunnysyed.avant.R;
import com.sunnysyed.avant.api.AvantApi;
import com.sunnysyed.avant.api.UserSingleton;
import com.sunnysyed.avant.api.model.UserModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends Activity {

    @Bind(R.id.logo)
    ImageView mLogo;


    /**
     * Determine if a user has logged in before
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


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
                ScaleAnimation anim = new ScaleAnimation(1, 1.2f, 1, 1.2f, mLogo.getWidth() / 2, mLogo.getHeight() / 2);
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(750);
                anim.setInterpolator(i);
                mLogo.startAnimation(anim);
            }
        };
        mLogo.post(action);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (!UserSingleton.getInstance().accessToken.equals("")) {
                    AvantApi.get().getProfile(UserSingleton.getInstance().accessToken).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful()) {
                                UserSingleton.getInstance().userModel = response.body();
                                openMain();
                            }else {
                                openLogin();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            openLogin();
                        }
                    });
                } else {
                    openLogin();
                }
            }
        }, 1500);

    }


    /**
     * Display ending animation and launch MainActivity
     */
    public void openMain() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.nothing);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogo.clearAnimation();
        mLogo.startAnimation(anim);
    }

    /**
     * Display ending animation and launch LoginActivity
     */
    public void openLogin() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.nothing);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogo.clearAnimation();
        mLogo.startAnimation(anim);
    }
}

