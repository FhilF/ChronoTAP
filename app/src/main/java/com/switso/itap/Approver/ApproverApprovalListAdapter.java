package com.switso.itap.Approver;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.switso.itap.AttendanceLogList;
import com.switso.itap.Form.FormList;
import com.switso.itap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApproverApprovalListAdapter extends RecyclerView.Adapter<ApproverApprovalListAdapter.ViewHolder> {
    public static final String TAG = "Adapter";
    Context mContext;
    private LayoutInflater mInflater;
    List<FormList> list;



    public ApproverApprovalListAdapter(Context ctx, List<FormList> list) {
        this.mContext = ctx;
        this.list = list;
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_approval_form_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        FormList formList = list.get(i);

        holder.txtDate.setText(formList.getDt_created());
        holder.txtStatus.setText(formList.getApproval_status());
        holder.txtFormName.setText(formList.getForm_name());
        holder.txtName.setText(formList.getEmployee_name());

        holder.relFormItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formList.getForm_type_id().equals("1")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName", formList.getEmployee_name());
                    bundle.putString("WorkDate", parseDate(formList.getDt_workdate()));
                    bundle.putString("DateFailure", parseDate(formList.getDt_failure_timeinout()));
                    bundle.putString("Time", parseTime(formList.getTm_time()));
                    bundle.putString("TimeType", formList.getShift_time_type());
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    bundle.putString("Status",formList.getApproval_status());
                    SetFragment(new ApproverFailureTimeInOutFragment(), "Approval Form Failure to Time in/out", bundle);
                }else if (formList.getForm_type_id().equals("2")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName", formList.getEmployee_name());
                    bundle.putString("Type", formList.getLeave_type_code());
                    bundle.putString("DateFrom", parseDate(formList.getDt_from()));
                    bundle.putString("DateTo", parseDate(formList.getDt_to()));
                    bundle.putString("HalfDay", formList.getIs_halfday());
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    bundle.putString("Status",formList.getApproval_status());
                    SetFragment(new ApproverLeaveOfAbsenceFragment(), "Approval Form Leave of Absence", bundle);

                }else if (formList.getForm_type_id().equals("3")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName",formList.getEmployee_name());
                    bundle.putString("StartDate",parseDate(formList.getDt_from()));
                    bundle.putString("EndDate",parseDate(formList.getDt_to()));
                    bundle.putString("StartTime",parseTime(formList.getTime_from()));
                    bundle.putString("EndTime",parseTime(formList.getTime_to()));
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("Status",formList.getApproval_status());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    SetFragment(new ApproverOffsiteFragment(),"Approval Form OBT", bundle);

                }else if (formList.getForm_type_id().equals("4")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName",formList.getEmployee_name());
                    bundle.putString("WorkDate",parseDate(formList.getDt_workdate()));
                    bundle.putString("StartDate",parseDate(formList.getDt_from()));
                    bundle.putString("EndDate",parseDate(formList.getDt_to()));
                    bundle.putString("StartTime",parseTime(formList.getTime_from()));
                    bundle.putString("EndTime",parseTime(formList.getTime_to()));
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("Status",formList.getApproval_status());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    SetFragment(new ApproverOvertimeFragment(),"Approval Form Overtime", bundle);

                }else if (formList.getForm_type_id().equals("5")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName",formList.getEmployee_name());
                    bundle.putString("Option", formList.getRest_day_type_id());
                    bundle.putString("OldDate",parseDate(formList.getOld_date()));
                    bundle.putString("NewDate",parseDate(formList.getNew_date()));
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("Status",formList.getApproval_status());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    SetFragment(new ApproverRestDayFragment(),"Approval Form RestDay", bundle);


                }else if (formList.getForm_type_id().equals("6")){

                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName", formList.getEmployee_name());
                    bundle.putString("ShiftType", formList.getShift_type_id());
                    bundle.putString("DateFrom", parseDate(formList.getDt_from()));
                    bundle.putString("DateTo", parseDate(formList.getDt_to()));
                    bundle.putString("SingleCompound", formList.getIs_compound());
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    bundle.putString("Status",formList.getApproval_status());

                    if (formList.getIs_compound().equals("0")){
                        bundle.putString("ShiftCode",formList.getShift_days().toString().substring(1, formList.getShift_days().toString().length()-1));
                    }else if (formList.getIs_compound().equals("1")){
                        String sunday = "",monday = "", tuesday = "", wednesday = "", thursday = "", friday = "", saturday = "";
                        String Test = formList.getShift_days().toString().replace("[","").replace("]","");
                        if(Test.contains(":{")){
                            try {

                                JSONObject json_obj= new JSONObject(Test);
                                if (json_obj.has("0")){
                                    JSONObject jsonObject0 =json_obj.getJSONObject("0");
                                    for (int i = 0; i < jsonObject0.length(); i++) {
                                        sunday = jsonObject0.getString("shift_code");
                                    }
                                }
                                if (json_obj.has("1")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("1");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        monday = jsonObject1.getString("shift_code");
                                    }
                                }

                                if (json_obj.has("2")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("2");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        tuesday = jsonObject1.getString("shift_code");
                                    }
                                }

                                if (json_obj.has("3")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("3");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        wednesday = jsonObject1.getString("shift_code");
                                    }
                                }

                                if (json_obj.has("4")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("4");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        thursday = jsonObject1.getString("shift_code");
                                    }
                                }

                                if (json_obj.has("5")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("5");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        friday = jsonObject1.getString("shift_code");
                                    }
                                }

                                if (json_obj.has("6")){
                                    JSONObject jsonObject1 =json_obj.getJSONObject("6");
                                    for (int i = 0; i < jsonObject1.length(); i++) {
                                        saturday = jsonObject1.getString("shift_code");
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
                                    sunday = jsonObject.getString("shift_code");
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
                                        monday = jsonObject.getString("shift_code");
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
                                        tuesday = jsonObject.getString("shift_code");
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
                                        wednesday = jsonObject.getString("shift_code");
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
                                        thursday = jsonObject.getString("shift_code");
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
                                        friday= jsonObject.getString("shift_code");
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
                                        saturday = jsonObject.getString("shift_code");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        bundle.putString("Sunday",sunday);
                        bundle.putString("Monday",monday);
                        bundle.putString("Tuesday",tuesday);
                        bundle.putString("Wednesday",wednesday);
                        bundle.putString("Thursday",thursday);
                        bundle.putString("Friday",friday);
                        bundle.putString("Saturday",friday);

                    }

                    SetFragment(new ApproverShiftCodeFragment(), "Approval Form Shift Code", bundle);

                }else if (formList.getForm_type_id().equals("9")){
                    final Bundle bundle = new Bundle();
                    bundle.putString("FormId",formList.getForm_id());
                    bundle.putString("EmployeeName",formList.getEmployee_name());
                    bundle.putString("TOILDate",formList.getToil_date());
                    bundle.putString("StartTime",parseTime(formList.getTime_from()));
                    bundle.putString("EndTime",parseTime(formList.getTime_to()));
                    bundle.putString("Reason",formList.getReason());
                    bundle.putString("Status",formList.getApproval_status());
                    bundle.putString("DateSubmitted",parseDateTime(formList.getDt_created()));
                    SetFragment(new ApproverTOILCreditFormFragment(),"Raw Logs", bundle);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtStatus, txtName,txtFormName;

        RelativeLayout relFormItem;
        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.TxtDate);
            txtStatus = itemView.findViewById(R.id.TxtStatus);
            txtName = itemView.findViewById(R.id.TxtName);
            txtFormName = itemView.findViewById(R.id.TxtFormType);
            relFormItem = itemView.findViewById(R.id.RelFormItem);

        }
    }

    public void SetFragment(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
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
    
}
