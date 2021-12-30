package com.tada.category

import androidx.lifecycle.ViewModel
import javax.inject.Inject


class CategoryViewModel
@Inject
constructor(
    private val repo: CategoryRepository
) : ViewModel() {

    fun getCategories() = repo.getCategories()

}