package com.bm.wjsj.SpiceStore;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wjsj.Bean.ClassfiyBean;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.View.AutomaticViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城首页
 */
public class SpiceStoreFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {
    private ImageView imageViewTT, imageViewNY, imageViewYP,
            imageViewMan, imageViewWoman, imageViewTQ, iv_search;
    private CardView cardView_shop_11;
    private View v;
    private EditText et_search;
    private LinearLayout dotLayout;
    private MainActivity ac;
    private List<ClassfiyBean> classfiyBeanList = new ArrayList<ClassfiyBean>();
    private List<ImageBean> list_my = new ArrayList<>();
    //private List<ImageBean> listspic = new ArrayList<>();
    private List<ImageBean> list = new ArrayList<>();
    private ImageView iv_classfiy_1, iv_classfiy_2, iv_classfiy_3,
            shop_1, shop_2, shop_3, shop_4, shop_5, shop_6,
            shop_7, shop_8, shop_9, shop_10, shop_11;
    private AutomaticViewPager view_pager;
    private String plateid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        int version = Build.VERSION.SDK_INT;
        Log.e("phoneVersion","" + version);

        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
            return v;
        }

        if (v == null && version < 21) {
            v = inflater.inflate(R.layout.fg_spice_store, container, false);
        }else{
            v = inflater.inflate(R.layout.fg_spice_store_5, container, false);
        }
        initView();

        return v;
    }

    private void initData() {
        WebServiceAPI.getInstance().store(SpiceStoreFragment.this, getActivity());
    }

    private void initView() {
        initData();
        ac = (MainActivity) getActivity();
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_right = (TextView) v.findViewById(R.id.tv_right);
        ImageView go_shopcat = (ImageView) v.findViewById(R.id.go_shopcat);
        ImageView icon = (ImageView) v.findViewById(R.id.icon_back);
        view_pager = (AutomaticViewPager) v.findViewById(R.id.view_pager);
        dotLayout = (LinearLayout) v.findViewById(R.id.dot_ll);
        et_search = (EditText) v.findViewById(R.id.et_search);
        iv_search = (ImageView) v.findViewById(R.id.iv_search);

        imageViewTT = (ImageView) v.findViewById(R.id.imageViewTT);
        imageViewNY = (ImageView) v.findViewById(R.id.imageViewNY);
        imageViewYP = (ImageView) v.findViewById(R.id.imageViewYP);
        imageViewMan = (ImageView) v.findViewById(R.id.imageViewMan);
        imageViewWoman = (ImageView) v.findViewById(R.id.imageViewWomen);
        imageViewTQ = (ImageView) v.findViewById(R.id.imageViewTQ);

        // 首页前三个
        iv_classfiy_1 = (ImageView) v.findViewById(R.id.iv_classfiy_1);
        iv_classfiy_2 = (ImageView) v.findViewById(R.id.iv_classfiy_2);
        iv_classfiy_3 = (ImageView) v.findViewById(R.id.iv_classfiy_3);

        //商品第一排
        shop_1 = (ImageView) v.findViewById(R.id.shop_1);
        shop_2 = (ImageView) v.findViewById(R.id.shop_2);

        //商品第二排
        shop_3 = (ImageView) v.findViewById(R.id.shop_3);
        shop_4 = (ImageView) v.findViewById(R.id.shop_4);
        shop_5 = (ImageView) v.findViewById(R.id.shop_5);

        //商品第三排
        shop_6 = (ImageView) v.findViewById(R.id.shop_6);

        //商品第四排
        shop_7 = (ImageView) v.findViewById(R.id.shop_7);
        shop_8 = (ImageView) v.findViewById(R.id.shop_8);
        shop_9 = (ImageView) v.findViewById(R.id.shop_9);

        //商品第五排
        shop_10 = (ImageView) v.findViewById(R.id.shop_10);
        shop_11 = (ImageView) v.findViewById(R.id.shop_11);

        tv_title.setText("商城");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(ac.getResources().getString(R.string.fenlei));
        tv_right.setOnClickListener(this);
        go_shopcat.setOnClickListener(this);
        icon.setImageResource(R.mipmap.sidebar);
        v.findViewById(R.id.tv_back).setOnClickListener(this);
        int width = DisplayUtil.getWidth(getActivity());
        DisplayUtil.setLayoutParams(view_pager, width, (int) (width / 2.2));
//        view_pager.start(getActivity(), 4000, dotLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
//                R.mipmap.dian_select, R.mipmap.dian_unselect, ((MainActivity) getActivity()).getImageList(Constant.imageUrls, 3), 2.2f);
//        NoScrollGridView gv_fenlei = (NoScrollGridView) v.findViewById(R.id.gv_fenlei);
        ClassfiyBean classfiyBean;
        for (int i = 0; i < Constant.classfiyNames.length; i++) {
            classfiyBean = new ClassfiyBean(Constant.classfiyNames[i], Constant.classfiyIcon[i], i);
            classfiyBeanList.add(classfiyBean);

        }
//        SpiceClassifyAdapter classifyAdapter = new SpiceClassifyAdapter(ac, classfiyBeanList);
//        gv_fenlei.setAdapter(classifyAdapter);
        //分类商品
//        iv_classfiy_1.setAspectRatio(1.25f);
//        iv_classfiy_2.setAspectRatio(1.25f);
//        iv_classfiy_3.setAspectRatio(3.4f);
        iv_classfiy_1.setOnClickListener(this);
        iv_classfiy_1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv_classfiy_2.setOnClickListener(this);
        iv_classfiy_2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv_classfiy_3.setOnClickListener(this);
        iv_classfiy_3.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageViewTT.setOnClickListener(this);
        imageViewNY.setOnClickListener(this);
        imageViewYP.setOnClickListener(this);
        imageViewMan.setOnClickListener(this);
        imageViewWoman.setOnClickListener(this);
        imageViewTQ.setOnClickListener(this);

//        //商品第一排
//        shop_1.setAspectRatio(1.54639f);
//        shop_2.setAspectRatio(1.54639f);
//
//        //商品第二排
//        shop_3.setAspectRatio(0.82278481f);
//        shop_4.setAspectRatio(0.82278481f);
//        shop_5.setAspectRatio(0.82278481f);
//
//        //商品第三排
//        shop_6.setAspectRatio(2.399224f);
//
//        //商品第四排
//        shop_7.setAspectRatio(0.82278481f);
//        shop_8.setAspectRatio(0.82278481f);
//        shop_9.setAspectRatio(0.82278481f);
//
//        //商品第五排
//        shop_10.setAspectRatio(1.54639f);
//        shop_11.setAspectRatio(1.54639f);


//        list_my.addAll(((MainActivity) getActivity()).getImageList(Constant.imageUrls2, Constant.imageUrls2.length));
//        iv_classfiy_1.setImageURI(Uri.parse(list_my.get(0).url));
//        iv_classfiy_2.setImageURI(Uri.parse(list_my.get(1).url));
//        iv_classfiy_3.setImageURI(Uri.parse(list_my.get(1).url));
//        shop_1.setImageURI(Uri.parse(list_my.get(3).url));
//        shop_2.setImageURI(Uri.parse(list_my.get(3).url));
//        shop_3.setImageURI(Uri.parse(list_my.get(4).url));
//        shop_4.setImageURI(Uri.parse(list_my.get(5).url));
//        shop_5.setImageURI(Uri.parse(list_my.get(5).url));
//        shop_6.setImageURI(Uri.parse(list_my.get(5).url));
//        shop_7.setImageURI(Uri.parse(list_my.get(6).url));
//        shop_8.setImageURI(Uri.parse(list_my.get(7).url));
//        shop_9.setImageURI(Uri.parse(list_my.get(7).url));
//        shop_10.setImageURI(Uri.parse(list_my.get(4).url));
//        shop_11.setImageURI(Uri.parse(list_my.get(4).url));
        //设监听
        iv_search.setOnClickListener(this);
        shop_1.setOnClickListener(this);
        shop_1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_2.setOnClickListener(this);
        shop_2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_3.setOnClickListener(this);
        shop_3.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_4.setOnClickListener(this);
        shop_4.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_5.setOnClickListener(this);
        shop_5.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_6.setOnClickListener(this);
        shop_6.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_7.setOnClickListener(this);
        shop_7.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_8.setOnClickListener(this);
        shop_8.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_9.setOnClickListener(this);
        shop_9.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_10.setOnClickListener(this);
        shop_10.setScaleType(ImageView.ScaleType.FIT_CENTER);
        shop_11.setOnClickListener(this);
        shop_11.setScaleType(ImageView.ScaleType.FIT_CENTER);


    }

    @Override
    public void onPause() {
        super.onPause();
        view_pager.stopTimer();
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        view_pager.startTimer();
//    }
//
//    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_back:
                ac.sm.toggle();
                break;
            case R.id.tv_right:
                intent.setClass(ac, ClassifiyAcitivity.class);
                startActivity(intent);
                break;
            case R.id.iv_search:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "3");
                intent.putExtra("selectName", et_search.getText().toString());
                startActivity(intent);
                break;
            case R.id.iv_classfiy_1:
                intent.setClass(ac, SecondClassifyActivity.class);
                plateid = iv_classfiy_1.getTag() != null ? iv_classfiy_1.getTag().toString() : "";
                intent.putExtra("parentFlag", "2");
                intent.putExtra("plateid", plateid);
                startActivity(intent);
                break;
            case R.id.iv_classfiy_2:
                intent.setClass(ac, SecondClassifyActivity.class);
                plateid = iv_classfiy_2.getTag() != null ? iv_classfiy_2.getTag().toString() : "";
                intent.putExtra("parentFlag", "2");
                intent.putExtra("plateid", plateid);
                startActivity(intent);
                break;
            case R.id.iv_classfiy_3:
                intent.setClass(ac, SecondClassifyActivity.class);
                plateid = iv_classfiy_3.getTag() != null ? iv_classfiy_3.getTag().toString() : "";
                intent.putExtra("parentFlag", "2");
                intent.putExtra("plateid", plateid);
                startActivity(intent);
                break;
            case R.id.imageViewTT:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "1");//套套世界
                intent.putExtra("parentTypeName", "套套世界");
                startActivity(intent);
                break;
            case R.id.imageViewNY:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "5");//情趣内衣
                intent.putExtra("parentTypeName", "情趣内衣");
                startActivity(intent);
                break;
            case R.id.imageViewYP:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "6");//情趣用品
                intent.putExtra("parentTypeName", "情趣用品");
                startActivity(intent);
                break;
            case R.id.imageViewMan:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "7");//男性玩具
                intent.putExtra("parentTypeName", "男性玩具");
                startActivity(intent);
                break;
            case R.id.imageViewWomen:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "8");//女性玩具
                intent.putExtra("parentTypeName", "女性玩具");
                startActivity(intent);
                break;
            case R.id.imageViewTQ:
                intent.setClass(ac, SecondClassifyActivity.class);
                intent.putExtra("parentFlag", "1");
                intent.putExtra("Level1Id", "9");//调情助兴
                intent.putExtra("parentTypeName", "调情助兴");
                startActivity(intent);
                break;
            case R.id.shop_1:
                if (list.size() > 0) {
                    intent.putExtra(Constant.ID, list.get(0).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_2:
                if (list.size() > 1) {
                    intent.putExtra(Constant.ID, list.get(1).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_3:
                if (list.size() > 2) {
                    intent.putExtra(Constant.ID, list.get(2).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_4:
                if (list.size() > 3) {
                    intent.putExtra(Constant.ID, list.get(3).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_5:
                if (list.size() > 4) {
                    intent.putExtra(Constant.ID, list.get(4).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_6:
                if (list.size() > 5) {
                    intent.putExtra(Constant.ID, list.get(5).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_7:
                if (list.size() > 6) {
                    intent.putExtra(Constant.ID, list.get(6).id);
                    intent.setClass(ac, ShopDeataisActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shop_8:
                if (list.size() > 7) {
                    intent.setClass(ac, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(7).id);
                    startActivity(intent);
                }
                break;
            case R.id.shop_9:
                if (list.size() > 8) {
                    intent.setClass(ac, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(8).id);
                    startActivity(intent);
                }
                break;
            case R.id.shop_10:
                if (list.size() > 9) {
                    intent.setClass(ac, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(9).id);
                    startActivity(intent);
                }
                break;
            case R.id.shop_11:
                if (list.size() > 10) {
                    intent.setClass(ac, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(10).id);
                    startActivity(intent);
                }
                break;
            case R.id.go_shopcat:
                intent.setClass(ac, MyShopCatActivity.class);
                startActivity(intent);
                break;


        }

    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            if (apiResponse.data.bannaerList!=null&&apiResponse.data.bannaerList.size()>0) {
                view_pager.setClickFlag("1");
                view_pager.setscaleFlag("1");
                view_pager.start(getActivity(), 4000, dotLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
                        R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet,
                        ((MainActivity) getActivity()).getImageBean(
                                apiResponse.data.bannaerList,
                                apiResponse.data.bannaerList.size()), 2.2f);
            }
            //list_my.addAll(apiResponse.data.moduleList);
            list_my.addAll(((MainActivity) getActivity()).getImageBean(apiResponse.data.moduleList, apiResponse.data.moduleList.size()));
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(list_my.get(0).path, iv_classfiy_1, options);
            ImageLoader.getInstance().displayImage(list_my.get(1).path, iv_classfiy_2, options);
            ImageLoader.getInstance().displayImage(list_my.get(2).path, iv_classfiy_3, options);
//            iv_classfiy_1.setImageURI(Uri.parse(list_my.get(0).path));
//            iv_classfiy_2.setImageURI(Uri.parse(list_my.get(1).path));
//            iv_classfiy_3.setImageURI(Uri.parse(list_my.get(2).path));

            iv_classfiy_1.setTag(Uri.parse(list_my.get(0).id));
            iv_classfiy_2.setTag(Uri.parse(list_my.get(1).id));
            iv_classfiy_3.setTag(Uri.parse(list_my.get(2).id));

            //listspic.addAll(apiResponse.data.productList);
            list.addAll(((MainActivity) getActivity()).getImageBean(apiResponse.data.productList, apiResponse.data.productList.size()));
            for (int i = 0; i < list.size(); i++) {
                switch (i) {
                    case 0:
                        ImageLoader.getInstance().displayImage(list.get(0).path, shop_1, options);
                        shop_1.setTag(list.get(0).id);
                        break;
                    case 1:
                        ImageLoader.getInstance().displayImage(list.get(1).path, shop_2, options);
                        shop_2.setTag(list.get(1).id);
                        break;
                    case 2:
                        ImageLoader.getInstance().displayImage(list.get(2).path, shop_3, options);
                        shop_3.setTag(list.get(2).id);
                        break;
                    case 3:
                        ImageLoader.getInstance().displayImage(list.get(3).path, shop_4, options);
                        shop_4.setTag(list.get(3).id);
                        break;
                    case 4:
                        ImageLoader.getInstance().displayImage(list.get(4).path, shop_5, options);
                        shop_5.setTag(list.get(4).id);
                        break;
                    case 5:
                        ImageLoader.getInstance().displayImage(list.get(5).path, shop_6, options);
                        shop_6.setTag(list.get(5).id);
                        break;
                    case 6:
                        ImageLoader.getInstance().displayImage(list.get(6).path, shop_7, options);
                        shop_7.setTag(list.get(6).id);
                        break;
                    case 7:
                        ImageLoader.getInstance().displayImage(list.get(7).path, shop_8, options);
                        shop_8.setTag(list.get(7).id);
                        break;
                    case 8:
                        ImageLoader.getInstance().displayImage(list.get(8).path, shop_9, options);
                        shop_9.setTag(list.get(8).id);
                        break;
                    case 9:
                        ImageLoader.getInstance().displayImage(list.get(9).path, shop_10, options);
                        shop_10.setTag(list.get(9).id);
                        break;
                    case 10:
                        ImageLoader.getInstance().displayImage(list.get(10).path, shop_11, options);
                        shop_11.setTag(list.get(10).id);
                        break;

                }
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
