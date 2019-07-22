package com.github.kr328.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.kr328.ui.fragment.holder.SettingHolder
import com.github.kr328.ui.fragment.parser.parseSettingXml

abstract class SettingFragment() : Fragment() {
    protected fun getViewHolderOriginal(id: String): SettingHolder? = viewMap[id]

    protected inline fun <reified T : SettingHolder> getViewHolder(id: String): T {
        val result = getViewHolderOriginal(id) ?: throw ViewNotFoundException("$id not found")

        return if ( result is T ) result else throw ViewNotFoundException("$id not mismatch ${T::class}")
    }

    abstract fun getXmlResourceId(): Int

    private lateinit var viewMap: Map<String, SettingHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val xml = requireContext().resources.getXml(getXmlResourceId())
        val result = parseSettingXml(requireContext(), inflater, container, xml)

        viewMap = result.holders

        return result.root
    }

    class ViewNotFoundException(message: String?) : Exception(message)
}