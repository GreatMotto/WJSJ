package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

/**
 * 二级分类gridview adapter
 * Created by Administrator on 2015/7/31 0031.
 */
public class ClassifiyGridAdapter extends BaseAdapter {
    private Context context;
    public List<Result> result;
    private String parentTypeName;
    private String Level1Id;
//    private ClassifyNameBean classifyNameBean;

    public ClassifiyGridAdapter(Context context, List<Result> result,String parentTypeName,String Level1Id) {
        this.context = context;
        this.result = result;
        this.parentTypeName=parentTypeName;
        this.Level1Id = Level1Id;
//        this.classifyNameBean = classifyNameBean;
    }

    @Override
    public int getCount() {
//        return classifyNameBean.children.size();
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.classifiy_grid_item, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
        SimpleDraweeView iv_shop_imag = ViewHolder.get(convertView, R.id.iv_shop_imag);
        iv_shop_imag.setAspectRatio(1.0f);
//        iv_shop_imag.setImageURI(Uri.parse(classifyNameBean.children.get(position).picUrl));
//        tv_shop_name.setText(classifyNameBean.children.get(position).moduleName);
        iv_shop_imag.setImageURI(Uri.parse(Urls.PHOTO + result.get(position).path));
        tv_shop_name.setText(result.get(position).name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondClassifyActivity.class);
                //intent.putExtra(Constant.ID, position);
//                intent.putExtra("name", classifyNameBean);
                intent.putExtra("parentFlag", "0");
                intent.putExtra("allTypes", (Serializable)result);
                intent.putExtra("Level1Id", Level1Id);
                intent.putExtra("curType",(Serializable)result.get(position));
                intent.putExtra("parentTypeName", parentTypeName);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void setName(List<Result> result) {
        this.result = result;
        this.notifyDataSetChanged();

    }
}
