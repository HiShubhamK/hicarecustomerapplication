package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.ui.adapter.OverviewDetailAdapter
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import org.json.JSONObject
import kotlin.collections.ArrayList

class OverviewProductDetailsActivity : AppCompatActivity() {

    var billdata = ""
    var shipdaata = ""
    private lateinit var binding: ActivityOverviewProductDetailsBinding
    private val viewProductModel: ProductViewModel by viewModels()
    private lateinit var mAdapter: OverviewDetailAdapter
    var shippingdata: String? = ""
    var billingdata: String? = ""
    lateinit var datalist: ArrayList<CartlistResponseData>
    lateinit var progressDialog: ProgressDialog
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""
    var checkalterbox = false
    var paymentvalue = ""
    var discountvalue = ""
    var paymenttotalamount = ""
    var voucherdiscount=""
    var vouchercode=""
    lateinit var cartid: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_product_details)
        binding = ActivityOverviewProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog =
            ProgressDialog(this, com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()
        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "400080").toString()
        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()

        AppUtils2.email = SharedPreferenceUtil.getData(this, "EMAIL", "").toString()

        binding.txtcoupon.setText("")

        binding.imgLogo.setOnClickListener {
            SharedPreferenceUtil.setData(this, "Billingdata", "")
            SharedPreferenceUtil.setData(this, "Shippingdata", "")
            val intent = Intent(this@OverviewProductDetailsActivity, AddressActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val intent = intent
        billdata = intent.getStringExtra("Billdata").toString()
        shipdaata = intent.getStringExtra("Shipdata").toString()

        getproductlist()
        getSummarydata("")
        getAddressList()
        if (!billdata.equals("")) {
            getAddressforbilling()
        }

        if (!AppUtils2.pincode.isNullOrEmpty()) {
            binding.tvPincode.text = "Deliver to pincode " + AppUtils2.pincode
        } else {
            binding.tvPincode.visibility = View.GONE
        }


        binding.txtplcaeorder.setOnClickListener {

            val notes = prepareNotes()

            Log.d("Notescheck", notes.toString())

            SharedPreferenceUtil.setData(this, "razorpayorderid", "")

            viewProductModel.GetCreateRazorpayProductOrderIdResponse.observe(this, Observer{

                if (it.IsSuccess == true) {
                    AppUtils2.razorpayorderid = it.Data.toString()
                    SharedPreferenceUtil.setData(this, "razorpayorderid", it.Data.toString())
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("Product", true)
                    startActivity(intent)
                } else {

                }
            })

//            notes.put("cartsyncforpayment", resultString)
//            notes.put("userId", AppUtils2.customerid.toString())
//            notes.put("TotalAmount", paymenttotalamount.toString())
//            notes.put("DeliveryCharges", "0")
//            notes.put("FinalAmount", paymentvalue)
//            notes.put("VoucherCode", vouchercode)
//            notes.put("VoucherDiscount", voucherdiscount)
//            notes.put("OrderSource", "CustomerApp")

            var resultString = cartid.joinToString(separator = "|")

            var data = HashMap<String, Any>()

            data["cartsyncforpayment"] = resultString
            data["userId"] = AppUtils2.customerid.toString()
            data["TotalAmount"] = paymenttotalamount
            data["DeliveryCharges"] = "0"
            data["FinalAmount"] = paymentvalue
            data["VoucherCode"] = vouchercode
            data["VoucherDiscount"] = voucherdiscount
                data["OrderSource"] ="CustomerApp"

            viewProductModel.GetCreateRazorpayProductOrderId(data)

//            viewProductModel.razorpayOrderIdResponse.observe(this, Observer {
//
//                if (it.IsSuccess == true) {
//                    AppUtils2.razorpayorderid = it.Data.toString()
//                    SharedPreferenceUtil.setData(this, "razorpayorderid", it.Data.toString())
//                    val intent = Intent(this, PaymentActivity::class.java)
//                    intent.putExtra("Product", true)
//                    startActivity(intent)
//                } else {
//
//                }
//
//            })
//
//            viewProductModel.CreateRazorpayOrderId(paymentvalue.toDouble(), 12342)

        }

        binding.btnappiledcoupon.setOnClickListener {

            if (binding.coupunname.text.toString().equals("Remove Coupon")) {
                binding.coupunname.text = "Apply Coupon"
                binding.txtcoupon.setText("")
                getSummarydata("")
            } else {
                if (binding.txtcoupon.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Please enter valid coupon", Toast.LENGTH_LONG).show()
                    DesignToast.makeText(
                        this,
                        "Please enter valid coupon",
                        Toast.LENGTH_SHORT,
                        DesignToast.TYPE_ERROR
                    ).show()

                } else {

                    viewProductModel.validateVoucherResponse.observe(this, Observer {
                        if (it.IsSuccess == true) {
                            if (it.Data.toString() != null) {
                                binding.coupunname.text = "Remove Coupon"
                                getSummarydata(binding.txtcoupon.text.toString())
//                                Toast.makeText(
//                                    this,
//                                    "Applied coupon successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()

                                DesignToast.makeText(
                                    this,
                                    "Applied coupon successfully",
                                    Toast.LENGTH_SHORT,
                                    DesignToast.TYPE_SUCCESS
                                ).show()

                            } else {
                                ShowCustomeDialog()
//                                Toast.makeText(this, "Invalid coupon", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            if (checkalterbox == false) {
                                checkalterbox = true
                                ShowCustomeDialog()
                            }
//                            Toast.makeText(this, "Invalid coupon", Toast.LENGTH_SHORT).show()
                        }
                    })

                    viewProductModel.getValidateVoucher(
                        binding.txtcoupon.text.toString(),
                        AppUtils2.customerid.toInt(),
                        AppUtils2.pincode
                    )
                }

            }
        }

    }


    private fun prepareNotes(): JSONObject {
        val notes = JSONObject()
        var resultString = cartid.joinToString(separator = "|")
        notes.put("cartsyncforpayment", resultString)
        notes.put("userId", AppUtils2.customerid.toString())
        notes.put("TotalAmount", paymenttotalamount.toString())
        notes.put("DeliveryCharges", "0")
        notes.put("FinalAmount", paymentvalue)
        notes.put("VoucherCode", vouchercode)
        notes.put("VoucherDiscount", voucherdiscount)
        notes.put("OrderSource", "CustomerApp")
        return notes
    }

    private fun getAddressforbilling() {
        progressDialog.show()
        viewProductModel.getaddressbydetailid.observe(this, Observer {
            progressDialog.dismiss()

            AppUtils2.flat = it.FlatNo.toString()
            AppUtils2.builingname = it.BuildingName.toString()
            AppUtils2.street = it.Street.toString()
            AppUtils2.locality = it.Locality.toString()
            AppUtils2.landmark = it.Landmark.toString()
            AppUtils2.pincodelast = it.Pincode.toString()
            AppUtils2.city = it.City.toString()
            AppUtils2.state = it.State.toString()
            binding.txtbilling.visibility = View.VISIBLE
            binding.txtbilling.text =
                it.FlatNo.toString() + "," + it.BuildingName.toString() + "," + it.Street.toString() + "," +
                        it.Locality.toString() + "," + it.Landmark.toString() + "," + it.Pincode.toString()
        })


        viewProductModel.getAddressDetailbyId(billdata!!.toInt())

    }

    private fun getAddressList() {

        progressDialog.show()

        viewProductModel.cutomeraddress.observe(this, Observer {

            progressDialog.dismiss()

            for (i in 0 until it.size) {
                var data = it.get(i).Id.toString()
                if (data.equals(shipdaata)) {
                    AppUtils2.cutomername = it.get(i).ContactPersonName.toString()
                    AppUtils2.customermobile = it.get(i).ContactPersonMobile.toString()
                    AppUtils2.customeremail = it.get(i).ContactPersonEmail.toString()
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                }
//                else if(data.equals(billdata)){
//                    binding.txtbilling.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
//                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
//                }
                else {

                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }

    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = OverviewDetailAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            progressDialog.dismiss()

            cartid= ArrayList()

            for (i in 0 until it.size){
                cartid.add(it.get(i).CartId.toString())
            }

            AppUtils2.leaderlist = it as ArrayList<CartlistResponseData>
//             datalist= it as ArrayList<CartlistResponseData>
//            Toast.makeText(this,AppUtils2.leaderlist.size.toString(),Toast.LENGTH_LONG).show()
            mAdapter.setCartList(it, this, viewProductModel)

        })

        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt(), AppUtils2.pincode)
    }

    fun getSummarydata(toString: String) {

        progressDialog.show()

        viewProductModel.getsummarydata.observe(this, Observer {

            AppUtils2.productamount = it.FinalAmount.toString()
            AppUtils2.actualvalue = it.TotalAmount.toString()
            AppUtils2.totaldiscount = it.TotalDiscount.toString()
            AppUtils2.vouchercode = it.VoucherCode.toString()
            AppUtils2.voucherdiscount = it.VoucherDiscount.toString()
            binding.txtfinaltext.text = "\u20B9" + it.FinalAmount!!.toDouble().toString()
            paymentvalue = it.FinalAmount!!.toDouble().toString()
            binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount!!.toDouble().toString()
            binding.txtdiscount.text = "-" + "\u20B9" + it.TotalDiscount!!.toDouble().toString()
            binding.txttoalamount.text = "\u20B9" + it.FinalAmount!!.toDouble().toString()
            binding.txtvoucherdiscount.text="\u20B9" + it.VoucherDiscount!!.toDouble().toString()
            discountvalue = it.TotalDiscount!!.toDouble().toString()
            paymenttotalamount = it.TotalAmount!!.toDouble().toString()
            vouchercode=it.VoucherCode.toString()
            voucherdiscount=it.VoucherDiscount!!.toDouble().toString()
            if (it.DeliveryCharges != null && it.DeliveryCharges!!.toDouble().toInt() == 0) {
                binding.tvDeliveryCharge.text = "Free "
            } else {
                binding.tvDeliveryCharge.text = it.DeliveryCharges!!.toDouble().toString()
            }
            progressDialog.dismiss()

        })

        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, toString)

    }

    override fun onResume() {
        super.onResume()
        getSummarydata("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharedPreferenceUtil.setData(this, "Billingdata", "")
        SharedPreferenceUtil.setData(this, "Shippingdata", "")
        val intent = Intent(this@OverviewProductDetailsActivity, AddressActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    public fun ShowCustomeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hicare Alert!")
        builder.setMessage("Invalid coupon")
        builder.setCancelable(false)
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//            Toast.makeText(
//                applicationContext,
//                android.R.string.ok, Toast.LENGTH_SHORT
//            ).show()
            checkalterbox = false
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            checkalterbox = false
            dialog.dismiss()
        }

        builder.show()
    }
}