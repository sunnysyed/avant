package com.sunnysyed.avant.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.soundcloud.android.crop.Crop;
import com.sunnysyed.avant.R;
import com.sunnysyed.avant.api.AvantApi;
import com.sunnysyed.avant.api.UserSingleton;
import com.sunnysyed.avant.api.model.AttachmentTypes;
import com.sunnysyed.avant.api.model.LoanApplication;
import com.sunnysyed.avant.api.model.LoanApplicationAttachment;
import com.sunnysyed.avant.api.model.LoanTypes;
import com.sunnysyed.avant.api.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanApplicationActivity extends AppCompatActivity{


    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String mCurrentPhotoPath;
    Uri image;
    int mPos = 0;


    private ACProgressFlower dialog;

    private GridView mGridview;
    private LinearLayout mEmptyView;
    private Toolbar mToolbar;
    private DisplayImageOptions options;
    private LoanApplication mLoanApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            mPos = extras.getInt("pos", 0);
        } else {
            mPos = savedInstanceState.getInt("pos", 0);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        mGridview = (GridView) findViewById(R.id.gridview);

        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.TRANSPARENT).build();
        dialog.setCanceledOnTouchOutside(false);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.logo_text)
                .showImageOnLoading(R.drawable.logo_text)
                .showImageOnFail(R.drawable.logo_text)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        if (UserSingleton.getInstance().userModel == null){
            onResume();
        }else {
            updateUi();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("pos", mPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog == null) {
            dialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.TRANSPARENT).build();
            dialog.setCanceledOnTouchOutside(false);
        }
        if (UserSingleton.getInstance().userModel == null){
            dialog.show();
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
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                image = Uri.fromFile(photoFile);
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        beginCrop(image);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        beginCrop(data.getData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(result);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(result);
            else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            }
        }

    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            selectAttachmentType(result);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void selectAttachmentType(final Intent result){

        dialog.show();
        AvantApi.get().getAttachmentTypes().enqueue(new Callback<AttachmentTypes>() {
            @Override
            public void onResponse(Call<AttachmentTypes> call, Response<AttachmentTypes> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationActivity.this);
                    builder.setTitle("Select Loan Type");
                    final CharSequence[] cs = response.body().getAttachmentTypes().toArray(new CharSequence[response.body().getAttachmentTypes().size()]);

                    builder.setItems(cs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog1, int which) {
                            dialog.show();
                            File f = new File(Crop.getOutput(result).getPath());
                            String photo_to_update = "image";
                            RequestBody body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart(photo_to_update, photo_to_update + ".jpeg",
                                            RequestBody.create(MediaType.parse("image/jpeg"), f))
                                    .addFormDataPart("loan_application_id", mLoanApplication.getId().toString())
                                    .addFormDataPart("attachment_type", cs[which].toString())
                                    .build();
                            AvantApi.get().addImageToLoanApplication(UserSingleton.getInstance().accessToken, body).enqueue(new Callback<UserModel>() {
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
                                    Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<AttachmentTypes> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void updateUi(){
        dialog.dismiss();
        mLoanApplication = UserSingleton.getInstance().userModel.getLoanApplications().get(mPos);
        getSupportActionBar().setTitle("Loan Application ID: " + mLoanApplication.getId());
        if (mLoanApplication.getLoanApplicationAttachments() == null || mLoanApplication.getLoanApplicationAttachments().size() ==0 ){
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
            mGridview.setVisibility(View.GONE);
        }else {
            mGridview.setAdapter(new LoanApplicationAttachmentsAdapter(mLoanApplication.getLoanApplicationAttachments()));
            mEmptyView.setVisibility(View.GONE);
            mGridview.setVisibility(View.VISIBLE);
        }
    }

    public  class LoanApplicationAttachmentsAdapter extends BaseAdapter {

        ArrayList<LoanApplicationAttachment> mLoanApplicationsAttachments;
        public LoanApplicationAttachmentsAdapter (List<LoanApplicationAttachment> attachments) {
            mLoanApplicationsAttachments = new ArrayList<>();
            mLoanApplicationsAttachments.addAll(attachments);
        }
        @Override
        public int getCount() {
            return mLoanApplicationsAttachments.size();
        }

        @Override
        public LoanApplicationAttachment getItem(int position) {
            return mLoanApplicationsAttachments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mLoanApplicationsAttachments.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(LoanApplicationActivity.this);
//                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3, parent.getWidth()/3));
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            ImageLoader.getInstance().displayImage(getItem(position).getImageUrl(), imageView, options);
            return imageView;
        }
    }

}
