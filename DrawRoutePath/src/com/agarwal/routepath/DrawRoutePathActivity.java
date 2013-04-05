package com.agarwal.routepath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DrawRoutePathActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.click).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedData data = SharedData.getInstance();
		        //Note you must change the below values as needed.
		        data.setAPIKEY("0RUTLH7cqd6yrZ0FdS0NfQMO3lioiCbnH-BpNQQ");
		        data.setSrc_lat(17);
		        data.setSrc_lng(78);
		        data.setDest_lat(18);
		        data.setDest_lng(77);
		        startActivity(new Intent(DrawRoutePathActivity.this,RoutePath.class));				
			}
		});
        
    }
}