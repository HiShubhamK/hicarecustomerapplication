package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProducDetailsData(
    @SerializedName("ProductDetails") var ProductDetails: ProductDetails? = ProductDetails(),
    @SerializedName("ProductGallery") var ProductGallery: ArrayList<ProductGallery> = arrayListOf(),
    @SerializedName("ProductConfiguration") var ProductConfiguration: ProductConfiguration? = ProductConfiguration(),
    @SerializedName("ProductFAQ") var ProductFAQ: ArrayList<ProductFAQ> = arrayListOf(),
    @SerializedName("RelatedProducts") var RelatedProducts: ArrayList<RelatedProducts> = arrayListOf(),
//    @SerializedName("ProductSpecificationList") var ProductSpecificationList: ArrayList<String> = arrayListOf(),
    @SerializedName("ProductTestimonialList") var ProductTestimonialList: ArrayList<ProductTestimonialList> = arrayListOf()
) {}
