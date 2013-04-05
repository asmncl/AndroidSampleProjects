package com.agarwal.callcard;



import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String accessCode = getStringFromSP("AccessCode");
		String pinNumber = getStringFromSP("PinNumber");
		
		if(accessCode.equals("")|| pinNumber.equals("")){
			Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
			startActivity(intent);
		}
		
		findViewById(R.id.call).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 0);  
			}
		});	
		
	}
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case 0 :
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					String number = "";
					Uri uri = data.getData();

					if (uri != null) {
						Cursor c = null;
						try {
							c = getContentResolver().query(uri, new String[]{ 
									ContactsContract.CommonDataKinds.Phone.NUMBER,  
									ContactsContract.CommonDataKinds.Phone.TYPE },
									null, null, null);

							if (c != null && c.moveToFirst()) {
								number = c.getString(0);
								Log.d("number",""+number);

							}
						} finally {
							if (c != null) {
								c.close();
							}
						}
					}
					try {
						String accessCode = getStringFromSP("AccessCode");
						String pinNumber = getStringFromSP("PinNumber");
						
						if(accessCode.equals("")|| pinNumber.equals("")){
							Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
							startActivityForResult(intent,1);
						}else{
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:"+accessCode+","+pinNumber+","+number.replace("+", "00")+"#"));
							startActivity(callIntent);
						}
					} catch (ActivityNotFoundException activityException) {
						Log.e("helloandroid dialing example", "Call failed",activityException);
					}	
				}
			}
		break;
		case 1:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            startActivityForResult(intent, 0);  
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case R.id.menu_settings:
			Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void saveStringInSP(String key, String value){
		SharedPreferences preferences = getSharedPreferences("CallCard", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	private String getStringFromSP(String key) {
		SharedPreferences preferences = getSharedPreferences("CallCard", android.content.Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}
}
