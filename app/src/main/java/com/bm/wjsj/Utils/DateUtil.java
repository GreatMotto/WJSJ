package com.bm.wjsj.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/10/26.
 */
public class DateUtil {
    private static String finalDate;
    private static  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getDate(String datefrom) {

        try{
            //1.返回字段
            String[] str = datefrom.split(" ");
            String[] date = str[0].split("-");
            String[] time = str[1].split(":");
            String year = date[0];
            String month = date[1];
            String day = date[2];
            String hour = time[0];
            String minute = time[1];
            String second = time[2];

            //2.发贴日期和当前日期
            Date sourceDate = df.parse(datefrom);
            Date curDate = new Date(System.currentTimeMillis());
            String curdates = df.format(curDate);
            Date nowDate = df.parse(curdates);

            //3.日期比较
            long diff = nowDate.getTime() - sourceDate.getTime();//这样得到的差值是微秒级别
            float days = diff / (1000 * 60 * 60 * 24);

            //int dd=(int)hh/24;   //共计天数
            if(days>30)//30天外显示日期
            {
                finalDate = str[0] + " " + hour + ":" + minute;
            }
            else if(days<=30 && days>=1){//30天内的用天显示
                finalDate = (long)days + "天前";
            }
            else{
                float ss=diff/(1000); //共计秒数
                float MM = ss/60;   //共计分钟数
                float hh=ss/3600;  //共计小时数
                if(hh>1) //一天内的用小时显示
                {
                    finalDate = (long)hh + "小时前";
                }
                else{
                    if(MM>1)
                    {
                        finalDate = (long)MM + "分钟前";
                    }
                    else{
                        finalDate =  "刚刚";
                    }
                }
            }

        }catch (Exception ex){}
        return finalDate;
    }
            /*
    public static String finalDate;
    public int dy;
    public static String getDate(String datefrom) {

        String[] str = datefrom.split(" ");
        String[] date = str[0].split("-");
        String[] time = str[1].split(":");
        String year = date[0];
        String month = date[1];
        String day = date[2];
        String hour = time[0];
        String minute = time[1];
        String second = time[2];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curdates = format.format(curDate);
        String[] curstr = curdates.split(" ");
        String[] curdate = curstr[0].split("-");
        String[] curtime = curstr[1].split(":");
        String curyear = curdate[0];
        String curmonth = curdate[1];
        String curday = curdate[2];
        String curhour = curtime[0];
        String curminute = curtime[1];
        String cursecond = curtime[2];
        if (Integer.parseInt(year) < Integer.parseInt(curyear)) {
            finalDate = str[0] + " " + hour + ":" + minute;
        } else if (Integer.parseInt(month) < Integer.parseInt(curmonth) && Integer.parseInt(curmonth) - Integer.parseInt(month) != 1) {
            finalDate = year + "-" + month + "-" + day;
        } else
        if (Integer.parseInt(curmonth) - Integer.parseInt(month) == 1) {
            int dy = 0;
                    if (Integer.parseInt(month) == 1 || Integer.parseInt(month) == 3 ||
                            Integer.parseInt(month) == 5 || Integer.parseInt(month) == 7
                            || Integer.parseInt(month) == 8 || Integer.parseInt(month) == 10
                            || Integer.parseInt(month) == 12 ){
                        dy = 31;
                    }
                    if (Integer.parseInt(month) == 4 || Integer.parseInt(month) == 6 ||
                            Integer.parseInt(month) == 9 || Integer.parseInt(month) == 11){
                        dy = 30;
                    }
                    if (Integer.parseInt(month) == 2){
                        if (Integer.parseInt(year)%4 == 0){
                            dy = 29;
                        }else {
                            dy = 28;
                        }
                    }
            if ((Integer.parseInt(curday) + dy) - Integer.parseInt(day) > 30){//30天前发布
                finalDate = month + "-" + day + " " + hour + ":" + minute;
            }else {//30天内发布
                finalDate = String.valueOf((Integer.parseInt(curday) + dy) - Integer.parseInt(day)) + "天前";
            }
        } else if (Integer.parseInt(hour) < Integer.parseInt(curhour)) {
            finalDate = String.valueOf(Integer.parseInt(curhour) - Integer.parseInt(hour)) + "小时前";
        } else if (Integer.parseInt(minute) < Integer.parseInt(curminute)) {
            finalDate = String.valueOf(Integer.parseInt(curminute) - Integer.parseInt(minute)) + "分钟前";
        }else if (Integer.parseInt(minute) == Integer.parseInt(curminute)){
            finalDate = "刚刚";
        }
        return finalDate;
    }
    */
}
