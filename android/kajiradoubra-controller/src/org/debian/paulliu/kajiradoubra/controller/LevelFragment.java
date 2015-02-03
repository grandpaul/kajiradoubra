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

public class LevelFragment extends Fragment {

    private KajiraDouBraController mActivity = null;
    private static final String LEVELSTR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View rootView = inflater.inflate(R.layout.level_fragment, container, false);
	return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	mActivity = (KajiraDouBraController)getActivity();
	
	SeekBar seekBarLeft = (SeekBar)mActivity.findViewById(R.id.seekbar_level_left);
	seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		}
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    onLeftProgressChanged(progress);
		}
	    });
	
	SeekBar seekBarRight = (SeekBar)mActivity.findViewById(R.id.seekbar_level_right);
	seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		    // TODO Auto-generated method stub
		}
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    onRightProgressChanged(progress);
		}
	    });
    }

    public void onLeftProgressChanged(int progress) {
	if (0 <= progress && progress < LEVELSTR.length()) {
	    sendBluetoothData(String.format("l%1$s", LEVELSTR.charAt(progress)));
	}
    }
    public void onRightProgressChanged(int progress) {
	if (0 <= progress && progress < LEVELSTR.length()) {
	    sendBluetoothData(String.format("r%1$s", LEVELSTR.charAt(progress)));
	}
    }

    private void sendBluetoothData(String s) {
	mActivity.sendBluetoothData(s);
    }
    
}
