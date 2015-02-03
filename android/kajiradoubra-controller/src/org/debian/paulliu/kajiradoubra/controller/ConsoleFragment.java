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

public class ConsoleFragment extends Fragment {

    private KajiraDouBraController mActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View rootView = inflater.inflate(R.layout.console_fragment, container, false);
	return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	mActivity = (KajiraDouBraController)getActivity();
	
	EditText edittext = (EditText) mActivity.findViewById(R.id.edittext_console_cmd);
	edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView view, int actionId, android.view.KeyEvent event) {
		    boolean handled = false;
		    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
			sendBluetoothData("l0r0");
			String cmd = String.format("(%1$s)",view.getText());
			sendBluetoothData(cmd);
			Log.i(KajiraDouBraController.LOGTAG,"send cmd "+cmd);
			handled = true;
		    }
		    return handled;
		}
	    });
	Button button = null;
	button = (Button)mActivity.findViewById(R.id.button_console_send);
	button.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		    sendBluetoothData("l0r0");
		    KajiraDouBraController mActivity = (KajiraDouBraController)getActivity();
		    EditText edittext = (EditText) mActivity.findViewById(R.id.edittext_console_cmd);
		    String cmd = String.format("(%1$s)",edittext.getText());
		    sendBluetoothData(cmd);
		    Log.i(KajiraDouBraController.LOGTAG,"send cmd "+cmd);
		}
	    });
    }

    private void sendBluetoothData(String s) {
	mActivity.sendBluetoothData(s);
    }
    
}
