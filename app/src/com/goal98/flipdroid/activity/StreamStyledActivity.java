package com.goal98.flipdroid.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.goal98.flipdroid.R;
import com.goal98.flipdroid.multiscreen.MultiScreenSupport;
import com.goal98.flipdroid.util.DeviceInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by IntelliJ IDEA.
 * User: janexie
 * Date: 12-1-26
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class StreamStyledActivity extends ActivityGroup implements TabHost.TabContentFactory, CompoundButton.OnCheckedChangeListener {
    private ArticleAdapter adapter;
    private RadioButton[] mRadioButtons;
    private TabHost tabHost;
    private DeviceInfo deviceInfo;
    private int bottomHeight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        deviceInfo = DeviceInfo.getInstance(this);
        bottomHeight = MultiScreenSupport.getInstance(deviceInfo).getBottomRadioHeight();
        tabHost = (TabHost) findViewById(R.id.tabHost);
        FrameLayout tabcontent = (FrameLayout)findViewById(android.R.id.tabcontent);
        tabcontent.setPadding(0,0,0,bottomHeight);
        tabHost.setup(this.getLocalActivityManager());

        String myStream = this.getString(R.string.mystream);
        tabHost.addTab(tabHost.newTabSpec(myStream)
                .setIndicator(myStream)
                .setContent(this));
        String myFeeds = this.getString(R.string.my_feed);
        tabHost.addTab(tabHost.newTabSpec(myFeeds)
                .setIndicator(myFeeds)
                .setContent(new Intent(this, IndexActivity.class)));
        String addFeeds = this.getString(R.string.addfeeds);
        tabHost.addTab(tabHost.newTabSpec(addFeeds)
                .setIndicator(addFeeds)
                .setContent(new Intent(this, SiteActivity.class)));
//        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 65;
//        tabHost.getTabWidget().setBackgroundResource(R.drawable.like);
        initRadios();
    }


    public View createTabContent(String s) {
        LayoutInflater li = LayoutInflater.from(this);
        View wrapper = li.inflate(R.layout.stream, null);
        final PullToRefreshListView mPullRefreshListView = (PullToRefreshListView) (wrapper.findViewById(R.id.pull_refresh_list));
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask(mPullRefreshListView).execute();
            }
        });
        adapter = new ArticleAdapter(this, mPullRefreshListView.getAdapterView(), R.layout.lvloading, R.layout.stream_styled_article_view, new ArticleLoader(this, 8), R.layout.nodataview);

        adapter.forceLoad();
        return wrapper;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        private PullToRefreshListView mPullRefreshListView;

        public GetDataTask(PullToRefreshListView mPullRefreshListView) {
            this.mPullRefreshListView = mPullRefreshListView;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            return null;
        }


        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView == mRadioButtons[0]) {
                tabHost.setCurrentTabByTag(this.getString(R.string.mystream));
            } else if (buttonView == mRadioButtons[1]) {
                tabHost.setCurrentTabByTag(this.getString(R.string.my_feed));
            }else if (buttonView == mRadioButtons[2]) {
                tabHost.setCurrentTabByTag(this.getString(R.string.addfeeds));
            }
            for (int i = 0; i < mRadioButtons.length; i++) {
            RadioButton mRadioButton = mRadioButtons[i];
            if(buttonView== mRadioButton)
                mRadioButton.setSelected(true);
            else
                mRadioButton.setSelected(false);
        }
        }

    }

    private void initRadios() {
        RadioGroup localRadioGroup = (RadioGroup) findViewById(R.id.main_radio);
        RadioButton[] arrayOfRadioButton1 = new RadioButton[5];
        this.mRadioButtons = arrayOfRadioButton1;

        localRadioGroup.getLayoutParams().height = bottomHeight;

        for (int i = 0; i < arrayOfRadioButton1.length; i++) {
            RadioButton radioButton = (RadioButton) localRadioGroup.findViewWithTag("radio_button" + i);
            radioButton.setOnCheckedChangeListener(this);

            mRadioButtons[i] = radioButton;
        }

    }
}


