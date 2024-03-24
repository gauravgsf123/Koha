package com.bbb.koha.module.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bbb.koha.R
import com.bbb.koha.databinding.ItemCheckoutListBinding
import com.bbb.koha.databinding.ItemNotificationListBinding
import com.bbb.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbb.koha.module.notification.model.NotificationModel
import com.bbb.koha.utils.Utils
import com.squareup.picasso.Picasso

class NotificationAdapter (
    private val itemList: ArrayList<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvMessage.text = item.message
        holder.binding.tvDateTime.text = item.dateTime

    }

    class NotificationViewHolder(
        val binding: ItemNotificationListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface NotificationDetailCallBack{
        fun onRenewClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
        fun onTitleClickListener(adapterPosition: Int, checkoutResponseModel: CheckoutResponseModel)
    }
}


