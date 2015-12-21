package com.bm.wjsj.Circle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.PostDetailInfo;
import com.bm.wjsj.Bean.ReviewBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.AccusationActivity;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DateUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.Emoji.SelectFaceHelper;
import com.bm.wjsj.Utils.InputTools;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.NoScrollGridView;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情(话题)界面
 */
public class PostDetailActivity extends BaseActivity implements APICallback.OnResposeListener {
    private int x, y;
    private ListView listview;
    private View layout_header, addFaceToolView;
    private ImageView iv_more, iv_face;
    private PopupWindow pop;
    private SimpleDraweeView sdv_pic;
    private NoScrollGridView gridView;
    private EditText et_review;
    private TextView tv_send, tv_title_right, tv_post_content, tv_name, tv_level, tv_sex,
            tv_age, tv_post_time, tv_post_title, tv_love, tv_loved, tv_review;
    private ReviewAdapter listAdapter;
    private List<ImageBean> listImage = new ArrayList<ImageBean>();
    private List<ReviewBean> listReport = new ArrayList<ReviewBean>();
    private PostDetailInfo postDetailInfo = new PostDetailInfo();
    private ImageGridAdapter gridAdapter;
    private SelectFaceHelper mFaceHelper;
    private boolean isMine, isVisbilityFace;
    private String postid = "", order = "1",// 1：最新评论；2：倒序排序；3最热排序
            content = "", touserid = "", id = "";

    public static boolean notifyFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_postdetail);
        isMine = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        postid = getIntent().getStringExtra(Constant.POSTID);
        id = getIntent().getStringExtra(Constant.ID);
        initTitle(getResources().getString(R.string.topic));
        assignViews();
        initData();
//        if (listAdapter.pldata != -1){
//            listReport.remove(listAdapter.pldata);
//            listAdapter.notifyDataSetInvalidated();
//        }
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

        layout_header = LayoutInflater.from(this).inflate(R.layout.header_postdetail, null);
        listview.addHeaderView(layout_header, null, false);

        sdv_pic = (SimpleDraweeView) layout_header.findViewById(R.id.sdv_pic);
        tv_name = (TextView) layout_header.findViewById(R.id.tv_name);
        tv_level = (TextView) layout_header.findViewById(R.id.tv_level);
        tv_sex = (TextView) layout_header.findViewById(R.id.tv_sex);
        tv_age = (TextView) layout_header.findViewById(R.id.tv_age);
        tv_post_time = (TextView) layout_header.findViewById(R.id.tv_post_time);
        tv_post_title = (TextView) layout_header.findViewById(R.id.tv_post_title);
        tv_post_content = (TextView) layout_header.findViewById(R.id.tv_post_content);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_love = (TextView) layout_header.findViewById(R.id.tv_love);
        tv_loved = (TextView) layout_header.findViewById(R.id.tv_loved);
        tv_review = (TextView) layout_header.findViewById(R.id.tv_review);
        gridView = (NoScrollGridView) layout_header.findViewById(R.id.gridview);

        if (isMine) {
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setText("删除该帖子");
        } else {
            iv_more = (ImageView) findViewById(R.id.iv_more);
            iv_more.setVisibility(View.VISIBLE);
            iv_more.setOnClickListener(this);
//            tv_title_right.setText("举报");
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
        sdv_pic.setOnClickListener(this);
        tv_love.setOnClickListener(this);
        tv_loved.setOnClickListener(this);
        tv_review.setOnClickListener(this);

        gridAdapter = new ImageGridAdapter(this, listImage);
        gridView.setAdapter(gridAdapter);
//        listAdapter = new ReviewAdapter(this, listReport, isMine, postid);
//        listview.setAdapter(listAdapter);

    }

    private void initData() {
        WebServiceAPI.getInstance().postDetail(postid, PostDetailActivity.this, PostDetailActivity.this);
//        tv_post_content.setText(getEmojiText(this, "[e]1f631[/e]烤肉不错哦"));
//        WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
    }

    public void onCallBack() {
        WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
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
    protected void onResume() {
        super.onResume();
        WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (isMine) {
                    WebServiceAPI.getInstance().deleteMyPost(postid, PostDetailActivity.this, PostDetailActivity.this);
                } else {
                    Intent intent = new Intent(this, AccusationActivity.class);
                    intent.putExtra(Constant.POSTID, postid);
                    intent.putExtra("objecttype", "2");
                    startActivity(intent);
                }
//                showSure();
                break;
            case R.id.iv_more:
                showPopWindow(view);
                break;
            case R.id.tv_flashback:
                order = "2";
                WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
                pop.dismiss();
                break;
            case R.id.tv_hotreview:
                order = "3";
                WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
                pop.dismiss();
                break;
            case R.id.tv_report:
                if (checkUserAuthority()) {
                    pop.dismiss();
                    Intent intent = new Intent(this, AccusationActivity.class);
                    intent.putExtra(Constant.ID, postid);
                    intent.putExtra("objecttype", "2");
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
                if (checkUserAuthority()) {
                    if (TextUtils.isEmpty(et_review.getText().toString().trim())) {
                        NewToast.show(PostDetailActivity.this, "请输入评论！", Toast.LENGTH_LONG);
                    } else {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        DialogUtils.showProgressDialog("", this);
                        content = et_review.getText().toString();
                        WebServiceAPI.getInstance().reviewPost(content, postid, "0", touserid, PostDetailActivity.this, PostDetailActivity.this);
                    }
                }
                break;
            case R.id.tv_love:
                if (checkUserAuthority()) {
                    WebServiceAPI.getInstance().addPraise(postid, "0", PostDetailActivity.this, PostDetailActivity.this);
                }
                break;
            case R.id.tv_loved:
                if (checkUserAuthority()) {
                    WebServiceAPI.getInstance().deletePraise(postid, "0", PostDetailActivity.this, PostDetailActivity.this);
                }
                break;
            case R.id.tv_review://评论(弹出虚拟键盘)
                et_review.setFocusable(true);
                et_review.setFocusableInTouchMode(true);
                et_review.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) et_review.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_review, 0);
                break;
            case R.id.sdv_pic:
                gotoActivity(MyDataActivity.class, postDetailInfo.userid);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        notifyFlag = true;
        if (isVisbilityFace) {
            isVisbilityFace = false;
            addFaceToolView.setVisibility(View.GONE);
            iv_face.setImageResource(R.mipmap.icon_face);
        } else {
            super.onBackPressed();
        }
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_delete);
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                PostDetailActivity.this.onBackPressed();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    private void showPopWindow(View v) {
        View popv1 = LayoutInflater.from(this).inflate(R.layout.pop_more, null);
        if (pop == null) {
            pop = new PopupWindow(popv1, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.update();
        pop.showAsDropDown(v, -v.getWidth() / 2, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop = null;
            }
        });
        TextView tv_flashback = (TextView) popv1.findViewById(R.id.tv_flashback);
        TextView tv_hotreview = (TextView) popv1.findViewById(R.id.tv_hotreview);
        TextView tv_report = (TextView) popv1.findViewById(R.id.tv_report);
        tv_flashback.setOnClickListener(this);
        tv_hotreview.setOnClickListener(this);
        tv_report.setOnClickListener(this);
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
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1:
                    postDetailInfo = null;
                    postDetailInfo = apiResponse.data.postinfo;
//                    postid = postDetailInfo.id;
                    if (postDetailInfo.head.equals("")) {
                        if (postDetailInfo.sex.equals("0")) {
                            sdv_pic.setImageResource(R.mipmap.touxiangnan);
                        }else {
                            sdv_pic.setImageResource(R.mipmap.touxiangnv);
                        }
                    } else{
                        sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + postDetailInfo.head));
                    }
                    if (postDetailInfo.praise.equals("0")) {
                        tv_loved.setVisibility(View.GONE);
                        tv_love.setVisibility(View.VISIBLE);
                        tv_love.setText(postDetailInfo.praisenum);
                    } else {
                        tv_love.setVisibility(View.GONE);
                        tv_loved.setVisibility(View.VISIBLE);
                        tv_loved.setText(postDetailInfo.praisenum);
                    }
                    tv_review.setText(postDetailInfo.comnum);
                    tv_name.setText(postDetailInfo.nickname);
                    if (postDetailInfo.sex.equals("0")) {
                        tv_sex.setText("帅哥");
                    } else {
                        tv_sex.setText("美女");
                    }
                    if (postDetailInfo.age == null){
                        tv_age.setText(0 + "岁");
                    }else {
                        tv_age.setText(postDetailInfo.age + "岁");
                    }
                    tv_level.setText("V" + postDetailInfo.level);
                    tv_post_time.setText(DateUtil.getDate(postDetailInfo.createTime));
                    tv_post_title.setText(postDetailInfo.title);
                    tv_post_content.setText(postDetailInfo.content);
                    listImage.clear();
                    listImage.addAll(postDetailInfo.imglist);
                    gridAdapter.notifyDataSetInvalidated();
                    break;
                case 2:
                    if (listReport.size() != 0) {
                        listReport.clear();
                    }
                    listReport.addAll(apiResponse.data.result);
                    listAdapter = new ReviewAdapter(this, listReport, isMine, postid, id, listview);
                    listview.setAdapter(listAdapter);
                    listAdapter.notifyDataSetInvalidated();
                    break;
                case 3:
                    NewToast.show(PostDetailActivity.this, "点赞成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().postDetail(postid, PostDetailActivity.this, PostDetailActivity.this);
                    break;
                case 4:
                    NewToast.show(PostDetailActivity.this, "点赞已取消", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().postDetail(postid, PostDetailActivity.this, PostDetailActivity.this);
                    break;
                case 5:
//                    alertDialog.cancel();
                    NewToast.show(PostDetailActivity.this, "帖子已删除", Toast.LENGTH_LONG);
                    onBackPressed();
                    break;
                case 6:
                    DialogUtils.cancleProgressDialog();
                    et_review.setText("");
                    et_review.setHint("我也说一句...");
                    touserid = "";
                    NewToast.show(PostDetailActivity.this, "评论成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().postReviewList(postid, order, PostDetailActivity.this, PostDetailActivity.this);
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
