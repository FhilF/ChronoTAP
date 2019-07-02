package com.switso.itap.Form;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.switso.itap.GlobalFunction;
import com.switso.itap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class FormListAdapter extends RecyclerView.Adapter<FormListAdapter.ViewHolder> {

    public static final String TAG = "Adapter";
    Context mContext;
    private LayoutInflater mInflater;
    List<FormList> list;
    GlobalFunction globalFunction;


    public FormListAdapter(Context ctx, List<FormList> list) {
        this.mContext = ctx;
        this.list = list;
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        globalFunction = new GlobalFunction(mContext);
    }

    @Override
    public FormListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_form_item, viewGroup, false);
        final FormListAdapter.ViewHolder holder = new FormListAdapter.ViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(FormListAdapter.ViewHolder holder, int i) {
        FormList formList = list.get(i);

        holder.txtFormName.setText(formList.getForm_name());
        holder.txtStatus.setText(formList.getApproval_status());
        holder.txtDateSubmitted.setText(formList.getDt_created());

        holder.relFormItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formList.getForm_type_id().equals("1")) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_failureinout);
                    dialog.setTitle("Custom Dialog");


                    TextView txtWorkdate = dialog.findViewById(R.id.TxtFailureWorkDate);
                    TextView txtFailureInOut = dialog.findViewById(R.id.TxtFailureDateOfFailure);
                    TextView txtTime = dialog.findViewById(R.id.TxtFailureTime);
                    TextView txtTimeType = dialog.findViewById(R.id.TxtFailureTimeType);
                    TextView txtReason = dialog.findViewById(R.id.TxtFailureReason);
                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }

                    txtWorkdate.setText(parseDate(formList.getDt_workdate()));
                    txtFailureInOut.setText(parseDate(formList.getDt_failure_timeinout()));
                    txtTime.setText(parseTime(formList.getTm_time()));
                    txtTimeType.setText(formList.getShift_time_type());
                    txtReason.setText(formList.getReason());
                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                }else if (formList.getForm_type_id().equals("2")){

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_leave);
                    dialog.setTitle("Custom Dialog");

                    TextView txtLeaveType = dialog.findViewById(R.id.TxtLeaveType);
                    TextView txtDateFrom = dialog.findViewById(R.id.TxtLeaveDateFrom);
                    TextView txtDateTo = dialog.findViewById(R.id.TxtLeaveDateTo);
                    TextView txtHalfDay = dialog.findViewById(R.id.TxtLeaveHalfDay);
                    TextView txtReason = dialog.findViewById(R.id.TxtLeaveReason);

                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }


                    txtLeaveType.setText(formList.getLeave_type_code());
                    txtDateFrom.setText(parseDate(formList.getDt_from()));
                    txtDateTo.setText(parseDate(formList.getDt_to()));
                    String isHalfDayValue = "";
                    if (formList.getIs_halfday().equals("1")){
                        isHalfDayValue = "Yes";
                    }else {
                        isHalfDayValue = "No";
                    }
                    txtHalfDay.setText(isHalfDayValue);
                    txtReason.setText(formList.getReason());

                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                }else if (formList.getForm_type_id().equals("3")){

                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_form_offsiteassignment);
                        dialog.setTitle("Custom Dialog");

                        TextView txtStartDate = dialog.findViewById(R.id.TxtOffSiteStartDate);
                        TextView txtStartTime = dialog.findViewById(R.id.TxtOffSiteStartTime);
                        TextView txtEndDate = dialog.findViewById(R.id.TxtOffSiteEndDate);
                        TextView txtEndTime = dialog.findViewById(R.id.TxtOffSiteEndTime);
                        TextView txtReason = dialog.findViewById(R.id.TxtOffSiteReason);

                        LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                        LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                        TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                        TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                        Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }


                        txtStartDate.setText(parseDate(formList.getDt_from()));
                        txtStartTime.setText(parseTime(formList.getTime_from()));
                        txtEndDate.setText(parseDate(formList.getDt_to()));
                        txtEndTime.setText(parseTime(formList.getTime_to()));
                        txtReason.setText(formList.getReason());

                        txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                        txtStatus.setText(formList.getApproval_status());
                        linLayoutApproveDetails.setVisibility(View.VISIBLE);
                        linLayoutSubmit.setVisibility(View.GONE);

                        btnDialogClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    }else if(formList.getForm_type_id().equals("4")) {

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_overtime);
                    dialog.setTitle("Custom Dialog");


                    TextView txtWorkdate = dialog.findViewById(R.id.TxtOvertimeWorkDate);
                    TextView txtStartDate = dialog.findViewById(R.id.TxtOvertimeStartDate);
                    TextView txtStartTime = dialog.findViewById(R.id.TxtOvertimeStartTime);
                    TextView txtEndDate = dialog.findViewById(R.id.TxtOvertimeEndDate);
                    TextView txtEndTime = dialog.findViewById(R.id.TxtOvertimeEndTime);
                    TextView txtReason = dialog.findViewById(R.id.TxtOvertimeReason);

                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }

                    txtWorkdate.setText(parseDate(formList.getDt_workdate()));
                    txtStartDate.setText(parseDate(formList.getDt_from()));
                    txtStartTime.setText(parseTime(formList.getTime_from()));
                    txtEndDate.setText(parseDate(formList.getDt_to()));
                    txtEndTime.setText(parseTime(formList.getTime_to()));
                    txtReason.setText(formList.getReason());

                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                }else if(formList.getForm_type_id().equals("5")){
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_restday);
                    dialog.setTitle("Custom Dialog");
                    LinearLayout linDialogConfirmAdd = dialog.findViewById(R.id.LinDialogOptionAdd);
                    LinearLayout linDialogConfirmChange = dialog.findViewById(R.id.LinDialogOptionChange);
                    LinearLayout linDialogConfirmRemove = dialog.findViewById(R.id.LinDialogOptionRemove);
                    TextView txtOption = dialog.findViewById(R.id.txtConfirmOption);
                    TextView txtReason = dialog.findViewById(R.id.txtConfirmRestdayReason);

                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }

                    txtReason.setText(formList.getReason());
                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                        if (formList.getRest_day_type_id().equals("1")){

                            linDialogConfirmAdd.setVisibility(View.VISIBLE);
                            linDialogConfirmChange.setVisibility(View.GONE);
                            linDialogConfirmRemove.setVisibility(View.GONE);
                            txtOption.setText("Add");
                            TextView txtAddNewRestDay = dialog.findViewById(R.id.txtConfirmAddNewRestDay);

                            txtAddNewRestDay.setText(parseDate(formList.getNew_date()));

                        }else if (formList.getRest_day_type_id().equals("2")){

                            linDialogConfirmChange.setVisibility(View.VISIBLE);
                            linDialogConfirmAdd.setVisibility(View.GONE);
                            linDialogConfirmRemove.setVisibility(View.GONE);
                            txtOption.setText("Change");
                            TextView txtChangeNewRestday = dialog.findViewById(R.id.txtConfirmChangeNewRestDay);
                            TextView txtChangeOldRestday = dialog.findViewById(R.id.txtConfirmChangeOldRestDay);
                            txtChangeOldRestday.setText(parseDate(formList.getOld_date()));
                            txtChangeNewRestday.setText(parseDate(formList.getNew_date()));

                        }else if (formList.getRest_day_type_id().equals("3")){

                            linDialogConfirmRemove.setVisibility(View.VISIBLE);
                            linDialogConfirmAdd.setVisibility(View.GONE);
                            linDialogConfirmChange.setVisibility(View.GONE);
                            txtOption.setText("Remove");
                            TextView txtRemoveOldRestDay = dialog.findViewById(R.id.txtConfirmRemoveOldRestDay);
                            txtRemoveOldRestDay.setText(parseDate(formList.getOld_date()));

                        }

                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                }else if(formList.getForm_type_id().equals("6")){

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_shiftcode);
                    dialog.setTitle("Custom Dialog");

                    TextView txtShiftType = dialog.findViewById(R.id.TxtShiftCodeType);
                    TextView txtDateFrom = dialog.findViewById(R.id.TxtShiftCodeDateFrom);
                    TextView txtDateTo = dialog.findViewById(R.id.TxtShiftCodeDateTo);
                    TextView txtSingleCompound = dialog.findViewById(R.id.TxtShiftCodeSingleCompound);
                    TextView txtReason = dialog.findViewById(R.id.TxtShiftCodeReason);
                    LinearLayout linSingle = dialog.findViewById(R.id.LinSingle);
                    LinearLayout linCompound = dialog.findViewById(R.id.LinCompound);

                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);

                    String ShiftTypeValue = "", SingleCompoundValue = "";
                    if (formList.getShift_type_id().equals("2")){
                        ShiftTypeValue = "Change";
                    }

                    if (formList.getIs_compound().equals("1")){
                        SingleCompoundValue = "Compound";
                    }else {
                        SingleCompoundValue = "Single";
                    }
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }

                    txtShiftType.setText(ShiftTypeValue);
                    txtDateFrom.setText(parseDate(formList.getDt_from()));
                    txtDateTo.setText(parseDate(formList.getDt_to()));
                    txtSingleCompound.setText(SingleCompoundValue);
                    txtReason.setText(formList.getReason());

                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                    if (formList.getIs_compound().equals("0")){
                        linCompound.setVisibility(View.GONE);
                        linSingle.setVisibility(View.VISIBLE);

                        TextView txtShiftCode = dialog.findViewById(R.id.TxtShiftCodeData);
                        txtShiftCode.setText(formList.getShift_code());

                    }else if (formList.getIs_compound().equals("1")){

                        TextView txtSunday = dialog.findViewById(R.id.TxtSundayData);
                        TextView txtMonday = dialog.findViewById(R.id.TxtMondayData);
                        TextView txtTuesday = dialog.findViewById(R.id.TxtTuesdayData);
                        TextView txtWednesday = dialog.findViewById(R.id.TxtWednesdayData);
                        TextView txtThursday = dialog.findViewById(R.id.TxtThursdayData);
                        TextView txtFriday = dialog.findViewById(R.id.TxtFridayData);
                        TextView txtSaturday = dialog.findViewById(R.id.TxtSaturdayData);

                        linCompound.setVisibility(View.VISIBLE);
                        linSingle.setVisibility(View.GONE);

                        String sunday = "",monday = "", tuesday = "", wednesday = "", thursday = "", friday = "", saturday = "";
                        String Test = formList.getShift_days().toString().replace("[","").replace("]","");
                        if(Test.contains(":{")){
                            try {

                                JSONObject json_obj= new JSONObject(Test);
                                if (json_obj.has("0")){
                                    JSONObject jsonObject0 =json_obj.getJSONObject("0");
                                    for (int i = 0; i < jsonObject0.length(); i++) {
                                        sunday = jsonObject0.getString("description");
                                    }
                                }
                                if (json_obj.has("1")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("1");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        monday = jsonObject1.getString("description");
                                    }
                                }

                                if (json_obj.has("2")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("2");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        tuesday = jsonObject1.getString("description");
                                    }
                                }

                                if (json_obj.has("3")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("3");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        wednesday = jsonObject1.getString("description");
                                    }
                                }

                                if (json_obj.has("4")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("4");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        thursday = jsonObject1.getString("description");
                                    }
                                }

                                if (json_obj.has("5")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("5");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        friday = jsonObject1.getString("description");
                                    }
                                }

                                if (json_obj.has("6")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("6");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        saturday = jsonObject1.getString("description");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else{



                            String sundayObject = "",mondayObject = "", tuesdayObject = "", wednesdayObject = "", thursdayObject = "", fridayObject = "", saturdayObject = "";
                            String[] values = Test.split(",(?![^{}])");
                            sundayObject = values[0];
                            try {
                                JSONObject jsonObject = new JSONObject(sundayObject);
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    sunday = jsonObject.getString("description");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(values.length >= 2)
                            {
                                mondayObject = values[1];
                                try {
                                    JSONObject jsonObject = new JSONObject(mondayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        monday = jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(values.length >= 3)
                            {
                                tuesdayObject = values[2];
                                try {
                                    JSONObject jsonObject = new JSONObject(tuesdayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        tuesday = jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(values.length >= 4)
                            {
                                wednesdayObject = values[3];
                                try {
                                    JSONObject jsonObject = new JSONObject(wednesdayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        wednesday = jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(values.length >= 5)
                            {
                                thursdayObject = values[4];
                                try {
                                    JSONObject jsonObject = new JSONObject(thursdayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        thursday = jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(values.length >= 6)
                            {
                                fridayObject = values[5];
                                try {
                                    JSONObject jsonObject = new JSONObject(fridayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        friday= jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(values.length >= 7)
                            {
                                saturdayObject = values[6];
                                try {
                                    JSONObject jsonObject = new JSONObject(saturdayObject);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        saturday = jsonObject.getString("description");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        txtSunday.setText(parseShiftCode(sunday));
                        txtMonday.setText(parseShiftCode(monday));
                        txtTuesday.setText(parseShiftCode(tuesday));
                        txtWednesday.setText(parseShiftCode(wednesday));
                        txtThursday.setText(parseShiftCode(thursday));
                        txtFriday.setText(parseShiftCode(friday));
                        txtSaturday.setText(parseShiftCode(saturday));



                    }


                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);





                }else if (formList.getForm_type_id().equals("9")){

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_form_toil);
                    dialog.setTitle("Custom Dialog");


                    TextView txtTOILDate = dialog.findViewById(R.id.TxtTOILDate);
                    TextView txtStartTime = dialog.findViewById(R.id.TxtTOILStartTime);
                    TextView txtEndTime = dialog.findViewById(R.id.TxtTOILEndTime);
                    TextView txtReason = dialog.findViewById(R.id.TxtTOILReason);

                    LinearLayout linLayoutSubmit = dialog.findViewById(R.id.LinLayoutSubmit);
                    LinearLayout linLayoutApproveDetails = dialog.findViewById(R.id.LinLayoutApproveDetails);
                    TextView txtDateSubmitted = dialog.findViewById(R.id.TxtDateSubmitted);
                    TextView txtStatus = dialog.findViewById(R.id.TxtStatus);
                    Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);
                    LinearLayout linDisapproval = dialog.findViewById(R.id.linDisapproval);
                    TextView txtDisapprovalReason = dialog.findViewById(R.id.TxtDisapprovalReason);
                    if (formList.getApproval_status().equals("Approved")){
                        linDisapproval.setVisibility(View.GONE);
                    }else {
                        linDisapproval.setVisibility(View.VISIBLE);
                        if (!formList.getReason_for_disapproving().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_disapproving());
                        }else if (!formList.getReason_for_cancellation().equals("null")){
                            txtDisapprovalReason.setText(formList.getReason_for_cancellation());
                        }else {
                            txtDisapprovalReason.setText("No data");
                        }
                    }

                    txtTOILDate.setText(parseDate(formList.getToil_date()));
                    txtStartTime.setText(parseTime(formList.getTime_from()));
                    txtEndTime.setText(parseTime(formList.getTime_to()));
                    txtReason.setText(formList.getReason());

                    txtDateSubmitted.setText(parseDateTime(formList.getDt_created()));
                    txtStatus.setText(formList.getApproval_status());
                    linLayoutApproveDetails.setVisibility(View.VISIBLE);
                    linLayoutSubmit.setVisibility(View.GONE);

                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                }else {
                    globalFunction.ShowErrorDialog("Sorry, this is not yet available");
                }


            }
        });

    }

    public String parseShiftCode(String string){
        if (string.equals("")){
            string = "Rest day";
            return string;
        }else {
            return string;
        }
    }

    public String parseTime(String timeToParse){
        if (timeToParse.equals("null")){
            timeToParse = "No Data";
            return timeToParse;
        }else {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
            String timeString = timeToParse;
            Date date = null;
            try {
                date = timeFormat.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
            timeToParse = timeFormat.format(date);
            return timeToParse;
        }

    }

    public String parseDate(String dateToParse){
        if (dateToParse.equals("null")){
            dateToParse = "No Data";
            return dateToParse;
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = dateToParse;
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            dateToParse = dateFormat.format(date);
            return dateToParse;
        }


    }


    public String parseDateTime(String datetimeToParse){
        if (datetimeToParse.equals("null")){
            datetimeToParse = "No Data";
            return datetimeToParse;
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String dateString = datetimeToParse;
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm", Locale.getDefault());
            datetimeToParse = dateFormat.format(date);
            return datetimeToParse;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFormName, txtDateSubmitted, txtStatus;
        RelativeLayout relFormItem;
        LinearLayout rootView;//newly added field
        public ViewHolder(View itemView) {
            super(itemView);

            txtFormName = itemView.findViewById(R.id.TxtFormTypeFormItem);
            txtDateSubmitted = itemView.findViewById(R.id.TxtDateSubmittedFormItem);
            txtStatus = itemView.findViewById(R.id.TxtStatusFormItem);
            relFormItem = itemView.findViewById(R.id.RelFormItem);


        }
    }
}
