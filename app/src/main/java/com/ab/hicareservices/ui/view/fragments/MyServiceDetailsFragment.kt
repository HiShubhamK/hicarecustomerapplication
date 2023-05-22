package com.ab.hicareservices.ui.view.fragments

import com.ab.hicareservices.data.model.service.ServiceTaskData
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.ui.adapter.MyServiceViewDetailsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.databinding.FragmentMyServiceDetailsBinding

class MyServiceDetailsFragment : Fragment() {
    lateinit var binding: FragmentMyServiceDetailsBinding
    private val tasksList: List<ServiceTaskData>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: MyServiceViewDetailsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            tasksList = getArguments().getParcelableArrayList(ARG_TASK);
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyServiceDetailsBinding.inflate(inflater, container, false)
        requireActivity().title = "My Service Details"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        binding.recycleView.layoutManager = layoutManager
        mAdapter = MyServiceViewDetailsAdapter(tasksList)
        binding.recycleView.adapter = mAdapter
    }

    companion object {
        private const val ARG_TASK = "ARG_TASK"
        fun newInstance( /*List<ServiceTaskData> service_details*/): MyServiceDetailsFragment {
            val args = Bundle()
            //        args.putParcelableArrayList(ARG_TASK, (ArrayList<? extends Parcelable>) service_details);
            val fragment = MyServiceDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}