package com.bm.wjsj.Personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ImageUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 我的主页
 */
public class PersonalFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {
    private View view;
    private ImageView iv_sidebar, iv_setup, iv_bg;
    private SimpleDraweeView sdv_pic;
    //    private ImageView iv_headpic;
    private TextView tvName, tvLevel, tvAge, tvSex, tvConstellation, tvSignature, tvMyDynamic,
            tvMyPost, tvMyPoint, tvMyOrder, tvMyCollect, tvMyPhone;

    private Uri fromFile;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private FileOutputStream foutput = null;
    private File head;
    private boolean pic;
    private String file, pic_path;
    private static final int RESULT_ALBUM = 0; // 相册
    private static final int RESULT_CAMERA = 1;// 相机
    private static final int CROP_PICTURE = 2;// 截取后返回
    public Dialog alertDialog;
    private SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
    public UserInfo appuser;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_persanal, container, false);
        initView();
        return view;
    }

    private void initView() {
        iv_sidebar = (ImageView) view.findViewById(R.id.iv_sidebar);
        iv_setup = (ImageView) view.findViewById(R.id.iv_setup);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        sdv_pic = (SimpleDraweeView) view.findViewById(R.id.sdv_pic);
        iv_sidebar.setOnClickListener(this);
        iv_setup.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvLevel = (TextView) view.findViewById(R.id.tv_level);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvSex = (TextView) view.findViewById(R.id.tv_sex);
        tvConstellation = (TextView) view.findViewById(R.id.tv_constellation);
        tvSignature = (TextView) view.findViewById(R.id.tv_signature);
        tvMyDynamic = (TextView) view.findViewById(R.id.tv_my_dynamic);
        tvMyPost = (TextView) view.findViewById(R.id.tv_my_post);
        tvMyPoint = (TextView) view.findViewById(R.id.tv_my_point);
        tvMyOrder = (TextView) view.findViewById(R.id.tv_my_order);
        tvMyCollect = (TextView) view.findViewById(R.id.tv_my_collect);
        tvMyPhone = (TextView) view.findViewById(R.id.tv_my_phone);
        if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO).equals("")) {
            if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
                sdv_pic.setImageResource(R.mipmap.touxiangnan);
            } else {
                sdv_pic.setImageResource(R.mipmap.touxiangnv);
            }
        } else {
            sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
            ImageLoader.getInstance().displayImage(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO), iv_bg,
                    ImageUtils.getSpecialOptions(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                            // TODO Auto-generated method stubk
                            iv_bg.setImageBitmap(ImageUtils.BoxBlurFilter(arg2));
                        }
                    });
        }
        tvName.setText("");
        tvName.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME));
        tvLevel.setText("");
        tvLevel.setText("V" + WJSJApplication.getInstance().getSp().getValue(Constant.SP_LEVEL));
        tvSignature.setText("");
        tvSignature.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_SIGNATURE));
        tvSex.setText("");
        tvSex.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0") ? "帅哥" : "美女");
        tvConstellation.setText("");
        if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_BIRTHDAY).equals("")) {
            tvConstellation.setText("星座");
        } else {
            showBrithdayDialog(WJSJApplication.getInstance().getSp().getValue(Constant.SP_BIRTHDAY));
        }
        tvMyDynamic.setOnClickListener(this);
        tvMyPost.setOnClickListener(this);
        tvMyPoint.setOnClickListener(this);
        tvMyOrder.setOnClickListener(this);
        tvMyCollect.setOnClickListener(this);
        tvMyPhone.setOnClickListener(this);
        sdv_pic.setOnClickListener(this);


    }

//    ControllerListener controllerListener = new BaseControllerListener() {
//        @Override
//        public void onFinalImageSet(String id, @Nullable Object imageInfo1, @Nullable Animatable animatable) {
//            super.onFinalImageSet(id, imageInfo1, animatable);
//            iv_bg.setImageBitmap(ImageUtils.drawableToBitmap(sdv_pic.getDrawable()));
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            WebServiceAPI.getInstance().login((WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_USER)),
                    (WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_PWD)),
                    PersonalFragment.this, getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sidebar:
                ((MainActivity) getActivity()).sm.toggle();
                break;
            case R.id.iv_setup:
                ((MainActivity) getActivity()).gotoOtherActivity(SetupActivity.class);
                break;
            case R.id.sdv_pic:
                showPhoto();
                break;
            case R.id.tv_my_dynamic:
                ((MainActivity) getActivity()).gotoOtherActivity(MyDynamicActivity.class);
                break;
            case R.id.tv_my_post:
                ((MainActivity) getActivity()).gotoOtherActivity(MyPostActivity.class);
                break;
            case R.id.tv_my_point:
                ((MainActivity) getActivity()).gotoOtherActivity(ScoreShopActivity.class);
                break;
            case R.id.tv_my_order:
                ((MainActivity) getActivity()).gotoOtherActivity(MyOrderHisActivity.class);
                break;
            case R.id.tv_my_collect:
                ((MainActivity) getActivity()).gotoOtherActivity(MyCollectionActivity.class);
                break;
            case R.id.tv_my_phone:
                showPhone();
                break;
            default:
                break;
        }
    }

    private void showPhone() {
        ((MainActivity) getActivity()).showdialog(Gravity.CENTER);
        ((MainActivity) getActivity()).alertDialog.getWindow().setContentView(R.layout.dialog_sure);
        TextView hint = (TextView) ((MainActivity) getActivity()).alertDialog.getWindow().findViewById(R.id.hint);
        TextView tv_sex = (TextView) ((MainActivity) getActivity()).alertDialog.getWindow().findViewById(
                R.id.tv_sex);
        TextView tv_yes = (TextView) ((MainActivity) getActivity()).alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) ((MainActivity) getActivity()).alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_sex.setText("400-6044-789");
        hint.setText("客服电话");
        tv_no.setText("拨打");
        tv_yes.setText("取消");
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006044789"));
                startActivity(intent);
                ((MainActivity) getActivity()).alertDialog.cancel();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).alertDialog.cancel();
            }
        });

    }

    //    private void doPicPath(){
//        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
//        sdv_pic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
////                            uploadPhoto(bitmap);
//        // 图像保存到文件中
//        file = ImageUtils.saveBitmap(bitmap, "header.jpg");
//        pic = file == null ? false : true;
////        if (!pic)
////            NewToast.show(this, "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
//    }
    private void showPhoto() {
        ((MainActivity) getActivity()).showdialog(Gravity.BOTTOM);
        final Dialog alertDialog = ((MainActivity) getActivity()).alertDialog;
        alertDialog.getWindow().setContentView(R.layout.dialog_photo);
        TextView tv_camera = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_camera);
        TextView tv_album = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_album);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCamera();
                alertDialog.cancel();
            }
        });
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAlbum();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_ALBUM:
                    if (data != null) {
                            startPhotoZoom(data.getData());
                    }

//                    if (imageUri != null) {
//                        bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
//                        sdv_pic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
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
//                        Log.e("ssss", mRecVedioPath.toString());
//                        head = mRecVedioPath;
//                        try {
//                            foutput = new FileOutputStream(mRecVedioPath);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, foutput);
//                            foutput.flush();
//                            foutput.close();
//                            Log.e("bmap", bitmap.toString());
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
//                    WebServiceAPI.getInstance().completeInfo(
//                            sp.getValue(Constant.SP_USERID),
//                            sp.getValue(Constant.SP_KEY_PWD),
//                            head,
//                            sp.getValue(Constant.SP_USERNAME),
//                            sp.getValue(Constant.SP_BIRTHDAY),
//                            sp.getValue(Constant.SP_PROVINCEID),
//                            sp.getValue(Constant.SP_CITYID),
//                            sp.getValue(Constant.SP_SIGNATURE),
//                            this, getActivity());
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
                            bitmap = data.getParcelableExtra("data");
                            sdv_pic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
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
                            Log.e("ssss", mRecVedioPath.toString());
                            head = mRecVedioPath;
                            try {
                                foutput = new FileOutputStream(mRecVedioPath);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, foutput);
                                foutput.flush();
                                foutput.close();
                                Log.e("bmap", bitmap.toString());
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
                    WebServiceAPI.getInstance().completeInfo(
                            sp.getValue(Constant.SP_USERID),
                            sp.getValue(Constant.SP_KEY_PWD),
                            head,
                            sp.getValue(Constant.SP_USERNAME),
                            sp.getValue(Constant.SP_BIRTHDAY),
                            sp.getValue(Constant.SP_PROVINCEID),
                            sp.getValue(Constant.SP_CITYID),
                            sp.getValue(Constant.SP_SIGNATURE),
                            this, getActivity());
                    break;
                default:
                    break;
            }
        }
    }

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
            NewToast.show(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG);
        }
    }

//    private Bitmap decodeUriAsBitmap(Uri uri) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return bitmap;
//    }

//    private void doPicPath() {
//        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
//        sdv_pic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
////                            uploadPhoto(bitmap);
//        // 图像保存到文件中
//        head = ImageUtils.saveBitmap(bitmap, "header.jpg");
//        pic = head == null ? false : true;
//        if (!pic)
//            NewToast.show(getActivity(), "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
//    }
//
//    @SuppressLint("NewApi")
//    public String doPhoto(Uri photoUri) {
//        String picPath = "";
//        if (DocumentsContract.isDocumentUri(getActivity(), photoUri)) {
//
//            String wholeID = DocumentsContract.getDocumentId(photoUri);
//
//            String id = wholeID.split(":")[1];
//
//            String[] column = {MediaStore.Images.Media.DATA};
//
//            String sel = MediaStore.Images.Media._ID + "=?";
//
//            Cursor cursor = getActivity().getContentResolver().query(
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
//
//    }
//
//    private String getPicPath(Uri photoUri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//
//        Cursor cursor = getActivity().getContentResolver().query(photoUri, projection, null, null,
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

    public void showdialog(int gravity) {
        alertDialog = new Dialog(getActivity(), R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(gravity);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void showBrithdayDialog(String birthday) {
        // TODO Auto-generated method stub
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String defaultDate = (TextUtils.isEmpty(birthday)) ?
                sDateFormat.format(new Date()) : birthday;
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

        int month2 = dp_user_bri.getMonth();
        int month3 = month2 + 1;
        tvConstellation.setText(date2Constellation(String.valueOf(month3), String.valueOf(dp_user_bri.getDayOfMonth())));
        alertDialog.dismiss();
    }

    public static String date2Constellation(String month, String day) {
        int monthTemp = Integer.parseInt(month) - 1;
        int dayTemp = Integer.parseInt(day);

        if (monthTemp == 9 && dayTemp == 23) {
            return "天秤座";
        }

        if (monthTemp == 3 && dayTemp == 20) {
            return "金牛座";
        }
        if (monthTemp == 10 && dayTemp == 22) {
            return "天蝎座";
        }
        if (dayTemp < Constant.constellationDay[monthTemp]) {
            monthTemp = monthTemp - 1;
        }
        if (monthTemp >= 0) {
            return Constant.constellationArr[monthTemp];
        }
        return Constant.constellationArr[11];
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1:
                    MainActivity.sdv_pic.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
                    NewToast.show(getActivity(), "头像修改成功", NewToast.LENGTH_LONG);
                    if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                        WebServiceAPI.getInstance().login(sp.getValue(Constant.SP_KEY_USER),
                                sp.getValue(Constant.SP_KEY_PWD),
                                this, getActivity());
                    }
                    break;
                case 2:
                    sp.putValue(Constant.SP_PHOTO, apiResponse.data.appuser.head);
                    appuser = apiResponse.data.appuser;
                    if (appuser.head.equals("")) {
                        if (appuser.sex.equals("0")) {
                            sdv_pic.setImageResource(R.mipmap.touxiangnan);
                        } else {
                            sdv_pic.setImageResource(R.mipmap.touxiangnv);
                        }
                    } else {
                        sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + appuser.head));
                        ImageLoader.getInstance().displayImage(Urls.PHOTO + appuser.head, iv_bg,
                                ImageUtils.getSpecialOptions(), new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                                        // TODO Auto-generated method stubk
                                        iv_bg.setImageBitmap(ImageUtils.BoxBlurFilter(arg2));
                                    }
                                });
                    }
                    tvName.setText(appuser.nickname);
                    tvLevel.setText("V" + appuser.level);
                    if (appuser.age == null) {
                        tvAge.setText(0 + "岁");
                    } else {
                        tvAge.setText(appuser.age + "岁");
                    }
                    tvSex.setText(appuser.sex.equals("0") ? "帅哥" : "美女");
                    if (appuser.birthday.equals("")) {
                        tvConstellation.setText("星座");
                    } else {
                        showBrithdayDialog(appuser.birthday);
                    }
                    if (appuser.sign.equals("")) {
                        tvSignature.setText("");
                    } else {
                        tvSignature.setText(appuser.sign);
                    }

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
