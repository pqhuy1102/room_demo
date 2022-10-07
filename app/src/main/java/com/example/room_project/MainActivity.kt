package com.example.room_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

    private lateinit var selectedStudent: Student
    private var isStudentItemClicked:Boolean = false


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
            if(isStudentItemClicked){
                updateStudentData()
                clearInput()
            } else {
                saveStudentData()
                clearInput()
            }
        }

        deleteButton.setOnClickListener {
            if(isStudentItemClicked){
                deleteStudentData()
                clearInput()
            } else {
                clearInput()
            }
        }

        initRecyclerView()

    }

    private fun saveStudentData(){
        viewModel.insertStudent(
            Student(0, nameEditText.text.toString(), emaiEditText.text.toString())
        )
    }

    private fun updateStudentData(){
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emaiEditText.text.toString()
            )
        )
        isStudentItemClicked = false
        saveButton.text = "Save"
        deleteButton.text = "Clear"
    }

    private fun deleteStudentData(){
        viewModel.deleteStudent(
                Student(
                    selectedStudent.id,
                    nameEditText.text.toString(),
                    emaiEditText.text.toString()
                )
            )
                    isStudentItemClicked = false
                    saveButton.text = "Save"
                    deleteButton.text = "Clear"

    }

    private fun clearInput(){
        nameEditText.setText("")
        emaiEditText.setText("")
    }

    private fun initRecyclerView(){
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerViewAdapter = StudentRecyclerViewAdapter{
            selectedStudent:Student -> studentItemClicked(selectedStudent)
        }
        studentRecyclerView.adapter = studentRecyclerViewAdapter
        displayStudentsList()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayStudentsList(){
        viewModel.students.observe(this) {
            studentRecyclerViewAdapter.setStudentList(it)
            studentRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun studentItemClicked(student:Student){
        isStudentItemClicked = true
        selectedStudent = student
        saveButton.text = getString(R.string.update_button)
        deleteButton.text = getString(R.string.remove_button)
        nameEditText.setText(selectedStudent.name)
        emaiEditText.setText(selectedStudent.email)
    }
}