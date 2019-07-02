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
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FormOffSiteAssignment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormOffSiteAssignment() {
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    GlobalFunction globalFunction;
    Button btnSubmitOffsiteAssignment;
    EditText etStartDate, etEndDate, etStartTime, etEndTime, etReason;
    TextView txtStartDate, txtEndDate, txtStartTime, txtEndTime, txtReason;
    String fStartDate, fEndDate, fStartTime, fEndTime, fReason;
    Calendar calendarStartDate, calendarEndDate;
    RelativeLayout dialogSubmit, dialogClose;
    ProgressDialog pd;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_off_site_assignment, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        btnSubmitOffsiteAssignment = view.findViewById(R.id.BtnSubmitOffsite);
        etStartDate = view.findViewById(R.id.EtOffSiteStartDate);
        etEndDate  = view.findViewById(R.id.EtOffSiteEndDate);
        etStartTime  = view.findViewById(R.id.EtOffSiteStartTime);
        etEndTime = view.findViewById(R.id.EtOffSiteEndTime);
        etReason = view.findViewById(R.id.EtOffsiteReason);


        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }

        calendarStartDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateDateStart = new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(getActivity(), dateDateStart, calendarStartDate
                        .get(Calendar.YEAR), calendarStartDate.get(Calendar.MONTH),
                        calendarStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarEndDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateDateEnd = new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(getActivity(), dateDateEnd, calendarEndDate
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



        btnSubmitOffsiteAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStartDate = String.valueOf(etStartDate.getText());
                fEndDate = String.valueOf(etEndDate.getText());
                fStartTime = String.valueOf(etStartTime.getText());
                fEndTime  = String.valueOf(etEndTime.getText());
                fReason = String.valueOf(etReason.getText());


                if (fStartDate.isEmpty() || fEndDate.isEmpty() || fStartTime.isEmpty() || fEndTime.isEmpty() || fReason.isEmpty()){
                    globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                }else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_form_offsiteassignment);
                    dialog.setTitle("Custom Dialog");
                    txtStartDate = dialog.findViewById(R.id.TxtOffSiteStartDate);
                    txtEndDate = dialog.findViewById(R.id.TxtOffSiteEndDate);
                    txtStartTime = dialog.findViewById(R.id.TxtOffSiteStartTime);
                    txtEndTime = dialog.findViewById(R.id.TxtOffSiteEndTime);
                    txtReason = dialog.findViewById(R.id.TxtOffSiteReason);
                    dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                    dialogClose = dialog.findViewById(R.id.DialogCancel);

                    dialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    txtStartDate.setText(fStartDate);
                    txtEndDate.setText(fEndDate);
                    txtStartTime.setText(fStartTime);
                    txtEndTime.setText(fEndTime);
                    txtReason.setText(fReason);



                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date DateStart = null;
                            Date DateEnd = null;
                            try {
                                DateStart = myformat.parse(fStartDate);
                                DateEnd = myformat.parse(fEndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            HashMap<String, String> params = new HashMap<>();
                            params.put("form_type_id", "3");
                            params.put("employee_id", employee_Id);
                            params.put("user_id", employee_User_Id);
                            params.put("dt_from", DateFormat.format(DateStart));
                            params.put("dt_to", DateFormat.format(DateEnd));
                            params.put("time_from", fStartTime);
                            params.put("time_to", fEndTime);
                            params.put("reason", fReason);
                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_OBT, params, CODE_POST_REQUEST);
                            dialog.dismiss();
                            request.execute();
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
                    globalFunction.setErrorCount(0);
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


    public void SetFragment(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }


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
}
