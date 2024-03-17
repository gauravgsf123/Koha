package com.bbb.koha.module.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbb.koha.R
import com.bbb.koha.app.BaseFragment
import com.bbb.koha.common.Constant
import com.bbb.koha.databinding.FragmentBookListBinding
import com.bbb.koha.module.dashboard.DashboardActivity
import com.bbb.koha.module.dashboard.DashboardViewModel
import com.bbb.koha.module.dashboard.adapter.BookListAdapter
import com.bbb.koha.module.my_account.summary.model.BookDetailResponseModel
import com.bbb.koha.network.Resource
import com.bbb.koha.network.ViewModelFactoryClass
import com.bbb.koha.utils.EndlessRecyclerViewScrollListener
import com.bbb.koha.utils.ProgressDialog
import com.google.gson.Gson


class BookListFragment(val type: Int) : BaseFragment() {
    private lateinit var binding:FragmentBookListBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter : BookListAdapter
    private var booksList: MutableList<BookDetailResponseModel> = mutableListOf()
    private var totalItemsCount = 0
    private var listSize = 0
    private var pageIndex = 1
    private var perPage = 5
    private val limitSize = 50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_book_list,
            container,
            false
        )
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[DashboardViewModel::class.java]
        when(type){
            Constant.BookListType.NEW_ARRIVAL->{viewModel.getBookList("-biblio_id",pageIndex,10)}
            Constant.BookListType.TOP_CIRCULATING->{viewModel.getCirculatingBookList("-checkouts_count",1,10)}
            Constant.BookListType.BORROWED_BOOK->{viewModel.getBorrowedBook(sharedPreference.getValueInt(Constant.PATRON_ID),
                "-checkout_id",
                true,
                1,
                10
            )}
        }
        (activity as DashboardActivity).run {
            showToolbar(false)
        }
       adapter = BookListAdapter(arrayListOf(), object : BookListAdapter.BooksListCallBack{
            override fun onDetailClickListener(
                adapterPosition: Int,
                item: BookDetailResponseModel
            ) {
                (activity as DashboardActivity).replaceFragment(BookDetailFragment(item))
            }

        } )
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.booksListResponseModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    //booksList = response.data!!
                    booksList.addAll(response.data!!)
                    listSize += response.data.size
                    setupRecylerView(response.data)
                    //viewModel.getCirculatingBookList("-checkouts_count", 1, 10)
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(requireContext())
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupRecylerView(data: List<BookDetailResponseModel>) {
        binding.tvTopResult.text = "Total Result (${listSize})"
        val layoutManager = LinearLayoutManager(requireContext())
       /* adapter = BookListAdapter(data as ArrayList<BookDetailResponseModel>,object :BookListAdapter.BooksListCallBack{
            override fun onDetailClickListener(adapterPosition: Int) {

            }

        })*/
        adapter.updateList(data as ArrayList<BookDetailResponseModel> )
        binding.rvBookList.layoutManager = layoutManager
        binding.rvBookList.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(listSize>totalItemsCount || listSize>=limitSize){

                }else{
                    listSize = totalItemsCount
                    viewModel.getBookList("biblio_id",pageIndex++,perPage)
                }

            }

        })
        binding.rvBookList.adapter = adapter
        binding.rvBookList.scrollToPosition(booksList.size - (perPage+5))
    }

}