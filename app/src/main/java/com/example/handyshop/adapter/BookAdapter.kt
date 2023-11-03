package farrukh.remotely.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.handyshop.R
import com.example.handyshop.data.Book

class BookAdapter(
    var array: MutableList<Book>,
    var listener: ItemClick, var onSelected: OnSelected
) : RecyclerView.Adapter<BookAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val name: TextView = itemView.findViewById(R.id.book_item_title)
        val author: TextView = itemView.findViewById(R.id.book_item_author)
        val rating: TextView = itemView.findViewById(R.id.book_item_rating)
        val img: ImageView = itemView.findViewById(R.id.book_item_image_iv)
        val bookmarkIV: ImageView = itemView.findViewById(R.id.book_item_bookmark_iv)
        val bookMarkCV: CardView = itemView.findViewById(R.id.book_item_bookmark_cardview)

        val layout: CardView = itemView.findViewById(R.id.book_item_image_card_view)
        val parentView: ConstraintLayout = itemView.findViewById(R.id.book_item_parent_constraint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val item = array.get(position)

        holder.name.text = item.name
        holder.rating.text = item.reyting.toString()
        holder.author.text = item.author
        holder.img.load(item.image)

        if (item.book_progress == 1) {
            holder.bookmarkIV.setImageResource(R.drawable.book_marked_true)
        }
        if (item.book_progress == 0) {
            holder.bookmarkIV.setImageResource(R.drawable.bookmark_icon)
        }

        holder.layout.setOnClickListener {
            listener.OnItemClick(item)
        }

        holder.bookmarkIV.setOnClickListener {
            if (item.book_progress == 0) {
                holder.bookmarkIV.setImageResource(R.drawable.book_marked_true)
                item.book_progress = 1
                notifyDataSetChanged()
            } else if (item.book_progress == 1) {
                holder.bookmarkIV.setImageResource(R.drawable.bookmark_icon)
                item.book_progress = 0
                notifyDataSetChanged()
            }
            onSelected.onSelected(item)
        }

    }

    interface ItemClick {
        fun OnItemClick(book: Book)
    }

    interface OnSelected {
        fun onSelected(book: Book)
    }

}