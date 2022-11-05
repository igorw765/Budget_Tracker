package com.example.budgettracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.FileDescriptor
import java.io.Serializable


@Entity(tableName = "transactions")
data class Transaction(@PrimaryKey(autoGenerate = true) val id: Int, val label: String, val amount: Double, val descriptor: String):
    Serializable {
}