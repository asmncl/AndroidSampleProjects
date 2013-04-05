package com.agarwal.readspeak;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ListActivity  implements OnInitListener {
private int MY_DATA_CHECK_CODE = 0;
	
	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<SMSData> smsList = new ArrayList<SMSData>();
		
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c= getContentResolver().query(uri, null, null ,null,null);
		startManagingCursor(c);
		
		// Read the sms data and store it in the list
		if(c.moveToFirst()) {
			for(int i=0; i < c.getCount(); i++) {
				SMSData sms = new SMSData();
				sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
				sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
				smsList.add(sms);
				
				c.moveToNext();
			}
		}
		c.close();
		
		// Set smsList in the ListAdapter
		setListAdapter(new ListAdapter(this, smsList));
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		SMSData sms = (SMSData)getListAdapter().getItem(position);
		
		//Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();*/

		if (sms.getBody()!=null && sms.getBody().length()>0) {
			Toast.makeText(MainActivity.this, "Saying: " + sms.getBody(), Toast.LENGTH_LONG).show();
			tts.speak(sms.getBody(), TextToSpeech.QUEUE_ADD, null);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

	}

	@Override
	public void onInit(int status) {		
		if (status == TextToSpeech.SUCCESS) {
			Toast.makeText(MainActivity.this, 
					"Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
		}
		else if (status == TextToSpeech.ERROR) {
			Toast.makeText(MainActivity.this, 
					"Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
		}
	}
	
}
