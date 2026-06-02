package com.dmg.muslimapp.ui.setup.language

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseFragment
import com.dmg.muslimapp.ui.setup.location.LocationFragment

class LanguageFragment: BaseFragment(), LanguageFragmentView {
    private lateinit var mPresenter : LanguageFragmentPresenter

    private lateinit var adb: AlertDialog.Builder
    private val locales = arrayOf("en", "id", "ar", "ms")

    @BindView(R.id.choosen)
    lateinit var txtChoosenLang: TextView

    companion object {
        val TAG = "LanguageFragment"

        fun newInstance(): LanguageFragment {
            val args = Bundle()
            val fragment = LanguageFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.setup_language_fragment, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    override fun setUp(view: View) {
        mPresenter = LanguageFragmentPresenter(this)

        adb = AlertDialog.Builder(this.activity!!)
        mPresenter.onPreparedLanguage(this.activity!!)
        mPresenter.onLocaleSelected("en")
    }

    override fun updateLanguage(languageList: MutableList<String>) {
        val languages = arrayOfNulls<String>(languageList.size)
        //languages = languageList.toTypedArray()

        txtChoosenLang.text = languageList.get(0)

        adb.setItems(languages) { dialog, which ->
            val clickedItemValue = languageList[which]
            txtChoosenLang.text = clickedItemValue
            mPresenter.onLocaleSelected(locales[which])
        }
    }

    @OnClick(R.id.choosen)
    fun chooseLang() {
        adb.show()
    }

    @OnClick(R.id.img_next)
    fun onNext(){
        fragmentManager?.beginTransaction()?.addToBackStack(LocationFragment.TAG)?.replace(R.id.content_layout, LocationFragment.newInstance())?.commit()
    }
}

