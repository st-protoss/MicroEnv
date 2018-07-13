package com.wm.toec.microenv.ui.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toolbar;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.bean.WearResultBean;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/11.
 * 日历界面
 */

public class ActivityCalender extends AppCompatActivity implements
        com.haibin.calendarview.CalendarView.OnDateSelectedListener,
        com.haibin.calendarview.CalendarView.OnMonthChangeListener{
    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    Toolbar mToolbar;

    private int mYear;

    private FamilyMemberBean familyMemberBean;
    private List<Calendar> schemes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_calender);
        initView();
        handleIntent();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(v->{
            mCalendarView.showYearSelectLayout(mYear);
        });
        findViewById(R.id.wear_current_day).setOnClickListener(v->{
            mCalendarView.scrollToCurrent();
        });
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    /**
     * 处理Intent
     */
    private void handleIntent() {
        Intent intent = getIntent();
        familyMemberBean = (FamilyMemberBean)intent.getSerializableExtra("BEAN");
        mToolbar.setNavigationOnClickListener(v ->{
            onBackPressed();
        });
        mToolbar.setTitle(familyMemberBean.getFamilyName());
        mToolbar.setSubtitle("点击日期进行数据上传");
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    /**
     * 处理服务器返回结果
     * @param wearResultBeans
     * @return
     */
    private List<Calendar> handleWearBeans(List<WearResultBean> wearResultBeans){
        if (wearResultBeans == null){
            return null;
        }
        List<Calendar> calendars = new ArrayList<>();
        for (int i=0;i<wearResultBeans.size();i++){
            WearResultBean wearResultBean = wearResultBeans.get(i);
            if (wearResultBean.getStatus()==1){
                calendars.add(getSchemeCalendar(Integer.parseInt(wearResultBean.getYear()), Integer.parseInt(wearResultBean.getMonth()),
                        Integer.parseInt(wearResultBean.getDay()), 0xff4059, "100"));
            }else if (wearResultBean.getStatus()==2){
                calendars.add(getSchemeCalendar(Integer.parseInt(wearResultBean.getYear()), Integer.parseInt(wearResultBean.getMonth()),
                        Integer.parseInt(wearResultBean.getDay()), 0xff4059, "50"));
            }
        }
        return  calendars;

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        //点击带日期和人员ID跳转到数据上传界面
        Intent intent = new Intent(ActivityCalender.this,ActivityWearUpload.class);
        intent.putExtra("memberId",familyMemberBean.getMemberId());
        intent.putExtra("date",calendar);
        startActivity(intent);
    }

    @Override
    public void onMonthChange(int year, int month) {
        //更换月份加载那个月的签到情况 只返回50%和100%
        HttpSet.getInstance().getToecServer().getMonthWearData(Constants.userID,familyMemberBean.getMemberId()
                ,String.valueOf(year),String.valueOf(month))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(wearResultBeans -> Observable.just(handleWearBeans(wearResultBeans)))
                .subscribe(calendars -> {
                    if (calendars==null){
                        return;
                    }
                    schemes.clear();
                    schemes.addAll(calendars);
                    mCalendarView.setSchemeDate(schemes);
                    mCalendarView.update();
                },throwable -> {
                    ToastUtil.showToast("网络错误，加载上传情况失败");
                });

    }
}
