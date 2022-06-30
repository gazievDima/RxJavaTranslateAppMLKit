package com.gaziev.vcttranslate.app.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.fragments.common.BaseFragment
import com.gaziev.vcttranslate.databinding.FrSettingsBinding

class SettingsFragment : BaseFragment<FrSettingsBinding>() {
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FrSettingsBinding =
        FrSettingsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChooseFromLang.setOnClickListener {
            findNavController().navigate(R.id.chooseFromLangDialogFragment)
        }
        
        binding.btnChooseToLang.setOnClickListener {
            findNavController().navigate(R.id.chooseToLangDialogFragment)
        }
    }

}