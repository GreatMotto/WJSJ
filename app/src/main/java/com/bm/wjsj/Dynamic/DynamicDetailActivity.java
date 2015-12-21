package com.bm.wjsj.Dynamic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.DynamicInfo;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.ReportBean;
import com.bm.wjsj.Circle.ImageGridAdapter;
import com.bm.wjsj.Circle.ReplyAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.AccusationActivity;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AdapterClickInterface;
import com.bm.wjsj.Utils.DateUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.Emoji.SelectFaceHelper;
import com.bm.wjsj.Utils.InputTools;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.NoScrollGridView;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.View.ViewPagerDialog;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态详情界面
 */
public class DynamicDetailActivity extends BaseActivity implements APICallback.OnResposeListener {
    private int x, y;
    private View layout_header, addFaceToolView;
    private ImageView iv_face, iv_image;
    private PopupWindow pop;
    private SimpleDraweeView sdv_pic;
    private NoScrollGridView gridView;
    private EditText et_review;
    private TextView tv_send, tv_review, tv_title_right, tv_name, tv_level, tv_sex, tv_age,
            tv_dynamic_distance, tv_dynamic_time, tv_dynamic_content, tv_love, tv_loved;
    private List<ImageBean> listImage = new ArrayList<ImageBean>();
    private List<ReportBean> listReport = new ArrayList<ReportBean>();
    private RefreshLayout rfl_lv;
    private ListView listview;
    private ReplyAdapter replyAdapter;
    private ImageGridAdapter gridAdapter;
    private DynamicInfo dynamicInfo = new DynamicInfo();
    private boolean isMine, isVisbilityFace;
    private SelectFaceHelper mFaceHelper;
    private String  userid = "", touserid = "", dynamicid = "", content = "", reportid = "", usid = "";
    private String dataUserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_postdetail);
        dynamicid = getIntent().getStringExtra(Constant.DYNAMICID);
        dataUserId=getIntent().getStringExtra(Constant.DATAUSERID);
        listImage = (List<ImageBean>) getIntent().getSerializableExtra(Constant.LIST);
        isMine = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        userid = WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID);
        initTitle("动态详情");
        assignViews();
        //initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        Log.e("eeeeeeeee","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) ev.getX();
                y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs((int) ev.getY() - y) <= Math.abs((int) ev.getX() - x)) {
                    View v = getCurrentFocus();
                    if (isShouldHideInput(v, ev)) {
                        addFaceToolView.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            et_review.setText("");
                            et_review.setHint("我也说一句...");
                            touserid = "";
                        }
                    }
                    x = 0;
                    y = 0;
                    return super.dispatchTouchEvent(ev);
                }
                x = 0;
                y = 0;
                break;

            default:
                break;
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private void assignViews() {
        addFaceToolView = (View) findViewById(R.id.add_tool);
        listview = (ListView) findViewById(R.id.listview);
        iv_face = (ImageView) findViewById(R.id.iv_face);
        et_review = (EditText) findViewById(R.id.et_review);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);

        layout_header = LayoutInflater.from(this).inflate(R.layout.header_dynamicdetail, null);
        listview.addHeaderView(layout_header, null, false);

        sdv_pic = (SimpleDraweeView) layout_header.findViewById(R.id.sdv_pic);
        gridView = (NoScrollGridView) layout_header.findViewById(R.id.gridview);
        tv_name = (TextView) layout_header.findViewById(R.id.tv_name);
        tv_level = (TextView) layout_header.findViewById(R.id.tv_level);
        tv_sex = (TextView) layout_header.findViewById(R.id.tv_sex);
        tv_age = (TextView) layout_header.findViewById(R.id.tv_age);
        tv_dynamic_distance = (TextView) layout_header.findViewById(R.id.tv_dynamic_distance);
        tv_dynamic_time = (TextView) layout_header.findViewById(R.id.tv_dynamic_time);
        tv_dynamic_content = (TextView) layout_header.findViewById(R.id.tv_dynamic_content);
        tv_review = (TextView) layout_header.findViewById(R.id.tv_review);
        tv_love = (TextView) layout_header.findViewById(R.id.tv_love);
        tv_loved = (TextView) layout_header.findViewById(R.id.tv_loved);
        iv_image = (ImageView) layout_header.findViewById(R.id.iv_image);


        tv_title_right.setVisibility(View.VISIBLE);
        if (dataUserId!=null&&dataUserId.equals(userid)) {
            tv_dynamic_distance.setVisibility(View.GONE);
            tv_title_right.setText("删除该动态");
        } else {
            tv_title_right.setText("举报");
        }
        et_review.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isVisbilityFace = false;
                addFaceToolView.setVisibility(View.GONE);
                iv_face.setImageResource(R.mipmap.icon_face);
                return false;
            }
        });
        tv_title_right.setOnClickListener(this);
        iv_face.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_love.setOnClickListener(this);
        tv_loved.setOnClickListener(this);
        sdv_pic.setOnClickListener(this);
        tv_review.setOnClickListener(this);

        if (listImage.size() == 1) {
            iv_image.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.PHOTO + listImage.get(0).path, iv_image, options);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPagerDialog dlg = new ViewPagerDialog(DynamicDetailActivity.this, listImage, 0);
                    dlg.showViewPagerDialog();
                }
            });
        } else {
            iv_image.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            gridAdapter = new ImageGridAdapter(this, listImage);
            gridView.setAdapter(gridAdapter);
        }
        replyAdapter = new ReplyAdapter(this, listReport, false,
                isMine, null, null, 0, clickInterface);
        listview.setAdapter(replyAdapter);
    }

    private void initData() {
        WebServiceAPI.getInstance().dynamicDetail(dynamicid, userid, DynamicDetailActivity.this, DynamicDetailActivity.this);
    }

    AdapterClickInterface clickInterface = new AdapterClickInterface() {
        @Override
        public void onClick(View v, String s, String id) {
            touserid = id;
            if (touserid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
                //ShowDeleteReportDlg("提示", "是否删除这条评论", false);
                et_review.setFocusable(true);
                et_review.setFocusableInTouchMode(true);
                et_review.requestFocus();
                et_review.setHint("回复" + s + ": ");
                InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_review, 0);
            } else {
                et_review.setFocusable(true);
                et_review.setFocusableInTouchMode(true);
                et_review.requestFocus();
                et_review.setHint("回复" + s + ": ");
                InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_review, 0);
            }
//            NewToast.show(DynamicDetailActivity.this, "position = " + position, Toast.LENGTH_LONG);
        }

        @Override
        public void getReport(View v, String id) {
            reportid = id;
        }

        @Override
        public void onLongClick(View v, String id,String reportid,String message) {
            touserid = id;
            if (touserid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
                ShowCopyDelDlg(v, message,reportid, true);
            }
            else{
                ShowCopyDelDlg(v, message,reportid, false);
            }
//            if (isMine) {
//                if (!id.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
//                    //ShowDeleteReportDlg("提示", "是否删除这条评论", false);
//                    ShowCopyDelDlg("test",false);
//                }
//            }
        }
    };

    private void ShowDeleteReportDlg(String title, String toast, final boolean isClose) {
        showDialog();
        alertDialog.getWindow().setContentView(R.layout.toast_have_cancle);
        TextView tv_toast_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_toast_title);
        TextView tv_content = (TextView) alertDialog.getWindow().findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_content.setText(toast);
        tv_toast_title.setText(title);
        tv_sure.setText("删除");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                WebServiceAPI.getInstance().deleteDynamicReport(reportid, DynamicDetailActivity.this, DynamicDetailActivity.this);
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                touserid = "";
                if (isClose) {
                    DynamicDetailActivity.this.finish();
                }
            }
        });

    }

    private void ShowCopyDelDlg(final View v, final String text,final String deleteID,final boolean showDel) {
        LayoutInflater mLayoutInflater = (LayoutInflater) DynamicDetailActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.dlg_long_press_del, null);
        // R.layout.pop为PopupWindow 的布局文件
        final   PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 指定 PopupWindow 的背景

        pop.setFocusable(true);

        pop.setOutsideTouchable(true);
        //指定PopupWindow显示在你指定的view下
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        pop.showAtLocation(v, Gravity.TOP, location[0], location[1]-60);

        //showDialog();
        //alertDialog.getWindow().setContentView(R.layout.dlg_long_press_del);
        TextView tv_copy = (TextView) contentView.findViewById(R.id.tv_dal_copy);
        TextView tv_del = (TextView) contentView.findViewById(R.id.tv_dlg_del);
        if(showDel){
            tv_del.setVisibility(View.VISIBLE);
        }
        else{
            tv_del.setVisibility(View.GONE);
        }


        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) DynamicDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(text); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                //cm.getText();//获取粘贴信息
         pop.dismiss();
            }
        });

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebServiceAPI.getInstance().deleteDynamicReport(deleteID, DynamicDetailActivity.this, DynamicDetailActivity.this);
                pop.dismiss();
            }
        });

    }

    private void showDialog() {
        alertDialog = new Dialog(this, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = this.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }


    private boolean checkUserAuthority() {
        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            gotoLoginAc(this, 333);
            return false;
        } else {
            if (checkStatus(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (checkUserAuthority()) {
                    if (dataUserId!=null&&dataUserId.equals(userid)) {
                        WebServiceAPI.getInstance().deleteDynamic(dynamicid, DynamicDetailActivity.this, DynamicDetailActivity.this);
                    } else {
                        Intent intent = new Intent(this, AccusationActivity.class);
                        intent.putExtra(Constant.ID, dynamicid);
                        intent.putExtra("objecttype", "3");
                        startActivity(intent);
                    }
                }

                break;
            case R.id.iv_face:
                if (null == mFaceHelper) {
                    mFaceHelper = new SelectFaceHelper(this, addFaceToolView);
                    mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
                }
                if (isVisbilityFace) {
                    isVisbilityFace = false;
                    addFaceToolView.setVisibility(View.GONE);
                    InputTools.toggleKeyboard(view);
                    iv_face.setImageResource(R.mipmap.icon_face);
                } else {
                    isVisbilityFace = true;
                    InputTools.HideKeyboard(view);
                    addFaceToolView.setVisibility(View.VISIBLE);
                    iv_face.setImageResource(R.mipmap.icon_keyboard);
                }
                break;
            case R.id.tv_send:
                if (checkUserAuthority()) {
                    if (TextUtils.isEmpty(et_review.getText().toString().trim())) {
                        NewToast.show(DynamicDetailActivity.this, "请输入评论！", Toast.LENGTH_LONG);
                    } else {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        DialogUtils.showProgressDialog("", this);
                        content = et_review.getText().toString();
                        WebServiceAPI.getInstance().commentDynamic(dynamicid, touserid, content, DynamicDetailActivity.this, DynamicDetailActivity.this);
                    }
                }
                break;
            case R.id.tv_love:
                if (checkUserAuthority()) {
                    WebServiceAPI.getInstance().addPraise(dynamicid, "2", DynamicDetailActivity.this, DynamicDetailActivity.this);
                }
                break;
            case R.id.tv_loved:
                if (checkUserAuthority()) {
                    WebServiceAPI.getInstance().deletePraise(dynamicid, "2", DynamicDetailActivity.this, DynamicDetailActivity.this);
                }
                break;
            case R.id.sdv_pic:
                gotoActivity(MyDataActivity.class, usid);
                break;
            case R.id.tv_review://评论(弹出虚拟键盘)
                et_review.setFocusable(true);
                et_review.setFocusableInTouchMode(true);
                et_review.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_review, 0);
                break;
            default:
                break;
        }
    }

    SelectFaceHelper.OnFaceOprateListener mOnFaceOprateListener = new SelectFaceHelper.OnFaceOprateListener() {
        @Override
        public void onFaceSelected(SpannableString spanEmojiStr) {
            if (null != spanEmojiStr) {
                et_review.append(spanEmojiStr);
            }
        }

        @Override
        public void onFaceDeleted() {
            int selection = et_review.getSelectionStart();
            String text = et_review.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    et_review.getText().delete(start, end);
                    return;
                }
                et_review.getText().delete(selection - 1, selection);
            }
        }

    };

    @Override
    public void onBackPressed() {
        if (isVisbilityFace) {
            isVisbilityFace = false;
            addFaceToolView.setVisibility(View.GONE);
            iv_face.setImageResource(R.mipmap.icon_face);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1:
                    dynamicInfo = null;
                    dynamicInfo = apiResponse.data.dynamicinfo;
                    usid = dynamicInfo.userid;
                    if (dynamicInfo.head.equals("")) {
                        if (dynamicInfo.sex.equals("0")) {
                            sdv_pic.setImageResource(R.mipmap.touxiangnan);
                        } else {
                            sdv_pic.setImageResource(R.mipmap.touxiangnv);
                        }
                    } else {
                        sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + dynamicInfo.head));
                    }
//                    sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + dynamicInfo.head));
                    if (dynamicInfo.praise.equals("0")) {
                        tv_loved.setVisibility(View.GONE);
                        tv_love.setVisibility(View.VISIBLE);
                        tv_love.setText(dynamicInfo.praisenum);
                    } else {
                        tv_love.setVisibility(View.GONE);
                        tv_loved.setVisibility(View.VISIBLE);
                        tv_loved.setText(dynamicInfo.praisenum);
                    }
                    tv_review.setText(dynamicInfo.comnum);
                    tv_name.setText(dynamicInfo.nickname);
                    if (dynamicInfo.sex.equals("0")) {
                        tv_sex.setText("帅哥");
                    } else {
                        tv_sex.setText("美女");
                    }
                    tv_age.setText(dynamicInfo.age + "岁");
                    tv_level.setText("V" + dynamicInfo.level);
                    tv_dynamic_time.setText(DateUtil.getDate(dynamicInfo.createTime));
                    tv_dynamic_content.setText(dynamicInfo.content);

                    if (!TextUtils.isEmpty(dynamicInfo.juli)) {
                        double distance = Double.parseDouble(dynamicInfo.juli);
                        if (distance >= 1000.0) {
                            distance = distance / 1000.0;
                            BigDecimal bigDecimal = new BigDecimal(distance);
                            int julis = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            tv_dynamic_distance.setText(julis + "km");
                        } else if (distance < 1000.00 && distance > 10.00) {
                            distance = distance / 1000.0;
                            BigDecimal bigDecimal = new BigDecimal(distance);
                            double julis = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                            tv_dynamic_distance.setText(julis + "km");
                        } else {
                            tv_dynamic_distance.setText(0.01 + "km");
                        }
                    }
//                    listImage.clear();
                    listReport.clear();
//                    listImage.addAll(dynamicInfo.imglist);
//                    gridAdapter.notifyDataSetInvalidated();
                    listReport.addAll(dynamicInfo.report);
                    replyAdapter.notifyDataSetInvalidated();
                    break;
                case 2:
                    DialogUtils.cancleProgressDialog();
                    et_review.setText("");
                    et_review.setHint("我也说一句...");
                    touserid = "";
                    NewToast.show(DynamicDetailActivity.this, "评论成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().dynamicDetail(dynamicid, userid, DynamicDetailActivity.this, DynamicDetailActivity.this);
                    break;
                case 3:
                    NewToast.show(DynamicDetailActivity.this, "点赞成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().dynamicDetail(dynamicid, userid, DynamicDetailActivity.this, DynamicDetailActivity.this);
                    break;
                case 4:
                    NewToast.show(DynamicDetailActivity.this, "点赞已取消", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().dynamicDetail(dynamicid, userid, DynamicDetailActivity.this, DynamicDetailActivity.this);
                    break;
                case 5:
                    NewToast.show(DynamicDetailActivity.this, "动态已删除", Toast.LENGTH_LONG);
                    onBackPressed();
                    finish();
                    break;
                case 6:
                    touserid = "";
                    NewToast.show(DynamicDetailActivity.this, "评论已删除", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().dynamicDetail(dynamicid, userid, DynamicDetailActivity.this, DynamicDetailActivity.this);
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
