package com.example.room_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room_project.db.Student
import com.example.room_project.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emaiEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private lateinit var viewModel: StudentViewModel

    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentRecyclerViewAdapter: StudentRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.edtName)
        emaiEditText = findViewById(R.id.edtEmail)
        saveButton = findViewById(R.id.btnSave)
        deleteButton = findViewById(R.id.btnDelete)
        studentRecyclerView = findViewById(R.id.rcvStudents)

        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]

        saveButton.setOnClickListener {
            saveStudentData()
            clearInput()
        }

        initRecyclerView()

    }

    private fun saveStudentData(){
        viewModel.insertStudent(
            Student(0, nameEditText.text.toString(), emaiEditText.text.toString())
        )
    }

    private fun clearInput(){
        nameEditText.setText("")
        emaiEditText.setText("")
    }

    private fun initRecyclerView(){
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerViewAdapter = StudentRecyclerViewAdapter()
        studentRecyclerView.adapter = studentRecyclerViewAdapter
        displayStudentsList()

    }
    private fun displayStudentsList(){
        viewModel.students.observe(this) {
            studentRecyclerViewAdapter.setStudentList(it)
            studentRecyclerViewAdapter.notifyDataSetChanged()
        }
    }
}