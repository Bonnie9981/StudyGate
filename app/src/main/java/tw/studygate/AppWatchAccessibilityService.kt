package tw.studygate

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppWatchAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOWS_CHANGED) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName) return
        if (!AppPrefs.isEnabled(this)) return
        if (packageName !in AppPrefs.targets(this)) return
        if (AppPrefs.isUnlocked(this, packageName)) return
        if (!AppPrefs.shouldLaunchGate(this)) return

        val intent = Intent(this, GateActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(GateActivity.EXTRA_TARGET_PACKAGE, packageName)
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {
            // Android/OEM background-activity restrictions may prevent launch.
        }
    }

    override fun onInterrupt() = Unit
}
