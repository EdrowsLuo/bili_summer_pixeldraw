package com.just.library.agentweb;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.AgentWebUtils;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.DefaultMsgConfig;
import com.just.library.DownLoadResultListener;
import com.just.library.LogUtils;
import com.just.library.WebDefaultSettingsManager;

/**
 * Created by cenxiaozhong on 2017/5/15.
 *
 *
 *
 * source code  https://github.com/Justson/AgentWeb
 */

public class AgentWebFragment extends Fragment implements FragmentKeyDown {


    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected AgentWeb mAgentWeb;
    public static final String URL_KEY = "url_key";
    private ImageView mMoreImageView;
    private PopupMenu mPopupMenu;

    public static AgentWebFragment getInstance(Bundle bundle) {

        AgentWebFragment mAgentWebFragment = new AgentWebFragment();
        if (bundle != null)
            mAgentWebFragment.setArguments(bundle);

        return mAgentWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agentweb, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//
                .setIndicatorColorWithHeight(-1, 2)//
                .setAgentWebWebSettings(getSettings())//
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);




          DefaultMsgConfig.DownLoadMsgConfig mDownLoadMsgConfig=mAgentWeb.getDefaultMsgConfig().getDownLoadMsgConfig();
        //  mDownLoadMsgConfig.setCancel("放弃");  // 修改下载提示信息，这里可以语言切换


    }


    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            //do you work
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
           //do you work
        }
    };

    public AgentWebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    public String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://www.jd.com";
        }
        return target;
    }

    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mTitleTextView != null && !TextUtils.isEmpty(title))
                if (title.length() > 10)
                    title = title.substring(0, 10) + "...";
            mTitleTextView.setText(title);

        }
    };
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
          //  super.onProgressChanged(view, newProgress);
            //Log.i("Info","onProgressChanged:"+newProgress+"  view:"+view);
        }
    };
    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }


        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            LogUtils.i("Info", "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?vid=XODEzMjU1MTI4&refer=&tuid=&ua=Mozilla%2F5.0%20(Linux%3B%20Android%207.0%3B%20SM-G9300%20Build%2FNRD90M%3B%20wv)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Chrome%2F58.0.3029.83%20Mobile%20Safari%2F537.36&source=exclusive-pageload&cookieid=14971464739049EJXvh|Z6i1re#Intent;scheme=youku;package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone"))
                return true;
            /*else if (isAlipay(view, url))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
                return true;*/

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.i("Info", "url:" + url + " onPageStarted  target:" + getUrl());
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
    }

    protected void initView(View view) {
        mBackImageView = (ImageView) view.findViewById(R.id.iv_back);
        mLineView = view.findViewById(R.id.view_line);
        mFinishImageView = (ImageView) view.findViewById(R.id.iv_finish);
        mTitleTextView = (TextView) view.findViewById(R.id.toolbar_title);
        mBackImageView.setOnClickListener(mOnClickListener);
        mFinishImageView.setOnClickListener(mOnClickListener);
        mMoreImageView = (ImageView) view.findViewById(R.id.iv_more);
        mMoreImageView.setOnClickListener(mOnClickListener);
        pageNavigator(View.GONE);
    }



     private void pageNavigator(int tag) {

        mBackImageView.setVisibility(tag);
        mLineView.setVisibility(tag);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {

                case R.id.iv_back:

                    if (!mAgentWeb.back())
                        AgentWebFragment.this.getActivity().finish();

                    break;
                case R.id.iv_finish:
                    AgentWebFragment.this.getActivity().finish();
                    break;
                case R.id.iv_more:
                    showPoPup(v);
                    break;

            }
        }


    };


    private void openBrowser(String targetUrl) {
        if(!TextUtils.isEmpty(targetUrl)&&targetUrl.startsWith("file://")){
            AgentWebUtils.toastShowShort(this.getContext(),targetUrl+" 该链接无法使用浏览器打开。");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(targetUrl);
        intent.setData(url);
        startActivity(intent);
    }

    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this.getActivity(), view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.refresh:
                    if (mAgentWeb != null)
                        mAgentWeb.getLoader().reload();
                    return true;

                case R.id.copy:
                    if (mAgentWeb != null)
                        toCopy(AgentWebFragment.this.getContext(), mAgentWeb.getWebCreator().get().getUrl());
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null)
                        openBrowser(mAgentWeb.getWebCreator().get().getUrl());
                    return true;
                default:
                    return false;
            }

        }
    };


    private void toCopy(Context context, String text) {

        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        //mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
        //  mAgentWeb.destroy();
    }
}
