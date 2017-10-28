package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.edu.gdmec.android.mobileguard.R;

public class LostFindActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mSafePhoneTV;
    private RelativeLayout mInterSetupRL;
    private SharedPreferences msharePreferences;
    private ToggleButton mTogglebutton;
    private TextView mProtectStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        msharePreferences = getSharedPreferences("config",MODE_PRIVATE);
        if (!isSetUp()){
            //如果没有进入过设置向导，则进入
            startSetUp1Activity();
        }
        initView();
    }



    private boolean isSetUp(){
        return msharePreferences.getBoolean("isSetUp",false);
    }
    /**初始化控件*/
    private void  initView(){
        TextView mTitleTV = (TextView)findViewById(R.id.tv_title);
        mTitleTV.setText("手机防盗");
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mSafePhoneTV = (TextView)findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(msharePreferences.getString("safephone",""));
        mTogglebutton = (ToggleButton)findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL = (RelativeLayout)findViewById(R.id.rl_titlebar);
        mInterSetupRL.setOnClickListener(this);
        mProtectStatusTV = (TextView)findViewById(R.id.tv_lostfind_protectstauts);
        //查询手机防盗是否开启
        boolean protecting = msharePreferences.getBoolean("protecting",true);
        if (protecting){
            mProtectStatusTV.setText("");
            mTogglebutton.setChecked(true);
        }else{
            mProtectStatusTV.setText("");
            mTogglebutton.setChecked(false);
        }
        mTogglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharePreferences.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }
    private  void startSetUp1Activity(){
        Intent intent=new Intent(LostFindActivity.this,Setup1Activty.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_inter_setup_wizard:
                //重新进入设置向导
                startSetUp1Activity();
                break;
            case R.id.imgv_leftbtn:
                //重新进入设置向导
                finish();
                break;
        }
    }
}
