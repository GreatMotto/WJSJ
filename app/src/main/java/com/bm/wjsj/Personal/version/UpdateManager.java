package com.bm.wjsj.Personal.version;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;

/**
 * Created by wangxl01 on 2015/11/29.
 */
public class UpdateManager {

    // 上下文对象
    private Context mContext;
    //更新版本信息对象
    private VersionInfo info = null;
    // 下载进度条
    private ProgressBar progressBar;
    // 是否终止下载
    private boolean isInterceptDownload = false;
    //进度条显示数值
    private int progress = 0;

    /**
     * 参数为Context(上下文activity)的构造函数
     *
     * @param context
     */
    public UpdateManager(Context context) {
        this.mContext = context;
    }

    private class GetVersionTask extends AsyncTask<Void, Void, VersionInfo> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext, "提示", "正在获取最新版本...");
        }

        @Override
        protected void onPostExecute(VersionInfo result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            try {
                if (result != null) {
                    try {

                        info = result;

                        // 获取当前软件包信息
                        PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
                        // 当前软件版本号
                        int versionCode = pi.versionCode;
                        if (versionCode < info.getVersionCode()) {
                            // 如果当前版本号小于服务端版本号,则弹出提示更新对话框
                            showUpdateDialog();
                        }
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{

                }
            }
            catch (Exception ex){
                Log.e("postError:", "^^^^^^^^^^^^^^^^^^^^^^" + ex.toString());
                NewToast.show(mContext, "安装最新版本失败！", Toast.LENGTH_LONG);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected VersionInfo  doInBackground(Void... params) {

            VersionInfo  info = getVersionInfoFromServer();
            return  info;
        }
    }

    public void checkUpdate() {
        // 从服务端获取版本信息
        //info = getVersionInfoFromServer();
        GetVersionTask getVersionId = new GetVersionTask();
        getVersionId.execute();

    }

    /*
    public void checkUpdate() {
        // 从服务端获取版本信息
        info = getVersionInfoFromServer();
        if (info != null) {
            try {
                // 获取当前软件包信息
                PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
                // 当前软件版本号
                int versionCode = pi.versionCode;
                if (versionCode < info.getVersionCode()) {
                    // 如果当前版本号小于服务端版本号,则弹出提示更新对话框
                    showUpdateDialog();
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
*/

    /**
     * 从服务端获取版本信息
     *
     * @return
     */
    private VersionInfo getVersionInfoFromServer() {
        VersionInfo info = null;
        URL url = null;
        try {
            // 10.0.2.2相当于localhost
            //url = new URL("http://10.0.2.2:8080/updateApkServer/version.xml");
            //http://10.58.174.150:8080/wj_web/loadPage/wj.apk
            url = new URL(Urls.PHOTO+"/wj_web/loadPage/version.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            NewToast.show(mContext, "已是最新版本！", Toast.LENGTH_LONG);
        }
        if (url != null) {
            try {
                if(isWifi(mContext)) {
                    // 使用HttpURLConnection打开连接
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setConnectTimeout(30000);
                    urlConn.setReadTimeout(30000);
                    // 读取服务端version.xml的内容(流)
                    info = XMLParserUtil.getUpdateInfo(urlConn.getInputStream());
                    urlConn.disconnect();

                    if(info==null)
                    {
                        NewToast.show(mContext, "已是最新版本！", Toast.LENGTH_SHORT);
                    }
                }
                else{
                    NewToast.show(mContext, "请在WIFI下使用！", Toast.LENGTH_SHORT);
                }
            } catch (IOException e) {
                e.printStackTrace();
                NewToast.show(mContext, "已是最新版本！", Toast.LENGTH_SHORT);
            }
        }

        //伪代码
//        info = new VersionInfo();
//        info.setApkName("com.bm.wjsj");
//        info.setDisplayMessage("有新版本了，快更新");
//        info.setDownloadURL("http://139.196.9.230:8080/wj_web_upload/temp/1448433359351_370392.jpg");
//        info.setUpdateTime("2015-11-29");
//        info.setVersion("3.0.1");
//        info.setVersionCode(3);
        return info;
    }


    private boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 提示更新对话框
     *
     *
     *            版本信息对象
     */
    private void showUpdateDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("版本更新");
        builder.setMessage(info.getDisplayMessage());
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 弹出下载框
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 弹出下载框
     */
    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("版本更新中...");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_prgress, null);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //终止下载
                isInterceptDownload = true;
            }
        });
        builder.create().show();
        //下载apk
        downloadApk();
    }

    /**
     * 下载apk
     */
    private void downloadApk(){
        //开启另一线程下载
        Thread downLoadThread = new Thread(downApkRunnable);
        downLoadThread.start();
    }

    /**
     * 从服务器下载新版apk的线程
     */
    private Runnable downApkRunnable = new Runnable(){
        @Override
        public void run() {
            if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                //如果没有SD卡
                Builder builder = new Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("当前设备无SD卡，数据无法下载");
                builder.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return;
            }else{
                try {
                    //服务器上新版apk地址
                    //URL url = new URL("http://10.0.2.2:8080/updateApkServer/updateApkDemo2.apk");
                    URL url = new URL(Urls.PHOTO+"/wj_web/loadPage/wj.apk");
                    //URL url = new URL(info.getDownloadURL());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/");
                    if(!file.exists()){
                        //如果文件夹不存在,则创建
                        file.mkdir();
                    }
                    //下载服务器中新版本软件（写文件）
                    String apkFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" + info.getApkName();
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do{
                        int numRead = is.read(buf);
                        count += numRead;
                        //更新进度条
                        progress = (int) (((float) count / length) * 100);
                        handler.sendEmptyMessage(1);
                        if(numRead <= 0){
                            //下载完成通知安装
                            handler.sendEmptyMessage(0);
                            break;
                        }
                        fos.write(buf,0,numRead);
                        //当点击取消时，则停止下载
                    }while(!isInterceptDownload);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 声明一个handler来跟进进度条
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新进度情况
                    progressBar.setProgress(progress);
                    break;
                case 0:
                    progressBar.setVisibility(View.INVISIBLE);
                    // 安装apk文件
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 安装apk
     */
    private void installApk() {
        // 获取当前sdcard存储路径
        File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" + info.getApkName());
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        // 安装，如果签名不一致，可能出现程序未安装提示
        i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
