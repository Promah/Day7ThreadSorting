package com.example.day7threadsorting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val random = Random()
    @Volatile private var workingSorterThreadsCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartSorting.setOnClickListener {
            startSorting()
        }
    }

    private fun startSorting(){
        if (workingSorterThreadsCount == 0){
            val dataForSorting = Array(barsView.height / BarsView.COLORS_COUNT){random.nextInt(BarsView.MAX_VALUE)}
            for (i in 0 until BarsView.COLORS_COUNT)
                doSorting(i, dataForSorting.copyOf())
        }else{
            Toast.makeText(this,"workingSorterThreadsCount = $workingSorterThreadsCount, wait until it == 0",Toast.LENGTH_SHORT).show()
        }
    }

    private fun doSorting(dataColour: Int, dataForSorting: Array<Int>){
        barsView.setData(dataColour, dataForSorting)
        thread {
            if (workingSorterThreadsCount++ < BarsView.COLORS_COUNT){
                val comparator = kotlin.Comparator<Int> { o1, o2 -> Thread.sleep(1); o2 - o1 }
                val notifier = {arr: Array<Int> ->
                    runOnUiThread{
                        barsView.setData(dataColour, arr)
                    }
                }
                val sorter = when(dataColour){
                    BarsView.DATA_COLOURS.DATA_COLOR_BLUE.ordinal -> BubbleSorter(comparator, notifier)
                    BarsView.DATA_COLOURS.DATA_COLOR_RED.ordinal -> InsertSorter(comparator, notifier)
                    BarsView.DATA_COLOURS.DATA_COLOR_GREEN.ordinal -> HeapSorter(comparator, notifier)
                    else -> throw Exception("wrong dataColour id fun doSorting")
                }
                sorter.sort(dataForSorting)
                workingSorterThreadsCount--
            }
        }
    }
}
