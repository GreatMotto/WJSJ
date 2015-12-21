package com.bm.wjsj.Dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.WJSJApplication;
import com.bm.wjsj.upload.UploadActivity;

import java.util.ArrayList;

/**
 * 动态
 */
public class DynamicFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {
    private View view;
    private TextView tvBack;
    private RadioGroup radiogroup;
    private RadioButton rb_one, rb_two, rb_three;
    private ImageView ivSubmit;
    private ViewPager vp_pager = null;
    private ArrayList<Fragment> dynamicFragments = new ArrayList<Fragment>();
    private PagerAdapter vpAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_dynamic, container, false);
        initFragment();
        assignViews();
        initPager();
        return view;
    }

    private void assignViews() {
        tvBack = (TextView) view.findViewById(R.id.tv_sidebar);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        rb_one = (RadioButton) view.findViewById(R.id.rb_one);
        rb_two = (RadioButton) view.findViewById(R.id.rb_two);
        rb_three = (RadioButton) view.findViewById(R.id.rb_three);
        ivSubmit = (ImageView) view.findViewById(R.id.iv_submit);
        vp_pager = (ViewPager) view.findViewById(R.id.vp_pager);
        ivSubmit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
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
        dynamicFragments.add(new HotFragment());
        dynamicFragments.add(new FocusFragment());
        dynamicFragments.add(new NearFragment());
    }

    private void initPager() {
        Log.e("dynamicFragments", dynamicFragments.toString());
        vpAdapter = new DynamicPagerAdapter(getChildFragmentManager(), dynamicFragments);
        vp_pager.setAdapter(vpAdapter);
        vp_pager.setCurrentItem(0);
        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_one.setChecked(true);
                        rb_two.setChecked(false);
                        rb_three.setChecked(false);
                        break;
                    case 1:
                        rb_one.setChecked(false);
                        rb_two.setChecked(true);
                        rb_three.setChecked(false);
                        break;
                    case 2:
                        rb_one.setChecked(false);
                        rb_two.setChecked(false);
                        rb_three.setChecked(true);
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
            case R.id.iv_submit:
                if ("1".equals(WJSJApplication.getInstance().getSp().getValue(Constant.STATUS))) {
                    NewToast.show(getActivity(), "用户被禁用！", Toast.LENGTH_LONG);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), UploadActivity.class);
                    intent.putExtra(Constant.BOOLEAN, true);
                    startActivity(intent);
                }
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
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
