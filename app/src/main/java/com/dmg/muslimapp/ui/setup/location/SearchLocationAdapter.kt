package com.dmg.muslimapp.ui.setup.location

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseViewHolder
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.gms.tasks.Tasks
import java.util.ArrayList
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlinx.android.synthetic.main.item_location_result.view.*

class SearchLocationAdapter(private val context: Context, private val presenter: LocationFragmentPresenter, private val mGeoDataClient: GeoDataClient, private val mBounds: LatLngBounds, private val mPlaceFilter: AutocompleteFilter?) : RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>(), Filterable {
    private var mResultList: ArrayList<AutocompletePrediction>

    init {
        mResultList = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SearchLocationAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(R.layout.item_location_result, viewGroup, false)
        )

    }

    override fun onBindViewHolder(viewHolder: SearchLocationAdapter.ViewHolder, i: Int) {
        viewHolder.onBind(i)
    }

    override fun getItemCount(): Int {
        return mResultList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                Log.d(TAG, "performFiltering: " + constraint!!.toString())
                val results = Filter.FilterResults()

                var filterData: ArrayList<AutocompletePrediction>? = ArrayList()

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = getAutocomplete(constraint)
                }

                results.values = filterData
                if (filterData != null) {
                    results.count = filterData.size
                } else {
                    results.count = 0
                }

                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mResultList = results.values as ArrayList<AutocompletePrediction>
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //                    notifyDataSetInvalidated();
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private lateinit var prediction: AutocompletePrediction

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)
            prediction = mResultList[position]
            itemView.tv_location.text = prediction.getPrimaryText(STYLE_BOLD)
        }

        override fun clear() {

        }

        @OnClick(R.id.root_layout)
        fun choose() {
            presenter.getDetailPlace(prediction.placeId!!)
            Log.e("PLACE ID", prediction.placeId!!.toString())
            mResultList.clear()
            notifyDataSetChanged()
        }
    }

    private fun getAutocomplete(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        Log.i(TAG, "Starting autocomplete query for: $constraint")

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.
        val results = mGeoDataClient.getAutocompletePredictions(constraint.toString(), LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)), null)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        try {
            val autocompletePredictions = results.result

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.count
                    + " predictions.")

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions)
        } catch (e: RuntimeExecutionException) {
            // If the query did not complete successfully return null
            Toast.makeText(context, "Error contacting API: " + e.toString(),
                    Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error getting autocomplete prediction API call", e)
            return null
        }

    }

    companion object {

        private val TAG = SearchLocationAdapter::class.java.simpleName
        private val STYLE_BOLD = StyleSpan(Typeface.BOLD)
    }
}