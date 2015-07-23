package com.xiaoying.networkmanager;

import android.app.Activity;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView mTvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvMsg = (TextView) findViewById(R.id.tv_msg);

        if(NetworkManager.getInstance(this).isNetworkConnected()) {
            mTvMsg.setText("网络已连接");
        } else {
            mTvMsg.setText("无网络已连接");
        }

        NetworkManager.getInstance(this).registerNetworkObserver(mNetworkObserver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance(this).unregisterNetworkObserver(mNetworkObserver);
    }

    private NetworkObserver mNetworkObserver = new NetworkObserver() {
        @Override
        public void onNetworkStateChaged(boolean noConnectivity, NetworkInfo currentNetwok, NetworkInfo lastNetwork) {
            if(noConnectivity) {
                mTvMsg.setText("网络已连接已断开");
            } else {
                mTvMsg.setText("网络已连接" + (null == currentNetwok ? "" : currentNetwok.toString()));
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
