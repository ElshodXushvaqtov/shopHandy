package farrukh.remotely.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.handyshop.R
import com.example.handyshop.data.CategoryData
import com.google.android.material.card.MaterialCardView


class CategoryAdapter(
    var array: MutableList<CategoryData>,
    var context: Context,
    var listener: ItemClick
) : RecyclerView.Adapter<CategoryAdapter.MyHolder>() {
    var current = 0

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text = itemView.findViewById<TextView>(R.id.category_name1)
        var cardView = itemView.findViewById<MaterialCardView>(R.id.category_mcv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_home, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val item = array.get(position)


        if (position == 0) {
            holder.text.text = "All"
        } else {
            holder.text.text = array[position - 1].type_name.capitalize()
        }
        if (current == position) {
            holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.back_blue))
            holder.text.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.white))
            holder.text.setTextColor(context.resources.getColor(R.color.back_blue))
        }
        holder.cardView.setOnClickListener {
            if (position != current) {
                notifyItemChanged(current)
                current = position
                notifyItemChanged(current)
                if (position == 0) listener.OnItemClick("")
                else listener.OnItemClick(array[position - 1].type_name)
            }
        }
    }

    interface ItemClick {
        fun OnItemClick(category: String)
    }

}