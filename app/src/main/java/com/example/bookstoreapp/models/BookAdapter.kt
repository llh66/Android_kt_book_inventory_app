package com.example.bookstoreapp.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreapp.databinding.RowBookInfoBinding

class BookAdapter (
    private val onEdit: (BookEntity) -> Unit,
    private val onDelete: (BookEntity) -> Unit,
    private val onRowClicked: (BookEntity) -> Unit
): ListAdapter<BookEntity, BookAdapter.BookViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        val binding = RowBookInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: RowBookInfoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(book: BookEntity) {
            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                tvPrice.text = "Price: $${book.price}"
                tvQuantity.text = "Quantity: ${book.quantity}"

                btnUpdate.setOnClickListener { onEdit(book) }
                btnDelete.setOnClickListener { onDelete(book) }
                root.setOnClickListener { onRowClicked(book) }
            }
        }
    }

    class BookDiffCallback: DiffUtil.ItemCallback<BookEntity>() {
        override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem == newItem
        }
    }
}