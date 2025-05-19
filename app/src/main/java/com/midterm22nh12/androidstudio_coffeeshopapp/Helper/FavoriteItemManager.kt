package com.midterm22nh12.androidstudio_coffeeshopapp.Helper

import android.content.Context
import android.util.Log // Thêm Log để debug nếu cần
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel

object FavoriteItemManager {

    // Key để lưu danh sách yêu thích trong TinyDB.
    // Đảm bảo key này là duy nhất và không trùng với "CartList" hoặc các key khác.
    private const val FAVORITES_LIST_KEY = "FavoriteItemsListTinyDB"

    private fun getTinyDB(context: Context): TinyDB {
        return TinyDB(context)
    }

    /**
     * Lấy danh sách các ItemsModel yêu thích từ TinyDB.
     * Trả về một ArrayList rỗng nếu không có item nào được lưu.
     */
    fun getFavoriteItems(context: Context): ArrayList<ItemsModel> {
        // Sử dụng getListObject từ TinyDB của bạn
        return getTinyDB(context).getListObject(FAVORITES_LIST_KEY) ?: ArrayList()
        // Lưu ý: TinyDB của bạn đã xử lý việc trả về ArrayList rỗng nếu key không tồn tại
        // hoặc không parse được, dựa trên cách triển khai `getListObject`.
        // Nếu `getListObject` có thể trả về null, bạn cần xử lý `?: ArrayList()`
    }

    /**
     * Lưu danh sách các ItemsModel yêu thích vào TinyDB.
     */
    private fun saveFavoriteItems(context: Context, favorites: ArrayList<ItemsModel>) {
        // Sử dụng putListObject từ TinyDB của bạn
        getTinyDB(context).putListObject(FAVORITES_LIST_KEY, favorites)
    }

    /**
     * Thêm một item vào danh sách yêu thích.
     * Sẽ không thêm nếu item đã tồn tại (kiểm tra dựa trên `title`).
     */
    fun addFavoriteItem(context: Context, item: ItemsModel) {
        val favorites = getFavoriteItems(context)
        // Kiểm tra xem item đã tồn tại trong danh sách yêu thích chưa (dựa trên title)
        if (!favorites.any { it.title == item.title }) {
            favorites.add(item)
            saveFavoriteItems(context, favorites)
            Log.d("FavoriteManager", "Added to favorites: ${item.title}. Total: ${favorites.size}")
        } else {
            Log.d("FavoriteManager", "${item.title} is already a favorite.")
        }
    }

    /**
     * Xóa một item khỏi danh sách yêu thích dựa trên `title` của item.
     */
    fun removeFavoriteItem(context: Context, itemTitle: String) {
        val favorites = getFavoriteItems(context)
        val initialSize = favorites.size
        // Xóa item dựa trên title
        val removed = favorites.removeAll { it.title == itemTitle }

        if (removed) {
            saveFavoriteItems(context, favorites)
            Log.d("FavoriteManager", "Removed from favorites: $itemTitle. Total: ${favorites.size}")
        } else {
            Log.d("FavoriteManager", "$itemTitle not found in favorites to remove.")
        }
    }

    /**
     * Kiểm tra xem một item có trong danh sách yêu thích hay không (dựa trên `title`).
     */
    fun isFavorite(context: Context, itemTitle: String): Boolean {
        val favorites = getFavoriteItems(context)
        val isFav = favorites.any { it.title == itemTitle }
        Log.d("FavoriteManager", "Is '$itemTitle' a favorite: $isFav")
        return isFav
    }

    /**
     * Xóa toàn bộ danh sách yêu thích.
     */
    fun clearFavorites(context: Context) {
        // Lưu một danh sách rỗng để xóa tất cả
        saveFavoriteItems(context, ArrayList())
        Log.d("FavoriteManager", "All favorites cleared.")
    }
}