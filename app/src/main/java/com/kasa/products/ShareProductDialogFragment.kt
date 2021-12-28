//package com.kasa.products
//
//import android.os.Bundle
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import com.kasa.R
//import com.kasa.databinding.FragmentShareProductDialogListDialogBinding
//import com.kasa.databinding.FragmentShareProductDialogListDialogItemBinding
//
//// TODO: Customize parameter argument names
//const val ARG_ITEM_COUNT = "item_count"
//
///**
// *
// * A fragment that shows a list of items as a modal bottom sheet.
// *
// * You can show this modal bottom sheet from your activity like this:
// * <pre>
// *    ShareProductDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
// * </pre>
// */
//class ShareProductDialogFragment : BottomSheetDialogFragment() {
//
//    private var _binding: FragmentShareProductDialogListDialogBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentShareProductDialogListDialogBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
//            LinearLayoutManager(context)
//        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
//            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }
//    }
//
//    private inner class ViewHolder internal constructor(binding: FragmentShareProductDialogListDialogItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        internal val text: TextView = binding.text
//    }
//
//    private inner class itemAdapter internal constructor(private val mItemCount: Int) :
//        RecyclerView.Adapter<ViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//            return ViewHolder(
//                FragmentShareProductDialogListDialogItemBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                )
//            )
//
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.text.text = position.toString()
//        }
//
//        override fun getItemCount(): Int {
//            return mItemCount
//        }
//    }
//
//    companion object {
//
//        // TODO: Customize parameters
//        fun newInstance(itemCount: Int): ShareProductDialogFragment =
//            ShareProductDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_ITEM_COUNT, itemCount)
//                }
//            }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}