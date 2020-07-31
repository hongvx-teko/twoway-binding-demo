package vn.teko.databindingissue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import vn.teko.databindingissue.databinding.Fragment1Binding
import java.lang.ref.WeakReference

class Fragment1 : Fragment(), Action {

    private val viewModel: Fragment1ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<Fragment1Binding>(
            inflater,
            R.layout.fragment1,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setView(this)

        viewModel.field1.observe(viewLifecycleOwner, Observer {
            Log.d("Fragment1", "onViewCreated: field1 ${it}")
        })
        viewModel.field2.observe(viewLifecycleOwner, Observer {
            Log.d("Fragment1", "onViewCreated: field2 ${it}")
        })

        viewModel.money.observe(viewLifecycleOwner, Observer {
            Log.d("Fragment1", "onViewCreated: money ${it}")
        })
    }

    override fun next() {
        findNavController().navigate(R.id.action_fragment1_to_fragment2)
    }

}

interface Action {
    fun next()
}

class Fragment1ViewModel : ViewModel() {

    val field1 = MutableLiveData<String>()
    val field2 = MutableLiveData<String>()

    val money = MutableLiveData<Long>()

    private var view: WeakReference<Action>? = null

    fun setView(action: Action) {
        view = WeakReference(action)
    }

    fun next() {
        view?.get()?.next()
    }

}