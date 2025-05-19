package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

// import android.content.Intent
import android.os.Bundle
// import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// import androidx.core.os.bundleOf
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.FavoriteItemManager
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ManagmentCart
import com.midterm22nh12.androidstudio_coffeeshopapp.R
// import com.midterm22nh12.androidstudio_coffeeshopapp.Activity.MainActivity
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding // Thay đổi thứ tự để phù hợp quy ước
    private lateinit var item: ItemsModel
    private lateinit var managerCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Giữ nguyên nếu bạn dùng
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managerCart = ManagmentCart(this)

        val receivedItem = intent.getSerializableExtra("object") as? ItemsModel
        if (receivedItem == null) {
            Toast.makeText(this, "Không thể tải chi tiết sản phẩm.", Toast.LENGTH_SHORT).show()
            finish()
            return // Thoát sớm nếu không có item
        }
        item = receivedItem // Gán item sau khi đã kiểm tra
        item.numberInCart = 1 // Khởi tạo số lượng trong giỏ hàng là 1 khi mở chi tiết

        loadItemDetails()
        initSizeList()
        setupClickListeners()
        updateFavoriteButtonState() // Cập nhật trạng thái nút yêu thích ban đầu
    }

    private fun initSizeList() {
        binding.apply {
            // Đặt size nhỏ làm mặc định khi vào
            smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
            mediumBtn.setBackgroundResource(0)
            largeBtn.setBackgroundResource(0)
            // Cập nhật giá hoặc logic khác dựa trên size nếu cần
            // Ví dụ: item.selectedSize = "S"; updatePriceBasedOnSize();

            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
                // item.selectedSize = "S"; updatePriceBasedOnSize();
            }
            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                largeBtn.setBackgroundResource(0)
                // item.selectedSize = "M"; updatePriceBasedOnSize();
            }
            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                // item.selectedSize = "L"; updatePriceBasedOnSize();
            }
        }
    }

    private fun loadItemDetails() {
        binding.apply {
            if (item.picUrl.isNotEmpty()) {
                val imageNameToLoad = item.picUrl[0]
                val resourceId = resources.getIdentifier(imageNameToLoad, "drawable", packageName)

                Glide.with(this@DetailActivity)
                    .load(if (resourceId != 0) resourceId else imageNameToLoad)
                    .placeholder(R.drawable.americano) // Nên có ảnh placeholder này
                    // .error(R.drawable.image_load_error) // Và ảnh lỗi nếu cần
                    .into(picMain) // Đổi tên binding.picMain cho phù hợp
            } else {
                picMain.setImageResource(R.drawable.americano) // Ảnh mặc định
            }

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$${item.price}" // Format giá nếu cần: String.format("%.2f", item.price)
            ratingTxt.text = item.rating.toString()
            numberItemTxt.text = item.numberInCart.toString() // Hiển thị số lượng ban đầu
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addToCartBtn.setOnClickListener {
                try {
                    // Lấy số lượng từ TextView tại thời điểm nhấn nút
                    val quantity = Integer.parseInt(numberItemTxt.text.toString())
                    // Tạo một bản sao của item hoặc cập nhật item hiện tại để thêm vào giỏ
                    // Điều này quan trọng nếu bạn muốn các item trong giỏ có số lượng độc lập
                    // với số lượng đang hiển thị trên DetailActivity sau khi đã thêm.
                    // Hoặc đơn giản là cập nhật số lượng của item này:
                    item.numberInCart = quantity
                } catch (e: NumberFormatException) {
                    item.numberInCart = 1 // Mặc định nếu parse lỗi
                }
                managerCart.insertItems(item) // Sử dụng hàm insertItems từ ManagmentCart
                Toast.makeText(this@DetailActivity, "'${item.title}' đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
            }

            favBtn.setOnClickListener { // favBtn là id của nút yêu thích trong XML
                toggleFavoriteStatus()
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener { // plusCart là id của nút +
                var currentQuantity = Integer.parseInt(numberItemTxt.text.toString())
                currentQuantity++
                numberItemTxt.text = currentQuantity.toString()
                // Không cần cập nhật item.numberInCart ở đây,
                // nó sẽ được cập nhật khi nhấn addToCartBtn
            }

            minusBtn.setOnClickListener { // minusBtn là id của nút -
                var currentQuantity = Integer.parseInt(numberItemTxt.text.toString())
                if (currentQuantity > 1) {
                    currentQuantity--
                    numberItemTxt.text = currentQuantity.toString()
                }
            }
        }
    }

    private fun toggleFavoriteStatus() {
        if (FavoriteItemManager.isFavorite(this, item.title)) {
            FavoriteItemManager.removeFavoriteItem(this, item.title)
            Toast.makeText(this, "'${item.title}' đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
        } else {
            // Khi thêm vào yêu thích, không cần quan tâm numberInCart của item
            // FavoriteItemManager nên chỉ lưu thông tin cơ bản của sản phẩm
            FavoriteItemManager.addFavoriteItem(this, item)
            Toast.makeText(this, "'${item.title}' đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
        }
        updateFavoriteButtonState()
    }

    private fun updateFavoriteButtonState() {
        // Đảm bảo favBtn không null (mặc dù với view binding thì ít khi)
        if (::binding.isInitialized) {
            if (FavoriteItemManager.isFavorite(this, item.title)) {
                binding.favBtn.setImageResource(R.drawable.favorite_white)
            } else {
                binding.favBtn.setImageResource(R.drawable.img)
            }
        }
    }
}