package com.example.factslistapplication.facts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.factslistapplication.R
import com.example.factslistapplication.facts.model.Row
import com.example.factslistapplication.utils.imageLoading
import kotlinx.android.synthetic.main.items_list_facts.view.*

class FactsItemAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FactsItemAdapter.FactsViewHolder>() {

    private val items = mutableListOf<Row>()

    /**
     * Notifies the adapter to load the list again.
     *
     * @param rowData to be added in list.
     */
    fun setItems(rowData: List<Row>?) {
        items.clear()
        if (!rowData.isNullOrEmpty()) {
            rowData.filter { !it.title.isNullOrBlank() }
                .forEach { items.add(it) }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        val holder = FactsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.items_list_facts, parent, false)
        )

        holder.itemView.setOnClickListener { itemClickListener.onItemClick(holder.row) }
        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        holder.setFactsData(items[position])
    }

    /**
     * Holds Items data
     */
    class FactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var row: Row

        fun setFactsData(row: Row) {
            this.row = row
            itemView.tvFactsTitle.text = row.title
            itemView.tvFactsDescription.text = row.description
            itemView.ivFactsImage.imageLoading(row.imageHref ?: "")
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(row: Row)
}
