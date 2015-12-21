package com.bm.wjsj.Circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Bean.CircleBean;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.AutomaticViewPager;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的圈子首页
 */
public class CircleFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {
    private View view, layout_header;
    private AutomaticViewPager view_pager;
    private LinearLayout dotLayout;
    private TextView tv_sidebar, tv_morecircle, tv_no_circle;
    private RadioGroup radioGroup;
    private ListView lv_circles, lv_my_circle;
    private RefreshLayout rfl_circle;

    private List<CircleBean> list_my = new ArrayList<>();
    private List<CircleBean> list = new ArrayList<>();
    public static List<ImageBean> imagelist = new ArrayList<>();
    private List<PostBean> list_post = new ArrayList<>();
    private int pageCircleNum = 1, pageCircleSize = 100;
    private int pageMoreNum = 1, pageMoreSize = 100;
    private int type = 0;// 热门帖子
    private CircleAdapter adapter, myadapter;
    private PostAdapter postAdapter;
    private boolean isCircle = true;
    CircleAdapter.JoinClickInterface joinClickInterface = new CircleAdapter.JoinClickInterface() {
        @Override
        public void joinClick(View v, int position) {
            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                if ("1".equals(WJSJApplication.getInstance().getSp().getValue(Constant.STATUS))) {
                    NewToast.show(getActivity(), "用户被禁用！", Toast.LENGTH_LONG);
                } else {
                    WebServiceAPI.getInstance().addCircle(list.get(position).id, CircleFragment.this, getActivity());
                }
            } else {
                NewToast.show(getActivity(), "您未登录，无法加入圈子", NewToast.LENGTH_LONG);
                tv_no_circle.setText("您未登录，无法查看我的圈子");
                tv_no_circle.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            radioGroup.check(R.id.rb_one);
//            //获得banner图
//            WebServiceAPI.getInstance().circleBanner(CircleFragment.this, getActivity());
            return view;
        }
        view = inflater.inflate(R.layout.fg_circle, container, false);
        initView();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            WebServiceAPI.getInstance().circleMy(this, getActivity());
        } else {
            tv_no_circle.setText("您未登录，无法查看我的圈子");
            tv_no_circle.setVisibility(View.VISIBLE);
        }
        WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, this, getActivity());
        WebServiceAPI.getInstance().circlePost(
                type, pageCircleNum, pageCircleSize, "", 1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                CircleFragment.this, getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)){
            WebServiceAPI.getInstance().circleMy(this, getActivity());
        }
        WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, this, getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        view_pager.stopTimer();
    }

    private void initView() {
        lv_circles = (ListView) view.findViewById(R.id.lv_circles);
        rfl_circle = (RefreshLayout) view.findViewById(R.id.rfl_circle);
        tv_sidebar = (TextView) view.findViewById(R.id.tv_sidebar);

        lv_circles.addFooterView(rfl_circle.getFootView(), null, false);
        layout_header = LayoutInflater.from(getActivity()).inflate(R.layout.header_circle, null);
        lv_circles.addHeaderView(layout_header, null, false);

        radioGroup = (RadioGroup) layout_header.findViewById(R.id.radiogroup);
        tv_morecircle = (TextView) layout_header.findViewById(R.id.tv_morecircle);
        lv_my_circle = (ListView) layout_header.findViewById(R.id.lv_my_circle);
        lv_my_circle.setFooterDividersEnabled(false);
        tv_no_circle = (TextView) layout_header.findViewById(R.id.tv_no_circle);
        view_pager = (AutomaticViewPager) layout_header.findViewById(R.id.view_pager);
        dotLayout = (LinearLayout) layout_header.findViewById(R.id.dot_ll);

        adapter = new CircleAdapter(getActivity(), list, false, joinClickInterface);
        myadapter = new CircleAdapter(getActivity(), list_my, true, joinClickInterface);
        lv_my_circle.setAdapter(myadapter);
        lv_circles.setAdapter(adapter);

        lv_circles.setOnScrollListener(rfl_circle);
        tv_sidebar.setOnClickListener(this);
        int width = DisplayUtil.getWidth(getActivity());
        //DisplayUtil.setLayoutParams(view_pager, width, (int) (width / 2.2));
        //DisplayUtil.setLayoutParams(view_pager, width, (int) (width / 2.2));
        //MainActivity ma = new MainActivity();

        //view_pager.setClickFlag("2");
//        view_pager.start(getActivity(), 4000, dotLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
//                R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, ((MainActivity) getActivity()).
//                        getImageBean(ma.imagelist, ma.imagelist.size()), 1.15f);

//        view_pager.start(getActivity(), 4000, dotLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
//                R.mipmap.dian_select, R.mipmap.dian_unselect, ((MainActivity) getActivity()).
//                        getImageList(Constant.imageUrls, 3), 2.2f);
//        //获得banner图
       WebServiceAPI.getInstance().circleBanner(CircleFragment.this, getActivity());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 点击事件获取的选择对象
                switch (checkedId) {
                    //我的圈子
                    case R.id.rb_one:
                        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                            WebServiceAPI.getInstance().circleMy(CircleFragment.this, getActivity());
                        } else {
                            tv_no_circle.setText("您未登录，无法查看我的圈子");
                            tv_no_circle.setVisibility(View.VISIBLE);
                        }
                        pageMoreNum = 1;
                        WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, CircleFragment.this, getActivity());
                        isCircle = true;
                        lv_circles.setAdapter(adapter);
                        lv_my_circle.setVisibility(View.VISIBLE);
                        tv_morecircle.setVisibility(View.VISIBLE);
                        lv_circles.setDivider(null);
                        lv_circles.setDividerHeight(DisplayUtil.dip2px(getActivity(), 8));
                        if (list_my.size() > 0)
                            tv_no_circle.setVisibility(View.VISIBLE);
                        break;
                    //热门帖子
                    case R.id.rb_two:
                        PostDetailActivity.notifyFlag = false;
                        pageCircleNum = 1;
                        WebServiceAPI.getInstance().circlePost(
                                type, pageCircleNum, pageCircleSize, "", 1,
                                WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                                CircleFragment.this, getActivity());
                        isCircle = false;
                        postAdapter = new PostAdapter(getActivity(), list_post, false, "", false);
                        tv_no_circle.setVisibility(View.GONE);
                        lv_circles.setAdapter(postAdapter);
                        postAdapter.notifyDataSetChanged();
                        lv_my_circle.setVisibility(View.GONE);
                        tv_morecircle.setVisibility(View.GONE);
                        lv_circles.setDivider(getResources().getDrawable(R.mipmap.xiantiao));
                        lv_circles.setDividerHeight(DisplayUtil.dip2px(getActivity(), 1));
                        break;
                }
            }
        });
        rfl_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isCircle) {
                    pageMoreNum = 1;
                    if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                        WebServiceAPI.getInstance().circleMy(CircleFragment.this, getActivity());
                    } else {
                        tv_no_circle.setText("您未登录，无法查看我的圈子");
                        tv_no_circle.setVisibility(View.VISIBLE);
                    }
                    WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, CircleFragment.this, getActivity());
                } else {
                    pageCircleNum = 1;
                    WebServiceAPI.getInstance().circlePost(
                            type, pageCircleNum, pageCircleSize, "", 1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            CircleFragment.this, getActivity());
                }
            }
        });
        rfl_circle.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (isCircle) {
                    pageMoreNum++;
                    if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                        WebServiceAPI.getInstance().circleMy(CircleFragment.this, getActivity());
                    } else {
                        tv_no_circle.setText("您未登录，无法查看我的圈子");
                        tv_no_circle.setVisibility(View.VISIBLE);
                    }
                    WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, CircleFragment.this, getActivity());
                } else {
                    pageCircleNum++;
                    WebServiceAPI.getInstance().circlePost(
                            type, pageCircleNum, pageCircleSize, "", 1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            CircleFragment.this, getActivity());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sidebar:
                ((MainActivity) getActivity()).sm.toggle();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        //网络请求异常的时候，例如没网、找不到服务器、404等
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 0://热门帖子列表
                    if (pageCircleNum == 1) {
                        if (list_post.size() != 0) {
                            list_post.clear();
                        }
                        rfl_circle.setRefreshing(false);
                        rfl_circle.setLoad_More(true);
                        list_post.addAll(apiResponse.data.list);
//                        postAdapter.notifyDataSetChanged();
                    } else {
                        rfl_circle.setLoading(false);
                        list_post.addAll(apiResponse.data.list);
//                        postAdapter.notifyDataSetChanged();
                    }
                    if (apiResponse.data.page.totalPage <= pageCircleNum) {
                        rfl_circle.setLoad_More(false);
                    }
                    break;
                case 2:
                    //我的圈子
                    list_my.clear();
                    list_my.addAll(apiResponse.data.result);
                    if (list_my != null && list_my.size() > 0) {
                        int height = 90 * (list_my.size());
                        try {
                            DisplayUtil.setLayoutParams(lv_my_circle, DisplayUtil.getWidth(getActivity())
                                    , DisplayUtil.dip2px(getActivity(), height));
                        } catch (Exception e) {

                        }
                        tv_no_circle.setVisibility(View.GONE);
                        if (PostDetailActivity.notifyFlag == false) {
                            lv_my_circle.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_no_circle.setText(getResources().getString(R.string.nocircle));
                        tv_no_circle.setVisibility(View.VISIBLE);
                        lv_my_circle.setVisibility(View.GONE);
                    }
                    lv_my_circle.setFocusable(true);
                    lv_my_circle.setFocusableInTouchMode(true);
                    myadapter.notifyDataSetChanged();
                    WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, CircleFragment.this, getActivity());
                    break;
                case 3:
                    //更多圈子
                    list.clear();
                    rfl_circle.setRefreshing(false);
                    list.addAll(apiResponse.data.list);
                    rfl_circle.setLoad_More(false);
                    adapter.notifyDataSetChanged();
                    break;
                case 4://加入圈子
                    WebServiceAPI.getInstance().circleMy(CircleFragment.this, getActivity());
//                    WebServiceAPI.getInstance().circleMore(pageMoreNum, pageMoreSize, CircleFragment.this, getActivity());
                    break;
                case 5://banner图
                    if (imagelist.size() != 0) {
                        imagelist.clear();
                    }
                    imagelist.addAll(apiResponse.data.list);
                    //MainActivity ma = new MainActivity();
                    view_pager.setClickFlag("2");
                    view_pager.start(getActivity(), 4000, dotLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
                            R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, ((MainActivity) getActivity()).
                                    getImageBean(imagelist, imagelist.size()), 1.15f);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        Log.e("whicherror???", String.valueOf(tag));
    }
}
