package com.agarwal.callcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final EditText accessCode = (EditText)findViewById(R.id.accesscode);
		accessCode.setText(getStringFromSP("AccessCode"));
		final EditText pinNumber = (EditText)findViewById(R.id.pinnumber);
		pinNumber.setText(getStringFromSP("PinNumber"));
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(accessCode.getText().toString().equals("") || pinNumber.getText().toString().equals("")){
					showAlert("Error","Please enter access code & Pin number");
				}else{
					saveStringInSP("AccessCode", accessCode.getText().toString());
					saveStringInSP("PinNumber", pinNumber.getText().toString());
					finish();
				}
			}
		});
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
	}
	private void saveStringInSP(String key, String value){
		SharedPreferences preferences = getSharedPreferences("CallCard", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	private String getStringFromSP(String key) {
		SharedPreferences preferences = getSharedPreferences("CallCard", android.content.Context.MODE_PRIVATE);
		return preferences.getString(key, null);
	}
	private void showAlert(String title, String alertMsg){
		AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(alertMsg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}//showAlert()
}
