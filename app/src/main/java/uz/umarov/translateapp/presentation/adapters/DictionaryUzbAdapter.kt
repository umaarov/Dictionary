package uz.umarov.translateapp.presentation.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.utils.BaseUtils.initDialogForAdapter
import uz.umarov.translateapp.R
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.databinding.LettersItemLyBinding

class DictionaryUzbAdapter : RecyclerView.Adapter<DictionaryUzbAdapter.LettersItemHolder>() {

    var baseList = emptyList<DictionaryModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((DictionaryModel) -> Unit)? = null

    fun setOnStarAddClickListener(listener: ((DictionaryModel) -> Unit)? = null) {
        onItemClickListener = listener
    }

    inner class LettersItemHolder(val b: LettersItemLyBinding) : RecyclerView.ViewHolder(b.root) {

        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind(result: DictionaryModel) {
            val captLetter = result.uzbek.take(1)
            b.apply {
                lettersInitial.text = captLetter
                letterBoldMain.text = result.uzbek
                letterSecondaryNormal.text = result.english

                val colorId = if (result.isFavourite) R.color.small_items_color
                else R.color.light_dark_color
                val color = ContextCompat.getColor(itemView.context, colorId)
                addToFavouritesStar.imageTintList = ColorStateList.valueOf(color)

                b.addToFavouritesStar.setOnClickListener {
                    onItemClickListener?.invoke(result)
                    notifyDataSetChanged()
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LettersItemHolder =
        LettersItemHolder(
            LettersItemLyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LettersItemHolder, position: Int) {
        val itemData = baseList[position]

        holder.bind(itemData)

        holder.itemView.setOnClickListener {
            initDialogForAdapter(holder.itemView.context, itemData)
        }

    }

    override fun getItemCount(): Int = baseList.size

}
