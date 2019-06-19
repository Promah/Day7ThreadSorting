package com.example.day7threadsorting

abstract class Sorter<T>(val comparator: Comparator<T>, val notifier: (Array<T>) -> Unit) {
    abstract fun sort(arr: Array<T>)

    companion object {
        var PERIOD = 0
    }

    private var lastCallTs = 0L
    fun filteredNotify(arr: Array<T>) {
        if (PERIOD > 0) {
            val now = System.currentTimeMillis()
            if (now - lastCallTs > PERIOD) {
                notifier(arr)
                lastCallTs = now
            }
        } else {
            notifier(arr)
        }
    }
}


class BubbleSorter<T>(comparator: Comparator<T>, notifier: (Array<T>) -> Unit) : Sorter<T>(comparator, notifier){
    override fun sort(arr: Array<T>) {
        for (i in 0 until arr.size) {
            for (j in i + 1 until arr.size) {
                if (comparator.compare(arr[i], arr[j]) < 0) {
                    val t = arr[i]
                    arr[i] = arr[j]
                    arr[j] = t

                    filteredNotify(arr)
                }
            }
        }
    }
}


class InsertSorter<T>(comparator: Comparator<T>, notifier: (Array<T>) -> Unit) : Sorter<T>(comparator, notifier){
    override fun sort(arr: Array<T>) {
        for (j in 1 until arr.size) {
            val key = arr[j]
            var i = j - 1
            while (i >= 0 && comparator.compare(key, arr[i]) > 0) {
                arr[i +  1] = arr[i--]
                filteredNotify(arr)
            }

            arr[i + 1] = key
            filteredNotify(arr)
        }
    }
}

class HeapSorter<T>(comparator: Comparator<T>, notifier: (Array<T>) -> Unit) : Sorter<T>(comparator, notifier){

    private var heapSize = 0

    override fun sort(arr: Array<T>) {
        buildMaxHeap(arr)
        var i: Int = arr.size - 1
        while (i >= 1){
            val t = arr[i]
            arr[i] = arr[0]
            arr[0] = t
            heapSize--
            maxHeapify(arr, 0)
//            filteredNotify(arr)
            i--
        }
    }


    private fun buildMaxHeap(arr: Array<T>){
        heapSize = arr.size
        var i: Int = Math.floor(arr.size / 2.0).toInt()
        while (i >= 0){
            maxHeapify(arr, i)
            i--
        }
    }

    private fun maxHeapify(arr: Array<T>, i: Int){
        val leftElementIndex = left(i)
        val rightElementIndex = right(i)
        var largestElementIndex : Int = i

        if ( (leftElementIndex <= heapSize - 1) && (comparator.compare(arr[i],arr[leftElementIndex]) > 0) ){
            largestElementIndex = leftElementIndex
        }

        if ( (rightElementIndex <= heapSize - 1) && (comparator.compare(arr[largestElementIndex],arr[rightElementIndex]) > 0) ){
            largestElementIndex = rightElementIndex
        }

        if (largestElementIndex != i){
            val t = arr[i]
            arr[i] = arr[largestElementIndex]
            arr[largestElementIndex] = t
            filteredNotify(arr)
            maxHeapify(arr, largestElementIndex)
        }
    }

    private fun left(i: Int) : Int =
        2 * i + 1

    private fun right(i: Int) : Int =
        2 * i + 2

}