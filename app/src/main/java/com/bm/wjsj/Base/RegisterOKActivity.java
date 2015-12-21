package com.bm.wjsj.Base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.ImageUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.WheelDialog;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;


/**
 * @author 杨凯
 * @description 注册的成功界面
 * @time 2015.3.12
 */
public class RegisterOKActivity extends HideSoftInputActivity implements APICallback.OnResposeListener {
    private ImageView ivHeadpic;//头像
    private EditText etNickname;//昵称
    //    private EditText etSex;//性别
    private EditText etBirthday;//生日
    private EditText etArea;//地区
    private RippleView rvSave;
    private Uri fromFile;

    private static final int RESULT_CAMERA = 1;// 相机
    private static final int RESULT_ALBUM = 0; // 相册
    private static final int CROP_PICTURE = 3;// 截取后返回
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private File file;
    private String mobile, pwd, provinceId, cityId, pic_path, code;
    private boolean pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_register_ok);
        mobile = getIntent().getStringExtra("mobile");
        pwd = getIntent().getStringExtra("pwd");
        code = getIntent().getStringExtra("code");
        initTitle(getResources().getString(R.string.register));
        assignViews();
    }


    private void assignViews() {
        ivHeadpic = (ImageView) findViewById(R.id.iv_headpic);
        etNickname = (EditText) findViewById(R.id.et_nickname);
//        etSex = (EditText) findViewById(R.id.et_sex);
        etBirthday = (EditText) findViewById(R.id.et_birthday);
        etArea = (EditText) findViewById(R.id.et_area);
        rvSave = (RippleView) findViewById(R.id.rv_save);
        ivHeadpic.setOnClickListener(this);
//        etSex.setOnClickListener(this);
        etBirthday.setOnClickListener(this);
        etArea.setOnClickListener(this);
        //保存按钮
        rvSave.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (isParamsOK()) {
                    if (rippleView.getId() == R.id.rv_save)
                        DialogUtils.showProgressDialog("正在注册，请稍等...", RegisterOKActivity.this);
                    SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                    Log.e("用户ID==========", sp.getValue(Constant.SP_USERID));
                    WebServiceAPI.getInstance().completeInfo(
                            sp.getValue(Constant.SP_USERID),
                            pwd,
                            file,
                            etNickname.getText().toString().trim(),
                            etBirthday.getText().toString(), provinceId, cityId, "",
                            RegisterOKActivity.this, RegisterOKActivity.this);

                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_headpic:
                showPhoto(false);
                break;
//            case R.id.et_sex:
//                showPhoto(true);
//                break;
            case R.id.et_birthday:
                showBrithdayDialog();
                break;
            case R.id.et_area:
                WheelDialog.getInstance().chooseCity(this, etArea, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String provinceId, String cityId) {
                        RegisterOKActivity.this.provinceId = provinceId;
                        RegisterOKActivity.this.cityId = cityId;
                    }
                });
                break;
            default:
                break;
        }
    }

    private void showBrithdayDialog() {
        // TODO Auto-generated method stub
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String defaultDate = (TextUtils.isEmpty(etBirthday.getText().toString())) ?
                sDateFormat.format(new Date()) : etBirthday.getText().toString();
        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.userinfo_data_pivk);
        final DatePicker dp_user_bri = (DatePicker) alertDialog.getWindow().findViewById(
                R.id.dp_user_bri);

        Calendar calendar = Calendar.getInstance();

        dp_user_bri.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        dp_user_bri.setMaxDate(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(sdf.parse(defaultDate));
            dp_user_bri.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TextView tv_quxiao = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cancel);
        TextView tv_queding = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        tv_queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int month2 = dp_user_bri.getMonth();
                int month3 = month2 + 1;
                etBirthday.setText(dp_user_bri.getYear() + "-" + month3 + "-"
                        + dp_user_bri.getDayOfMonth());
                alertDialog.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    /**
     * 性别和选择头像弹框
     *
     * @param isSex 是否是性别弹框
     */
    private void showPhoto(final boolean isSex) {
        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.dialog_photo);
        TextView tv_camera = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_camera);
        TextView tv_album = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_album);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        if (isSex) {
            tv_camera.setText("男");
            tv_album.setText("女");
        }
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSex) {
//                    etSex.setText("男");
                } else {
                    //拍照的方法
                    doCamera();
                }
                alertDialog.cancel();
            }
        });
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSex) {
//                    etSex.setText("女");
                } else {
                    //从相册选择的方法
                    doAlbum();
                }
                alertDialog.cancel();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }


//    private String getSex() {
//        return etSex.getText().toString().trim().equals("男") ? "1" : "2";
//    }

//    private void doAlbum() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
//        startActivityForResult(intent, RESULT_ALBUM);
//    }

    private void doAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_ALBUM);
    }

    private void doCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStorageDirectory();

            File file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                    + ".jpg");
            pic_path = file.getPath();
            fromFile = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fromFile);
            startActivityForResult(intent, RESULT_CAMERA);
        } else {
            NewToast.show(RegisterOKActivity.this, "请确认已经插入SD卡", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_ALBUM:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
//                    if (imageUri != null) {
//                        Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
//                        ivHeadpic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
//                        // 图像保存到文件中
//                        FileOutputStream foutput = null;
//                        File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp");
//                        if (!files.exists()) {
//                            files.mkdirs();
//                        }
//                        File mRecVedioPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/", "cropPicture.jpg");
//                        if (mRecVedioPath.exists()) {
//                            mRecVedioPath.delete();
//                        }
//                        file = mRecVedioPath;
//                        pic = file == null ? false : true;
//                        if (!pic)
//                            NewToast.show(this, "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
//                        try {
//                            foutput = new FileOutputStream(mRecVedioPath);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, foutput);
//                            foutput.flush();
//                            foutput.close();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            if (null != foutput) {
//                                try {
//                                    foutput.flush();
//                                    foutput.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
                    break;
                case RESULT_CAMERA:
                    if (null != fromFile) {
                        startPhotoZoom(fromFile);
                    }
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                case CROP_PICTURE:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                            ivHeadpic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
                            // 图像保存到文件中
                            FileOutputStream foutput = null;
                            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp");
                            if (!files.exists()) {
                                files.mkdirs();
                            }
                            File mRecVedioPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/", "cropPicture.jpg");
                            if (mRecVedioPath.exists()) {
                                mRecVedioPath.delete();
                            }
                            file = mRecVedioPath;
                            pic = file == null ? false : true;
                            if (!pic)
                                NewToast.show(this, "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
                            try {
                                foutput = new FileOutputStream(mRecVedioPath);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, foutput);
                                foutput.flush();
                                foutput.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (null != foutput) {
                                    try {
                                        foutput.flush();
                                        foutput.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

//    private void doPicPath() {
//        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
//        ivHeadpic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
////                            uploadPhoto(bitmap);
//        // 图像保存到文件中
//        file = ImageUtils.saveBitmap(bitmap, "header.jpg");
//        pic = file == null ? false : true;
//        if (!pic)
//            NewToast.show(this, "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
//    }
//
//    @SuppressLint("NewApi")
//    public String doPhoto(Uri photoUri) {
//        String picPath = "";
//        if (DocumentsContract.isDocumentUri(this, photoUri)) {
//
//            String wholeID = DocumentsContract.getDocumentId(photoUri);
//
//            String id = wholeID.split(":")[1];
//
//            String[] column = {MediaStore.Images.Media.DATA};
//
//            String sel = MediaStore.Images.Media._ID + "=?";
//
//            Cursor cursor = getContentResolver().query(
//
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
//
//                    new String[]{id}, null);
//
//            int columnIndex = cursor.getColumnIndex(column[0]);
//
//            if (cursor.moveToFirst()) {
//
//                picPath = cursor.getString(columnIndex);
//
//            }
//            cursor.close();
//        } else {
//            picPath = getPicPath(photoUri);
//        }
//        return picPath;
//    }
//
//    private String getPicPath(Uri photoUri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//
//        Cursor cursor = getContentResolver().query(photoUri, projection, null, null,
//                null);
//
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//
//        String picPath = cursor.getString(column_index);
//
//        return picPath;
//
//    }

//    private Bitmap decodeUriAsBitmap(Uri uri) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return bitmap;
//    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PICTURE);
    }

    // 判断输入参数
    boolean isParamsOK() {
        if (!pic) {
            NewToast.show(this, "请选择头像", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Flash).duration(1000).playOn(ivHeadpic);
            return false;
        } else if (TextUtils.isEmpty(etNickname.getText().toString().trim())) {
            NewToast.show(this, "请输入昵称", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etNickname);
            return false;
        } else if (TextUtils.isEmpty(etBirthday.getText().toString().trim())) {
            NewToast.show(this, "请输入生日", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etBirthday);
            return false;
        } else if (TextUtils.isEmpty(etArea.getText().toString().trim())) {
            NewToast.show(this, "请输入所在地", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etArea);
            return false;
        }
        return true;
    }


    @Override
    public void OnFailureData(String error, Integer tag) {
        Log.e("error", "error = " + error);
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
            switch (tag) {
                case 1:
                    DialogUtils.cancleProgressDialog();
                    WJSJApplication.getInstance().clearAc();
                    WebServiceAPI.getInstance().login(mobile, pwd,
                            RegisterOKActivity.this, RegisterOKActivity.this);
                    //上线
                    WebServiceAPI.getInstance().online(JPushInterface.getRegistrationID(this), RegisterOKActivity.this, RegisterOKActivity.this);
                    break;
                case 2:
                    sp.putBooleanValue(Constant.SP_KEY_ISLOGIN, true);
                    sp.putValue(Constant.SP_USERNAME, apiResponse.data.appuser.nickname);
                    sp.putValue(Constant.SP_USERID, apiResponse.data.appuser.id.toString());
                    sp.putValue(Constant.SP_KEY_USER, mobile);
                    sp.putValue(Constant.SP_KEY_PWD, pwd);
                    sp.putValue(Constant.SP_PHOTO, apiResponse.data.appuser.head);
                    sp.putValue(Constant.SP_LEVEL, apiResponse.data.appuser.level);
                    sp.putValue(Constant.SP_INTEGRAL, apiResponse.data.appuser.integral);
                    sp.putValue(Constant.SP_SEX, apiResponse.data.appuser.sex);
                    sp.putValue(Constant.SP_BIRTHDAY, apiResponse.data.appuser.birthday);
                    sp.putValue(Constant.SP_PROVINCEID, apiResponse.data.appuser.provinceId);
                    sp.putValue(Constant.SP_CITYID, apiResponse.data.appuser.cityId);
                    sp.putValue(Constant.SP_SIGNATURE, apiResponse.data.appuser.sign);
                    sp.putValue(Constant.SP_CONSTELLATION, apiResponse.data.appuser.constellation);
                    sp.putValue(Constant.SP_AGE, apiResponse.data.appuser.age);
                    sp.putValue(Constant.SP_TOKEN, apiResponse.data.appuser.token);
                    Log.e("Loginac", "token = " + apiResponse.data.appuser.token);
//                    WJSJApplication.getInstance().connectRongCloud();
                    Intent intent = new Intent(RegisterOKActivity.this, MainActivity.class);
                    intent.putExtra("isScan", false);
                    intent.putExtra("fromLogin", true);
                    startActivityForResult(intent, 100);
                    RegisterOKActivity.this.finish();
                    break;
                case 22:
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    sp1.putValue(Constant.STATUS, apiResponse.data.status);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
