package tw.studygate

import android.content.Context

object AppPrefs {
    private const val PREFS = "study_gate_prefs"
    private const val KEY_TARGETS = "target_packages"
    private const val KEY_UNLOCKED = "unlocked_until"
    private const val KEY_LAST_GATE = "last_gate_launch"
    private const val KEY_ENABLED = "gate_enabled"

    fun targets(context: Context): Set<String> =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getStringSet(KEY_TARGETS, emptySet())?.toSet() ?: emptySet()

    fun setTargets(context: Context, values: Set<String>) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putStringSet(KEY_TARGETS, values.toSet()).apply()
    }

    fun isEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_ENABLED, true)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun grantUnlock(context: Context, packageName: String, minutes: Int = 10) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putLong("unlock_$packageName", System.currentTimeMillis() + minutes * 60_000L).apply()
    }

    fun isUnlocked(context: Context, packageName: String): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getLong("unlock_$packageName", 0L) > System.currentTimeMillis()

    fun shouldLaunchGate(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val last = prefs.getLong(KEY_LAST_GATE, 0L)
        if (now - last < 1800L) return false
        prefs.edit().putLong(KEY_LAST_GATE, now).apply()
        return true
    }
}
