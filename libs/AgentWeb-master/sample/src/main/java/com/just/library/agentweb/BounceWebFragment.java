package com.just.library.agentweb;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.AgentWebUtils;

/**
 * Created by cenxiaozhong on 2017/7/1.
 *  source CODE  https://github.com/Justson/AgentWeb
 */

public class BounceWebFragment extends AgentWebFragment {

    public static BounceWebFragment getInstance(Bundle bundle){

        BounceWebFragment mBounceWebFragment =new BounceWebFragment();
        if(mBounceWebFragment !=null)
            mBounceWebFragment.setArguments(bundle);

        return mBounceWebFragment;
    }


    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//
                .setIndicatorColorWithHeight(-1, 2)//
                .setAgentWebWebSettings(getSettings())//
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setWebLayout(new WebLayout(this.getActivity()))
                .setSecurityType(AgentWeb.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());



        addBGChild((FrameLayout) mAgentWeb.getWebCreator().getGroup()); // 得到 AgentWeb 最底层的控件
        initView(view);

    }




    protected void addBGChild(FrameLayout frameLayout) {

        TextView mTextView=new TextView(frameLayout.getContext());
        mTextView.setText("技术由 AgentWeb 提供");
        mTextView.setTextSize(16);
        mTextView.setTextColor(Color.parseColor("#727779"));
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
        FrameLayout.LayoutParams mFlp=new FrameLayout.LayoutParams(-2,-2);
        mFlp.gravity= Gravity.CENTER_HORIZONTAL;
        mFlp.topMargin= AgentWebUtils.dp2px(frameLayout.getContext(),15);
        frameLayout.addView(mTextView,0,mFlp);
    }


}
