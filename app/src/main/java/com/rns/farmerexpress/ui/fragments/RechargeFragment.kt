package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.adapter.RechargeAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.FragmentRechargeBinding
import com.rns.farmerexpress.model.*
import kotlinx.android.synthetic.main.fragment_recharge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class RechargeFragment : Fragment() {
    var getOpName: ArrayList<String> = ArrayList()
    var getOpCode: ArrayList<String> = ArrayList()
    var getCName: ArrayList<String> = ArrayList()
    lateinit var opName: String
    lateinit var stateName: String
    lateinit var spOperator: Spinner
    lateinit var spState: Spinner
    val getPlanData: ArrayList<Plan> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayout: LinearLayout
    lateinit var btnprocced: Button
    lateinit var progressBar: ProgressBar
    lateinit var etAmounnt: EditText
    var opCode: Int = 1
    var circleCode: Int = 1


    private var _binding: FragmentRechargeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRechargeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        spState = binding.spState
        spOperator = binding.spOperator
        recyclerView = binding.rvRecharge
        progressBar = binding.progressBar
        etAmounnt = binding.etAmount
        linearLayout = binding.ll
        btnprocced = binding.btnProceed
        progressBar.visibility = View.VISIBLE



        getCircleList()
        getOperatorList()
        btnprocced.setOnClickListener {
            if (etPhone.text.isEmpty() && etPhone.text.length < 10) {
                etPhone.error = "अपना फोन नंबर लिखे "
            } else {
                PreferenceConnector.writeString(requireContext(),PreferenceConnector.stateName,stateName)
                PreferenceConnector.writeString(requireContext(),PreferenceConnector.opName,opName)
                PreferenceConnector.writeString(requireContext(),PreferenceConnector.MOBILE,etPhone.text.toString())
                getPlanData.clear()
                 progressBar.visibility = View.VISIBLE
                getRechargeList(circleCode,opCode)
                recyclerView.visibility = View.INVISIBLE
//                Toast.makeText(requireContext(), "plan", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }


    private fun getOperatorList() {
        val service: ApiInterface = APIClient.getPayment()!!.create(ApiInterface::class.java)
        val call: Call<OperatorResModel> = service.getOperatorData()
        try {
            call.enqueue(object : Callback<OperatorResModel> {
                override fun onResponse(
                    call: Call<OperatorResModel>,
                    response: Response<OperatorResModel>
                ) {
                    val responseBody = response.body()!!
                    Log.d("onRes", "onResponse: $responseBody")
                    val getOperatorData: ArrayList<OperatorModel> = ArrayList()
                    val getOpDataPrepaidID: ArrayList<String> = ArrayList()
                    val getOpDataPrepaidName: ArrayList<String> = ArrayList()
                    for (operatorData in responseBody.response) {
                        val data: OperatorModel =
                            OperatorModel(
                                operatorData.operator_name,
                                operatorData.operator_id,
                                operatorData.service_type,
                                operatorData.status,
                                operatorData.bill_fetch,
                                operatorData.bbps_enabled,
                                operatorData.message,
                                operatorData.description,
                                operatorData.amount_minimum,
                                operatorData.amount_maximum
                            )
                        getOperatorData.add(data)
                    }

                    if (response.body() != null) {
                        try {
                            for (s in 0..getOperatorData.size) {
                                if (getOperatorData[s].service_type == "Prepaid") {
                                    getOpDataPrepaidID.add(getOperatorData[s].operator_id)
                                    getOpDataPrepaidName.add(getOperatorData[s].operator_name)
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    try {
                        for (s in 0..getOpDataPrepaidID.size) {
                            getOpCode.add(getOpDataPrepaidID[s])
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                    try {
                        for (s in 0..getOpDataPrepaidName.size) {
                            getOpName.add(getOpDataPrepaidName[s])
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }


                    val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getOpName as List<Any?>
                    )
                    spOperator.adapter = adapter
                    progressBar.visibility = View.INVISIBLE
                    linearLayout.visibility = View.VISIBLE
                    spOperator.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                opCode = Integer.parseInt(getOpCode[p2])
                                opName = getOpName[p2]

                            Toast.makeText(requireContext(),opName,Toast.LENGTH_SHORT).show()
//                            Toast.makeText(requireContext(),i.toString(),Toast.LENGTH_SHORT).show()
//                            ProfileActivity.subDistPos = p2
                            }


                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }
                }


                override fun onFailure(call: Call<OperatorResModel>, t: Throwable) {
                    Log.d("onFail", "onResponse: ${t.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getCircleList() {
        val service: ApiInterface = APIClient.getPayment()!!.create(ApiInterface::class.java)
        val call: Call<CircleResModel> = service.getCircleData()
        try {
            call.enqueue(object : Callback<CircleResModel> {
                override fun onResponse(
                    call: Call<CircleResModel>,
                    response: Response<CircleResModel>
                ) {
                    val responseBody = response.body()!!
                    Log.d("onRes", "onResponse: $responseBody")
                    val getCircleData: ArrayList<CircleModel> = ArrayList()
                    for (circleData in responseBody.response) {
                        val data: CircleModel =
                            CircleModel(circleData.circle_name, circleData.circle_code)
                        getCircleData.add(data)
                    }

                    if (response.body() != null) {
                        try {
                            for (s in 0..getCircleData.size) {
                                getCName.add(getCircleData[s].circle_name)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getCName as List<Any?>
                    )
                    spState.adapter = adapter

                    adapter.notifyDataSetChanged()
                    spState.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                circleCode =
                                    Integer.parseInt(getCircleData[p2].circle_code)
                                stateName = getCircleData[p2].circle_name
//                                getRechargeList(circleCode,opCode)
//                            Toast.makeText(requireContext(),getOperatorData[p2].operator_name.toString(),Toast.LENGTH_SHORT).show()
//                            Toast.makeText(requireContext(),i.toString(),Toast.LENGTH_SHORT).show()
//                            ProfileActivity.subDistPos = p2
                            }


                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }
                }


                override fun onFailure(call: Call<CircleResModel>, t: Throwable) {
                    Log.d("onFail", "onResponse: ${t.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRechargeList(circle: Int, op: Int) {
        val service: ApiInterface = APIClient.getPayment()!!.create(ApiInterface::class.java)
        val call: Call<RechargePlan> = service.getPlanData(ApiInterface.PAY_KEY, circle, op)
        try {
            call.enqueue(object : Callback<RechargePlan> {
                @SuppressLint("WrongConstant", "NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<RechargePlan>,
                    response: Response<RechargePlan>
                ) {
                    val responseBody = response.body()!!
                    Log.d("onResPlan", "onResponse: $responseBody")
                    if(responseBody.success == "false"){
                        tvInvalidOP.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }else {
                        tvInvalidOP.visibility = View.GONE
                        try {
                            for (planData in responseBody.plans.FULLTT) {
                                val data: Plan =
                                    Plan(
                                        planData.rs,
                                        planData.validity,
                                        planData.desc,
                                        planData.Type,
                                        responseBody.operator
                                    )
                                getPlanData.add(data)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            for (planData in responseBody.plans.TOPUP) {
                                val data: Plan =
                                    Plan(
                                        planData.rs,
                                        planData.validity,
                                        planData.desc,
                                        planData.Type,
                                        responseBody.operator
                                    )
                                getPlanData.add(data)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        val adapterrv: RechargeAdapter =
                            RechargeAdapter(requireActivity(), getPlanData)
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                        recyclerView.adapter = adapterrv
//                    adapterrv.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                }


                override fun onFailure(call: Call<RechargePlan>, t: Throwable) {
                    Log.d("onFailPlan", "onResponse: ${t.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
    }
}