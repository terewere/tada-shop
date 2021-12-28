package com.kasa.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.kasa.models.ProductWithImages
import java.text.ParseException
import javax.inject.Inject


@ExperimentalPagingApi
class ProductViewModel
@Inject
constructor(
    private val repo: ProductRepository
) : ViewModel() {


    private var savedState: SavedStateHandle = SavedStateHandle()


    fun getProducts(categoryId: Int) = repo.getProducts(categoryId).cachedIn(viewModelScope)



    fun addProfit( profit: Float) {
//        i(TAG, "SET STATUS CHANNEL ${toChannel}")
            savedState["profit"] = profit

    }

    fun getProfitLiveData(): LiveData<Float> {
        return savedState.getLiveData<Float>("profit")
    }


    fun getProfit(): String {
        return savedState.get("profit")?:""
    }














//    fun fetchDoggoImages2(): Flow<PagingData<UiModel>> {
//        return repo.letDoggoImagesFlowDb()
//            .map { pagingData: PagingData<ItemWithImagesAndCategory> ->
//                // Map outer stream, so you can perform transformations on
//                // each paging generation.
//                pagingData
//                    .map { user ->
//                        // Convert items in stream to UiModel.UserModel.
//                        UiModel.ItemModel(user)
//                    }
//                    .insertSeparators<UiModel.ItemModel, UiModel> { before, after ->
//
//
//                        when {
////                            before == null -> after?.model?.category?.label?.let {
////                                UiModel.SeparatorItem(
////                                    it
////                                )
////                            }
//
//                            before == null ->  null
//
//                            after == null -> before.model.category?.label?.let {
//                                UiModel.SeparatorItem(
//                                    it
//                                )
//                            }
//
//                            shouldSeparate(before, after) -> before.model.category?.label?.let {
//                                UiModel.SeparatorItem(
//                                    it
//                                )
//                            }
//
//                            // Return null to avoid adding a separator between two items.
//                            else -> null
////                    }
//                        }
//                    }
//
//            }.cachedIn(viewModelScope)
//    }
//
//
//    private fun shouldSeparate(before: UiModel.ItemModel, after: UiModel.ItemModel): Boolean {
//       return before.model.category?.categoryId != after.model.category?.categoryId
//    }
}

    sealed class UiModel {
        data class ItemModel(val model: ProductWithImages) : UiModel()
        data class SeparatorItem(val description: String) : UiModel()
    }




//TODO ADDING A RANDOM ITEM INN THE LIST
//val newResult: Flow<PagingData<UiModel>> = repository.getSearchResultStream(queryString)
//    .map { pagingData ->
//        var index = 0
//        pagingData.map { UiModel.RepoItem(it, index++) }
//    }
//    .map {
//        it.insertSeparators<UiModel.RepoItem, UiModel> { before, after ->
//            if (before.index % 10) {
//                Do something
//            }
//        }
//    }