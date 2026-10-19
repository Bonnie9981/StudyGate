package tw.studygate

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Typeface

class MainActivity : android.app.Activity() {
    private val checks = mutableMapOf<String, CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(20))
            setBackgroundColor(0xFFF5F7FB.toInt())
        }
        root.addView(TextView(this).apply {
            text = "StudyGate"
            textSize = 30f
            setTypeface(null, Typeface.BOLD)
            setTextColor(0xFF18233B.toInt())
        })
        root.addView(TextView(this).apply {
            text = "答對題目，解鎖 10 分鐘。先選擇要限制的 App，再啟用攔截服務。"
            textSize = 15f
            setPadding(0, dp(8), 0, dp(14))
        })

        val accessibilityButton = Button(this).apply {
            text = "① 開啟 Android 無障礙設定"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        }
        root.addView(accessibilityButton)

        root.addView(TextView(this).apply {
            text = "② 選擇要限制的 App"
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, dp(14), 0, dp(6))
        })

        val scroll = ScrollView(this)
        val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        checks.clear()
        val launchIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(launchIntent, PackageManager.MATCH_ALL)
            .mapNotNull { info ->
                val pkg = info.activityInfo?.packageName ?: return@mapNotNull null
                if (pkg == packageName) return@mapNotNull null
                Triple(pkg, info.loadLabel(packageManager)?.toString() ?: pkg, info.activityInfo?.iconResource ?: 0)
            }
            .distinctBy { it.first }
            .sortedBy { it.second.lowercase() }

        val selected = AppPrefs.targets(this)
        for ((pkg, label, _) in apps) {
            val cb = CheckBox(this).apply {
                text = "$label\n$pkg"
                textSize = 14f
                isChecked = pkg in selected
                setPadding(dp(4), dp(5), dp(4), dp(5))
            }
            checks[pkg] = cb
            list.addView(cb)
        }
        scroll.addView(list)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        root.addView(Button(this).apply {
            text = "③ 儲存限制 App"
            setOnClickListener {
                AppPrefs.setTargets(this@MainActivity, checks.filterValues { it.isChecked }.keys)
                Toast.makeText(this@MainActivity, "已儲存 ${checks.values.count { it.isChecked }} 個 App", Toast.LENGTH_SHORT).show()
            }
        })
        root.addView(Button(this).apply {
            text = "開啟 / 暫停答題攔截"
            setOnClickListener {
                val enabled = !AppPrefs.isEnabled(this@MainActivity)
                AppPrefs.setEnabled(this@MainActivity, enabled)
                Toast.makeText(this@MainActivity, if (enabled) "攔截已啟用" else "攔截已暫停", Toast.LENGTH_SHORT).show()
            }
        })
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        val manager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val active = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any { it.resolveInfo.serviceInfo.packageName == packageName &&
                it.resolveInfo.serviceInfo.name == AppWatchAccessibilityService::class.java.name }
        if (!active) {
            // The user must enable the service in Android Settings; this app cannot enable it itself.
        }
    }
}
