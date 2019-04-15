package com.hooloovoochimico.clickablestickyheader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ExampleAdapter().apply {
            itemClickListener = { item ->
                Toast.makeText(this@MainActivity,
                    "Tapped item: ${(item as Item).text}",Toast.LENGTH_SHORT).show()
            }

            stickyHeaderClickListener = {item ->
                Toast.makeText(this@MainActivity,
                    "Tapped Header: ${(item as Header).text}",Toast.LENGTH_SHORT).show()
            }

            items.addAll(getFakeList())
        }

        val stickyList = findViewById<StickyRecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

        }

        stickyList.setRecyclerStiky(adapter)

        adapter.notifyDataSetChanged()


    }
}

fun getFakeList() : List<Any>{
    val l = mutableListOf<Any>()
    for (i in 0..30){
        l.add(if(i.rem(5) == 0) Header("HEADER $i") else Item("Item $i"))
    }

    return l.toList()
}

/**
 * MODELS
 */
class Header(val text:String)
class Item(val text:String)


/**
 * Adapter.
 * This class extends AdapterStickyHeader
 */
class ExampleAdapter : AdapterStickyHeader() {



    val items = mutableListOf<Any>()

    var itemClickListener : ((Any) -> Unit)?  = null
    var stickyHeaderClickListener: ((Any) -> Unit)? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when ( p1){
            HEADER_VIEW -> {
                HeaderViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.header_row,p0,false))
            }
            else -> {
                ItemViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_row,p0,false))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        when(p0){
            is HeaderViewHolder -> {
                p0.text.text = (items[p1] as Header).text
                p0.itemView.setOnClickListener {
                    stickyHeaderClickListener?.invoke(items[p1])
                }
            }
            is ItemViewHolder -> {
                p0.text.text = (items[p1] as Item).text
                p0.itemView.setOnClickListener {
                    itemClickListener?.invoke(items[p1])
                }
            }
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPosition = itemPosition
        var headerPosition = 0
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getItemViewType(position: Int): Int {
        return if(isHeader(position)){
            HEADER_VIEW
        }else{
            ITEM_VIEW
        }
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.header_row
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header?.findViewById<TextView>(R.id.text)?.text = (items[headerPosition] as Header).text
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return   items[itemPosition] is Header
    }

    override fun propatageTap(headerPosition: Int): Boolean {
        stickyHeaderClickListener?.invoke(items[headerPosition])
        return true
    }

    companion object {
        const val HEADER_VIEW = 1
        const val ITEM_VIEW = 2
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var text = view.findViewById<TextView>(R.id.text)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var text = view.findViewById<TextView>(R.id.text)
    }
}

