import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
public class GeocodingDemoActivity extends MapActivity
{
    Geocoder geocoder = null;
    MapView mapView = null;
    ProgressDialog progDialog=null;
    List<Address> addressList=null;

    @Override
    protected boolean isLocationDisplayed() {
        return false;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.geocode);
        mapView = (MapView)findViewById(R.id.geoMap);
        mapView.setBuiltInZoomControls(true);

        // lat/long of Jacksonville, FL
        int lat = (int)(30.334954*1000000);
        int lng = (int)(-81.5625*1000000);
        GeoPoint pt = new GeoPoint(lat,lng);
        mapView.getController().setZoom(10);
        mapView.getController().setCenter(pt);

        Button geoBtn =(Button)findViewById(R.id.geocodeBtn);

        geocoder = new Geocoder(this);

        geoBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View view) {
                EditText loc = (EditText)findViewById(R.id.location);
                String locationName = loc.getText().toString();

                progDialog = 
ProgressDialog.show(GeocodingDemoActivity.this, 
"Processing...", "Finding Location...", true, false);

                findLocation(locationName);
            }});

    }

    private void findLocation(final String locationName)
    {
        Thread thrd = new Thread()
        {
            public void run()
            {
                try {
                    // do backgrond work
                    addressList = geocoder.getFromLocationName(locationName, 5);
                    //send message to handler to process results
                    uiCallback.sendEmptyMessage(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thrd.start();
    }
    // ui thread callback handler
    private Handler uiCallback = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            progDialog.dismiss();

            if(addressList!=null && addressList.size()>0)
            {
                int lat = (int)(addressList.get(0).getLatitude()*1000000);
                int lng = (int)(addressList.get(0).getLongitude()*1000000);
                GeoPoint pt = new GeoPoint(lat,lng);
                mapView.getController().setZoom(15);
                mapView.getController().setCenter(pt);
            }
            else
            {
                Dialog foundNothingDlg = new 
AlertDialog.Builder(GeocodingDemoActivity.this)
                  .setIcon(0)
                  .setTitle("Failed to Find Location")
                  .setPositiveButton("Ok", null)
                  .setMessage("Location Not Found...")
                .create();
                foundNothingDlg.show();
            }
        }
    };
}

