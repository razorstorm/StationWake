package petrifiednightmares.stationwake;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jason on 3/14/2015.
 */
public class Alert {
  private LatLng _position;
  // Shouldn't keep alerting if we don't ever exit the area.
  private boolean _alreadyAlerted;

  private double _threshold;

  public Alert(LatLng position) {
    _position = position;
    _alreadyAlerted = false;
    _threshold = 2000; //TODO 2km about 1.24 miles
  }

  /**
   * Haversine formula
   * a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
   * c = 2 ⋅ atan2( √a, √(1−a) )
   * d = R ⋅ c
   * <p/>
   * φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
   * Everything is in metric
   *
   * @param position
   * @return
   */
  private double distanceFrom(LatLng position) {
    double myLatRad = Math.toRadians(_position.latitude);
    double itLatRad = Math.toRadians(position.latitude);
    double deltaLatRad = itLatRad - myLatRad;
    double myLngRad = Math.toRadians(_position.longitude);
    double itLngRad = Math.toRadians(position.longitude);
    double deltaLngRad = itLngRad - myLngRad;

    double a = Math.pow(Math.sin(deltaLatRad/2), 2)
      + Math.cos(myLatRad) * Math.cos(itLatRad)
      * Math.pow(Math.sin(deltaLngRad) , 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    double d = Constants.EARTH_RADIUS * c;

    return Math.abs(d);
   }
}
