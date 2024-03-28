package uz.umarov.translateapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.utils.BaseUtils.initDialogForAdapter
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.data.models.FavouritesModel
import uz.umarov.translateapp.databinding.LettersItemLyBinding

class FavouritesAdapter :
    ListAdapter<FavouritesModel, FavouritesAdapter.FavouriteItemHolder>(FavDiffUtil()) {

    private var onItemClickListener: ((FavouritesModel) -> Unit)? = null

    fun setOnDeleteClickListener(listener: ((FavouritesModel) -> Unit)? = null) {
        onItemClickListener = listener
    }

    inner class FavouriteItemHolder(val b: LettersItemLyBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(result: FavouritesModel) {
            val captLetter = result.english.take(1)
            b.apply {
                deleteBtn.isVisible = true
                addToFavouritesStar.isVisible = false
            }
            b.letterBoldMain.text = result.english
            b.letterSecondaryNormal.text = result.uzbek
            b.lettersInitial.text = captLetter

            b.deleteBtn.setOnClickListener {
                onItemClickListener?.invoke(result)
            }

        }
    }

    internal class FavDiffUtil : DiffUtil.ItemCallback<FavouritesModel>() {
        override fun areItemsTheSame(
            oldItem: FavouritesModel,
            newItem: FavouritesModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavouritesModel,
            newItem: FavouritesModel
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteItemHolder =
        FavouriteItemHolder(
            LettersItemLyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavouriteItemHolder, position: Int) {
        val itemData = getItem(position)

        holder.bind(itemData)

        val dictionary = itemData?.let {
            DictionaryModel(it.id, it.english, it.uzbek, it.transcript, it.type, it.isFavourite)
        }

        holder.itemView.setOnClickListener {
            dictionary?.let { words ->
                initDialogForAdapter(
                    holder.itemView.context, words
                )
            }
        }

    }

}