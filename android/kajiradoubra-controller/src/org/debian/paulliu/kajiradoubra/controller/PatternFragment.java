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

public class PatternFragment extends Fragment implements View.OnClickListener {

    private KajiraDouBraController mActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View rootView = inflater.inflate(R.layout.pattern_fragment, container, false);
	return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	mActivity = (KajiraDouBraController)getActivity();

	Button button = null;
	button = (Button)mActivity.findViewById(R.id.button_pattern_stop);
	button.setOnClickListener(this);
	button = (Button)mActivity.findViewById(R.id.button_pattern_FF);
	button.setOnClickListener(this);
	button = (Button)mActivity.findViewById(R.id.button_pattern_11claps);
	button.setOnClickListener(this);
	
    }
    
    @Override
    public void onClick(View view) {
	switch (view.getId()) {
	case R.id.button_pattern_stop:
	    onClicked_stop(view);
	    break;
	case R.id.button_pattern_FF:
	    onClicked_FF(view);
	    break;
	case R.id.button_pattern_11claps:
	    onClicked_11claps(view);
	    break;
	}
    }

    public void onClicked_stop(View view) {
	sendBluetoothData("lArA");
    }
    public void onClicked_FF(View view) {
	sendBluetoothData("(lAr/sDlKr0sDlUrqsDlgrgsDlqrUsDl0rKsDl/rAsDl0rKsDlqrUsDlgrgsDlUrqsDlKr0sDlAr/sD)");
    }
    public void onClicked_11claps(View view) {
	sendBluetoothData("(lurusElArAsElurusElArAsEl/r/sClArAsCl/r/sClArAsClurusElArAsEl/r/sClArAsCl/r/sClArAsCl/r/sClArAsCl/r/sClArAsGl/r/sClArAsClurusElArAsE)");
    }

    private void sendBluetoothData(String s) {
	mActivity.sendBluetoothData(s);
    }
    
}
