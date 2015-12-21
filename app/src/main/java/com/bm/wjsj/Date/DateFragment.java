package com.bm.wjsj.Date;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Attention.AttentionFragment;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.MyCardView.SwipeFlingAdapterView;
import com.bm.wjsj.View.MyScrollView;
import com.bm.wjsj.View.SlidingMenu.SlidingMenu;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DateFragment extends Fragment implements APICallback.OnResposeListener {

    private SwipeFlingAdapterView my_cardview;
    private SwipeAdapter swiadapter;
    private SlidingMenu ss;
    private View v;
    private Dialog welcomeDialog;// 欢迎弹出框
    private SharedPreferencesHelper sp;
    private MainActivity ac;
    private List<UserInfo> listUser = new ArrayList<UserInfo>();
    private int pageNum = 1, pageSize = 10;
    private String sex="";
    private List<ProvinceBean> listProvince = new ArrayList<ProvinceBean>();
    private String currentUserid="";
    private boolean hasNext=true;
    private ImageView buzan_view,zan_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        hasNext=true;
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }

            //每次调用想约时，刷新数据，重新调接口
            hasNext = true;
            pageNum = 1;
            listUser.clear();
            swiadapter.notifyDataSetChanged();

            return v;
        }

        if (v == null) {
            v = inflater.inflate(R.layout.fg_date, container, false);
        }
        initView(v);
        return v;
    }

    private void initView(View v) {

        TextView tv_Title = (TextView) v.findViewById(R.id.tv_title);
        tv_Title.setText("想约");
        ImageView icon_back = (ImageView) v.findViewById(R.id.icon_back);
        icon_back.setImageResource(R.mipmap.sidebar);
        ImageView iv_shaixuan = (ImageView) v.findViewById(R.id.iv_shaixuan);
        iv_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setImageResource(R.mipmap.want_stat);
        iv_shaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.putExtra(Constant.BOOLEAN, true);
//                    startActivityForResult(intent, 5);
//                } else {
                AttentionFragment.vpPagerTemp = true;
                ac.isToggle = false;
                ac.goToFragment();
//                }
//                ac.getAttentionFragment().type = "1";
            }
        });
        v.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss.toggle();
            }
        });
        v.findViewById(R.id.tv_touming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        my_cardview = (SwipeFlingAdapterView) v.findViewById(R.id.my_cardview);

        buzan_view= (ImageView)v.findViewById(R.id.iv_buzan);
         zan_view= (ImageView)v.findViewById(R.id.iv_zan);

        v.findViewById(R.id.iv_buzan).setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 try {
                                                                     if (my_cardview.getTopCardListener() != null) {
                                                                         my_cardview.getTopCardListener().selectLeft();
                                                                     }
                                                                 } catch (Exception ex) {
                                                                 }
                                                                 //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                                                 //Log.e("action:", "不赞" +currentUserid+":"+ df.format(new Date()));
                                                             }
                                                         });
        v.findViewById(R.id.iv_zan).setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                                               //Log.e("action:", "赞"+currentUserid+":"+ df.format(new Date()));
                                                               try {
                                                                   if (my_cardview.getTopCardListener() != null) {
                                                                       my_cardview.getTopCardListener().selectRight();
                                                                   }
                                                               } catch (Exception ex) {
                                                               }
                                                           }
                                                       });
        ac = (MainActivity) getActivity();
        ss = ac.sm;

        swiadapter = new SwipeAdapter(getActivity(), listUser, ac);
        my_cardview.setAdapter(swiadapter);

//        int width = DisplayUtil.getWidth(getActivity());
//        ImageView iv_2 = (ImageView) v.findViewById(R.id.iv_2);
//        ImageView iv_1 = (ImageView) v.findViewById(R.id.iv_1);
//        DisplayUtil.setLayoutParams(iv_2, width, width + swiadapter.getHeight());
//        DisplayUtil.setLayoutParams(iv_1, width, width + swiadapter.getHeight());
        my_cardview.setMinStackInAdapter(0);
        my_cardview.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (listUser.size() > 0) {
                    currentUserid = listUser.get(0).id;//删除当前item时记录ID

                    listUser.remove(0);
                    swiadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                //System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
                //Log.e("action:", "左不赞"+currentUserid+":"+ df.format(new Date()));

            }

            @Override
            public void onRightCardExit(Object dataObject) {
               //赞
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
               // Log.e("action", "右赞"+currentUserid+":"+ df.format(new Date()));
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    NewToast.show(getActivity(), "关注失败，请先登录！", Toast.LENGTH_LONG);
                }else {
                    if (ac.checkStatus(getActivity())) {
                        WebServiceAPI.getInstance().srarchInfo(currentUserid, DateFragment.this, getActivity());
                    }
                    //WebServiceAPI.getInstance().attentionPerson(currentUserid, DateFragment.this, getActivity());
                }

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //WebServiceAPI.getInstance().serNear(gender, ageUp, ageDown, loginTime,provinceId, cityId, pageNum, pageSize, DateFragment.this, getActivity());
               if(hasNext) {
                   //Log.e("empty:", "pageNum:" + pageNum+"pageCount:");
                   WebServiceAPI.getInstance().findtryst(sex, pageNum, pageSize, DateFragment.this, getActivity());
                   //swiadapter.notifyDataSetChanged();
               }
                else {
                   //resetAttentionState(hasNext);
                   //Toast.makeText(ac,"没了",Toast.LENGTH_LONG).show();
               }
            }
        });
        my_cardview.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Log.e("action", "currentUserid:"+currentUserid+"---------"+ "clickid:"+listUser.get(itemPosition).id);
                Intent intent = new Intent(ac, MyDataActivity.class);
                intent.putExtra(Constant.ID,listUser.get(itemPosition).id);
                startActivity(intent);
            }
        });
        sp = new SharedPreferencesHelper(getActivity(), Constant.SP_FILENAME);
        if (!sp.getBooleanValue(Constant.ISWANTPOP)) {
            showWelcomeDialog();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        getData();
        // WebServiceAPI.getInstance().serNear(gender, ageUp, ageDown, loginTime,provinceId, cityId, pageNum, pageSize, DateFragment.this, getActivity());
        //Log.e("2154654","545545454525454454654641diaole ");
       //WebServiceAPI.getInstance().findtryst(sex, pageNum, pageSize, DateFragment.this, getActivity());
        //swiadapter.notifyDataSetChanged();
    }

    public void showWelcomeDialog() {
        welcomeDialog = new AlertDialog.Builder(getActivity()).create();
        welcomeDialog.show();

        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();

        welcomeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //        welcomeDialog.getWindow().setLayout(display.getWidth(),
//                WindowManager.LayoutParams.MATCH_PARENT);

        welcomeDialog.getWindow().setContentView(R.layout.frist_pop);
        welcomeDialog.getWindow().findViewById(R.id.rl_tshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeDialog.dismiss();

            }
        });
        welcomeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sp.putBooleanValue(Constant.ISWANTPOP, true);
            }
        });
    }

    private void resetAttentionState(boolean enable) {
        buzan_view.setEnabled(enable);
        zan_view.setEnabled(enable);
    }



    @Override
    public void onPause() {
        super.onPause();
        ss.clearIgnoredViews();
        ss.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        switch (tag) {
            case 11:
                resetAttentionState(true);
                if (apiResponse.data.page.currentPage == apiResponse.data.page.totalPage) {
                    pageNum = 1;
                    hasNext = false;
                } else {
                    pageNum = pageNum + 1;
                    hasNext = true;
                }
                //pageCount=apiResponse.data.list.size();
                //Log.e("successData:", "pageNum:" + pageNum+"pageCount:"+apiResponse.data.list.size());
                listUser.clear();
                listUser.addAll(apiResponse.data.list);
                swiadapter.notifyDataSetChanged();

                break;
            case 4:
                NewToast.show(getActivity(), "关注成功", Toast.LENGTH_LONG);
                break;
            case 1:
                if (apiResponse.data.appuser!=null&&"0".equals(apiResponse.data.appuser.isfollow)) {
                    WebServiceAPI.getInstance().attentionPerson(currentUserid, DateFragment.this, getActivity());
                }
                else{
                    NewToast.show(getActivity(), "已经关注该用户", Toast.LENGTH_LONG);
                }

                break;
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        switch (tag) {
            case 1:
                break;
            case 4:
                NewToast.show(getActivity(), "已经关注", Toast.LENGTH_LONG);
                break;
        }
    }
}
