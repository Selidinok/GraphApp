package com.example.graph.presenter

import android.os.Bundle
import android.view.View
import com.example.graph.R
import com.example.model.domain.Point
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pointer.core.base.BaseFragment
import com.pointer.core.consts.ScreenEnum
import com.pointer.core.extensions.extraNotNull
import kotlinx.android.synthetic.main.graph_layout.*
import kotlinx.android.synthetic.main.graph_table_item.view.*
import timber.log.Timber

class GraphFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.graph_layout

    private val points by extraNotNull<Array<Point>>(ScreenEnum.GRAPH.name, emptyArray<Point>())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val p = arguments?.getParcelableArray(ScreenEnum.GRAPH.name)
        Timber.d("Points = $points, p = $p")
        points.forEachIndexed { index, point ->
            val row = createRow(index, point)
            table.addView(row)
        }
        setupChart(points)
    }

    private fun createRow(index: Int, point: Point): View =
        layoutInflater.inflate(R.layout.graph_table_item, null).apply {
            number.text = index.toString()
            xColumn.text = point.x
            yColumn.text = point.y
        }

    private fun setupChart(points: Array<Point>) {
        val entries = points.map { Entry(it.x.toFloat(), it.y.toFloat()) }
        val dataSet = LineDataSet(entries, "Points")
        val lineData = LineData(dataSet)
        chart.apply {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            setScaleEnabled(true)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawAxisLine(true)
                setDrawGridLines(false)
            }
            axisLeft.apply {
                setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
                setDrawGridLines(false)
                setDrawAxisLine(true)
            }
        }
    }
}