package com.bbb.koha.module.my_account.purchase_suggestions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bbb.koha.R
import com.bbb.koha.databinding.ItemCheckoutListBinding
import com.bbb.koha.databinding.ItemHoldsListBinding
import com.bbb.koha.databinding.ItemSuggestionListBinding
import com.bbb.koha.module.my_account.purchase_suggestions.model.SuggestionListResponseModel
import com.bbb.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbb.koha.module.my_account.summary.model.HoldsResponseModel
import com.bbb.koha.utils.Utils
import com.squareup.picasso.Picasso
import okhttp3.internal.notify

class SuggestionAdapter (
    private val itemList: ArrayList<SuggestionListResponseModel>,
    private val suggestionCallBack: SuggestionCallBack
) : RecyclerView.Adapter<SuggestionAdapter.SummaryDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDetailViewHolder {
        val binding =
            ItemSuggestionListBinding.inflate(
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
        holder.binding.tvSummaryTitle.text = item.title
        holder.binding.tvSuggestedOn.text = item.suggestionDate
        holder.binding.tvSummaryDetail.text = item.volumeDesc
        holder.binding.tvManageBy.text = item.managedBy
        holder.binding.tvStatus.text = item.status
        holder.binding.cbItem.isChecked = item.isChecked
        holder.binding.cbItem.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
            suggestionCallBack.onCheckBoxClickListener(position
                    ,itemList)
            //Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    class SummaryDetailViewHolder(
        val binding: ItemSuggestionListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface SuggestionCallBack{
        fun onCheckBoxClickListener(adapterPosition: Int, suggestionListResponseModel: ArrayList<SuggestionListResponseModel>)
    }

    fun updateContentList(contentList: ArrayList<SuggestionListResponseModel>) {
        itemList.clear()
        itemList.addAll(contentList)
        notifyDataSetChanged()
    }

    fun getList():ArrayList<SuggestionListResponseModel>{
        return itemList
    }
}


