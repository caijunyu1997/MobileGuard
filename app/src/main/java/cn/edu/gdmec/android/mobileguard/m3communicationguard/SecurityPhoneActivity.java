package cn.edu.gdmec.android.mobileguard.m3communicationguard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.adapter.BlackContactAdapter;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;

public class SecurityPhoneActivity extends AppCompatActivity implements View.OnClickListener{
    /** 有黑名单时，显示的帧布局 */
    private FrameLayout mHaveBlackNumber;
    /** 没有黑名单时，显示的帧布局 */
    private FrameLayout mNoBlackNumber;
    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber = 0;
    private int pagesiz = 4;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    private void fillData(){
        dao = new BlackNumberDao(SecurityPhoneActivity.this);
        totalNumber = dao.getTotalNumber();
        if (totalNumber ==0){
            //数据库没有黑名单数据
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }else if (totalNumber > 0){
            //数据库含有黑名单数据
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber = 0;
            if (pageBlackNumber.size() > 0){
                pageBlackNumber.clear();
            }
            pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber, pagesiz));
            if (adapter == null){
                adapter = new BlackContactAdapter(pageBlackNumber, SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactAdapter.BlackConactCallBack(){
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("通讯卫士");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mHaveBlackNumber = (FrameLayout)findViewById(R.id.fl_haveblacknumber);
        mNoBlackNumber = (FrameLayout)findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        mListView = (ListView)findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView abslistView, int i) {
                switch (i){   //i是列表的滚动状态
                    //case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:  //列表滑动后静止
                    //case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:  //手指正拖着列表滑动
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //列表正自由滑动
                        //获取最后一个可见条目
                        int lastVisiblePosition = mListView.getLastVisiblePosition();
                        //如果当前条目是最后一个 增查询更多的数据
                        if (lastVisiblePosition == pageBlackNumber.size() -1){
                            pagenumber++;
                            if (pagenumber * pagesiz >= totalNumber){
                                Toast.makeText(SecurityPhoneActivity.this,"没有更多数据了", Toast.LENGTH_LONG).show();
                            }else{
                                pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber,pagesiz));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_phone);
        initView();
        fillData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_addblacknumber:
                //跳转至添加黑名单页面
                startActivity(new Intent(this, AddBlackNumberActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dao.getTotalNumber() > 0){
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
        }else{
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }
        pagenumber = 0;
        pageBlackNumber.clear();
        pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber, pagesiz));
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}