package com.bignerdranch.andriod.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val KEY_HAS_CHEATED = "cheated"

class CheatActivity : AppCompatActivity() {
  private lateinit var answerTextView: TextView
  private lateinit var showAnswerButton: Button
  private var answerIsTrue = false

  private val cheatViewModel: CheatViewModel by lazy {
    ViewModelProvider(this).get(CheatViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cheat)
    val isCheater = savedInstanceState?.getBoolean(KEY_HAS_CHEATED, false) ?: false
    cheatViewModel.isCheater = isCheater
    answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
    answerTextView = findViewById(R.id.answer_text_view)
    showAnswerButton = findViewById(R.id.show_answer_button)
    showAnswerButton.setOnClickListener {
      val answerText = when {
        answerIsTrue -> R.string.true_button
        else -> R.string.false_button
      }
      answerTextView.setText(answerText)
      setAnswerShownResult()
      cheatViewModel.isCheater = true
    }

    if (cheatViewModel.isCheater) {
      setAnswerShownResult()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean(KEY_HAS_CHEATED, cheatViewModel.isCheater)
  }

  companion object {
    fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
      return Intent(packageContext, CheatActivity::class.java).apply {
        putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
      }
    }
  }

  private fun setAnswerShownResult() {
    val data = Intent().apply {
      putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isCheater)
    }
    setResult(Activity.RESULT_OK, data)
  }
}