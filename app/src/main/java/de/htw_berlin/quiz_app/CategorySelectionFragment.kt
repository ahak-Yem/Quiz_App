package de.htw_berlin.quiz_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.htw_berlin.quiz_app.databinding.FragmentCategorySelectionBinding

class CategorySelectionFragment : Fragment() {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var binding: FragmentCategorySelectionBinding
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_category_selection,container,false)
        categoryViewModel=ViewModelProvider(this)[CategoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getCategories()
    }


    private fun initRecyclerView(){
        categoryAdapter = CategoryAdapter(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                navigateToNextScreen(category)
            }
        })
        binding.categoryRecyclerView.apply {
            layoutManager=LinearLayoutManager(this.context)
            adapter = categoryAdapter
        }
    }

    private fun getCategories(){
        categoryViewModel.setCategories()
        categoryViewModel.categories.observe(viewLifecycleOwner){ categoryList ->
            categoryAdapter.categoryList = categoryList
        }
        categoryViewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(
                this.context,
                it,
                Toast.LENGTH_SHORT
            ).show()}
    }

    private fun navigateToNextScreen(category: Category) {
        val action = CategorySelectionFragmentDirections
            .actionCategorySelectionFragmentToNormalerModusFragment(category)
        findNavController().navigate(action)
    }
}
