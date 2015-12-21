package com.bm.wjsj.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Circle.CircleDetailActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.ImageUtils;
import com.bm.wjsj.Utils.NewToast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发布新动态/发帖
 */
public class UploadActivity extends BaseActivity implements APICallback.OnResposeListener {

    private GridView gridview;
    private GridAdapter adapter;
    private TextView tv_upload, tv_numbers;
    private EditText et_post_title, et_post_content;

    private static final int RESULT_CAMERA = 1;// 相机
    private static final int RESULT_ALBUM = 0; // 相册
    private static final int CROP_CAMERA = 2;// 相机截取后返回
    private int maxLength = 500;
    private boolean isDynamic;
    private List<String> listpath;
    private List<File> files = new ArrayList<File>();
    ;
    private Uri imageUri;
    private String pic_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDynamic = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        setContentView(R.layout.ac_uploadlayout);
        initTitle(isDynamic ? "发布新动态" : "发帖");
        initView();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        et_post_title = (EditText) findViewById(R.id.et_post_title);
        et_post_content = (EditText) findViewById(R.id.et_post_content);
        tv_upload = (TextView) findViewById(R.id.tv_title_right);
        tv_upload.setVisibility(View.VISIBLE);
        tv_upload.setText(isDynamic ? "发布" : "提交");
        tv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDynamic) {
                    if (TextUtils.isEmpty(et_post_content.getText().toString().trim())) {
                        onBackPressed();
                        return;
                    } else {
                        showSure();
                    }
                } else {
                    if (TextUtils.isEmpty(et_post_title.getText().toString().trim()) && TextUtils.isEmpty(et_post_content.getText().toString().trim())) {
                        onBackPressed();
                    } else {
                        showSure();
                    }
                }
            }
        });
        tv_upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDynamic) {
                    if (TextUtils.isEmpty(et_post_content.getText().toString().trim())) {
                        NewToast.show(UploadActivity.this, "请输入动态内容", Toast.LENGTH_LONG);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(et_post_content);
                    } else {
                        DialogUtils.showProgressDialog("", UploadActivity.this);
                        getPictures();
                        WebServiceAPI.getInstance().dynamicPublish(et_post_content.getText().toString(), files, UploadActivity.this, UploadActivity.this);
                    }
                } else {
                    if (TextUtils.isEmpty(et_post_title.getText().toString().trim())) {
                        NewToast.show(UploadActivity.this, "请输入帖子标题", Toast.LENGTH_LONG);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(et_post_title);
                    } else if (TextUtils.isEmpty(et_post_content.getText().toString().trim())) {
                        NewToast.show(UploadActivity.this, "请输入帖子内容", Toast.LENGTH_LONG);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(et_post_content);
                    } else {
                        DialogUtils.showProgressDialog("", UploadActivity.this);
                        getPictures();
                        WebServiceAPI.getInstance().postPublish(CircleDetailActivity.circleIdFlag,
                                et_post_title.getText().toString(),
                                et_post_content.getText().toString(), files, UploadActivity.this, UploadActivity.this);
                    }
                }
            }
        });

        if (isDynamic) {
            et_post_title.setVisibility(View.GONE);
            maxLength = 140;
        }
        tv_numbers = (TextView) findViewById(R.id.tv_numbers);
        tv_numbers.setText("(0/" + maxLength + ")");
        et_post_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        et_post_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                tv_numbers.setText("(" + text.length() + "/" + maxLength + ")");
            }
        });
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new GridAdapter(this);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Bimp.bmp.size()) {
                    showPhoto();
                } else {
                    String newStr = Bimp.drr.get(position).substring(
                            Bimp.drr.get(position).lastIndexOf("/") + 1,
                            Bimp.drr.get(position).lastIndexOf("."));
                    Bimp.bmp.remove(position);
                    Bimp.drr.remove(position);
                    Bimp.max = Bimp.drr.size();
                    FileUtils.delFile(newStr + ".JPEG");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_sure);
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        TextView hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.hint);
        hint.setText("是否退出此次编辑？");
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                onBackPressed();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    // 获取图片路径
    public void getPicturesPath() {
        // 高清的压缩图片全部就在  list 路径里面了
        // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
        listpath = new ArrayList<String>();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            String Str = Bimp.drr.get(i).substring(
                    Bimp.drr.get(i).lastIndexOf("/") + 1,
                    Bimp.drr.get(i).lastIndexOf("."));
            listpath.add(FileUtils.SDPATH + Str + ".JPEG");
        }
    }

    // 获取图片文件
    public void getPictures() {
        getPicturesPath();
        files.clear();
        for (int i = 0; i < listpath.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(listpath.get(i));
            File file = ImageUtils.saveBitmap(bitmap, "image" + i + ".jpg");
            if (file != null) {
                files.add(file);
            } else {
                NewToast.show(this, "图像保存失败，请检查SD卡是否连接正常", Toast.LENGTH_LONG);
            }
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteDir();
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private void showSuccess() {
        DialogUtils.cancleProgressDialog();
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_success);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sure);
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText(isDynamic ? "发布成功" : "发贴成功");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                UploadActivity.this.onBackPressed();
            }
        });
    }

    // 弹出对话框,提示选择拍照上传或者从相册上传
    private void showPhoto() {
        showdialog(Gravity.BOTTOM);
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
                takePhoto();// 拍照上传
                alertDialog.cancel();
            }
        });
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this,
                        AlbumFileActivity.class);
                startActivity(intent);// 相册上传
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

    /**
     * 调用相机拍照
     */
    public void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStorageDirectory();

            File file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                    + ".jpg");
            pic_path = file.getPath();
            imageUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, RESULT_CAMERA);
        } else {
            NewToast.show(this, "请确认已经插入SD卡", Toast.LENGTH_LONG);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CAMERA:
                if (Bimp.drr.size() < 9 && imageUri != null) {
                    Bimp.drr.add(pic_path);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_upload_grid,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.iv_delete = (ImageView) convertView
                        .findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                holder.iv_delete.setVisibility(View.GONE);
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
                holder.iv_delete.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image, iv_delete;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    showSuccess();
                    break;
                case 2:
                    showSuccess();
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
