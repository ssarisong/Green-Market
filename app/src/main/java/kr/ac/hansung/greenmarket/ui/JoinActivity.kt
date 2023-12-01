package kr.ac.hansung.greenmarket.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil
import java.text.SimpleDateFormat
import java.util.Calendar

class JoinActivity : AppCompatActivity() {

    private var selectedDateTextView: TextView? = null
    private var isPasswordHidden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val userUtil = FirebaseUserUtil()

        val passwordEditText = findViewById<EditText>(R.id.editText_Pw)
        val togglePasswordButton = findViewById<Button>(R.id.button_TogglePassword)

        // 비밀번호 보이기/숨기기
        togglePasswordButton.setOnClickListener {
            isPasswordHidden = !isPasswordHidden
            val start = passwordEditText.selectionStart
            val end = passwordEditText.selectionEnd
            updatePasswordVisibility(passwordEditText)
            passwordEditText.setSelection(start, end)
        }

        fun updatePasswordVisibility(passwordEditText: EditText) {
            if (isPasswordHidden) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = null
            }
        }
        updatePasswordVisibility(passwordEditText)


        findViewById<Button>(R.id.button_DatePicker).setOnClickListener {
            startActivity(Intent(this, DatePickerActivity::class.java))
        }

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

        findViewById<Button>(R.id.button_Join).setOnClickListener { // 회원가입 버튼
            val userEmail = findViewById<EditText>(R.id.editText_Email)?.text.toString()
            val password = findViewById<EditText>(R.id.editText_Pw)?.text.toString()
            val name = findViewById<EditText>(R.id.editText_Name)?.text.toString()
            val birth = findViewById<TextView>(R.id.selectedDateTextView)?.text.toString()

            if (userEmail.isEmpty()) {
                findViewById<TextView>(R.id.textView_join_failed).text = "이메일을 입력하세요"
                findViewById<TextView>(R.id.textView_join_failed).visibility = View.VISIBLE
            } else if (password.isEmpty()) {
                findViewById<TextView>(R.id.textView_join_failed).text = "비밀번호를 입력하세요"
                findViewById<TextView>(R.id.textView_join_failed).visibility = View.VISIBLE
            }  else if (name.isEmpty()) {
                findViewById<TextView>(R.id.textView_join_failed).text = "이름을 입력하세요"
                findViewById<TextView>(R.id.textView_join_failed).visibility = View.VISIBLE
            } else if (selectedDateTextView?.text.isNullOrEmpty() || selectedDateTextView?.text == "날짜 선택") { // birth == "날짜 선택"
                findViewById<TextView>(R.id.textView_join_failed).text = "생년월일을 입력하세요"
                findViewById<TextView>(R.id.textView_join_failed).visibility = View.VISIBLE
            } else {
                // 이메일과 비밀번호가 입력되었을 때 회원가입 시도
                userUtil.doSignUp(userEmail, password, name, birth) { STATUS_CODE, uid ->
                    if (STATUS_CODE == StatusCode.SUCCESS) {
                        // 회원가입 성공
                        startActivity(
                            Intent(this, HomeActivity::class.java)
                        )
                        finish()
                    } else {
                        // 회원가입 실패
                        findViewById<TextView>(R.id.textView_join_failed).text = "회원가입에 실패했습니다"
                    }
                }

            }

        }

        findViewById<Button>(R.id.button_Back).setOnClickListener { // 이전 버튼
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
    }
    private fun updatePasswordVisibility(passwordEditText: EditText) {
        val selectionStart = passwordEditText.selectionStart
        val selectionEnd = passwordEditText.selectionEnd

        if (isPasswordHidden) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            passwordEditText.transformationMethod = null
        }

        passwordEditText.setSelection(selectionStart, selectionEnd)
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