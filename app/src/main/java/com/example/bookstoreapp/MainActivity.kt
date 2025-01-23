package com.example.bookstoreapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstoreapp.databinding.ActivityMainBinding
import com.example.bookstoreapp.databinding.BookDialogBinding
import com.example.bookstoreapp.interfaces.BookDao
import com.example.bookstoreapp.models.AppDatabase
import com.example.bookstoreapp.models.BookAdapter
import com.example.bookstoreapp.models.BookEntity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: BookAdapter

    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao

    private var isSortAsc: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setTitle("Book Management")

        database = AppDatabase.getInstance(this)
        bookDao = database.bookDao()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = BookAdapter(
            onEdit = { book -> showEditDialog(book) },
            onDelete = { book -> showDeleteDialog(book) },
            onRowClicked = { book -> showDetailsDialog(book)}
        )

        binding.rvBooks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeBooks() {
        lifecycleScope.launch {
            bookDao.getAllBooksOptionalSortByQuantity(isSortAsc).collect { books ->
                adapter.submitList(books)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeBooks()
    }

    private fun showAddDialog() {
        val dialogBinding = BookDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvDialogTitle.text = "Add new inventory"
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirm.setOnClickListener {
            val title = dialogBinding.etTitle.text.toString()
            val author = dialogBinding.etAuthor.text.toString()
            val price = dialogBinding.etPrice.text.toString().toDoubleOrNull()
            val quantity = dialogBinding.etQuantity.text.toString().toIntOrNull()

            if (title.isNotBlank() && author.isNotBlank() && price!= null && quantity != null) {
                lifecycleScope.launch {
                    bookDao.insertBook(BookEntity(0, title, author, price, quantity))
                    observeBooks()
                }
                dialog.dismiss()
            }
            else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showDetailsDialog(book: BookEntity) {
        AlertDialog.Builder(this)
            .setTitle("Inventory details")
            .setMessage(
                """
                    ID: ${book.id}
                    Title: ${book.title}
                    Author: ${book.author}
                    Price: $${book.price}
                    Quantity: ${book.quantity}
                """.trimIndent()
            )
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showEditDialog(book: BookEntity) {
        val dialogBinding = BookDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.apply {
            tvDialogTitle.text = "Edit inventory"
            etTitle.setText(book.title)
            etAuthor.setText(book.author)
            etPrice.setText(book.price.toString())
            etQuantity.setText(book.quantity.toString())

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnConfirm.setOnClickListener {
                val title = dialogBinding.etTitle.text.toString()
                val author = dialogBinding.etAuthor.text.toString()
                val price = dialogBinding.etPrice.text.toString().toDoubleOrNull()
                val quantity = dialogBinding.etQuantity.text.toString().toIntOrNull()

                if (title.isNotBlank() && author.isNotBlank() && price!= null && quantity != null) {
                    lifecycleScope.launch {
                        bookDao.updateBook(BookEntity(book.id, title, author, price, quantity))
                        observeBooks()
                    }
                    dialog.dismiss()
                }
                else {
                    Toast.makeText(applicationContext, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(book: BookEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete inventory")
            .setMessage("Are you sure you want to delete ${book.title}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    bookDao.deleteBook(book)
                    observeBooks()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.actions_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miRefresh -> {
                observeBooks()
                return true
            }
            R.id.miAddBook -> {
                showAddDialog()
                return true
            }
            R.id.miSortQuantityAsc -> {
                isSortAsc = true
                observeBooks()
                return true
            }
            R.id.miSortQuantityDesc -> {
                isSortAsc = false
                observeBooks()
                return true
            }
            R.id.miResetSort -> {
                isSortAsc = null
                observeBooks()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}