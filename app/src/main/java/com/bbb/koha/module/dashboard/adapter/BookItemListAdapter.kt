package com.bbb.koha.module.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bbb.koha.databinding.ItemBookListBinding
import com.bbb.koha.databinding.ItemBookStatusListBinding
import com.bbb.koha.databinding.ItemHoldsListBinding
import com.bbb.koha.module.dashboard.model.ItemListOfBookResponseModel
import com.bbb.koha.module.my_account.summary.model.HoldsResponseModel
import com.bbb.koha.utils.Utils

class BookItemListAdapter (
    private val itemList: ArrayList<ItemListOfBookResponseModel>
) : RecyclerView.Adapter<BookItemListAdapter.BookListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val binding =
            ItemBookStatusListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BookListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.tvItemTitle.text = "Item <${item.externalId}>"//item?.itemTypeId
        holder.binding.tvItemType.text = item.itemTypeId
        holder.binding.tvBranch.text = item.homeLibraryId
        holder.binding.tvCallNumber.text = item.callnumber
       if(!item.checkedOutDate.isNullOrEmpty()) {
           holder.binding.tvIssuedDate.text = item.checkedOutDate
       }
        holder.binding.constraintTop.setOnClickListener {
            if(holder.binding.llDetail.isVisible){
                holder.binding.ivDownArrow.rotation = 0f
                holder.binding.llDetail.visibility = View.GONE
            }else {
                holder.binding.ivDownArrow.rotation = 180f
                holder.binding.llDetail.visibility = View.VISIBLE
            }
        }

        if(item.checkoutResponseModel!=null){
            holder.binding.tvIssuedDate.visibility = View.VISIBLE
            holder.binding.tvIssuedDateTitle.visibility = View.VISIBLE
        }else {
            holder.binding.tvIssuedDate.visibility = View.GONE
            holder.binding.tvIssuedDateTitle.visibility = View.GONE
        }

        if(item.itemId==item.checkoutResponseModel?.itemId){
            holder.binding.tvItem.text = "Issued"
        }else if(item.itemDetailResponseModel?.lostStatus!! >1){
            holder.binding.tvItem.text = "Lost"
        }else if(item.itemDetailResponseModel?.damagedStatus!! >1){
            holder.binding.tvItem.text = "Damage"
        }else if(item.itemDetailResponseModel?.withdrawn!! >1){
            holder.binding.tvItem.text = "Withdrawn"
        }else if(item.itemDetailResponseModel?.notForLoanStatus!! >1){
            holder.binding.tvItem.text = "Not for loan"
        }else{
            holder.binding.tvItem.text = "Available"
        }


    }


    class BookListViewHolder(
        val binding: ItemBookStatusListBinding
    ) : RecyclerView.ViewHolder(binding.root)

}


