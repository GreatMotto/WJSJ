package com.bm.wjsj.Date;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.CityBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/*首页卡片adapter*/
public class SwipeAdapter extends BaseAdapter {
    private Context mContext;
    private MainActivity ac;
    //   private ArrayList<String> list;
    private List<UserInfo> list;
    private int height;
    private DecimalFormat decimalFormat =new DecimalFormat("0.00");

    public SwipeAdapter(Context mContext, List<UserInfo> list, MainActivity ac) {
        this.mContext = mContext;
        this.list = list;
        this.ac = ac;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_item,
                    parent, false);
        }
        TextView tv_age = ViewHolder.get(convertView, R.id.tv_trystAge);
//        convertView=  View.inflate(mContext, R.layout.my_item,null);
        TextView person_name = ViewHolder.get(convertView, R.id.tv_trystName);
        TextView tv_trystLevel=ViewHolder.get(convertView, R.id.tv_trystLevel);
        TextView tv_trystSex=ViewHolder.get(convertView, R.id.tv_trystSex);
        TextView tv_trystDate=ViewHolder.get(convertView, R.id.tv_trystDate);
        TextView tv_trystSign=ViewHolder.get(convertView, R.id.tv_trystSign);
        TextView tv_trystDistance=ViewHolder.get(convertView, R.id.tv_trystDistance);
        TextView tv_trystAddsress=ViewHolder.get(convertView, R.id.tv_trystAddsress);

        final SimpleDraweeView iv_person = ViewHolder.get(convertView, R.id.iv_person);
        View rl_bottom = ViewHolder.get(convertView, R.id.rl_bottom);
        rl_bottom.measure(0, 0);
        height = rl_bottom.getMeasuredHeight();
//        iv_person.setAspectRatio(1.0f);
        int width = DisplayUtil.getWidth(mContext);
        //DisplayUtil.setLayoutParams(iv_person, width, width - DisplayUtil.dip2px(mContext, 39));
        String picUri= Urls.PHOTO + list.get(position).head;
        Uri imageUri=Uri.parse(picUri);
        //String imagePath=imageUri.toString();
        //ImageLoader.getInstance().displayImage(imagePath, iv_person, ImageUtils.getOptions());
        //ImageLoader.getInstance().displayImage(list.get(position).head, iv_person, ImageUtils.getOptions());

        if (list.get(position).head.equals("")) {
            if (list.get(position).sex.equals("0")) {
                iv_person.setImageResource(R.mipmap.touxiangnan);
            } else {
                iv_person.setImageResource(R.mipmap.touxiangnv);
            }
        }else {
            iv_person.setImageURI(imageUri);
        }
        tv_age.setText(list.get(position).age + "岁");
        person_name.setText(getFilterName(list.get(position).nickname,8));
        //person_name.setText(list.get(position).nickname);
        tv_trystLevel.setText("V"+list.get(position).level);
        if("1".equals(list.get(position).sex))
        {
            tv_trystSex.setText("美女");
        }
        else{
            tv_trystSex.setText("帅哥");
        }
        if(!"".equals(list.get(position).hours))
        {
            tv_trystDate.setText(list.get(position).hours+"小时前");
        }
        if(!"".equals(list.get(position).minutes))
        {
            tv_trystDate.setText(list.get(position).minutes+"分钟前");

        }
        tv_trystSign.setText(getFilterName(list.get(position).sign,40));
        if (!TextUtils.isEmpty(list.get(position).distance)){
            double distance = Double.parseDouble(list.get(position).distance);
            if (distance >= 1000.0) {
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                int julis = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                tv_trystDistance.setText(julis + "km");
            } else if (distance < 1000.00 && distance > 10.00){
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                double julis = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                tv_trystDistance.setText(julis + "km");
            }else {
                tv_trystDistance.setText(0.01 + "km");
            }
        }
        tv_trystAddsress.setText(AddressUtil.getInstance(mContext).getCityNameById(list.get(position).provinceId, list.get(position).cityId));
        //tv_trystAddsress.setText(getCityNameById(list.get(position).provinceId,list.get(position).cityId));
//        iv_person.setImageURI(Uri.parse(list.get(position).path));
//        view.setTag("Card");
//        ac.getSlidingMenu().addIgnoredView(view);
        return convertView;
    }

    public int getHeight() {
        return height;
    }




    //函数功能：从一个字符串中按字节数截取一部分，但不能截取出半个中文（GBK码表）
    private String getFilterName(String str,int key) {
        try {
            byte[] buf = str.getBytes("GBK");//编码
            if(buf.length>(key+2)) {
                //因为中文GBK是返回两个负数，所以从最后一个开始往前遍历
                //如果负数个数为单数，那么为半个中文，舍弃，否则为一个中文，返回
                int count = 0;
                for (int x = key - 1; x >= 0; x--) {
                    if (buf[x] < 0)
                        count++;
                    else
                        break;
                }

                if (count % 2 == 0)//解码。如果为单数就是半个中文，要舍弃最后的数
                    return new String(buf, 0, key, "GBK")+"…";
                else
                    return new String(buf, 0, key - 1, "GBK")+"…";
            }
            else{
                return str;
            }

        } catch (Exception ex) {
            return str;
        }
    }


    //函数功能：从一个字符串中按字节数截取一部分，但不能截取出半个中文（GBK码表）
    private String getFilterSign(String str,int key) {
        try {
            str=str+"                                                ";
            byte[] buf = str.getBytes("GBK");//编码
            if(buf.length>(key+2)) {
                //因为中文GBK是返回两个负数，所以从最后一个开始往前遍历
                //如果负数个数为单数，那么为半个中文，舍弃，否则为一个中文，返回
                int count = 0;
                for (int x = key - 1; x >= 0; x--) {
                    if (buf[x] < 0)
                        count++;
                    else
                        break;
                }

                if (count % 2 == 0)//解码。如果为单数就是半个中文，要舍弃最后的数
                    return new String(buf, 0, key, "GBK")+"…";
                else
                    return new String(buf, 0, key - 1, "GBK")+"…";
            }
            else{
                return str;
            }

        } catch (Exception ex) {
            return str;
        }
    }




}

