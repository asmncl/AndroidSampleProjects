package we.dev.drawroutev2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;



public class MainActivity extends Activity {
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	double src_lat = 17;
	double src_long = 78;
	double dest_lat = 18;
	double dest_long = 77;
	 MarkerOptions markerOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
		myMap = myMapFragment.getMap();

		

		
		LatLng srcLatLng = new LatLng(src_lat, src_long);
		LatLng destLatLng = new LatLng(dest_lat, dest_long);
		
		 myMap.addMarker(new MarkerOptions()
	       .position(srcLatLng).title("Source place"));
		 
		 myMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));
		 
		 myMap.addMarker(new MarkerOptions()
	       .position(destLatLng).title("Destination place"));
		 
		// Enabling MyLocation in Google Map
		myMap.setMyLocationEnabled(true);
		myMap.getUiSettings().setZoomControlsEnabled(true);
		myMap.getUiSettings().setCompassEnabled(true);
		myMap.getUiSettings().setMyLocationButtonEnabled(true);
		myMap.getUiSettings().setAllGesturesEnabled(true);
		myMap.setTrafficEnabled(true);
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng,12));
        markerOptions = new MarkerOptions();
         
		 
		// Polyline line = myMap.addPolyline(new PolylineOptions().add(srcLatLng, destLatLng).width(5).color(Color.RED));
		 
		 connectAsyncTask _connectAsyncTask = new connectAsyncTask();
         _connectAsyncTask.execute(); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_legalnotices:
			String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
					getApplicationContext());
			AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
			LicenseDialog.setTitle("Legal Notices");
			LicenseDialog.setMessage(LicenseInfo);
			LicenseDialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS){
			Toast.makeText(getApplicationContext(), 
					"isGooglePlayServicesAvailable SUCCESS", 
					Toast.LENGTH_LONG).show();
		}else{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}

	}

	 private class connectAsyncTask extends AsyncTask<Void, Void, Void>{
	        private ProgressDialog progressDialog;
	       
	        @Override
	        protected void onPreExecute() {
	            // TODO Auto-generated method stub
	            super.onPreExecute();
	            progressDialog = new ProgressDialog(MainActivity.this);
	            progressDialog.setMessage("Fetching route, Please wait...");
	            progressDialog.setIndeterminate(true);
	            progressDialog.show();
	        }
	        @Override
	        protected Void doInBackground(Void... params) {
	            // TODO Auto-generated method stub
	            fetchData();
	            return null;
	        }
	        @Override
	        protected void onPostExecute(Void result) {
	            // TODO Auto-generated method stub
	            super.onPostExecute(result);
	            if(doc!=null){
	                NodeList _nodelist = doc.getElementsByTagName("status");
	                Node node1 = _nodelist.item(0);
	                String _status1 = node1.getChildNodes().item(0).getNodeValue();
	                if(_status1.equalsIgnoreCase("OK")){
	                    NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
	                    Node node_path = _nodelist_path.item(0);
	                    Element _status_path = (Element)node_path;
	                    NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
	                    Node _nodelist_dest = _nodelist_destination_path.item(0);
	                    String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
	                    List<LatLng> directionPoint = decodePoly(_path);
	                    
	                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
	                  for (int i = 0; i < directionPoint.size(); i++) {
	                        rectLine.add(directionPoint.get(i));
	                  }
	                  // Adding route on the map
	                  myMap.addPolyline(rectLine);
	                  markerOptions.position(new LatLng(dest_lat, dest_long));
	                  markerOptions.draggable(true);
	                  myMap.addMarker(markerOptions);
	                }else{
	                    showAlert("Unable to find the route");
	                }

	                
	            }else{
	                showAlert("Unable to find the route");
	            }
	            
	            progressDialog.dismiss();

	        }

	    }
	 
	    Document doc = null;
	    private void fetchData()
	    {	    	
	        StringBuilder urlString = new StringBuilder();
	        urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
	        urlString.append(src_lat);
	        urlString.append(",");
	        urlString.append(src_long);
	        urlString.append("&destination=");//to
	        urlString.append(dest_lat);
	        urlString.append(",");
	        urlString.append(dest_long);
	        urlString.append("&sensor=true&mode=driving");
	        Log.d("url","::"+urlString.toString());
	        HttpURLConnection urlConnection= null;
	        URL url = null;
	        try
	        {
	            url = new URL(urlString.toString());
	            urlConnection=(HttpURLConnection)url.openConnection();
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setDoOutput(true);
	            urlConnection.setDoInput(true);
	            urlConnection.connect();
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
	        }catch (MalformedURLException e){
	            e.printStackTrace();
	        }catch (IOException e){
	            e.printStackTrace();
	        }catch (ParserConfigurationException e){
	            e.printStackTrace();
	        }
	        catch (SAXException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    
	    private void showAlert(String message){
	        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
	        alert.setTitle("Error");
	        alert.setCancelable(false);
	        alert.setMessage(message);
	        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub

	            }
	        });
	        alert.show();
	    }
	    
	     private ArrayList<LatLng> decodePoly(String encoded) {
	         ArrayList<LatLng> poly = new ArrayList<LatLng>();
	         int index = 0, len = encoded.length();
	         int lat = 0, lng = 0;
	         while (index < len) {
	             int b, shift = 0, result = 0;
	             do {
	                 b = encoded.charAt(index++) - 63;
	                 result |= (b & 0x1f) << shift;
	                 shift += 5;
	             } while (b >= 0x20);
	             int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	             lat += dlat;
	             shift = 0;
	             result = 0;
	             do {
	                 b = encoded.charAt(index++) - 63;
	                 result |= (b & 0x1f) << shift;
	                 shift += 5;
	             } while (b >= 0x20);
	             int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	             lng += dlng;

	             LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	             poly.add(position);
	         }
	         return poly;
	     }
}