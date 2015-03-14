package petrifiednightmares.stationwake;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapClickListener, TextWatcher {
  private GoogleMap _map;
  private ImageView _pin;
  private ImageView _shadow;
  private AutoCompleteTextView _searchBar;
  private Geocoder _geo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
      setContentView(R.layout.activity_map);
      MapFragment mapFragment = (MapFragment) getFragmentManager()
        .findFragmentById(R.id.map);
      mapFragment.getMapAsync(this);

      _pin = (ImageView) findViewById(R.id.pin);
      _shadow = (ImageView) findViewById(R.id.shadow);

      _searchBar = (AutoCompleteTextView) findViewById(R.id.search_bar);
      _searchBar.addTextChangedListener(this);

      _geo = new Geocoder(getBaseContext());
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_map, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onMapReady(GoogleMap map) {
    _map = map;

    _map.setMyLocationEnabled(true);
    _map.setOnCameraChangeListener(this);

    UiSettings settings = map.getUiSettings();
    settings.setZoomControlsEnabled(true);
    settings.setCompassEnabled(true);
    settings.setZoomGesturesEnabled(true);
    settings.setScrollGesturesEnabled(true);
    settings.setTiltGesturesEnabled(true);
    settings.setRotateGesturesEnabled(true);
  }

  @Override
  public void onCameraChange(CameraPosition cameraPosition) {
    LatLng newPosition = cameraPosition.target;
    _map.addMarker(new MarkerOptions()
      .position(newPosition)
      .title("Hello world"));
  }

  @Override
  public void onMapClick(LatLng latLng) {

  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        onTouchDown();
        break;
      case MotionEvent.ACTION_UP:
        onTouchUp();
        break;
    }
    return super.dispatchTouchEvent(ev);
  }

  public void onTouchDown() {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) _pin.getLayoutParams();
    layoutParams.topMargin = -52;
    _pin.setLayoutParams(layoutParams);
    _shadow.setVisibility(View.VISIBLE);
  }

  public void onTouchUp() {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) _pin.getLayoutParams();
    layoutParams.topMargin = -38;
    _pin.setLayoutParams(layoutParams);
    _shadow.setVisibility(View.GONE);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    System.out.println(s.toString());

    List<Address> gotAddresses = null;
    try {
      gotAddresses = _geo.getFromLocationName(s.toString(), 5);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String[] addresses = new String[gotAddresses.size()];
    for (int i = 0; i < addresses.length; i++) {
      addresses[i] = gotAddresses.get(i).toString();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
      android.R.layout.simple_dropdown_item_1line, addresses);
    _searchBar.setAdapter(adapter);
  }
}
