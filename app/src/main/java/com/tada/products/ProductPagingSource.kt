package com.tada.products

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tada.models.ProductWithImages
import com.tada.network.ApiService
import com.tada.products.ProductRepository.Companion.DEFAULT_PAGE_INDEX

import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ProductPagingSource(val api: ApiService) :
    PagingSource<Int, ProductWithImages>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductWithImages> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response: List<ProductWithImages> = api.getProducts(params.loadSize, page, null)

            LoadResult.Page(
                response, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductWithImages>) = DEFAULT_PAGE_INDEX


}