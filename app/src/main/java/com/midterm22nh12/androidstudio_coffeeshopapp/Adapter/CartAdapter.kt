package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ChangeNumberItemsListener
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ManagmentCart
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderCartBinding

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null,
    private val readonly: Boolean = false
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root)

    private val managmentCart = ManagmentCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${String.format("%.2f", item.price)}"
        holder.binding.totalEachItem.text = "$${String.format("%.2f", item.price * item.numberInCart)}"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl.firstOrNull() ?: "")
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picCart)

        if (readonly) {
            holder.binding.plusEachItem.visibility = View.GONE
            holder.binding.minusEachItem.visibility = View.GONE
            holder.binding.removeItemBtn.visibility = View.GONE
        } else {
            holder.binding.plusEachItem.setOnClickListener {
                managmentCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }

            holder.binding.minusEachItem.setOnClickListener {
                managmentCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }

            holder.binding.removeItemBtn.setOnClickListener {
                managmentCart.romveItem(listItemSelected, position, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
