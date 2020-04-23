package com.frostdev.valutaconverter.main

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.frostdev.valutaconverter.R
import com.frostdev.valutaconverter.databinding.CellRateBinding
import com.frostdev.valutaconverter.model.SingleRate
import java.util.*


class ConverterAdapter : RecyclerView.Adapter<ConverterAdapter.SingleConverterViewHolder>() {

    private lateinit var singleConverterList:List<SingleRate>
    private lateinit var mRecycler: RecyclerView
    private lateinit var mContext: Context

    companion object {
        private lateinit var mAdapter: ConverterAdapter
        var inputAmount = 1f
        var focusCurrency = "EUR"
        var hasChanged = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleConverterViewHolder {
        mContext = MainActivity.activityComponent.activityContext()
        mAdapter = this
        val binding: CellRateBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.cell_rate, parent, false)
        return SingleConverterViewHolder(binding, mContext)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecycler = recyclerView
        (mRecycler.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun onBindViewHolder(holder: SingleConverterViewHolder, position: Int) {
        holder.bindItems(singleConverterList[position])
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    fun setInputAmount(input: Float){
        inputAmount = input
    }

    fun getFocusValuta(): String {
        return focusCurrency
    }

    fun setFocusValuta(country: String) {
        focusCurrency = country
    }

    fun getInputAmount(): Float {
        return inputAmount
    }

    override fun getItemCount(): Int {
        return if(::singleConverterList.isInitialized) singleConverterList.size else 0
    }

    fun updateConverterList(list: List<SingleRate>){
        this.singleConverterList = list
        if(hasChanged)
            notifyItemRangeChanged(0, singleConverterList.size)
        else
            notifyItemRangeChanged(1, singleConverterList.size)

        hasChanged = false
    }

    class SingleConverterViewHolder(private val binding: CellRateBinding, mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = RateViewModel(mContext)

        fun bindItems(rate: SingleRate) {
            viewModel.bind(rate)
            binding.viewModel = viewModel
            binding.valutaConverterRate.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 1)
                    mAdapter.setFocusValuta(binding.valutaConverterAbbreviation.text.toString())
                    binding.textIndicator.setBackgroundColor(itemView.resources.getColor(R.color.colorBlue, itemView.context.theme))
                    inputAmount = 1f
                    moveListItem(adapterPosition)
                }else{
                    binding.textIndicator.setBackgroundColor(itemView.resources.getColor(R.color.colorLightGrey, itemView.context.theme))
                }
            }
            binding.valutaConverterRate.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(!p0.isNullOrEmpty()) {
                        if (binding.valutaConverterAbbreviation.text.toString().equals(
                                mAdapter.getFocusValuta()))
                            mAdapter.setInputAmount(p0.toString().toFloat())
                    } else {
                        mAdapter.setInputAmount(0f)
                    }
                }
            })
            binding.valutaConverterRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    return@OnKeyListener true
                }
                false
            })
            binding.root.setOnClickListener {
                moveListItem(adapterPosition)
            }
        }

        fun moveListItem (position: Int){
            if(position != 0) {
                hasChanged = true
                inputAmount = mAdapter.singleConverterList.get(position).rate
                focusCurrency = mAdapter.singleConverterList.get(position).abbreviation
                mAdapter.singleConverterList[position].rate = 1f
                mAdapter.notifyItemChanged(position)
                Collections.swap(mAdapter.singleConverterList, position, 0)
                mAdapter.notifyItemMoved(position, 0)
                mAdapter.mRecycler.scrollToPosition(0)
            }
        }
    }
}
