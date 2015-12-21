package com.bm.wjsj.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.MyDynamicListBean;
import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Circle.PostAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.Personal.MyDynamicAdapter;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.ImageUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.ViewPagerDialog;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 查看个人信息
 * Created by Administrator on 2015/7/28 0028.
 */
public class MyDataActivity extends BaseActivity implements APICallback.OnResposeListener {
    private FrameLayout viewData;
    private ListView lvData;
    private MyDynamicAdapter adapter;
    private ImageView iv_bg, iv_dt, iv_tz, iv_dt_left, iv_tz_left;
    private PostAdapter tzAdapter;
    private List<PostBean> list = new ArrayList<>();
    private List<MyDynamicListBean> list_my = new ArrayList<>();
    private List<ImageBean> touxiang = new ArrayList<>();
    private String dataUserid;
    private LinearLayout ll_atantion, ll_dt, ll_tz;//关注
    private TextView person_name, tv_level, tv_age, tv_constellation, tv_address, tv_sign, tv_follownum,
            tv_fansnum, juli, tv_time, tv_time_right, tv_sex, rb_dt, rb_tz;
    private ImageView tv_guanzhu;
    private SimpleDraweeView iv_icon;
    private int pageSize = 10, pageNum = 1;
    public static UserInfo appuser = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mydata);
        dataUserid = getIntent().getStringExtra(Constant.ID);
        WebServiceAPI.getInstance().srarchInfo(dataUserid, this, this);


//        WebServiceAPI.getInstance().myPostList( id, pageNum, pageSize,
//                MyDataActivity.this, MyDataActivity.this);
        assignViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        WebServiceAPI.getInstance().myDynamicList(dataUserid,
                pageNum, pageSize, MyDataActivity.this, MyDataActivity.this);
        //Log.e("myDataActivity:", "#############################");
    }

    @SuppressLint("WrongViewCast")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void assignViews() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        viewData = (FrameLayout) findViewById(R.id.view_data);
        lvData = (ListView) findViewById(R.id.lv_data);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.tupian_demo);
//        Bitmap bcbitmap = ImageUtils.BoxBlurFilter(bm);
//        ImageView iv_beijng = (ImageView) findViewById(R.id.iv_beijng);
//        iv_beijng.setImageBitmap(bcbitmap);
//        Drawable drawable = new BitmapDrawable(bcbitmap);
        View v = findViewById(R.id.re_title);
//        LinearLayout llview = (LinearLayout) findViewById(R.id.ll_view);
//        v.setAlpha(100);
//        llview.setAlpha(100);
//        viewData.setAlpha(100);
        /**
         * 详细资料
         */
        View my_datatop = View.inflate(this, R.layout.my_data_top, null);
        lvData.addHeaderView(my_datatop, null, false);
//        ll_atantion//关注
        // ll_juli//聊天
        // person_name//昵称
        // tv_level//等级 tv_age//年龄
//        tv_constellation//星座  tv_address//地址  tv_sign//个性签名  tv_follownum//关注的人的数量
//        tv_fansnum//粉丝数量  juli//距离  tv_time//距离现在的时间
        //yangy
        person_name = (TextView) my_datatop.findViewById(R.id.person_name);
        ll_atantion = (LinearLayout) my_datatop.findViewById(R.id.ll_atantion);
        ll_dt = (LinearLayout) my_datatop.findViewById(R.id.ll_dt);
        ll_tz = (LinearLayout) my_datatop.findViewById(R.id.ll_tz);
        tv_level = (TextView) my_datatop.findViewById(R.id.tv_level);
        tv_age = (TextView) my_datatop.findViewById(R.id.tv_age);
        tv_constellation = (TextView) my_datatop.findViewById(R.id.tv_constellation);
        tv_address = (TextView) my_datatop.findViewById(R.id.tv_address);
        tv_sign = (TextView) my_datatop.findViewById(R.id.tv_sign);
        tv_follownum = (TextView) my_datatop.findViewById(R.id.tv_follownum);
        tv_fansnum = (TextView) my_datatop.findViewById(R.id.tv_fansnum);
        juli = (TextView) my_datatop.findViewById(R.id.juli);
        tv_time = (TextView) my_datatop.findViewById(R.id.tv_time);
        tv_time_right = (TextView) my_datatop.findViewById(R.id.tv_time_right);
        tv_sex = (TextView) my_datatop.findViewById(R.id.tv_sex);
        rb_dt = (TextView) my_datatop.findViewById(R.id.rb_dt);
        rb_tz = (TextView) my_datatop.findViewById(R.id.rb_tz);
        tv_guanzhu = (ImageView) my_datatop.findViewById(R.id.tv_guanzhu);
        ll_atantion.setOnClickListener(this);
        ll_dt.setOnClickListener(this);
        ll_tz.setOnClickListener(this);
        //yangy
        iv_bg = (ImageView) my_datatop.findViewById(R.id.iv_bg);
        iv_dt = (ImageView) my_datatop.findViewById(R.id.iv_dt);
        iv_tz = (ImageView) my_datatop.findViewById(R.id.iv_tz);
        iv_dt_left = (ImageView) my_datatop.findViewById(R.id.iv_dt_left);
        iv_tz_left = (ImageView) my_datatop.findViewById(R.id.iv_tz_left);
        ImageView iv_liliao = (ImageView) my_datatop.findViewById(R.id.iv_liliao);
        iv_liliao.setOnClickListener(this);
        //头像
        iv_icon = (SimpleDraweeView) my_datatop.findViewById(R.id.iv_icon);
//        iv_icon.setImageURI(Uri.parse(Constant.imageUrls2[2]));
//        ImageLoader.getInstance().displayImage(Constant.imageUrls2[2], iv_bg,
//                ImageUtils.getSpecialOptions(), new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//                        // TODO Auto-generated method stubk
//                        iv_bg.setImageBitmap(ImageUtils.BoxBlurFilter(arg2));
//                    }
//                });
//        tzAdapter = new PostAdapter(this, list, false, false);
        iv_icon.setOnClickListener(this);
//        adapter = new MyDataAdapter(this,list_my);
//        lvData.setAdapter(adapter);
        tzAdapter = new PostAdapter(this, list, false, dataUserid, true);
        lvData.setAdapter(tzAdapter);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                    //NewToast.show(this, "请先登录！", Toast.LENGTH_LONG);
                } else {
                    if (checkStatus(this)) {
                        Intent intent = new Intent(this, AccusationActivity.class);
                        intent.putExtra(Constant.ID, dataUserid);
                        intent.putExtra("objecttype", "0");
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.ll_atantion:
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                    //NewToast.show(this, "请先登录！", Toast.LENGTH_LONG);
                } else {
                    if (checkStatus(this)) {
                        DialogUtils.showProgressDialog("正在加载...", this);
                        if (appuser.isfollow.equals("0")) {
                            //关注
                            WebServiceAPI.getInstance().attentionPerson(dataUserid, this, this);
                        } else {
                            //取消关注
                            WebServiceAPI.getInstance().deleteAttention(dataUserid, this, this);
                        }
                    }
                }
                break;

            case R.id.ll_dt://TA的动态
                iv_tz.setVisibility(View.INVISIBLE);
                iv_dt.setVisibility(View.VISIBLE);
                iv_dt_left.setImageResource(R.mipmap.dt_check);
                iv_tz_left.setImageResource(R.mipmap.tiezi_uncheck);
                rb_dt.setTextColor(getResources().getColor(R.color.white));
                rb_tz.setTextColor(getResources().getColor(R.color.my_brown));
                WebServiceAPI.getInstance().myDynamicList(dataUserid,
                        pageNum, pageSize, MyDataActivity.this, MyDataActivity.this);
                break;

            case R.id.ll_tz://TA的帖子
                iv_tz.setVisibility(View.VISIBLE);
                iv_dt.setVisibility(View.INVISIBLE);
                iv_dt_left.setImageResource(R.mipmap.dt_uncheck);
                iv_tz_left.setImageResource(R.mipmap.tiezi_check);
                rb_dt.setTextColor(getResources().getColor(R.color.my_brown));
                rb_tz.setTextColor(getResources().getColor(R.color.white));
                WebServiceAPI.getInstance().myPostList(dataUserid, pageNum, pageSize,
                        MyDataActivity.this, MyDataActivity.this);
                break;

            case R.id.iv_liliao:
                /**
                 * 启动单聊
                 * context - 应用上下文。
                 * targetUserId - 要与之聊天的用户 Id。
                 * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                } else {
                    if (checkStatus(this)) {
                        WebServiceAPI.getInstance().isMutual(dataUserid, this, this);
                    }
                }
                break;
            case R.id.iv_icon:
                if (touxiang.size() > 0) {
                    ViewPagerDialog dlg = new ViewPagerDialog(this, touxiang, 0);
                    dlg.showViewPagerDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1:
                    appuser = apiResponse.data.appuser;
                    person_name.setText(appuser.nickname);
                    tv_level.setText("V" + appuser.level);
                    if (TextUtils.isEmpty(appuser.age)) {
                        tv_age.setText("");
                    } else {
                        tv_age.setText(appuser.age + "岁");
                    }
                    showBrithdayDialog();//星座
                    if (TextUtils.isEmpty(appuser.provinceId)) {
                        tv_address.setVisibility(View.INVISIBLE);
                    } else {
                        tv_address.setVisibility(View.VISIBLE);
                        tv_address.setText(AddressUtil.getInstance(this).getCityNameById(appuser.provinceId, appuser.cityId));
                        //tv_address.setText(getCityNameById(appuser.provinceId, appuser.cityId));//地址
                    }
                    if (TextUtils.isEmpty(appuser.sign)) {
                        tv_sign.setVisibility(View.INVISIBLE);
                    } else {
                        tv_sign.setVisibility(View.VISIBLE);
                        tv_sign.setText("个性签名：" + appuser.sign);
                    }
                    tv_follownum.setText(appuser.follownum);
                    tv_fansnum.setText(appuser.fansnum);
                    Log.e("appuser.distance",""+appuser.distance);
                    if (!TextUtils.isEmpty(appuser.distance)) {
                        double distance = Double.parseDouble(appuser.distance);
                        if (distance >= 1000.0) {
                            distance = distance / 1000.0;
                            BigDecimal bigDecimal = new BigDecimal(distance);
                            int julis = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            juli.setText(julis + "km");
                        } else if (distance < 1000.00 && distance > 10.00) {
                            distance = distance / 1000.0;
                            BigDecimal bigDecimal = new BigDecimal(distance);
                            double julis = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            juli.setText(julis + "km");
                        } else {
                            juli.setText(0.01 + "km");
                        }
                    }
                    if (appuser.isfollow.equals("0")) {
                        tv_guanzhu.setImageResource(R.mipmap.guanzhu);
                    } else {
                        tv_guanzhu.setImageResource(R.mipmap.quguan);
                    }
//                    iv_icon.setImageURI(Uri.parse(Urls.PHOTO + appuser.head));
                    if (appuser.head.equals("")) {
                        if (appuser.sex.equals("0")) {
                            iv_icon.setImageResource(R.mipmap.touxiangnan);
                        } else {
                            iv_icon.setImageResource(R.mipmap.touxiangnv);
                        }
                    } else {
                        iv_icon.setImageURI(Uri.parse(Urls.PHOTO + appuser.head));
                        touxiang.clear();
                        ImageBean ib = new ImageBean();
                        ib.path = "";
                        touxiang.add(ib);
                        touxiang.get(0).path = appuser.head;
                        ImageLoader.getInstance().displayImage(Urls.PHOTO + appuser.head, iv_bg,
                                ImageUtils.getSpecialOptions(), new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                                        // TODO Auto-generated method stubk
                                        iv_bg.setImageBitmap(ImageUtils.BoxBlurFilter(arg2));
                                    }
                                });
                    }
//                    ImageLoader.getInstance().displayImage(Urls.PHOTO + appuser.head, iv_bg,
//                            ImageUtils.getSpecialOptions(), new SimpleImageLoadingListener() {
//                                @Override
//                                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//                                    // TODO Auto-generated method stubk
//                                    iv_bg.setImageBitmap(ImageUtils.BoxBlurFilter(arg2));
//                                }
//                            });
                    tv_sex.setText(appuser.sex.equals("0") ? "帅哥" : "美女");
                    if (!TextUtils.isEmpty(appuser.days)) {
                        tv_time.setText(appuser.days);
                        tv_time_right.setText("天前");
                    }
                    if (!TextUtils.isEmpty(appuser.hours)) {
                        tv_time.setText(appuser.hours);
                        tv_time_right.setText("小时前");
                    }
                    if (!TextUtils.isEmpty(appuser.minutes)) {
                        tv_time.setText(appuser.minutes);
                        tv_time_right.setText("分钟前");
                    }
                    break;
                case 2:
                    if (list_my.size() != 0) {
                        list_my.clear();
                    }
                    list_my.addAll(apiResponse.data.list);
                    adapter = new MyDynamicAdapter(this, list_my,dataUserid);
                    lvData.setAdapter(adapter);
//                    lvData.setOnClickListener();
                    break;
                case 3:
                    if (list.size() != 0) {
                        list.clear();
                    }
                    list.addAll(apiResponse.data.list);
                    //lvData.setAdapter(tzAdapter);

                    tzAdapter = new PostAdapter(this, list, false, dataUserid, true);
                    lvData.setAdapter(tzAdapter);
                    tzAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    WebServiceAPI.getInstance().srarchInfo(dataUserid, this, this);
                    NewToast.show(this, "关注成功", Toast.LENGTH_LONG);
                    break;
                case 5:
                    WebServiceAPI.getInstance().srarchInfo(dataUserid, this, this);
                    NewToast.show(this, "取消关注成功", Toast.LENGTH_LONG);
                    break;
                case 6:
                    if (apiResponse.data.ismutual.equals("1")) {
                        if (checkStatus(this)) {
                            if (RongIM.getInstance() != null) {
                                RongIM.getInstance().startPrivateChat(MyDataActivity.this, dataUserid, appuser.nickname);
                            }
                        }
                    } else {
                        NewToast.show(this, "必须相互关注了才能聊天哦", Toast.LENGTH_LONG);
                    }
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    private void showBrithdayDialog() {
        // TODO Auto-generated method stub
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String defaultDate = (TextUtils.isEmpty(appuser.birthday)) ?
                sDateFormat.format(new Date()) : appuser.birthday;
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
        if (TextUtils.isEmpty(appuser.birthday)) {
            tv_constellation.setVisibility(View.INVISIBLE);
        } else {
            tv_constellation.setVisibility(View.VISIBLE);
            tv_constellation.setText(date2Constellation(String.valueOf(month3), String.valueOf(dp_user_bri.getDayOfMonth())));
        }
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


}
