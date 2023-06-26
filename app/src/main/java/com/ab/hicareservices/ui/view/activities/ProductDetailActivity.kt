package com.ab.hicareservices.ui.view.activities

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.databinding.ActivityProductDetailBinding
import com.ab.hicareservices.ui.adapter.ProductDetailAdapter
import com.ab.hicareservices.ui.adapter.RelatedProductAdapter
import com.ab.hicareservices.ui.view.fragments.HomeFragment
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    var productid: String? = null
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null
    private lateinit var mAdapter: ProductDetailAdapter
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private lateinit var productGallery: ArrayList<ProductGallery>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        productid = intent.getStringExtra("productid").toString()

        customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()

        productGallery = ArrayList()

//        Toast.makeText(this, AppUtils2.producDetailsResponse.toString(), Toast.LENGTH_LONG).show()


        getlist()

    }

    private fun getlist() {

        binding.recyleproductdetails.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = ProductDetailAdapter()

        binding.recyleproductdetails.adapter = mAdapter
        binding.recyleproductdetails.addItemDecoration(CirclePagerIndicatorDecoration())
        binding.recyleproductdetails.layoutManager!!.smoothScrollToPosition(
            binding.recyleproductdetails,
            RecyclerView.State(),
            binding.recyleproductdetails.adapter!!.itemCount
        )

        //Related product
        binding.recRelatedProduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        relatedProductAdapter = RelatedProductAdapter()

        binding.recRelatedProduct.adapter = relatedProductAdapter
        binding.recRelatedProduct.addItemDecoration(CirclePagerIndicatorDecoration())
        binding.recRelatedProduct.layoutManager!!.smoothScrollToPosition(
            binding.recRelatedProduct,
            RecyclerView.State(),
            binding.recRelatedProduct.adapter!!.itemCount
        )


        viewProductModel.producDetailsResponse.observe(this, Observer {

//            if (it.ProductGallery!=null) {
            mAdapter.setPrductdetail(it.ProductGallery, this)
//            }
            relatedProductAdapter.setRelatedProduct(it.RelatedProducts,this)

            binding.tvProductName.text =
                it.ProductDetails!!.ProductName + " (" + it.ProductDetails!!.ProductCode + ")"
  binding.tvCategory.text =
                it.ProductDetails!!.ProductSEOCategory.toString()
            binding.tvCategory.text =
                it.ProductDetails!!.ProductSEOCategory.toString()

            binding.txtpriceline.text = "M.R.P: " + "\u20B9" + it.ProductConfiguration!!.PricePerQuantity
            binding.txtpriceline.paintFlags=binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            binding.txtprice.text =  "Our Price: " +"\u20B9" + it.ProductConfiguration!!.DiscountedPrice
            binding.txtratingvalues.text= it.ProductConfiguration!!.ProductRating.toString()
            binding.tvCustomerRatesCount.text= it.ProductConfiguration!!.CustomRatingMessage.toString()
            if (it.ProductConfiguration!!.IsStockAvailable==true){
                binding.tvStockAvailable.setTextColor(Color.parseColor("#217d55"))
                binding.tvStockAvailable.text= it.ProductConfiguration!!.StockCount.toString() +" Available in stock"

            }else {
                binding.tvStockAvailable.setTextColor(Color.parseColor("#C62828"))
                binding.tvStockAvailable.text= "Out of stock"

            }
            binding.ratingbar.rating = it.ProductConfiguration!!.ProductRating!!.toFloat()
            val drawable: Drawable = binding.ratingbar.progressDrawable
            drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
        })


        viewProductModel.getProductDetails(productid!!.toInt(), "400601", customerid!!.toInt())

    }
    class CirclePagerIndicatorDecoration : RecyclerView.ItemDecoration() {
        private val colorActive = 0x727272
        private val colorInactive = 0xF44336

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private val mIndicatorHeight = (DP * 16).toInt()

        /**
         * Indicator stroke width.
         */
        private val mIndicatorStrokeWidth = DP * 2

        /**
         * Indicator width.
         */
        private val mIndicatorItemLength = DP * 16

        /**
         * Padding between indicators.
         */
        private val mIndicatorItemPadding = DP * 4

        /**
         * Some more natural animation interpolation
         */
        private val mInterpolator: AccelerateDecelerateInterpolator =
            AccelerateDecelerateInterpolator()
        private val mPaint: Paint = Paint()
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val itemCount = parent.adapter!!.itemCount

            // center horizontally, calculate width and subtract half from center
            val totalLength = mIndicatorItemLength * itemCount
            val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

            // center vertically in the allotted space
            val indicatorPosY = parent.height - mIndicatorHeight / 2f
            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


            // find active page (which should be highlighted)
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            val activePosition = layoutManager!!.findFirstVisibleItemPosition()
            if (activePosition == RecyclerView.NO_POSITION) {
                return
            }

            // find offset of active page (if the user is scrolling)
            val activeChild = layoutManager.findViewByPosition(activePosition)
            val left = activeChild!!.left
            val width = activeChild.width

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            val progress: Float = mInterpolator.getInterpolation(left * -1 / width.toFloat())
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
        }
//        fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
//            super.onDrawOver(c, parent, state!!)

//        }

        private fun drawInactiveIndicators(
            c: Canvas,
            indicatorStartX: Float,
            indicatorPosY: Float,
            itemCount: Int
        ) {
            mPaint.setColor(Color.GRAY)

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            var start = indicatorStartX
            for (i in 0 until itemCount) {
                // draw the line for every item
                c.drawCircle(start + mIndicatorItemLength, indicatorPosY, itemWidth / 6, mPaint)
                //  c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth
            }
        }

        private fun drawHighlights(
            c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
            highlightPosition: Int, progress: Float, itemCount: Int
        ) {
            mPaint.color = Color.parseColor("#2bb77a")

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            if (progress == 0f) {
                // no swipe, draw a normal indicator
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                /*   c.drawLine(highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
        */c.drawCircle(highlightStart, indicatorPosY, itemWidth / 6, mPaint)
            } else {
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                // calculate partial highlight
                val partialLength = mIndicatorItemLength * progress
                c.drawCircle(
                    highlightStart + mIndicatorItemLength,
                    indicatorPosY,
                    itemWidth / 6,
                    mPaint
                )

                // draw the cut off highlight
                /* c.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
*/
                // draw the highlight overlapping to the next item as well
                /* if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                */
                /*c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);*/
                /*
                c.drawCircle(highlightStart ,indicatorPosY,itemWidth/4,mPaint);

            }*/
            }
        }


        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = mIndicatorHeight

        }

        companion object {
            private val DP: Float = Resources.getSystem().getDisplayMetrics().density
        }

        init {
            mPaint.setStrokeCap(Paint.Cap.ROUND)
            mPaint.setStrokeWidth(mIndicatorStrokeWidth)
            mPaint.setStyle(Paint.Style.FILL)
            mPaint.setAntiAlias(true)
        }
    }

}