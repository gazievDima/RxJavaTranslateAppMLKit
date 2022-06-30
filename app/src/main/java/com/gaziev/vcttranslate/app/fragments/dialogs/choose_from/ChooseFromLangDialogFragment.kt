package com.gaziev.vcttranslate.app.fragments.dialogs.choose_from

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.list.ChooseToAdapter
import com.gaziev.vcttranslate.core.Language
import com.gaziev.vcttranslate.core.LanguageList
import com.gaziev.vcttranslate.databinding.DialogChooseLangBinding

class ChooseFromLangDialogFragment : DialogFragment() {

    companion object {
        var CHOOSE_FROM_LANG = Language("en", "English")
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ChooseFromLangViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_choose_lang, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogChooseLangBinding.bind(view)
        binding.btnCloseDialog.setOnClickListener {
            this.dismiss()
        }

        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.adapter = ChooseToAdapter(LanguageList.list) {
            viewModel.chooseLanguage(it)
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams = dialog?.window?.attributes!!
        params.width = 900
        params.height = 1200
        dialog?.window?.attributes = params as WindowManager.LayoutParams

    }

}