package com.io.unknow.ui.dialogfragment


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.io.unknow.R
import com.io.unknow.navigation.ISetting
import com.io.unknow.util.Setting
import java.util.*

private val TAG = SettingDialogFragment::class.simpleName!!
class SettingDialogFragment: DialogFragment() {

    private var mContext: Context? = null
    private lateinit var setting: ISetting
    private var languageChange: String = ""
    private lateinit var spinner: Spinner
    private var isBlack = false
    private lateinit var switchCompat: SwitchCompat

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        Log.i(TAG,"OnAttach")
        setting = context as ISetting
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Log.i(TAG,"OnCreateView")
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchCompat = view.findViewById(R.id.theme_switch)
        spinner = view.findViewById(R.id.language_spinner)

        val adapter = ArrayAdapter(
            mContext!!, android.R.layout.simple_spinner_item, arrayListOf(
                "Русский",
                "English"
            )
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinnerInit()
        switchInit()
        Log.i(TAG,"OnViewCreated")
    }

    private fun switchInit(){
        isBlack = Setting.getThemeBlack()

        switchCompat.isChecked = !isBlack

        val edit = Setting.edit()
        switchCompat.setOnCheckedChangeListener { _, isChecked ->
            Setting.setTheme(edit,!isChecked)
        }
    }

    private fun spinnerInit(){
        val edit = Setting.edit()

        val languages = arrayListOf("ru","en")
        val language = Locale.getDefault().language.toLowerCase(Locale.getDefault())
        if (language in languages){
            if(language == "ru") {
                spinner.setSelection(0)
            }
            else{
                spinner.setSelection(1)
            }
        }

        languageChange = spinner.selectedItem.toString()

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View,
                selectedItemPosition: Int,
                selectedId: Long
            ) {
                Setting.setLanguage(edit,languages[selectedItemPosition])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"OnDestroy")
        mContext = null
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (languageChange != spinner.selectedItem.toString() || isBlack == switchCompat.isChecked) {
            setting.updateSetting()
        }
    }
}