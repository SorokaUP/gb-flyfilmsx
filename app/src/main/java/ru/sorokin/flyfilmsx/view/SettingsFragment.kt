package ru.sorokin.flyfilmsx.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import ru.sorokin.flyfilmsx.databinding.FragmentSettingsBinding


const val IS_SHOW_18_PLUS = "SHOW18PLUS"

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.isContent18plus.isChecked = false
        activity?.let {
            binding.isContent18plus.isChecked = (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_SHOW_18_PLUS, false))
        }
        binding.isContent18plus.setOnCheckedChangeListener { buttonView, isChecked ->
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putBoolean(IS_SHOW_18_PLUS, isChecked)
                    apply()
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}
