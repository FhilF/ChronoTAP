package com.switso.itap;

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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ViewHolder> {

    public static final String TAG = "Adapter";
    Context mContext;
    private LayoutInflater mInflater;
    List<AttendanceLogList> list;


    public AttendanceListAdapter(Context ctx, List<AttendanceLogList> list) {
        this.mContext = ctx;
        this.list = list;
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AttendanceListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.attendanceitem, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceListAdapter.ViewHolder holder, int i) {
        AttendanceLogList attendanceList = list.get(i);
        SimpleDateFormat formatWorkDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date employeeWorkdate = null;
        try {
            employeeWorkdate = formatWorkDate.parse(attendanceList.getWorkdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatWorkDate = new SimpleDateFormat("MMM dd, yyyy EEE");
        holder.txtWorkdate.setText(formatWorkDate.format(employeeWorkdate));
        if (!attendanceList.getTime_in().equals("0000-00-00 00:00:00") || !attendanceList.getTime_out().equals("0000-00-00 00:00:00")){
            holder.txtTimeIn.setText(parseDateTimemillisec(attendanceList.getTime_in()));
            holder.txtTimeOut.setText(parseDateTimemillisec(attendanceList.getTime_out()));
        }else{
            holder.txtTimeIn.setText("");
            holder.txtTimeOut.setText("");
        }

        if (!attendanceList.getLeave_type().equals("null")){
            holder.txtLeave.setText(attendanceList.getLeave_type());
        }else {
            holder.txtLeave.setText("");
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !attendanceList.getDay_type().equals("restday")) {
                    if (!attendanceList.getTime_in().equals("0000-00-00 00:00:00") && !attendanceList.getTime_in().equals("null") && !attendanceList.getTime_out().equals("0000-00-00 00:00:00") && !attendanceList.getTime_out().equals("null")) {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_attendance_log);
                        dialog.setTitle("Custom Dialog");
                        TextView txtDayType = dialog.findViewById(R.id.TxtDayType);
                        TextView txtWorkdate = dialog.findViewById(R.id.TxtWorkdate);
                        TextView txtShiftCode = dialog.findViewById(R.id.TxtShiftCode);
                        TextView txtTimeIn = dialog.findViewById(R.id.TxtTimeIn);
                        TextView txtTimeOut = dialog.findViewById(R.id.TxtTimeOut);
                        TextView txtTardiness = dialog.findViewById(R.id.TxtTardiness);
                        TextView txtOvertime = dialog.findViewById(R.id.TxtOvertime);

                        LinearLayout linNonLeave = dialog.findViewById(R.id.LinNonLeave);
                        LinearLayout linLeave = dialog.findViewById(R.id.LinLeave);

                        txtDayType.setText(attendanceList.getDay_type());
                        txtWorkdate.setText(parseDate(attendanceList.getWorkdate()));
                        txtShiftCode.setText(attendanceList.getShift_code());

                        linNonLeave.setVisibility(View.VISIBLE);
                        linLeave.setVisibility(View.GONE);
                        txtTimeIn.setText(parseDateTime(attendanceList.getTime_in()));
                        txtTimeOut.setText(parseDateTime(attendanceList.getTime_out()));
                        txtTardiness.setText(attendanceList.getTardiness());
                        txtOvertime.setText(attendanceList.getOt());

                        Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);

                        btnDialogClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }else if (!attendanceList.getLeave_type().equals("null")){
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_attendance_log);
                        dialog.setTitle("Custom Dialog");
                        TextView txtDayType = dialog.findViewById(R.id.TxtDayType);
                        TextView txtWorkdate = dialog.findViewById(R.id.TxtWorkdate);
                        TextView txtShiftCode = dialog.findViewById(R.id.TxtShiftCode);
                        TextView txtLeaveType = dialog.findViewById(R.id.TxtLeaveType);

                        LinearLayout linNonLeave = dialog.findViewById(R.id.LinNonLeave);
                        LinearLayout linLeave = dialog.findViewById(R.id.LinLeave);

                        txtDayType.setText(attendanceList.getDay_type());
                        txtWorkdate.setText(parseDate(attendanceList.getWorkdate()));
                        txtShiftCode.setText(attendanceList.getShift_code());

                        linNonLeave.setVisibility(View.GONE);
                        linLeave.setVisibility(View.VISIBLE);
                        txtLeaveType.setText(attendanceList.getLeave_type());

                        Button btnDialogClose = dialog.findViewById(R.id.BtnDialogClose);

                        btnDialogClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                    }
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWorkdate,txtTimeIn,txtTimeOut, txtLeave;
        LinearLayout rootView;//newly added field
        public ViewHolder(View itemView) {
            super(itemView);

            txtLeave = itemView.findViewById(R.id.TxtLeave);
            txtWorkdate = (TextView) itemView.findViewById(R.id.TxtWorkdate);
            txtTimeIn = (TextView) itemView.findViewById(R.id.TxtTimeIn);
            txtTimeOut= (TextView) itemView.findViewById(R.id.TxtTimeOut);
            rootView=(LinearLayout) itemView.findViewById(R.id.attendanceitem);


        }
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

    public String parseDateTimemillisec(String datetimeToParse){
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
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
            datetimeToParse = dateFormat.format(date);
            return datetimeToParse;
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


