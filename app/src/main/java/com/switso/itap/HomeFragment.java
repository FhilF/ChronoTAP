package com.switso.itap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.switso.itap.Form.FormList;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private FragmentActivity myContext;
    String employee_fname, employee_email, employee_image,employee_deparment,employee_role,employee_id,employee_user_id, MonthCalendar, YearCalendar;
    int timesheet_id;
    Button timein,timeout;
    TextView txtprofilefullname,txtprofiledepartment, txtprofilerole, txtTimeIn, txtMonthYear;
    ImageView profileimage;
    Dialog dialogtimeout;
    private List<AttendanceLogList> attendanceData = new ArrayList<>();
    com.applandeo.materialcalendarview.CalendarView calendarView;
    RelativeLayout btnRelViewCalendar, btnRelViewTable, relCalendar, relTable, btnRelMonthYear,reltimein,reltimeout,relNoData;
    NestedScrollView svHome;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog pd;
    GlobalFunction globalFunction;


    private List<EventDay> events = new ArrayList<>();

    private ArrayList<Date> dates;
    private Date date = new Date();
    Calendar cal;
    DateTime dateToday;
    DateTime dateEnd;
    AttendanceLogList attendanceList;


    private AttendanceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView attendanceRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        svHome = (NestedScrollView) view.findViewById(R.id.svHome);
        svHome.setFocusableInTouchMode(true);
        svHome.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,R.color.colorPrimaryDark);
        txtprofilefullname = (TextView) view.findViewById(R.id.txtProfileFullName);
        txtprofiledepartment = (TextView) view.findViewById(R.id.txtProfileDepartment);
        txtprofilerole = view.findViewById(R.id.txtProfileRole);
        profileimage = (ImageView) view.findViewById(R.id.imageProfileImage);
        timein = (Button) view.findViewById(R.id.btnTimeIn);
        timeout = (Button) view.findViewById(R.id.btnTimeOut);
        reltimein = (RelativeLayout) view.findViewById(R.id.relTimeIn);
        reltimeout = (RelativeLayout) view.findViewById(R.id.relTimeOut);
        txtTimeIn = (TextView) view.findViewById(R.id.txtTimeIn);
        txtMonthYear = (TextView) view.findViewById(R.id.txtMonthYear);
        relNoData = (RelativeLayout) view.findViewById(R.id.relNoData);
        btnRelViewCalendar = (RelativeLayout) view.findViewById(R.id.btnRelCalendarView);
        btnRelViewTable = (RelativeLayout) view.findViewById(R.id.btnRelTableView);
        relCalendar = (RelativeLayout)view.findViewById(R.id.relCalendarTimeSheet);
        relTable = (RelativeLayout)view.findViewById(R.id.relTableTimeSheet);
        btnRelMonthYear = (RelativeLayout)view.findViewById(R.id.btnRelMonthYear);
        attendanceRecyclerView = (RecyclerView) view.findViewById(R.id.attendance_list) ;
        attendanceRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        attendanceRecyclerView.setLayoutManager(layoutManager);
        attendanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AttendanceListAdapter(getActivity(), attendanceData);
        attendanceRecyclerView.setAdapter(adapter);

        JodaTimeAndroid.init(getActivity());

        if (getActivity().getIntent().getExtras() != null) {
            employee_id = getActivity().getIntent().getStringExtra("employee_id");
            employee_user_id = getActivity().getIntent().getStringExtra("user_id");
            employee_fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_email = getActivity().getIntent().getStringExtra("employee_email");
            employee_image = getActivity().getIntent().getStringExtra("employee_image");
            employee_deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_role = getActivity().getIntent().getStringExtra("employee_role");
        }
        dateToday = new DateTime();




        final Calendar today = Calendar.getInstance();
        view.findViewById(R.id.btnRelMonthYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarset = Calendar.getInstance();
                calendarset.set(Integer.parseInt(YearCalendar), (Integer.parseInt(MonthCalendar)-1),1);
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getActivity(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {

                        YearCalendar = String.valueOf(selectedYear);
                        MonthCalendar = String.valueOf(String.valueOf(selectedMonth + 1));

                        HashMap<String, String> params = new HashMap<>();
                        GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
                        request.execute();

                        SimpleDateFormat format = new SimpleDateFormat("MM", Locale.getDefault());
                        Date monthSelected = null;
                        try {
                            monthSelected = format.parse(String.valueOf((selectedMonth+1)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat getMonthformat = new SimpleDateFormat("MMM");
                        txtMonthYear.setText(getMonthformat.format(monthSelected)+ ", "+ String.valueOf(selectedYear));

                    }
                }, calendarset.get(Calendar.YEAR), calendarset.get(Calendar.MONTH));

                builder
                        .setMinYear(1990)
                        .setTitle("Select a month to show")
                        .build()
                        .show();

            }
        });


        calendarView = (com.applandeo.materialcalendarview.CalendarView) view.findViewById(R.id.calendarView);
        MonthCalendar = new SimpleDateFormat("MM").format(calendarView.getCurrentPageDate().getTime());
        YearCalendar = new SimpleDateFormat("yyyy").format(calendarView.getCurrentPageDate().getTime());
        txtMonthYear.setPaintFlags(txtMonthYear.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        HashMap<String, String> params = new HashMap<>();

        GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
        request.execute();




        btnRelViewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relCalendar.setVisibility(View.GONE);
                relTable.setVisibility(View.VISIBLE);
                HashMap<String, String> params = new HashMap<>();
                GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ new SimpleDateFormat("yyyy").format(calendarView.getCurrentPageDate().getTime()) +"&month="+ new SimpleDateFormat("MM").format(calendarView.getCurrentPageDate().getTime()), params, CODE_GET_REQUEST);
                request.execute();
                txtMonthYear.setText(new SimpleDateFormat("MMM").format(calendarView.getCurrentPageDate().getTime())+ ", "+  new SimpleDateFormat("yyyy").format(calendarView.getCurrentPageDate().getTime()));

            }
        });

        btnRelViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relTable.setVisibility(View.GONE);
                relCalendar.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(YearCalendar), (Integer.parseInt(MonthCalendar)-1),1);

                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> params = new HashMap<>();
                GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
                request.execute();


            }
        });


        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                MonthCalendar = new SimpleDateFormat("MM").format(calendarView.getCurrentPageDate().getTime());
                YearCalendar = new SimpleDateFormat("yyyy").format(calendarView.getCurrentPageDate().getTime());
                HashMap<String, String> params = new HashMap<>();

                GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
                request.execute();

            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                MonthCalendar = new SimpleDateFormat("MM").format(calendarView.getCurrentPageDate().getTime());
                YearCalendar = new SimpleDateFormat("yyyy").format(calendarView.getCurrentPageDate().getTime());
                HashMap<String, String> params = new HashMap<>();

                GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
                request.execute();
            }
        });

        if(employee_fname != null){
            Glide.with(getActivity()).load(employee_image).error(R.drawable.ic_account_circle_120dp).placeholder(R.drawable.ic_account_circle_120dp).apply(RequestOptions.circleCropTransform()).into(profileimage);
            txtprofilefullname.setText(employee_fname);
            txtprofiledepartment.setText(employee_deparment);
            if (employee_role.equals("null")){
                txtprofilerole.setVisibility(View.GONE);
            }else {
                txtprofilerole.setText(employee_role);
            }
        }

        timein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                globalFunction.ShowErrorSnackbar(getActivity(),"Sorry, this is not yet available");


            }
        });

        timeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalFunction.ShowErrorSnackbar(getActivity(),"Sorry, this is not yet available");

            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        HashMap<String, String> params = new HashMap<>();

                        GetAttendanceLogs request = new GetAttendanceLogs(Api.URL_GET_ATTENDANCE_LOGS+"?employee_id="+ employee_id +"&year="+ YearCalendar +"&month="+ MonthCalendar, params, CODE_GET_REQUEST);
                        request.execute();
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000);
            }
        });




        return view;
    }





    //GetAttendanceLogs
    private class GetAttendanceLogs extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetAttendanceLogs(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage(getActivity().getString(R.string.pdLoading));
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            events.clear();
            attendanceData.clear();
            if (s != null){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);

                        if (relCalendar.getVisibility()  == View.VISIBLE) {
                            String workdate = json_obj.getString("workdate");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String dateString = workdate;
                            Date date = null;
                            try {
                                date = sdf.parse(dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (json_obj.getString("leaves").toString().equals("8.0000")) {
                                dateEnd = new DateTime(date);
                                int days = Days.daysBetween(new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay(),
                                        dateEnd.withTimeAtStartOfDay()).getDays();
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.add(Calendar.DAY_OF_MONTH, days);
                                events.add(new EventDay(calendar1, R.drawable.ic_close_blue));
                            } else if (json_obj.getString("leave_type").equals("null") && !json_obj.getString("day_type").equals("restday")) {
                                if (!json_obj.getString("time_in").equals("0000-00-00 00:00:00") && !json_obj.getString("time_in").equals("null") && !json_obj.getString("time_out").equals("0000-00-00 00:00:00") && !json_obj.getString("time_out").equals("null")) {
                                    dateEnd = new DateTime(date);
                                    int days = Days.daysBetween(new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay(),
                                            dateEnd.withTimeAtStartOfDay()).getDays();
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.add(Calendar.DAY_OF_MONTH, days);
                                    events.add(new EventDay(calendar1, R.drawable.ic_check));
                                }

                            } else if (!json_obj.getString("leave_type").equals("null")) {
                                dateEnd = new DateTime(date);
                                int days = Days.daysBetween(new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay(),
                                        dateEnd.withTimeAtStartOfDay()).getDays();
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.add(Calendar.DAY_OF_MONTH, days);
                                events.add(new EventDay(calendar1, R.drawable.ic_date_range_blue));
                            }

                        }

                        AttendanceLogList attendanceLogList = new AttendanceLogList();
                        attendanceLogList.setTimekeeping_inandout_id(json_obj.getString("timekeeping_inandout_id"));
                        attendanceLogList.setEmployee_id(json_obj.getString("employee_id"));
                        attendanceLogList.setShift_code_id(json_obj.getString("shift_code_id"));
                        attendanceLogList.setDay_type_id(json_obj.getString("day_type_id"));
                        attendanceLogList.setLeave_type_id(json_obj.getString("leave_type_id"));
                        attendanceLogList.setWorkdate(json_obj.getString("workdate"));
                        attendanceLogList.setTime_in(json_obj.getString("time_in"));
                        attendanceLogList.setTime_out(json_obj.getString("time_out"));
                        attendanceLogList.setBreak_1_in(json_obj.getString("break_1_in"));
                        attendanceLogList.setBreak_1_out(json_obj.getString("break_1_out"));
                        attendanceLogList.setBreak_2_in(json_obj.getString("break_2_in"));
                        attendanceLogList.setBreak_2_out(json_obj.getString("break_2_out"));
                        attendanceLogList.setBreak_3_in(json_obj.getString("break_3_in"));
                        attendanceLogList.setBreak_3_out(json_obj.getString("break_3_out"));
                        attendanceLogList.setBasic(json_obj.getString("basic"));
                        attendanceLogList.setOt(json_obj.getString("ot"));
                        attendanceLogList.setSot(json_obj.getString("sot"));
                        attendanceLogList.setNdif(json_obj.getString("ndif"));
                        attendanceLogList.setNdot(json_obj.getString("ndot"));
                        attendanceLogList.setNdsot(json_obj.getString("ndsot"));
                        attendanceLogList.setTardiness(json_obj.getString("tardiness"));
                        attendanceLogList.setUndertime(json_obj.getString("undertime"));
                        attendanceLogList.setBreak_late(json_obj.getString("break_late"));
                        attendanceLogList.setLeaves(json_obj.getString("leaves"));
                        attendanceLogList.setFiled_leaves(json_obj.getString("filed_leaves"));
                        attendanceLogList.setExcess_hours(json_obj.getString("excess_hours"));
                        attendanceLogList.setExcess_hours_filed(json_obj.getString("excess_hours_filed"));
                        attendanceLogList.setShift_code(json_obj.getString("shift_code"));
                        attendanceLogList.setLeave_type(json_obj.getString("leave_type"));
                        attendanceLogList.setDay_type(json_obj.getString("day_type"));
                        attendanceData.add(attendanceLogList);
                    }
                    if (relCalendar.getVisibility() == View.VISIBLE){
                        calendarView.setEvents(events);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        calendarView.setOnDayClickListener(new OnDayClickListener() {
                            @Override
                            public void onDayClick(EventDay eventDay) {

                                for (int i = 0; i < attendanceData.size(); i++) {

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    if (attendanceData.get(i).getWorkdate().equals(new SimpleDateFormat("yyyy-MM-dd").format(eventDay.getCalendar().getTime()))) {
                                        if ( !attendanceData.get(i).getDay_type().equals("restday")) {
                                            if (!attendanceData.get(i).getTime_in().equals("0000-00-00 00:00:00") && !attendanceData.get(i).getTime_in().equals("null") && !attendanceData.get(i).getTime_out().equals("0000-00-00 00:00:00") && !attendanceData.get(i).getTime_out().equals("null")) {
                                                final Dialog dialog = new Dialog(getActivity());
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

                                                txtDayType.setText(attendanceData.get(i).getDay_type());
                                                txtWorkdate.setText(parseDate(attendanceData.get(i).getWorkdate()));
                                                txtShiftCode.setText(attendanceData.get(i).getShift_code());

                                                linNonLeave.setVisibility(View.VISIBLE);
                                                linLeave.setVisibility(View.GONE);
                                                txtTimeIn.setText(parseDateTime(attendanceData.get(i).getTime_in()));
                                                txtTimeOut.setText(parseDateTime(attendanceData.get(i).getTime_out()));
                                                txtTardiness.setText(attendanceData.get(i).getTardiness());
                                                txtOvertime.setText(attendanceData.get(i).getOt());

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
                                            }else if (!attendanceData.get(i).getLeave_type().equals("null")){
                                                final Dialog dialog = new Dialog(getActivity());
                                                dialog.setContentView(R.layout.dialog_attendance_log);
                                                dialog.setTitle("Custom Dialog");
                                                TextView txtDayType = dialog.findViewById(R.id.TxtDayType);
                                                TextView txtWorkdate = dialog.findViewById(R.id.TxtWorkdate);
                                                TextView txtShiftCode = dialog.findViewById(R.id.TxtShiftCode);
                                                TextView txtLeaveType = dialog.findViewById(R.id.TxtLeaveType);

                                                LinearLayout linNonLeave = dialog.findViewById(R.id.LinNonLeave);
                                                LinearLayout linLeave = dialog.findViewById(R.id.LinLeave);

                                                txtDayType.setText(attendanceData.get(i).getDay_type());
                                                txtWorkdate.setText(parseDate(attendanceData.get(i).getWorkdate()));
                                                txtShiftCode.setText(attendanceData.get(i).getShift_code());

                                                linNonLeave.setVisibility(View.GONE);
                                                linLeave.setVisibility(View.VISIBLE);
                                                txtLeaveType.setText(attendanceData.get(i).getLeave_type());

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



                                }
                            }
                        });

                    }else if (relTable.getVisibility() == View.VISIBLE){
                        adapter.notifyDataSetChanged();
                        if(attendanceRecyclerView.getVisibility() == View.GONE){
                            attendanceRecyclerView.setVisibility(View.VISIBLE);
                            relNoData.setVisibility(View.GONE);
                        }
                        if (attendanceData.size() > 5){

                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                            int a =  (displaymetrics.heightPixels*60)/100;

                            attendanceRecyclerView.getLayoutParams().height =a;
                        }

                    }
                    globalFunction.setErrorCount(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                }
            }else {
                globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredCannotConnect));
            }

            pd.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {
            GetPostRequest requestHandler = new GetPostRequest();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    @Override
    public void onDestroy() {
        pd.dismiss();
        super.onDestroy();
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




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void SetFragment(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }
}
