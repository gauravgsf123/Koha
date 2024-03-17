package com.bbb.koha.module.my_account.charges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bbb.koha.R
import com.bbb.koha.databinding.ItemChargesListBinding
import com.bbb.koha.databinding.ItemCheckoutListBinding
import com.bbb.koha.databinding.ItemHoldsListBinding
import com.bbb.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbb.koha.module.my_account.summary.model.HoldsResponseModel
import com.bbb.koha.utils.Utils
import com.squareup.picasso.Picasso

class ChargeAdapter (
    private val itemList: ArrayList<ChargesResponseModel>
) : RecyclerView.Adapter<ChargeAdapter.SummaryDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemChargesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SummaryDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SummaryDetailViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvDate.text = item.date?.let{Utils.changeDateFormat(it)}
        holder.binding.tvDescription.text = item.description
        holder.binding.tvAmount.text = item.amountOutstanding.toString()
    }

    class SummaryDetailViewHolder(
        val binding: ItemChargesListBinding
    ) : RecyclerView.ViewHolder(binding.root)
}


