package com.example.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val button = findViewById<Button>(R.id.addTransactionBtn)
        val closeButton = findViewById<ImageButton>(R.id.closeBtn)
        val labelInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.labelInput)
        val descriptionInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.descriptionInput)
        val amountInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.amountInput)
        val labelLayout = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.amountLayout)

        labelInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                amountLayout.error = null
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
                val transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }


        }

        closeButton.setOnClickListener {
            finish()
        }

    }
    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}