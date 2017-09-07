package net.suntrans.ebuilding.fragment.din;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.yalantis.ucrop.UCrop;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.UpLoadImageMessage;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.PhotoUtils;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/8/16.
 */

public class UpLoadImageFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 0x02;

    private static final String CROPPED_IMAGE_NAME = "SceneCropImage.jpg";
    public String mCurrentPhotoPath;
    static String FILE_PROVIDER = "net.suntrans.ebuilding.fileProvider";
    private Uri photoURI;
    private ImageView ll;
    private Subscription subscribe;
    private String destinationFileName;
    private String type;

    public static UpLoadImageFragment newInstance(String type) {
        UpLoadImageFragment dialogFragment = new UpLoadImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.gallery).setOnClickListener(this);
        view.findViewById(R.id.takePhoto).setOnClickListener(this);
        ll = (ImageView) view.findViewById(R.id.root);
        type = getArguments().getString("type");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery:
                openGallery();
                break;
            case R.id.takePhoto:
                dispatchTakePictureIntent(FILE_PROVIDER);
                break;
        }
    }

    private void openGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    private void dispatchTakePictureIntent(String fileprovider) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        fileprovider,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("requestCode =" + requestCode + ",resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                startCropActivity(photoURI);
            } else if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    UiUtils.showToast("选择图片失败");
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }

    }

    private void startCropActivity(@NonNull Uri uri) {
        destinationFileName = CROPPED_IMAGE_NAME;

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getContext().getCacheDir(), destinationFileName)));
        if (type.equals("1")){
            uCrop.withAspectRatio(16, 9);
        }else if (type.equals("2")){
            uCrop.withAspectRatio(1, 1);
            uCrop.withMaxResultSize(UiUtils.dip2px(40),UiUtils.dip2px(40));
        }

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        uCrop.withOptions(options);
        uCrop.start(getContext(), this);
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            System.out.println(resultUri.toString());
            File file = new File(getContext().getCacheDir(), destinationFileName);
            upLoad(file);
        } else {
            UiUtils.showToast("裁剪图片失败");
        }
    }


    private LoadingDialog dialog;

    private void upLoad(File file) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext());
            dialog.setCancelable(false);
            dialog.setWaitText("请稍后..");
        }
        dialog.show();
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
        dialog.setWaitText(getString(R.string.tips_uploading));

        subscribe = RetrofitHelper.getApi().upload(imageBodyPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UpLoadImageMessage>(getActivity()) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       super.onError(e);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(UpLoadImageMessage info) {
                        dialog.dismiss();
                        if (info != null) {
                            if (info.code == 200) {
                                LogUtil.i("图片上传成功！path：" + info.data);
                                if (loadListener != null)
                                    loadListener.uploadImageSuccess(info.data);
                                dismiss();
                            } else {
                                UiUtils.showToast(getString(R.string.tips_upload_failed));

                            }
                        }else {
                            UiUtils.showToast(getString(R.string.tips_upload_failed));

                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null) {
            if (!subscribe.isUnsubscribed())
                subscribe.unsubscribe();
        }
    }

    private onUpLoadListener loadListener;

    public onUpLoadListener getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(onUpLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public interface onUpLoadListener {
        void uploadImageSuccess(String path);
    }
}
