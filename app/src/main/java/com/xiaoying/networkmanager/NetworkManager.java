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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态管理类
 * Created by yinglovezhuzhu@gmail.com on 2015/7/23.
 */
public class NetworkManager {

    private final Context mAppContext;

    private final ConnectivityManager mConnectivityManager;

    private final NetworkObservable mNetworkObservable = new NetworkObservable();

    private boolean mNetworkConnected = false;

    private NetworkInfo mCurrentNetwork = null;

    private static NetworkManager mInstance = null;

    private NetworkManager(Context context) {
        mAppContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mAppContext.registerReceiver(new NetworkReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // 初始化网络状态
        mCurrentNetwork = mConnectivityManager.getActiveNetworkInfo();
        mNetworkConnected = null != mCurrentNetwork && mCurrentNetwork.isConnected();
    }

    public static NetworkManager getInstance(Context context) {
        synchronized (NetworkManager.class) {
            if(null == mInstance) {
                mInstance = new NetworkManager(context);
            }
            return mInstance;
        }
    }

    /**
     * 网络是否连接
     * @return
     */
    public boolean isNetworkConnected() {
        return mNetworkConnected;
    }

    /**
     * 获取当前网络信息
     * @return 当前网络信息，如果有网络连接，则为null
     */
    public NetworkInfo getCurrentNetwork() {
        return mCurrentNetwork;
    }

    /**
     * 注册一个网络状态观察者
     * @param observer
     */
    public void registerNetworkObserver(NetworkObserver observer) {
        synchronized (mNetworkObservable) {
            mNetworkObservable.registerObserver(observer);
        }
    }

    /**
     * 反注册一个网络状态观察者
     * @param observer
     */
    public void unregisterNetworkObserver(NetworkObserver observer) {
        synchronized (mNetworkObservable) {
            mNetworkObservable.unregisterObserver(observer);
        }
    }

    /**
     * 反注册所有的观察者，建议这个只在退出程序时做清理用
     */
    public void unregisterAllObservers() {
        synchronized (mNetworkObservable) {
            mNetworkObservable.unregisterAll();
        }
    }


    /**
     * 网络状态广播接收器
     */
    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(null == intent) {
                return;
            }
            if(!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                return;
            }

            NetworkInfo lastNetwork = mCurrentNetwork;
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            mNetworkConnected = !noConnectivity;
            if(mNetworkConnected) {
                mCurrentNetwork = mConnectivityManager.getActiveNetworkInfo();
            } else {
                mCurrentNetwork = null;
                // 没有网络连接，直接返回
            }

            mNetworkObservable.notifyNetworkChaged(mNetworkConnected, mCurrentNetwork, lastNetwork);
        }
    }

}
