package com.bm.wjsj.Circle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.ReportBean;
import com.bm.wjsj.Bean.ReviewBean;
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
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论详情界面
 */
public class ReviewDetailActivity extends BaseActivity implements APICallback.OnResposeListener {
    private int x, y;
    private View layout_header, addFaceToolView;
    private ImageView iv_face;
    private PopupWindow pop;
    private SimpleDraweeView sdv_pic;
    private NoScrollGridView gridView;
    private EditText et_review;
    private TextView tv_send, tv_review, tv_title_right, tv_name, tv_level, tv_sex, tv_age,
            tv_dynamic_distance, tv_dynamic_time, tv_dynamic_content, tv_love, tv_loved;
    private List<ImageBean> listImage = new ArrayList<ImageBean>();
    private List<ReviewBean> listReview = new ArrayList<ReviewBean>();
    private List<ReportBean> listReport = new ArrayList<ReportBean>();
    private RefreshLayout rfl_lv;
    private ListView listview;
    private ReplyAdapter replyAdapter;
    private ImageGridAdapter gridAdapter;
    private boolean isMine, isVisbilityFace;
    private SelectFaceHelper mFaceHelper;
    private int position = 0;
    private String postid = "", order = "1",id = "";
    private String reviewid = "", touserid = "", content = "", reportid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_postdetail);
        position = getIntent().getIntExtra("position", 0);
        postid = getIntent().getStringExtra(Constant.POSTID);
        id = getIntent().getStringExtra(Constant.ID);
        Log.e("onCreate id",id);
        isMine = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        initTitle("评论详情");
        assignViews();
        initData();
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

        layout_header = LayoutInflater.from(this).inflate(R.layout.header_dynamicdetail_ht, null);
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

        tv_title_right.setVisibility(View.VISIBLE);
        if (isMine) {
            tv_dynamic_distance.setVisibility(View.GONE);
            tv_title_right.setText("删除评论");
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

        gridAdapter = new ImageGridAdapter(this, listImage);
        gridView.setAdapter(gridAdapter);
        replyAdapter = new ReplyAdapter(this, listReport, false,
                isMine, null, null, 0, clickInterface);
        listview.setAdapter(replyAdapter);
    }

    private void initData() {
        WebServiceAPI.getInstance().reviewDetail(postid, order, ReviewDetailActivity.this, ReviewDetailActivity.this);
    }

    AdapterClickInterface clickInterface = new AdapterClickInterface() {
        @Override
        public void onClick(View v, String s, String id) {
            et_review.setFocusable(true);
            et_review.setFocusableInTouchMode(true);
            et_review.requestFocus();
            et_review.setHint("回复" + s + ": ");
            InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(et_review, 0);
            //touserid = id;
//            if (touserid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
//                ShowDeleteReportDlg("提示", "是否删除这条评论", false);
//            } else {
//                et_review.setFocusable(true);
//                et_review.setFocusableInTouchMode(true);
//                et_review.requestFocus();
//                et_review.setHint("回复" + s + ": ");
//                InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(et_review, 0);
//            }

        }

        @Override
        public void getReport(View v, String id) {
            reportid = id;
        }

        @Override
        public void onLongClick(View v, String userid,String reportid,String message) {
            if (isMine) {
                //ShowDeleteReportDlg("提示", "是否删除这条评论", false,reportid);
                Log.e("onLongClick:","----------------------------------------1");
                if (!userid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
                    //ShowDeleteReportDlg("提示", "是否删除这条评论", false,reportid);
                    Log.e("onLongClick:","----------------------------------------2");
                    ShowCopyDelDlg(v,message,reportid,false);
                }
            }
            else{
                Log.e("onLongClick:","----------------------------------------3");
                if (userid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
                    //ShowDeleteReportDlg("提示", "是否删除这条评论", false,reportid);
                    Log.e("onLongClick:","----------------------------------------4");
                    ShowCopyDelDlg(v,message,reportid,true);
                }
                else{
                    Log.e("onLongClick:","----------------------------------------5");
                    ShowCopyDelDlg(v,message,reportid,false);
                }
            }
        }
    };


    private void ShowCopyDelDlg(final View v, final String text,final String deleteID,final boolean showDel) {
        LayoutInflater mLayoutInflater = (LayoutInflater) ReviewDetailActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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
                ClipboardManager cmb = (ClipboardManager) ReviewDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(text); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                //cm.getText();//获取粘贴信息
                pop.dismiss();
            }
        });

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebServiceAPI.getInstance().deleteReportDetailReprot(deleteID, ReviewDetailActivity.this, ReviewDetailActivity.this);
                pop.dismiss();
            }
        });

    }

    private void ShowDeleteReportDlg(String title, String toast, final boolean isClose, final String childReportID) {
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
                if(TextUtils.isEmpty(childReportID))
                {
                    WebServiceAPI.getInstance().deleteReportDetailReprot(reportid, ReviewDetailActivity.this, ReviewDetailActivity.this);
                }
                else{
                    WebServiceAPI.getInstance().deleteReportDetailReprot(childReportID, ReviewDetailActivity.this, ReviewDetailActivity.this);
                }

            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                touserid = "";
                if (isClose) {
                    ReviewDetailActivity.this.finish();
                }
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


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (isMine) {
                    WebServiceAPI.getInstance().deleteMyPostReview(reviewid, ReviewDetailActivity.this, ReviewDetailActivity.this);
                } else {
                    Intent intent = new Intent(this, AccusationActivity.class);
                    intent.putExtra(Constant.ID, reviewid);
                    intent.putExtra("objecttype", "1");
                    startActivity(intent);
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
                    addFaceToolView.setVisibility(View.VISIBLE);
                    InputTools.HideKeyboard(view);
                    iv_face.setImageResource(R.mipmap.icon_keyboard);
                }
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(et_review.getText().toString().trim())) {
                    NewToast.show(ReviewDetailActivity.this, "请输入评论！", Toast.LENGTH_LONG);
                } else {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    DialogUtils.showProgressDialog("", this);
                    content = et_review.getText().toString();
                    WebServiceAPI.getInstance().reviewPost(content, reviewid, "1", touserid, ReviewDetailActivity.this, ReviewDetailActivity.this);                }
                break;
            case R.id.tv_love:
                WebServiceAPI.getInstance().addPraise(reviewid, "1", ReviewDetailActivity.this, ReviewDetailActivity.this);
                break;
            case R.id.tv_loved:
                WebServiceAPI.getInstance().deletePraise(reviewid, "1", ReviewDetailActivity.this, ReviewDetailActivity.this);
                break;
            case R.id.sdv_pic:
                Log.e("id",id);
                gotoActivity(MyDataActivity.class, id);
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
                    listReview.clear();
                    listReview.addAll(apiResponse.data.result);
                    reviewid = listReview.get(position).id;
                    WebServiceAPI.getInstance().reviewDetailList(reviewid, ReviewDetailActivity.this, ReviewDetailActivity.this);
                    if (listReview.get(position).head.equals("")) {
                        if (listReview.get(position).sex.equals("0")) {
                            sdv_pic.setImageResource(R.mipmap.touxiangnan);
                        } else {
                            sdv_pic.setImageResource(R.mipmap.touxiangnv);
                        }
                    } else {
                        sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + listReview.get(position).head));
                    }
                    if (listReview.get(position).praise.equals("0")) {
                        tv_loved.setVisibility(View.GONE);
                        tv_love.setVisibility(View.VISIBLE);
                        tv_love.setText(listReview.get(position).praisenum);
                    } else {
                        tv_love.setVisibility(View.GONE);
                        tv_loved.setVisibility(View.VISIBLE);
                        tv_loved.setText(listReview.get(position).praisenum);
                    }
//                    tv_review.setText(listReview.get(position).comnum);
                    tv_name.setText(listReview.get(position).nickname);
                    if (listReview.get(position).sex.equals("0")) {
                        tv_sex.setText("帅哥");
                    } else {
                        tv_sex.setText("美女");
                    }
                    if (TextUtils.isEmpty(listReview.get(position).age)){
                        tv_age.setText(0 + "岁");
                    }else {
                        tv_age.setText(listReview.get(position).age + "岁");
                    }
                    tv_level.setText("V" + listReview.get(position).level);
                    tv_dynamic_time.setText(DateUtil.getDate(listReview.get(position).createTime));
                    //表情图
                    BaseActivity ba = new BaseActivity();
                    tv_dynamic_content.setText(ba.getEmojiText(this, listReview.get(position).content));
//                    listImage.clear();
//                    listImage.addAll(listReview.get(position).imglist);
//                    gridAdapter.notifyDataSetInvalidated();
                    break;
                case 7:
                    listReport.clear();
                    listReport.addAll(apiResponse.data.list);
                    replyAdapter.notifyDataSetInvalidated();
                    Log.e("ReviewDetailActivity", String.valueOf(listReport.size()));
                    break;
                case 6:
                    DialogUtils.cancleProgressDialog();
                    et_review.setText("");
                    et_review.setHint("我也说一句...");
                    touserid = "";
                    NewToast.show(ReviewDetailActivity.this, "评论成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().reviewDetailList(reviewid, ReviewDetailActivity.this, ReviewDetailActivity.this);
                    break;
                case 3:
                    NewToast.show(ReviewDetailActivity.this, "点赞成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().reviewDetail(postid, order, ReviewDetailActivity.this, ReviewDetailActivity.this);
                    break;
                case 4:
                    NewToast.show(ReviewDetailActivity.this, "点赞已取消", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().reviewDetail(postid, order, ReviewDetailActivity.this, ReviewDetailActivity.this);
                    break;
                case 8:// 主评论
                    NewToast.show(ReviewDetailActivity.this, "评论已删除", Toast.LENGTH_LONG);
                    onBackPressed();
                    break;
                case 66:// 子评论
                    touserid = "";
                    NewToast.show(ReviewDetailActivity.this, "评论已删除", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().reviewDetailList(reviewid, ReviewDetailActivity.this, ReviewDetailActivity.this);
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
