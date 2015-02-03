/*
 * Copyright 2015 Ying-Chun Liu (PaulLiu) <paulliu@debian.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.debian.paulliu.kajiradoubra.controller;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class KajiraDouBraController extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager mViewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar mActionBar;
    // Tab titles
    private String[] mTabs = { "LRF", "Level", "Pattern", "Console" };
    // Bluetooth
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothSocket mBluetoothSocket = null;
    // SPP UUID
    private static final UUID SPP_UUID =
	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String LOGTAG = "KajiraDouBraLRFController";
     
    class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int index) {

	    switch (index) {
	    case 0:
		return new LRFFragment();
	    case 1:
		return new LevelFragment();
	    case 2:
		return new PatternFragment();
	    case 3:
		return new ConsoleFragment();
	    }
	
	    return null;
	}

	@Override
	public int getCount() {
	    // get item count - equal to number of tabs
	    return 4;
	}
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);

	// Initilization
	mViewPager = (ViewPager) findViewById(R.id.main_pager);
	mActionBar = getActionBar();
	mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

	mViewPager.setAdapter(mAdapter);
	mActionBar.setHomeButtonEnabled(false);
	mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	// Adding Tabs
	for (String tab_name : mTabs) {
	    mActionBar.addTab(mActionBar.newTab().setText(tab_name).setTabListener(this));
	}

	/**
         * on swiping the viewpager make respective tab selected
         * */
	mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
		    // on changing the page
		    // make respected tab selected
		    mActionBar.setSelectedNavigationItem(position);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	    });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	// on tab selected
	// show respected fragment view
	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onStart() {
	super.onStart();
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	if (mBluetoothAdapter == null) {
	    // Device does not support Bluetooth
	    Log.w(LOGTAG,"No Bluetooth Adapter");
	    return;
	}
	if (!mBluetoothAdapter.isEnabled()) {
	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	}
    }

    @Override
    public void onResume() {
	super.onResume();
	java.util.Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	mBluetoothDevice = null;
	// If there are paired devices
	if (pairedDevices.size() > 0) {
	    // Loop through paired devices
	    for (BluetoothDevice device : pairedDevices) {
		// Add the name and address to an array adapter to show in a ListView
		if (device.getName().compareTo("KajiraDouBra")==0) {
		    mBluetoothDevice = device;
		    Log.i(LOGTAG, "BT Mac: "+device.getAddress());
		    break;
		}
	    }
	}
	if (mBluetoothDevice != null) {
	    try {
		mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
	    } catch (java.io.IOException e) {
		Log.w(LOGTAG, "Cannot get BluetoothSocket");
	    }
	    
	    // Discovery is resource intensive.  Make sure it isn't going on
	    // when you attempt to connect and pass your message.
	    mBluetoothAdapter.cancelDiscovery();

	    // Establish the connection.  This will block until it connects.
	    Log.d(LOGTAG, "Connecting to Device...");
	    try {
		mBluetoothSocket.connect();
		Log.d(LOGTAG, "Connection established and data link opened");
	    } catch (java.io.IOException e) {
		try {
		    mBluetoothSocket.close();
		} catch (java.io.IOException e2) {
		    Log.e(LOGTAG, "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
		}
	    }
	}
    }

    @Override
    public void onPause() {
	super.onPause();
	java.io.OutputStream outputStream = null;
	try {
	    outputStream = mBluetoothSocket.getOutputStream();
	} catch (java.io.IOException e) {
	}
	
	if (outputStream != null) {
	    try {
		outputStream.flush();
	    } catch (java.io.IOException e) {
		Log.e(LOGTAG, "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
	    }
	}
	try {
	    mBluetoothSocket.close();
	} catch (java.io.IOException e) {
	    Log.e(LOGTAG, "In onPause() and failed to close socket." + e.getMessage() + ".");
	}
	mBluetoothSocket = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main_activity_actions, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle presses on the action bar items
	switch (item.getItemId()) {
	case R.id.action_settings:
	    //openSettings();
	    Log.d(LOGTAG, "open Settings");
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    public void sendBluetoothData(String s) {
	java.io.PrintStream outputStream = null;
	try {
	    outputStream = new java.io.PrintStream(mBluetoothSocket.getOutputStream());
	} catch (java.io.IOException e) {
	    Log.w(LOGTAG, "Cannot get outputStream from BluetoothSocket");
	}
	
	if (outputStream != null) {
	    outputStream.print(s);
	    outputStream.flush();
	} else {
	    Log.w(LOGTAG, "Bluetooth outputStream is null");
	}
    }
}
