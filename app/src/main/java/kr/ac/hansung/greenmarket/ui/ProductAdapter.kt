package kr.ac.hansung.greenmarket.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.models.Product

class ProductAdapter(private val context: Context, private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        // ViewHolder 클래스에서 뷰에 직접 접근

        // 가격이 일정 길이 이상이면 '...'으로 생략
        val priceTextView = holder.itemView.findViewById<TextView>(R.id.tv_price1)

        val maxPriceLength = if (product.name.length<=5) {
            9
        } else {
            6
        }
        val truncatedPrice = if (product.price.toString().length > maxPriceLength) {
            "${product.price.toString().substring(0, maxPriceLength)}⋯"
        } else {
            product.price.toString()
        }
        priceTextView.text = truncatedPrice
//        holder.itemView.findViewById<TextView>(R.id.tv_price1).text = product.price.toString()

        // 제목이 일정 길이 이상이면 '...'으로 생략
        val maxTitleLength = if (product.price.toString().length <= 5) {
            8
        } else {
            6
        }
        val truncatedTitle = if (product.name.length > maxTitleLength) {
            "${product.name.substring(0, maxTitleLength)}⋯"
        } else {
            product.name
        }
        holder.itemView.findViewById<TextView>(R.id.tv_title1).text = truncatedTitle
//        holder.itemView.findViewById<TextView>(R.id.tv_title1).text = product.name

        // 제품 설명이 일정 길이 이상이면 '...'으로 생략
        val maxDetailLength = 10
        val truncatedDetail = if (product.detail.length > maxDetailLength) {
            "${product.detail.substring(0, maxDetailLength)}⋯"
        } else {
            product.detail
        }
        holder.itemView.findViewById<TextView>(R.id.tv_productdetail1).text = truncatedDetail
//        holder.itemView.findViewById<TextView>(R.id.tv_productdetail1).text = product.detail


        Glide.with(holder.itemView.context)
            .load(product.img)
            .into(holder.itemView.findViewById<ImageView>(R.id.img_product))

        // item 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val intent = when (context) {
                is HomeActivity -> Intent(context, ProductDetailActivity::class.java)
                is MypostListActivity -> Intent(context, RePostActivity::class.java)
                else -> null
            }

            intent?.let {
                // 클릭한 제품의 정보를 전달
                intent.putExtra("productId", product.productId)
                context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


