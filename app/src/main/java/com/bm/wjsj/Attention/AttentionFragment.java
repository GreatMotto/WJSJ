package com.bm.wjsj.Attention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;

/**
 * 关注列表
 */
public class AttentionFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {
    private View view;
    private TextView tvSidebar, tv_attention_num;
    private RadioGroup radiogroup;
    private RadioButton rbOne, rbTwo, rbThree;
    private RelativeLayout rlFirst, rlSecond, rlThird;
    private ImageView ivFirst, ivSecond, ivThird;
    private ViewPager vp_pager = null;
    private ArrayList<Fragment> attentionFragments = new ArrayList<Fragment>();
    private PagerAdapter vpAdapter = null;
    private String attentionNum1 = "", attentionNum2 = "", attentionNum3 = "";
    public static boolean vpPagerTemp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_attention, container, false);
        initFragment();
        initView();
        initPager();
        return view;
    }

    private void initView() {
        tvSidebar = (TextView) view.findViewById(R.id.tv_sidebar);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        rbOne = (RadioButton) view.findViewById(R.id.rb_one);
        rbTwo = (RadioButton) view.findViewById(R.id.rb_two);
        rbThree = (RadioButton) view.findViewById(R.id.rb_three);
        rlFirst = (RelativeLayout) view.findViewById(R.id.rl_first);
        ivFirst = (ImageView) view.findViewById(R.id.iv_first);
        rlSecond = (RelativeLayout) view.findViewById(R.id.rl_second);
        ivSecond = (ImageView) view.findViewById(R.id.iv_second);
        rlThird = (RelativeLayout) view.findViewById(R.id.rl_third);
        ivThird = (ImageView) view.findViewById(R.id.iv_third);
        vp_pager = (ViewPager) view.findViewById(R.id.vp_pager);
        tv_attention_num = (TextView) view.findViewById(R.id.tv_attention_num);
        tvSidebar.setOnClickListener(this);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 点击事件获取的选择对象
                switch (checkedId) {
                    case R.id.rb_one:
                        vp_pager.setCurrentItem(0);
                        break;
                    case R.id.rb_two:
                        vp_pager.setCurrentItem(1);
                        break;
                    case R.id.rb_three:
                        vp_pager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initFragment() {
        attentionFragments.add(new MutualAttentionFragment());
        attentionFragments.add(new MyAttentionFragment());
        attentionFragments.add(new AttentionMeFragment());
    }

    private void initPager() {
        vpAdapter = new AttentionPagerAdapter(getChildFragmentManager(), attentionFragments);
        vp_pager.setAdapter(vpAdapter);
        if (!vpPagerTemp) {
            vp_pager.setCurrentItem(0);
            WebServiceAPI.getInstance().attentionNum("0", AttentionFragment.this, getActivity());
        }else{
            vp_pager.setCurrentItem(1);
            rbTwo.setChecked(true);
            vpPagerTemp = false;
            WebServiceAPI.getInstance().attentionNum("1", AttentionFragment.this, getActivity());
        }
//        WebServiceAPI.getInstance().attentionNum("0", AttentionFragment.this, getActivity());
//        WebServiceAPI.getInstance().attentionNum("1", AttentionFragment.this, getActivity());
//        WebServiceAPI.getInstance().attentionNum("2", AttentionFragment.this, getActivity());
        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        vp_pager.setCurrentItem(0);
                        WebServiceAPI.getInstance().attentionNum("0", AttentionFragment.this, getActivity());
                        rbOne.setChecked(true);
                        //tv_attention_num.setText(attentionNum1);
                        break;
                    case 1:
                        ivFirst.setVisibility(View.GONE);
                        vp_pager.setCurrentItem(1);
                        WebServiceAPI.getInstance().attentionNum("1", AttentionFragment.this, getActivity());
                        rbTwo.setChecked(true);
                        //tv_attention_num.setText(attentionNum2);
                        break;
                    case 2:
                        ivFirst.setVisibility(View.GONE);
                        vp_pager.setCurrentItem(2);
                        WebServiceAPI.getInstance().attentionNum("2", AttentionFragment.this, getActivity());
                        rbThree.setChecked(true);
                        //tv_attention_num.setText(attentionNum3);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 0:
                    int mtlattention = 0;
                    mtlattention = WJSJApplication.getInstance().getSp().getIntValue(Constant.SP_MTLATTENTION);
                    attentionNum1 = apiResponse.data.num;
                    if (mtlattention >= Integer.parseInt(attentionNum1)) {
                        ivFirst.setVisibility(View.GONE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_MTLATTENTION, Integer.parseInt(attentionNum1));
                    } else {
                        ivFirst.setVisibility(View.GONE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_MTLATTENTION, Integer.parseInt(attentionNum1));
                    }
                    tv_attention_num.setText(attentionNum1);
                    break;
                case 1:
                    int myattention = 0;
                    myattention = WJSJApplication.getInstance().getSp().getIntValue(Constant.SP_MYATTENTION);
                    attentionNum2 = apiResponse.data.num;
                    if (myattention >= Integer.parseInt(attentionNum2)) {
                        ivSecond.setVisibility(View.GONE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_MYATTENTION, Integer.parseInt(attentionNum2));
                    } else {
                        ivSecond.setVisibility(View.VISIBLE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_MYATTENTION, Integer.parseInt(attentionNum2));
                    }
                    tv_attention_num.setText(attentionNum2);
                    break;
                case 2:
                    int attentionme = 0;
                    attentionme = WJSJApplication.getInstance().getSp().getIntValue(Constant.SP_ATTENTIONME);
                    attentionNum3 = apiResponse.data.num;
                    if (attentionme >= Integer.parseInt(attentionNum3)) {
                        ivThird.setVisibility(View.GONE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_ATTENTIONME, Integer.parseInt(attentionNum3));
                    } else {
                        ivThird.setVisibility(View.VISIBLE);
                        WJSJApplication.getInstance().getSp().putIntValue(Constant.SP_ATTENTIONME, Integer.parseInt(attentionNum3));
                    }
                    tv_attention_num.setText(attentionNum3);
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
