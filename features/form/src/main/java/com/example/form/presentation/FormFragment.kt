package com.example.form.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import com.example.form.R
import com.pointer.core.base.BaseFragment
import com.pointer.core.extensions.gone
import com.pointer.core.extensions.visible
import kotlinx.android.synthetic.main.form_fragment.*
import kotlinx.android.synthetic.main.form_layout.*
import org.koin.android.ext.android.inject

class FormFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.form_fragment
    private val viewModel by inject<FormViewModel>()
    private val inputManager: InputMethodManager? by lazy { context!!.getSystemService<InputMethodManager>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    okButton.isEnabled = it.isNotEmpty()
                    viewModel.onEvent(ViewEvent.TextChange(it.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        okButton.setOnClickListener {
            activity?.currentFocus?.windowToken?.let {
                inputManager?.hideSoftInputFromWindow(
                    it,
                    0
                )
            }
            viewModel.onEvent(ViewEvent.Request)
        }
        viewModel.viewEffect.observe(this, Observer { onEffect(it) })
    }

    override fun onBackPressed() {
        viewModel.onEvent(ViewEvent.BackPressed)
    }

    private fun onEffect(effect: ViewEffect?) {
        when (effect) {
            is ViewEffect.Loading -> onLoading()
            is ViewEffect.Error -> onError(effect)
            else -> return
        }
    }

    private fun onError(effect: ViewEffect.Error) {
        hideLoading()
        countField.setText("")
        showToast(effect.error.message!!)
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun onLoading() {
        progressLayout.visible()
    }

    private fun hideLoading() {
        progressLayout.gone()
    }
}