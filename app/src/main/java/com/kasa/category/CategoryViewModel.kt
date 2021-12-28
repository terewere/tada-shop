package com.kasa.category

import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class CategoryViewModel
@Inject
constructor(
    private val repo: CategoryRepository
) : ViewModel() {

    fun getCategories() = repo.getCategories()

}