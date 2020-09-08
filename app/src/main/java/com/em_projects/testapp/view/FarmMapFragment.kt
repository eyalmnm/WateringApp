package com.em_projects.testapp.view

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.em_projects.testapp.R
import com.em_projects.testapp.data.entity.FarmData
import com.em_projects.testapp.data.entity.GeoPoint
import com.em_projects.testapp.view.base.ScopedFragment
import com.em_projects.testapp.viewmodel.FarmMapViewModelFactory
import com.em_projects.testapp.viewmodel.FramMapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fram_map_fragment.*
import kotlinx.android.synthetic.main.frams_list_fragment.loadingGrout
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class FarmMapFragment : ScopedFragment(), KodeinAware, OnMapReadyCallback {

    // Kodein Dependency Injection
    override val kodein by closestKodein()

    // Live Data Components
    private val viewModelFactory: FarmMapViewModelFactory by instance()
    private lateinit var viewModel: FramMapViewModel

    // Args Data
    private var farmId: Int? = null
    private var farmData: FarmData? = null

    // Map Components
    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var polygon: Polygon? = null
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private var markerList: MutableList<Marker> = mutableListOf()
    var marker: Marker? = null  // text marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        farmId = arguments?.getInt("farmId")
        farmData = arguments?.getParcelable("farmData")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fram_map_fragment, container, false)
        initMap(view, savedInstanceState)
        return view
    }

    private fun initMap(view: View?, savedInstanceState: Bundle?) {
        mapView = view?.findViewById(R.id.googleMap)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Farm: ${farmData?.name}"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            "click on map for create your farm"
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FramMapViewModel::class.java)
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingGrout.visibility = View.GONE
        cancelButton.setOnClickListener { activity?.onBackPressed() }
        saveButton.setOnClickListener { saveData() }
        clearButton.setOnClickListener { clearMap() }
    }

    private fun clearMap() {
        // Clear All
        polygon?.remove()
        for (marker in markerList) marker.remove()
        markerList.clear()
        latLngList.clear()
        marker?.remove()
    }

    private fun saveData() {
        launch {
            val points: MutableList<GeoPoint> = mutableListOf()
            if (latLngList.size > 0) {
                for (latLng in latLngList) {
                    points.add(GeoPoint(lat = latLng.latitude, lng = latLng.longitude))
                }
            }
            farmData?.points = points
            val response = async { viewModel.updateFarmData(farmId!!, farmData!!) }.await()
            if (response) {
                Toast.makeText(activity, "Data saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Data saving failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgressBar() {
        loadingGrout.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        loadingGrout.visibility = View.GONE
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(farmData!!.lat, farmData!!.lng),
                17F
            )
        )
        googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
        googleMap?.setOnMapClickListener {
            // Create Marker
            drawMarkers(it)
        }
        initFarm()
    }

    private fun initFarm() {
        if (farmData?.points?.isNotEmpty() == true) {
            for (point: GeoPoint in farmData?.points!!) {
                drawMarkers(LatLng(point.lat, point.lng))
            }
        }
    }

    private fun drawMarkers(latLng: LatLng) {
        val markerOption = MarkerOptions().position(latLng)
        val marker = googleMap?.addMarker(markerOption)
        latLngList.add(latLng)
        markerList.add(marker!!)
        if (latLngList.size > 2) {
            drawPolygon()
        }
    }

    private fun drawPolygon() {
        // Clear the "old" polygon before drawing the new one
        marker?.remove()
        polygon?.remove()
        // Draw the new Polygon
        val polygonOption = PolygonOptions().addAll(latLngList).clickable(true)
        polygon = googleMap?.addPolygon(polygonOption)
        polygon?.strokeColor = Color.CYAN
        polygon?.fillColor = resources.getColor(R.color.polygon_semi_transparent)
        val centerPoint = centroid(latLngList)
        addText(activity, googleMap, centerPoint, "75%", 2, 20)
    }

    override fun onResume() {
        mapView?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
        super.onLowMemory()
    }

    private fun addText(
        context: Context?, map: GoogleMap?,
        location: LatLng?, text: String?, padding: Int,
        fontSize: Int
    ): Marker? {

        if (context == null || map == null || location == null || text == null || fontSize <= 0) {
            return marker
        }
        val textView = TextView(context)
        textView.text = text
        textView.textSize = fontSize.toFloat()
        val paintText: Paint = textView.paint
        val boundsText = Rect()
        paintText.getTextBounds(text, 0, textView.length(), boundsText)
        paintText.textAlign = Align.CENTER
        val conf = Bitmap.Config.ARGB_8888
        val bmpText = Bitmap.createBitmap(
            boundsText.width() + 2
                    * padding, boundsText.height() + 2 * padding, conf
        )
        val canvasText = Canvas(bmpText)
        paintText.color = Color.WHITE
        canvasText.drawText(
            text, canvasText.width / 2F,
            canvasText.height.toFloat() - padding - boundsText.bottom, paintText
        )
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
            .anchor(0.5f, 1f)
        marker = map.addMarker(markerOptions)
        return marker
    }

    private fun centroid(points: List<LatLng>): LatLng? {
        val centroid = doubleArrayOf(0.0, 0.0)
        for (i in points.indices) {
            centroid[0] += points[i].latitude
            centroid[1] += points[i].longitude
        }
        val totalPoints = points.size
        centroid[0] = centroid[0] / totalPoints
        centroid[1] = centroid[1] / totalPoints
        return LatLng(centroid[0], centroid[1])
    }
}