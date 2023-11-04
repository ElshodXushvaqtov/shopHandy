package farrukh.remotely.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.handyshop.R
import com.example.handyshop.data.Comment

class CommentAdapter(
    private var array: List<Comment>,

    ) : RecyclerView.Adapter<CommentAdapter.MyHolder>() {


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var img = itemView.findViewById<ImageView>(R.id.userImage)
        var userName = itemView.findViewById<TextView>(R.id.username)
        var rating = itemView.findViewById<TextView>(R.id.rating)
        var comment_body = itemView.findViewById<TextView>(R.id.comment_body)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val item = array.get(position)

//        holder.rating.text = item.
        holder.userName.text = item.username
        holder.comment_body.text = item.text

    }


}