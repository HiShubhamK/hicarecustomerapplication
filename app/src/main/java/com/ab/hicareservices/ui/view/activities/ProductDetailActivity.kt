package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.databinding.ActivityProductDetailBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.FAQAdapter
import com.ab.hicareservices.ui.adapter.ProductDetailAdapter
import com.ab.hicareservices.ui.adapter.ProductDetailCustomerReviewAdapter
import com.ab.hicareservices.ui.adapter.RelatedProductAdapter
import com.ab.hicareservices.ui.handler.OnRelatedProductClick
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    var productid: String? = null
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null
    private lateinit var mAdapter: ProductDetailAdapter
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private lateinit var faqAdapter: FAQAdapter
    private lateinit var productGallery: ArrayList<ProductGallery>
    private lateinit var customerReviewAdapter: ProductDetailCustomerReviewAdapter
    lateinit var progressDialog: ProgressDialog
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        productid = intent.getStringExtra("productid").toString()
        if (intent != null) {
            if (intent.getStringExtra("pincode").toString() != "") {
                AppUtils2.pincode = intent.getStringExtra("pincode").toString()
            } else {
//                Toast.makeText(this,"two "+AppUtils2.pincode.toString(),Toast.LENGTH_SHORT).show()
                AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "400080").toString()
                MyLocationListener(this@ProductDetailActivity)
            }
        }

        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "400080").toString()

        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        productGallery = ArrayList()

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
        getlist()


    }

    private fun getlist() {
        var counts = 1
        progressDialog.show()


        customerReviewAdapter = ProductDetailCustomerReviewAdapter(binding.vpTestomonial, this)
        binding.vpTestomonial.adapter = customerReviewAdapter
        binding.vpTestomonial.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

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
        relatedProductAdapter = RelatedProductAdapter(this, viewProductModel)


        binding.recRelatedProduct.adapter = relatedProductAdapter
//        binding.recRelatedProduct.addItemDecoration(CirclePagerIndicatorDecoration())
        binding.recRelatedProduct.layoutManager!!.smoothScrollToPosition(
            binding.recRelatedProduct,
            RecyclerView.State(),
            binding.recRelatedProduct.adapter!!.itemCount
        )

//        relatedProductAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
//
//
//            override fun onProductClickedHandler(position: Int, productid: Int) {
////                viewProductModel.getAddProductInCart(
////                    1,
////                    productid!!.toInt(),
////                    AppUtils2.customerid.toInt()
////                )
//            }
//
//            override fun onProductView(position: Int, productid: OrderSummeryData) {
//                TODO("Not yet implemented")
//            }
//
//        })
        //FAQ
        binding.recFAQ.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        faqAdapter = FAQAdapter()
        binding.recFAQ.adapter = faqAdapter
//        binding.recRelatedProduct.addItemDecoration(CirclePagerIndicatorDecoration())
        binding.recRelatedProduct.layoutManager!!.smoothScrollToPosition(
            binding.recFAQ,
            RecyclerView.State(),
            binding.recFAQ.adapter!!.itemCount
        )
        viewProductModel.CreateEventNotificationResponse.observe(
            this@ProductDetailActivity,
            Observer {
                if (it.IsSuccess == true) {
//                    Toast.makeText(
//                        this@ProductDetailActivity,
//                        "Thank You! For Notifying Us",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    DesignToast.makeText(this, "ThankYou For Notifying Us!", Toast.LENGTH_SHORT, DesignToast.TYPE_SUCCESS).show();

                }
            })


        viewProductModel.producDetailsResponse.observe(this, Observer {


            var maxquantity = it.ProductConfiguration!!.MaximumBuyQuantity
            var productid = it.ProductConfiguration!!.ProductId
            if (it.ProductConfiguration!!.StockCount!!.toInt() <= 0 || it.ProductConfiguration!!.IsStockAvailable == false) {
                binding.tvAddToCart.visibility = View.GONE
                binding.lnrDetailCount.visibility = View.GONE
                binding.btnNotifyMe.visibility = View.VISIBLE
                var productid = it.ProductDetails!!.Id
                var productthmbnail = it.ProductConfiguration!!.ProductThumbnail

                binding.btnNotifyMe.setOnClickListener {
//
                    val data = HashMap<String, Any>()
                    data["Id"] = 0
                    data["Mobile_No"] = AppUtils2.mobileno
                    data["Event_Source"] = "Product"
                    data["Event_Type"] = "Out of stock"
                    data["Reference_Id"] = productid.toString()
                    data["Additional_Data"] =
                        "Product|" + productid.toString() + "|" + AppUtils2.customerid + "|" + AppUtils2.pincode + "|" + AppUtils2.mobileno + "|" + productthmbnail
                    data["NextNotified_On"] = getCurrentDate()
                    data["Is_Notify"] = true
                    data["Created_By"] = 0
                    data["Created_On"] = getCurrentDate()
                    data["Notification_Tag"] = "string"
                    data["Notification_Title"] = "string"
                    data["Notification_Body"] = "string"
                    viewProductModel.CreateEventForMobileAppNotification(data)
                }


            } else {
                binding.tvAddToCart.visibility = View.VISIBLE
                binding.btnNotifyMe.visibility = View.GONE
                binding.lnrDetailCount.visibility = View.VISIBLE
                binding.tvAddToCart.setOnClickListener {
//                if (binding.tvAddToCart.text == "Goto Cart") {
//                    val intent = Intent(this, AddToCartActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Product Added To Cart", Toast.LENGTH_SHORT).show()

                    viewProductModel.addtocart.observe(this, Observer {
                        if (it.IsSuccess == true) {

                            getSummarydata()
//                            Toast.makeText(this, "Product Added To Cart", Toast.LENGTH_SHORT).show()
                            DesignToast.makeText(this, "Product Added To Cart", Toast.LENGTH_SHORT, DesignToast.TYPE_SUCCESS).show();


//                            binding.tvAddToCart.text = "Goto Cart"
                        } else {
//                            Toast.makeText(
//                                this,
//                                "Something went wrong! Unable to add product into cart",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            DesignToast.makeText(this, "Something went wrong! Unable to add product into cart", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();


                        }
                    })

                    viewProductModel.getAddProductInCart(
                        counts,
                        productid!!.toInt(),
                        AppUtils2.customerid.toInt()
                    )
//                }
                }

            }
            if (it.ProductTestimonialList != null) {
                if (it.ProductTestimonialList.isEmpty()) {
                    binding.lnrCustomerReview.visibility = View.GONE
                } else {
                    customerReviewAdapter.setProductReview(it.ProductTestimonialList)
                }
            } else {
                binding.lnrCustomerReview.visibility = View.GONE
            }
            if (it.ProductGallery != null) {
                mAdapter.setPrductdetail(it.ProductGallery, this)
            }
            if (it.RelatedProducts != null) {
                if (it.RelatedProducts.isEmpty()) {
                    binding.lnrRelatedData.visibility = View.GONE
                } else {
                    binding.lnrRelatedData.visibility = View.VISIBLE
                    relatedProductAdapter.setRelatedProduct(it.RelatedProducts, this)
                }

            } else {
                binding.lnrRelatedData.visibility = View.GONE

            }
            if (it.ProductFAQ != null) {
                if (it.ProductFAQ.isEmpty()) {
                    binding.lnrFAQ.visibility = View.GONE
                } else {
                    binding.lnrFAQ.visibility = View.VISIBLE

                    binding.recFAQ.visibility = View.VISIBLE
                    faqAdapter.setFaq(it.ProductFAQ, this)
                }

            } else {
                binding.lnrFAQ.visibility = View.GONE

                binding.recFAQ.visibility = View.GONE

            }


            relatedProductAdapter.setOnOrderItemClicked(object : OnRelatedProductClick {
                //            override fun onRelatedProdAddtoCart(position: Int, productid: Int, data: Int?) {
//                binding.appCompatImageViewd.text = data.toString()
//            }

                override fun setonaddclicklistener(
                    position: Int,
                    productid: Int,
                    i: Int,
                ) {
                    progressDialog.show()


//                    Handler(Looper.getMainLooper()).postDelayed({
//                        viewProductModel.addtocart.observe(this@ProductDetailActivity, Observer {
//                            progressDialog.dismiss()
//                            if(it.IsSuccess==true){
//                                progressDialog.dismiss()
//                                Toast.makeText(this@ProductDetailActivity,"Product Added to Cart", Toast.LENGTH_LONG).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        getSummarydata()
                        AppUtils2.eventCall(this@ProductDetailActivity,"Product Added To Cart: {ProductId:"+productid+" Quantity: "+i+"}")

                        val mIntent = intent
                        finish()
                        startActivity(mIntent)
                        progressDialog.dismiss()

                    }, 2000)


//                    viewProductModel.productcount.observe(this, Observer {
//                        if (it.IsSuccess == true) {
//
//                            if (it.Data == 0) {
//                                binding.cartmenu.visibility = View.GONE
//                            } else {
//                                binding.cartmenu.visibility = View.VISIBLE
//                                AppUtils2.cartcounts = it.Data.toString()
//                                binding.appCompatImageViewd.text = it.Data.toString()
////                    Toast.makeText(this@ProductDetailActivity,"Product Added to Cart", Toast.LENGTH_LONG).show()
//
//                            }
//                        } else {
//                            binding.cartmenu.visibility = View.GONE
//                        }
//                    })

//                    viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

//                            }else{
//                                progressDialog.dismiss()
//                                Toast.makeText(this@ProductDetailActivity,"Something went to wrong", Toast.LENGTH_LONG).show()
//                            }
//                        })
                    viewProductModel.getAddProductInCart(i, productid, AppUtils2.customerid.toInt())
//                    }, 500)
                }
            })



            binding.tvProductName.text =
                it.ProductDetails!!.ProductName
            binding.tvCategory.text =
                it.ProductDetails!!.ProductSEOCategory.toString()
            binding.tvCategory.text =
                it.ProductDetails!!.ProductSEOCategory.toString()

            binding.txtpriceline.text =
                "M.R.P: " + "\u20B9" + it.ProductConfiguration!!.PricePerQuantity
            binding.txtpriceline.paintFlags =
                binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            binding.txtprice.text =
                "Our Price: " + "\u20B9" + it.ProductConfiguration!!.DiscountedPrice
            binding.txtratingvalues.text = it.ProductConfiguration!!.ProductRating.toString()
            if (it.ProductConfiguration!!.Discount.toString() != "") {
                binding.tvDisccount.text =
                    "Save " + "\u20B9" + it.ProductConfiguration!!.Discount.toString()

            } else {
                binding.tvDisccount.visibility = View.GONE
            }
            //counter quantity
            binding.tvCustomerRatesCount.text =
                it.ProductConfiguration!!.CustomRatingMessage.toString()
            if (it.ProductConfiguration!!.MinimumBuyQuantity == 0) {
                binding.textcount.text = "1"
                counts = 1
            } else {
                binding.textcount.text = it.ProductConfiguration!!.MinimumBuyQuantity.toString()
                counts = it.ProductConfiguration!!.MinimumBuyQuantity!!.toInt()
            }

            binding.imgadd.setOnClickListener {
                counts = counts + 1
                if (counts > 1) {
                    binding.imgremove.visibility = View.VISIBLE
                    binding.imgdelete.visibility = View.GONE
                } else if (counts <= maxquantity!!.toInt()) {
                    binding.imgadd.isClickable = false
                }
                binding.textcount.text = counts.toString()
//                viewProductModel.getAddProductInCart(counts, productlists.ProductId!!.toInt(), 20)

            }

            if (it.ProductConfiguration!!.ProductShortDescription != null || it.ProductConfiguration!!.ProductShortDescription != "") {
                binding.tvProductdesc.text =
                    Html.fromHtml(it.ProductConfiguration!!.ProductShortDescription, HtmlCompat.FROM_HTML_MODE_COMPACT)

            } else {
                binding.tvProductdesc.visibility = View.GONE
            }
            if (it.ProductConfiguration!!.ProductDetailDescription != null || it.ProductConfiguration!!.ProductDetailDescription != "") {
                binding.tvProductdescLong.text =
                    Html.fromHtml(it.ProductConfiguration!!.ProductDetailDescription,HtmlCompat.FROM_HTML_MODE_COMPACT)

            } else {
                binding.lnrMoreItem.visibility = View.GONE
                binding.tvProductdescLong.visibility = View.GONE
            }



            if (counts == 1) {
                binding.imgremove.visibility = View.GONE
                binding.imgdelete.visibility = View.VISIBLE
            } else if (counts > 1) {
                binding.imgremove.visibility = View.VISIBLE
                binding.imgdelete.visibility = View.GONE
            }

            binding.imgremove.setOnClickListener {
                counts = counts - 1
                binding.textcount.text = counts.toString()
                if (counts == 1) {
//
                    binding.imgremove.visibility = View.GONE
                    binding.imgdelete.visibility = View.VISIBLE
                } else {
                    binding.imgdelete.visibility = View.GONE
                }
                if (counts <= maxquantity!!.toInt()) {
                    binding.imgadd.isClickable = true
                }
            }




            binding.ratingbar.rating = it.ProductConfiguration!!.ProductRating!!.toFloat()
            val drawable: Drawable = binding.ratingbar.progressDrawable
            drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
            progressDialog.dismiss()
        })


//        viewProductModel.getProductDetails(productid!!.toInt(), "400601", customerid!!.toInt())

        if(AppUtils2.pincode.equals("")){
            viewProductModel.getProductDetails(
                productid!!.toInt(),
                "400080",
                AppUtils2.customerid.toInt()
            )
        }else{

            viewProductModel.getProductDetails(
                productid!!.toInt(),
                AppUtils2.pincode,
                AppUtils2.customerid.toInt()
            )
        }


        binding.cartmenu.setOnClickListener {
            val intent = Intent(this, AddToCartActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getSummarydata() {
        viewProductModel.productcount.observe(this, Observer {
            if (it.IsSuccess == true) {

                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    AppUtils2.cartcounts = it.Data.toString()
                    binding.appCompatImageViewd.text = it.Data.toString()
//                    Toast.makeText(this@ProductDetailActivity,"Product Added to Cart", Toast.LENGTH_LONG).show()

                }
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

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

    override fun onResume() {
        super.onResume()
        viewProductModel.productcount.observe(this, Observer {
            if (it.IsSuccess == true) {

                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    AppUtils2.cartcounts = it.Data.toString()
                    binding.appCompatImageViewd.text = it.Data.toString()
                }
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })
        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }
}