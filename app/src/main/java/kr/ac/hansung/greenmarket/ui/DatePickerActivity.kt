package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import kr.ac.hansung.greenmarket.R
import java.text.SimpleDateFormat
import java.util.Calendar

class DatePickerActivity : Activity() {

    private var selectedDateTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        selectedDateTextView = findViewById(R.id.selectedDateTextView)
        val button_DatePicker = findViewById<Button>(R.id.button_DatePicker)

        // 현재 날짜를 가져와서 DatePicker 초기화
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        // DatePicker 버튼 클릭 시 DatePickerDialog 표시
        button_DatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.THEME_HOLO_LIGHT, // AlertDialog.THEME_HOLO_LIGHT 사용
                dateSetListener,
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // 과거 날짜 선택 방지
            datePickerDialog.show()
        }
    }

    // DatePicker에서 날짜 선택 시 호출됨
    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val selectedDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthOfYear)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }.time)

        // 선택한 날짜를 TextView에 표시
        selectedDateTextView?.text = selectedDate
    }
}
