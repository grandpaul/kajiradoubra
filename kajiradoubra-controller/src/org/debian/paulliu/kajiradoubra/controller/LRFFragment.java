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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.util.Log;
import android.widget.*;

public class LRFFragment extends Fragment {

    private KajiraDouBraController mActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View rootView = inflater.inflate(R.layout.lrf, container, false);
	return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	mActivity = (KajiraDouBraController)getActivity();
	
	View forwardButton = mActivity.findViewById(R.id.button_forward);
	forwardButton.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent e) {
		    if (e.getAction() == MotionEvent.ACTION_DOWN) {
			return onForwardButtonPressed(view);
		    } else if (e.getAction() == MotionEvent.ACTION_UP) {
			return onForwardButtonReleased(view);
		    }
		    return false;
		}
	    });
	
	View leftButton = mActivity.findViewById(R.id.button_left);
	leftButton.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent e) {
		    if (e.getAction() == MotionEvent.ACTION_DOWN) {
			return onLeftButtonPressed(view);
		    } else if (e.getAction() == MotionEvent.ACTION_UP) {
			return onLeftButtonReleased(view);
		    }
		    return false;
		}
	    });

	View rightButton = mActivity.findViewById(R.id.button_right);
	rightButton.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent e) {
		    if (e.getAction() == MotionEvent.ACTION_DOWN) {
			return onRightButtonPressed(view);
		    } else if (e.getAction() == MotionEvent.ACTION_UP) {
			return onRightButtonReleased(view);
		    }
		    return false;
		}
	    });
	
    }

    public boolean onForwardButtonPressed(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onForwardButtonPressed");
	sendBluetoothData("lMrM");
	return false;
    }
    public boolean onForwardButtonReleased(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onForwardButtonReleased");
	sendBluetoothData("l0r0");
	return false;
    }
    public boolean onLeftButtonPressed(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onLeftButtonPressed");
	sendBluetoothData("lM");
	return false;
    }
    public boolean onLeftButtonReleased(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onLeftButtonReleased");
	sendBluetoothData("l0");
	return false;
    }
    public boolean onRightButtonPressed(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onRightButtonPressed");
	sendBluetoothData("rM");
	return false;
    }
    public boolean onRightButtonReleased(View view) {
	Log.i(KajiraDouBraController.LOGTAG,"onRightButtonReleased");
	sendBluetoothData("r0");
	return false;
    }

    private void sendBluetoothData(String s) {
	mActivity.sendBluetoothData(s);
    }
    
}
