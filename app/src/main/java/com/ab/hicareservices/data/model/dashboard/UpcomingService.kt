package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class UpcomingService(
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
//    @SerializedName("Amount_to_collect__c") var AmountToCollect_c: String? = null,
//    @SerializedName("Advance_Payment_Amount__c") var AdvancePaymentAmount_c: String? = null,
    @SerializedName("Order__c") var Order_c: String? = null,
//    @SerializedName("HR_Status__c") var HRStatus_c: String? = null,
//    @SerializedName("IsMegatask__c") var IsMegatask_c: Boolean? = null,
//    @SerializedName("IsDoubleCheckConfirmed__c") var IsDoubleCheckConfirmed_c: Boolean? = null,
    @SerializedName("Service_Sequence_Number__c") var ServiceSequenceNumber_c: Int? = null,
//    @SerializedName("Amount") var Amount: String? = null,
//    @SerializedName("AssetCode") var AssetCode: String? = null,
//    @SerializedName("Location") var Location: String? = null,
//    @SerializedName("Device") var Device: String? = null,
    @SerializedName("Description") var Description: String? = null,
//    @SerializedName("Createdby") var Createdby: String? = null,
//    @SerializedName("Created_Datetime") var CreatedDatetime: String? = null,
//    @SerializedName("Payment_OTP__c") var PaymentOTP_c: String? = null,
//    @SerializedName("CustomerId__c") var CustomerId_c: String? = null,
    @SerializedName("OrderNumber__c") var OrderNumber_c: String? = null,
//    @SerializedName("OrderType__c") var OrderType_c: String? = null,
//    @SerializedName("OE_Order_Number__c") var OEOrderNumber_c: String? = null,
//    @SerializedName("HR_Account__r") var HRAccount_r: HRAccount_r? = HRAccount_r(),
//    @SerializedName("Technician_Mobile__c") var TechnicianMobile_c: String? = null,
//    @SerializedName("Last_Interaction__c") var LastInteraction_c: String? = null,
//    @SerializedName("Payment_AlternateMobileNo") var PaymentAlternateMobileNo: String? = null,
//    @SerializedName("Account_Name") var AccountName: String? = null,
//    @SerializedName("HR_Appointment_Finish_Date_Time__c") var HRAppointmentFinishDateTime_c: String? = null,
//    @SerializedName("HR_Appointment_Start_Date_Time__c") var HRAppointmentStartDateTime_c: String? = null,
//    @SerializedName("Rule_Violation__c") var RuleViolation_c: String? = null,
    @SerializedName("SR_Date__c") var SRDate_c: String? = null,
//    @SerializedName("HR_AccountName__c") var HRAccountName_c: String? = null,
//    @SerializedName("Email__c") var Email_c: String? = null,
//    @SerializedName("Mobile__c") var Mobile_c: String? = null,
//    @SerializedName("Alternate_Mobile__c") var AlternateMobile_c: String? = null,
//    @SerializedName("Account_Type__c") var AccountType_c: String? = null,
    @SerializedName("HR_TaskType__r") var HRTaskType_r: HRTaskType_r? = HRTaskType_r(),
    @SerializedName("Order_Service_Area__r") var OrderServiceArea_r: OrderServiceArea_r? = OrderServiceArea_r(),
    @SerializedName("Order__r") var Order_r: Order_r? = Order_r(),
//    @SerializedName("Case") var Case: String? = null,
//    @SerializedName("Task_Type") var TaskType: String? = null,
    @SerializedName("Service_Number") var ServiceNumber: String? = null,
    @SerializedName("ServicePlan__c") var ServicePlan_c: String? = null,
//    @SerializedName("Service_Area__c") var ServiceArea_c: String? = null,
    @SerializedName("Service_Step__c") var ServiceStep_c: String? = null,
    @SerializedName("BuildingName__c") var BuildingName_c: String? = null,
//    @SerializedName("WingFlatUnitNo__c") var WingFlatUnitNo_c: String? = null,
//    @SerializedName("Locality__c") var Locality_c: String? = null,
    @SerializedName("HR_Zip_Postal_Code__c") var HRZipPostalCode_c: String? = null,
//    @SerializedName("HR_Early_Start_Date__c") var HREarlyStartDate_c: String? = null,
//    @SerializedName("HR_Due_Date_Date__c") var HRDueDateDate_c: String? = null,
//    @SerializedName("cccccccc__c") var cccccccc_c: String? = null,
    @SerializedName("HR_Appointment_Start_Date__c") var HRAppointmentStartDate_c: String? = null,
    @SerializedName("HR_Appointment_Start_Time_AMPM__c") var HRAppointmentStartTimeAMPM_c: String? = null,
    @SerializedName("HR_Appointment_Finish_Date__c") var HRAppointmentFinishDate_c: String? = null,
    @SerializedName("HR_Appointment_Finish_Time_AMPM__c") var HRAppointmentFinishTimeAMPM_c: String? = null,
    @SerializedName("HR_Assignment_Start_Date__c") var HRAssignmentStartDate_c: String? = null,
//    @SerializedName("OnSiteDateTime__c") var OnSiteDateTime_c: String? = null,
    @SerializedName("HR_Assignment_Start_Time_AMPM__c") var HRAssignmentStartTimeAMPM_c: String? = null,
//    @SerializedName("HR_Assignment_Finish_Date__c") var HRAssignmentFinishDate_c: String? = null,
    @SerializedName("HR_Assignment_Finish_Time_AMPM__c") var HRAssignmentFinishTimeAMPM_c: String? = null,
    @SerializedName("HR_Assigned_Resource__r") var HRAssignedResource_r: HRAssignedResource_r? = HRAssignedResource_r(),
    @SerializedName("New_Task__c") var NewTask_c: String? = null,
//    @SerializedName("Customer_Rating__c") var CustomerRating_c: String? = null,
//    @SerializedName("Service_Request__r") var ServiceRequest_r: ServiceRequest_r? = ServiceRequest_r(),
    @SerializedName("Task_Skill__c") var TaskSkill_c: String? = null,
//    @SerializedName("Is_Last_Service__c") var IsLastService_c: Boolean? = null,
//    @SerializedName("Resource_Name") var ResourceName: String? = null,
//    @SerializedName("Resource_Total_Jobs") var ResourceTotalJobs: String? = null,
//    @SerializedName("Resource_Rating") var ResourceRating: String? = null,
//    @SerializedName("Employee_Id") var EmployeeId: String? = null,
//    @SerializedName("Resource_Id") var ResourceId: String? = null,
//    @SerializedName("TakeFeedback__c") var TakeFeedback_c: Boolean? = null,
//    @SerializedName("Amount_collected__c") var AmountCollected_c: String? = null,
    @SerializedName("HR_Region__r") var HRRegion_r: HRRegion_r? = HRRegion_r(),
//    @SerializedName("Region_Name") var RegionName: String? = null,
//    @SerializedName("HR_Duration__c") var HRDuration_c: Int? = null,
//    @SerializedName("Duration_In_Minute") var DurationInMinute: Int? = null,
//    @SerializedName("ScheduleDay") var ScheduleDay: String? = null,
    @SerializedName("Onsite_OTP__c") var OnsiteOTP_c: String? = null,
    @SerializedName("Onsiteotp") var Onsiteotp: String? = null,
    @SerializedName("Technician_Onsite_OTP__c") var TechnicianOnsiteOTP_c: String? = null,
    @SerializedName("Customer_OTP__c") var CustomerOTP_c: String? = null,
//    @SerializedName("TaskTypeName") var TaskTypeName: String? = null,
//    @SerializedName("TaskTypeDescription") var TaskTypeDescription: String? = null,
    @SerializedName("AppointmentStartDate_YYYMMDD") var AppointmentStartDateYYYMMDD: String? = null,
    @SerializedName("AppointmentStartDate") var AppointmentStartDate: String? = null,
    @SerializedName("AppointmentDateTime") var AppointmentDateTime: String? = null,
    @SerializedName("AppointmentTime") var AppointmentTime: String? = null,
//    @SerializedName("AppointmentStartTimeAsPerAssignment") var AppointmentStartTimeAsPerAssignment: String? = null,
//    @SerializedName("AppointmentEndTimeAsPerAssignment") var AppointmentEndTimeAsPerAssignment: String? = null,
//    @SerializedName("AssignmentDateTime") var AssignmentDateTime: String? = null,
    @SerializedName("AppointmentStartDateTime") var AppointmentStartDateTime: String? = null,
//    @SerializedName("AppointmentFinishDateTime") var AppointmentFinishDateTime: String? = null,
//    @SerializedName("AssignmentStartDateTime") var AssignmentStartDateTime: String? = null,
//    @SerializedName("AssignmentFinishDateTime") var AssignmentFinishDateTime: String? = null,
//    @SerializedName("AssignmentDate") var AssignmentDate: String? = null,
//    @SerializedName("AssignmentTime") var AssignmentTime: String? = null,
//    @SerializedName("AccountLat__c") var AccountLat_c: Int? = null,
//    @SerializedName("AccountLong__c") var AccountLong_c: Int? = null,
    @SerializedName("Google_Lat__c") var GoogleLat_c: String? = null,
    @SerializedName("Google_Long__c") var GoogleLong_c: String? = null,
//    @SerializedName("Incomplete_reason__c") var IncompleteReason_c: String? = null,
//    @SerializedName("IsBlocked__c") var IsBlocked_c: Boolean? = null,
//    @SerializedName("OnsiteLat__c") var OnsiteLat_c: String? = null,
//    @SerializedName("OnsiteLong__c") var OnsiteLong_c: String? = null,
//    @SerializedName("CompletedLat__c") var CompletedLat_c: String? = null,
//    @SerializedName("CompletedLong__c") var CompletedLong_c: String? = null,
//    @SerializedName("OTP_Status__c") var OTPStatus_c: String? = null,
//    @SerializedName("HR_Street__c") var HRStreet_c: String? = null,
//    @SerializedName("Customer_Suggestion__c") var CustomerSuggestion_c: String? = null,
//    @SerializedName("Floating_Question__c") var FloatingQuestion_c: String? = null,
//    @SerializedName("Floating_Question_Rating__c") var FloatingQuestionRating_c: String? = null,
//    @SerializedName("OBD_Rating__c") var OBDRating_c: String? = null,
//    @SerializedName("OBD_Remark__c") var OBDRemark_c: String? = null,
//    @SerializedName("Service_Missing_Items__c") var ServiceMissingItems_c: String? = null,
//    @SerializedName("HR_WorkOrderName__c") var HRWorkOrderName_c: String? = null,
//    @SerializedName("CompletionDateTime__c") var CompletionDateTime_c: String? = null,
//    @SerializedName("CompletionTime__c") var CompletionTime_c: String? = null,
    @SerializedName("Tag__c") var Tag_c: String? = null,
//    @SerializedName("SC_OTP__c") var SCOTP_c: Int? = null,
//    @SerializedName("Landmark__c") var Landmark_c: String? = null,
//    @SerializedName("creation_datetime") var creationDatetime: String? = null,
//    @SerializedName("Lead_Utm_Source") var LeadUtmSource: String? = null,
//    @SerializedName("Lead_Utm_Campaign") var LeadUtmCampaign: String? = null,
    @SerializedName("SR_Plan_Date") var SRPlanDate: String? = null,
//    @SerializedName("New_Task__r") var NewTask_r: String? = null,
//    @SerializedName("Technician_Onsite_Image__c") var TechnicianOnsiteImage_c: String? = null,
//    @SerializedName("Infestation_Level__c") var InfestationLevel_c: String? = null,
//    @SerializedName("Is_Consultation_Inspection_Done__c") var IsConsultationInspectionDone_c: Boolean? = null,
//    @SerializedName("Is_Consultation_Required__c") var IsConsultationRequired_c: Boolean? = null,
//    @SerializedName("Inspection_Image") var InspectionImage: String? = null,
    @SerializedName("Appointmentstarttime") var Appointmentstarttime: String? = null,
    @SerializedName("AppointmentFinishtime") var AppointmentFinishtime: String? = null,
    @SerializedName("AppointmentDate") var AppointmentDate: String? = null,
    @SerializedName("AppointmentDate__c") var AppointmentDate_c: String? = null,
//    @SerializedName("Next_SR_Service_Date__c") var NextSRServiceDate_c: String? = null,
//    @SerializedName("Next_SR_Service_Start_Time__c") var NextSRServiceStartTime_c: String? = null,
//    @SerializedName("Next_SR_Service_End_Time__c") var NextSRServiceEndTime_c: String? = null,
//    @SerializedName("Infestation_Level_c__c") var InfestationLevelC_c: String? = null,
//    @SerializedName("Slot_Booked_Online__c") var SlotBookedOnline_c: Boolean? = null,
//    @SerializedName("SlotUrl") var SlotUrl: String? = null,
//    @SerializedName("PaymentLink") var PaymentLink: String? = null,
//    @SerializedName("AppointmentMissed") var AppointmentMissed: Boolean? = null,
//    @SerializedName("IsDeafTechnicain") var IsDeafTechnicain: Boolean? = null,
//    @SerializedName("DeliveredServiceMessage") var DeliveredServiceMessage: String? = null,
    @SerializedName("ServiceETA") var ServiceETA: String? = null,
//    @SerializedName("TechnicianTrackingLink") var TechnicianTrackingLink: String? = null,
//    @SerializedName("TaskAssignment_AsPer_TechnicianLocation") var TaskAssignmentAsPerTechnicianLocation: String? = null,
//    @SerializedName("Referral_Code") var ReferralCode: String? = null,
//    @SerializedName("Trigger_Type") var TriggerType: String? = null,
//    @SerializedName("Technician_Location") var TechnicianLocation: String? = null,
//    @SerializedName("Confirm_Url_SMS") var ConfirmUrlSMS: String? = null,
//    @SerializedName("Callback_Url_SMS") var CallbackUrlSMS: String? = null,
//    @SerializedName("Confirm_Url_WA") var ConfirmUrlWA: String? = null,
//    @SerializedName("Callback_Url_WA") var CallbackUrlWA: String? = null,
//    @SerializedName("OrdinalAppointmentStartDate") var OrdinalAppointmentStartDate: String? = null,
//    @SerializedName("SlotUrlExpiry") var SlotUrlExpiry: String? = null,
    @SerializedName("FeedbackLink") var FeedbackLink: String? = null,
    @SerializedName("Order_SP_Code") var OrderSPCode: String? = null,
    @SerializedName("Unit") var Unit: String? = null
) {}
