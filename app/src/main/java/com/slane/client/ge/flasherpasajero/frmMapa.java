package com.slane.client.ge.flasherpasajero;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class frmMapa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IAsyncEvent, MapboxMap.OnMapClickListener {

    private MapView mapView;
    public IAsyncEvent Evento = null;
    static private Thread _threadAsyncSincronizar;
    static private AsyncGenerarDatos oAsyncSincronizar;
    static MapboxMap mapboxMap;
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    int contador = 0;
    double latant = 0;
    double logant = 0;
    static MapboxMap.OnMapClickListener listener;
    LatLng miposicion;

    static public boolean isrun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.Evento = this;
        listener = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frmMapa.isrun = true;

                originPosition = Point.fromLngLat(logant, latant);
                destinationPosition = Point.fromLngLat(miposicion.getLongitude(), miposicion.getLatitude());

                getRoute(originPosition, destinationPosition);

                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build();

                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(frmMapa.this, options);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMapg) {
            // Customize map with markers, polylines, etc.

                mapboxMap = mapboxMapg;

                AsyncGenerarDatos oAsyncSincronizar = new AsyncGenerarDatos();
                oAsyncSincronizar.evento = frmMapa.this.Evento;
                _threadAsyncSincronizar = new Thread(oAsyncSincronizar);
                _threadAsyncSincronizar.start();

                mapboxMap.addOnMapClickListener(listener);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.frm_mapa, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void Event(String event, String args) {
        this.runOnUiThread(new Runnable() {
            public void run() {

                if(contador > 0)
                {
                    destinationPosition = Point.fromLngLat(Double.valueOf(args), Double.valueOf(event));
                    originPosition = Point.fromLngLat(logant, latant);
                    getRoute(originPosition, destinationPosition);

                    latant = Double.valueOf(event);
                    logant = Double.valueOf(args);
                }
                else
                {
                    contador++;
                    latant = Double.valueOf(event);
                    logant = Double.valueOf(args);
                }

                IconFactory iconFactory = IconFactory.getInstance(frmMapa.this);
                Icon icon = iconFactory.fromResource(R.drawable.bus);

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(event), Double.valueOf(args)))
                        .icon(icon)
                        .title("Llegara en x min."));

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(Double.valueOf(event), Double.valueOf(args))) // Sets the new camera position
                        .zoom(13) // Sets the zoom
                        .bearing(180) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
            }
        });
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

        IconFactory iconFactory = IconFactory.getInstance(frmMapa.this);
        Icon icon = iconFactory.fromResource(R.drawable.pasagero);

        miposicion = point;
        mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                        .icon(icon)
                .title("Mi posición actual."));

//        originPosition = Point.fromLngLat(logant, latant);
//        destinationPosition = Point.fromLngLat(miposicion.getLongitude(), miposicion.getLatitude());
//
//        getRoute(originPosition, destinationPosition);
//
//        boolean simulateRoute = true;
//        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                .directionsRoute(currentRoute)
//                .shouldSimulateRoute(simulateRoute)
//                .build();
//
//        // Call this method with Context from within an Activity
//        NavigationLauncher.startNavigation(frmMapa.this, options);
    }
}
