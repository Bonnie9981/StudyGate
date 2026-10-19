package tw.studygate

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class GateActivity : Activity() {
    companion object { const val EXTRA_TARGET_PACKAGE = "target_package" }

    private var targetPackage: String = ""
    private var current: QuizQuestion? = null
    private var remediation = false
    private var chosenIndex = -1
    private lateinit var root: LinearLayout
    private lateinit var feedback: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE).orEmpty()
        if (targetPackage.isBlank()) { finish(); return }
        showQuestion()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE).orEmpty()
        showQuestion()
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun showQuestion() {
        remediation = false
        chosenIndex = -1
        current = QuestionBank.questions.random()
        renderQuestion()
    }

    private fun renderQuestion() {
        val q = current ?: return
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(24))
            setBackgroundColor(0xFFF6F8FC.toInt())
        }
        root.addView(TextView(this).apply {
            text = "StudyGate  ·  ${if (remediation) "補救挑戰" else q.subject}"
            textSize = 15f
            setTextColor(0xFF3457A4.toInt())
        })
        root.addView(TextView(this).apply {
            text = if (remediation) q.remediationPrompt else q.prompt
            textSize = 23f
            setTypeface(null, Typeface.BOLD)
            setTextColor(0xFF18233B.toInt())
            setPadding(0, dp(18), 0, dp(16))
        })
        optionsGroup = RadioGroup(this).apply { orientation = RadioGroup.VERTICAL }
        val opts = if (remediation) q.remediationOptions else q.options
        opts.forEachIndexed { i, option ->
            optionsGroup.addView(RadioButton(this).apply {
                id = View.generateViewId()
                tag = i
                text = option
                textSize = 17f
                setPadding(0, dp(8), 0, dp(8))
            })
        }
        root.addView(optionsGroup)
        feedback = TextView(this).apply {
            textSize = 15f
            setPadding(0, dp(14), 0, dp(10))
        }
        root.addView(feedback)
        submit = Button(this).apply {
            text = "提交答案"
            setOnClickListener { checkAnswer() }
        }
        root.addView(submit)
        root.addView(Button(this).apply {
            text = "返回 StudyGate 設定"
            setOnClickListener { finish() }
        })
        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)
    }

    private fun checkAnswer() {
        val id = optionsGroup.checkedRadioButtonId
        if (id == -1) {
            Toast.makeText(this, "先選一個答案喔！", Toast.LENGTH_SHORT).show()
            return
        }
        val selected = findViewById<RadioButton>(id).tag as Int
        val q = current ?: return
        val correctIndex = if (remediation) q.remediationAnswerIndex else q.answerIndex
        val explanation = if (remediation) q.remediationExplanation else q.explanation

        if (selected == correctIndex) {
            if (remediation) {
                AppPrefs.grantUnlock(this, targetPackage, 10)
                val appName = try {
                    packageManager.getApplicationLabel(packageManager.getApplicationInfo(targetPackage, 0)).toString()
                } catch (_: PackageManager.NameNotFoundException) { "指定 App" }
                Toast.makeText(this, "答對了！${appName} 已解鎖 10 分鐘。", Toast.LENGTH_LONG).show()
                finish()
            } else {
                feedback.text = "答對了！\n$explanation\n\n接著完成一道補救題，就能解鎖 10 分鐘。"
                submit.text = "開始補救題"
                submit.setOnClickListener {
                    remediation = true
                    renderQuestion()
                }
            }
        } else {
            if (remediation) {
                feedback.text = "還沒答對，再試一次！\n$explanation"
            } else {
                feedback.text = "這題答錯了。\n$explanation\n\n看完解析後，完成一道補救題才能解鎖。"
                submit.text = "我懂了，做補救題"
                submit.setOnClickListener {
                    remediation = true
                    renderQuestion()
                }
            }
        }
    }
}
