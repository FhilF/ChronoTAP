package com.switso.itap.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.switso.itap.Api;
import com.switso.itap.GetPostRequest;
import com.switso.itap.GlobalFunction;
import com.switso.itap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class FormOvertimeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public FormOvertimeFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressDialog pd;
    Button btnSubmitOvertime;
    EditText etWorkDate, etStartDate, etStartTime, etEndDate, etEndTime, etReason;
    String fWorkDate, fStartDate, fStartTime, fEndDate, fEndTime, fReason;
    TextView txtWorkDate, txtStartDate, txtStartTime, txtEndDate, txtEndTime, txtReason;
    Calendar calendarWorkDate,calendarStartDate, calendarEndDate;
    RelativeLayout dialogSubmit, dialogClose;
    GlobalFunction globalFunction;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_overtime, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        etWorkDate = view.findViewById(R.id.EtOvertimeDate);
        etStartDate = view.findViewById(R.id.EtOvertimeStartDate);
        etStartTime = view.findViewById(R.id.EtOvertimeStartTime);
        etEndDate = view.findViewById(R.id.EtOvertimeEndDate);
        etEndTime = view.findViewById(R.id.EtOvertimeEndTime);
        etReason = view.findViewById(R.id.EtOvertimeReason);
        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }


        calendarWorkDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateWorkDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarWorkDate.set(Calendar.YEAR, year);
                calendarWorkDate.set(Calendar.MONTH, monthOfYear);
                calendarWorkDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etWorkDate.setText(sdf.format(calendarWorkDate.getTime()));
            }

        };
        etWorkDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateWorkDate, calendarWorkDate
                        .get(Calendar.YEAR), calendarWorkDate.get(Calendar.MONTH),
                        calendarWorkDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        calendarStartDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateStartDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarStartDate.set(Calendar.YEAR, year);
                calendarStartDate.set(Calendar.MONTH, monthOfYear);
                calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etStartDate.setText(sdf.format(calendarStartDate.getTime()));
            }

        };
        etStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateStartDate, calendarStartDate
                        .get(Calendar.YEAR), calendarStartDate.get(Calendar.MONTH),
                        calendarStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarEndDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateEndDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarEndDate.set(Calendar.YEAR, year);
                calendarEndDate.set(Calendar.MONTH, monthOfYear);
                calendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etEndDate.setText(sdf.format(calendarEndDate.getTime()));

            }

        };
        etEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateEndDate, calendarEndDate
                        .get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH),
                        calendarEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour +":"+selectedMinute;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("H:m");
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                        Date Time;
                        String convertedTime = null;
                        try {
                            Time = dateFormat.parse(time);
                            convertedTime = dateFormat2.format(Time);
                        } catch (ParseException e) {
                        }
                        etStartTime.setText( convertedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour +":"+selectedMinute;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("H:m");
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                        Date Time;
                        String convertedTime = null;
                        try {
                            Time = dateFormat.parse(time);
                            convertedTime = dateFormat2.format(Time);
                        } catch (ParseException e) {
                        }
                        etEndTime.setText( convertedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnSubmitOvertime = view.findViewById(R.id.BtnSubmitOvertime);
        btnSubmitOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fWorkDate = String.valueOf(etWorkDate.getText());
                fStartDate = String.valueOf(etStartDate.getText());
                fStartTime = String.valueOf(etStartTime.getText());
                fEndDate = String.valueOf(etEndDate.getText());
                fEndTime = String.valueOf(etEndTime.getText());
                fReason = String.valueOf(etReason.getText());
                if (fWorkDate.isEmpty() || fStartDate.isEmpty() || fEndDate.isEmpty() || fReason.trim().length() == 0){
                    globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                }else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_form_overtime);
                    dialog.setTitle("Custom Dialog");
                    txtWorkDate = (TextView) dialog.findViewById(R.id.TxtOvertimeWorkDate);
                    txtStartDate = (TextView) dialog.findViewById(R.id.TxtOvertimeStartDate);
                    txtStartTime = (TextView) dialog.findViewById(R.id.TxtOvertimeStartTime);
                    txtEndDate = (TextView) dialog.findViewById(R.id.TxtOvertimeEndDate);
                    txtEndTime = (TextView) dialog.findViewById(R.id.TxtOvertimeEndTime);
                    txtReason = (TextView) dialog.findViewById(R.id.TxtOvertimeReason);
                    dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                    dialogClose = dialog.findViewById(R.id.DialogCancel);

                    txtWorkDate.setText(fWorkDate);
                    txtStartDate.setText(fStartDate);
                    txtStartTime.setText(fStartTime);
                    txtEndDate.setText(fEndDate);
                    txtEndTime.setText(fEndTime);
                    txtReason.setText(fReason);

                    dialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            Date DateFrom = null;
                            Date DateTo = null;
                            Date DateWork = null;
                            try {
                                DateFrom = myformat.parse(fStartDate);
                                DateTo = myformat.parse(fEndDate);
                                DateWork = myformat.parse(fWorkDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            HashMap<String, String> params = new HashMap<>();
                            params.put("employee_id", employee_Id);
                            params.put("user_id", employee_User_Id);
                            params.put("form_type_id", "4");
                            params.put("dt_workdate", DateFormat.format(DateWork));
                            params.put("dt_from" , DateFormat.format(DateFrom));
                            params.put("dt_to" , DateFormat.format(DateTo));
                            params.put("time_from" , fStartTime);
                            params.put("time_to" , fEndTime);
                            params.put("reason", fReason);
                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_OVERTIME, params, CODE_POST_REQUEST);
                            request.execute();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }


            }
        });


        return view;
    }



    private class SubmitForm extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        SubmitForm(String url, HashMap<String, String> params, int requestCode) {
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
            if (s != null){
                String result = "" , message = "";
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);
                        result = json_obj.getString("success");
                        message = json_obj.getString("msg");
                    }
                    if (result.equals("true")){
                        globalFunction.ShowSucessSnackbar(getActivity(),message);
                        Bundle bundle = new Bundle();
                        SetFragment(new FilingFormHomeFragment(),"Filing Form Home",bundle);
                    }else {
                        globalFunction.ShowErrorSnackbar(getActivity(),message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                }
            }else {
                globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredCannotConnect));
                Bundle bundle = new Bundle();
                SetFragment(new FilingFormFragment(),"FilingForm", bundle);
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
