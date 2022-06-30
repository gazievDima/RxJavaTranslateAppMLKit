package com.gaziev.vcttranslate.app.fragments.stars

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.fragments.common.BaseFragment
import com.gaziev.vcttranslate.app.fragments.common.MainViewModelFactory
import com.gaziev.vcttranslate.app.fragments.stars.list.Callback
import com.gaziev.vcttranslate.app.fragments.stars.list.StarsAdapter
import com.gaziev.vcttranslate.core.AdditionalEditText
import com.gaziev.vcttranslate.core.SimpleTextWatcher
import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.data.model.HistoryEntity
import com.gaziev.vcttranslate.data.model.FavoriteEntity
import com.gaziev.vcttranslate.databinding.FrStarsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class StarsFragment : BaseFragment<FrStarsBinding>() {

    companion object {
        var LAST_WORD_FROM_HISTORY: HistoryEntity = HistoryEntity(word = "", langCode = "")
    }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FrStarsBinding =
        FrStarsBinding::inflate
    private val viewModel: StarsViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory())[StarsViewModel::class.java]
    }
    private var list: List<FavoriteEntity> = mutableListOf()
    private val additionalEditText by lazy { AdditionalEditText(requireActivity()) }
    private val simpleTextWatcher = SimpleTextWatcher()
    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHistory()
        observed()
        clickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }

    private fun observed() {
        binding.etSearch.addTextChangedListener(simpleTextWatcher)
        disposable = simpleTextWatcher.behaviorSubject
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ searchWord ->

                val newList = viewModel.getNewList(list, searchWord)
                createNewAdapterList(newList)
            }, {

                Log.e(TAG, "Cannot find words!")
            })

        viewModel.listWords.observe(viewLifecycleOwner) {
            list = it.reversed()
            createNewAdapterList(list)
        }
    }

    private fun createNewAdapterList(updatedList: List<FavoriteEntity>) {

        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.adapter = StarsAdapter(updatedList, object : Callback {
            override fun playVoice(word: String) {
                additionalEditText.voiceText(word)
            }

            override fun copy(word: String) {
                additionalEditText.copyText(word)
            }
        })
    }

    private fun clickListeners() {
        binding.apply {

            btnHistory.setOnClickListener {
                btnHistory.setBackgroundResource(R.drawable.bg_button_blue)
                btnDictionary.setBackgroundResource(R.drawable.bg_button_blue_light)
                viewModel.getHistory()
            }

            btnDictionary.setOnClickListener {
                btnHistory.setBackgroundResource(R.drawable.bg_button_blue_light)
                btnDictionary.setBackgroundResource(R.drawable.bg_button_blue)
                viewModel.getDictionary()
            }
        }
    }
}