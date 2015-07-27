/*
 * Copyright (C) 2015 The Android Open Source Project.
 *
 *        yinglovezhuzhu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
        public void onNetworkStateChaged(boolean networkConnected, NetworkInfo currentNetwok, NetworkInfo lastNetwork) {
            if(networkConnected) {
                mTvMsg.setText("网络已连接" + (null == currentNetwok ? "" : currentNetwok.toString()));
            } else {
                mTvMsg.setText("网络已连接已断开");
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
