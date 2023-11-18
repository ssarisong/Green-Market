package kr.ac.hansung.greenmarket.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.itemView.findViewById<TextView>(R.id.tv_title1).text = product.name
        holder.itemView.findViewById<TextView>(R.id.tv_price1).text = product.price.toString()
        holder.itemView.findViewById<TextView>(R.id.tv_productdetail1).text = product.detail

        // 추가: itemView 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            // 클릭한 제품의 정보를 전달
            intent.putExtra("productId", product.productId)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


