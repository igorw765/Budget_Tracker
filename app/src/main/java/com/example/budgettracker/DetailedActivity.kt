package com.example.budgettracker

import android.content.Context
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {

    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        transaction = intent.getSerializableExtra("transaction") as Transaction
        val rootView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.rootView)
        val button = findViewById<Button>(R.id.updateBtn)
        val closeButton = findViewById<ImageButton>(R.id.closeBtn)
        val labelInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.labelInput)
        val descriptionInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.descriptionInput)
        val amountInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.amountInput)
        val labelLayout = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.amountLayout)



        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descriptionInput.setText(transaction.descriptor)

        rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

        }

        labelInput.addTextChangedListener {
            button.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            button.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }

        descriptionInput.addTextChangedListener {
            button.visibility = View.VISIBLE
        }

        button.setOnClickListener{
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                labelLayout.error = "Please, enter a valid label"

            else if (amount == null)
                amountLayout.error = "Please, enter a valid label"
            else {
                val transaction = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }


        }

        closeButton.setOnClickListener {
            finish()
        }

    }
    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
