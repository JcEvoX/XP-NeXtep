package io.github.lujinxin.nextep.config

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButton
import io.github.lujinxin.nextep.R
import io.github.lujinxin.nextep.trigger.TriggerBroadcastContract

internal object AdvancedScreen {
    fun create(activity: AppCompatActivity): View {
        fun dp(value: Int) = (value * activity.resources.displayMetrics.density).toInt()
        fun paragraph(value: String) = TextView(activity).apply {
            text = value
            textSize = 14f
            setTextColor(SettingsPalette.secondary(activity))
            setLineSpacing(dp(3).toFloat(), 1f)
        }
        fun heading(label: Int, size: Float = 18f) = TextView(activity).apply {
            setText(label)
            textSize = size
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setTextColor(SettingsPalette.text(activity))
            ViewCompat.setAccessibilityHeading(this, true)
        }
        val queryCommand = "settings get global nextep_workspace_active_v1"
        val setCommand = "am broadcast -n ${TriggerBroadcastContract.MODULE_PACKAGE}" +
            "/.trigger.WorkspaceControlBrokerReceiver" +
            " -a ${TriggerBroadcastContract.ACTION_HOST_SET_REQUEST}" +
            " --ez ${TriggerBroadcastContract.EXTRA_REQUESTED_ACTIVE}"
        val enterCommand = "$setCommand true"
        val exitCommand = "$setCommand false"

        return LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(24))
            setBackgroundColor(SettingsPalette.page(activity))
            addView(heading(R.string.navigation_advanced, 28f),
                LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) })
            addView(paragraph(activity.getString(R.string.advanced_intro)),
                LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(24) })
            addView(heading(R.string.advanced_requirements_title),
                LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })
            addView(paragraph(activity.getString(R.string.advanced_requirements)),
                LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(24) })
            addView(heading(R.string.advanced_commands_title),
                LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) })
            val commands = listOf(
                Triple(R.string.advanced_enter_title, R.string.advanced_enter_summary, enterCommand),
                Triple(R.string.advanced_exit_title, R.string.advanced_exit_summary, exitCommand),
                Triple(R.string.advanced_query_title, R.string.advanced_query_summary, queryCommand),
            )
            commands.forEach { (title, summary, command) ->
                addView(commandBlock(activity, title, summary, command),
                    LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(20) })
            }
            addView(heading(R.string.advanced_setup_title),
                LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(4); bottomMargin = dp(12) })
            listOf(
                R.string.advanced_setup_copy,
                R.string.advanced_setup_root,
                R.string.advanced_setup_bind,
            ).forEachIndexed { index, step ->
                addView(paragraph("${index + 1}. ${activity.getString(step)}"),
                    LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) })
            }
        }
    }

    private fun commandBlock(activity: AppCompatActivity, title: Int, summary: Int, command: String): View {
        fun dp(value: Int) = (value * activity.resources.displayMetrics.density).toInt()
        val label = activity.getString(title)
        return LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            addView(LinearLayout(activity).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                addView(LinearLayout(activity).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(TextView(activity).apply {
                        text = label
                        textSize = 16f
                        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                        setTextColor(SettingsPalette.text(activity))
                        ViewCompat.setAccessibilityHeading(this, true)
                    })
                    addView(TextView(activity).apply {
                        setText(summary)
                        textSize = 13f
                        setTextColor(SettingsPalette.secondary(activity))
                        setLineSpacing(dp(2).toFloat(), 1f)
                    }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(4) })
                }, LinearLayout.LayoutParams(0, -2, 1f))
                addView(MaterialButton(activity, null,
                    com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                    setText(R.string.advanced_copy)
                    contentDescription = activity.getString(R.string.advanced_copy_description, label)
                    minWidth = dp(72)
                    minHeight = dp(48)
                    insetTop = dp(4)
                    insetBottom = dp(4)
                    cornerRadius = dp(12)
                    val resetLabel = Runnable { setText(R.string.advanced_copy) }
                    setOnClickListener {
                        val clipboard = activity.getSystemService(ClipboardManager::class.java)
                            ?: return@setOnClickListener
                        clipboard.setPrimaryClip(ClipData.newPlainText(label, command))
                        setText(R.string.advanced_copied)
                        removeCallbacks(resetLabel)
                        postDelayed(resetLabel, 1_500L)
                    }
                }, LinearLayout.LayoutParams(-2, -2).apply { marginStart = dp(12) })
            }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })
            addView(TextView(activity).apply {
                text = command
                textSize = 12f
                typeface = Typeface.MONOSPACE
                setTextColor(SettingsPalette.text(activity))
                setTextIsSelectable(true)
                setHorizontallyScrolling(false)
                breakStrategy = Layout.BREAK_STRATEGY_SIMPLE
                textDirection = View.TEXT_DIRECTION_LTR
                layoutDirection = View.LAYOUT_DIRECTION_LTR
                setLineSpacing(dp(2).toFloat(), 1f)
                setPadding(dp(12), dp(12), dp(12), dp(12))
                background = GradientDrawable().apply {
                    cornerRadius = dp(12).toFloat()
                    setColor(SettingsPalette.container(activity))
                }
            }, LinearLayout.LayoutParams(-1, -2))
        }
    }
}
