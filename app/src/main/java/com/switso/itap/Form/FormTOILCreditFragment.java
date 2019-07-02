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


public class FormTOILCreditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormTOILCreditFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressDialog pd;
    Button btnSubmitTOIL;
    EditText etTOILDate, etStartTime, etEndTime,etReason;
    TextView txtTOILDate, txtStartTime, txtEndTime, txtReason;
    String fTOILDate , fStartTime, fEndTime, fReason;
    Calendar calendarTOILDate;
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
        View view = inflater.inflate(R.layout.fragment_form_toilcredit, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        btnSubmitTOIL = view.findViewById(R.id.BtnSubmitTOIL);
        etTOILDate = view.findViewById(R.id.EtTOILDate);
        etStartTime = view.findViewById(R.id.EtTOILStartTime);
        etEndTime = view.findViewById(R.id.EtTOILEndTime);
        etReason = view.findViewById(R.id.EtTOILReason);
        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }


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

        calendarTOILDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener TOILDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarTOILDate.set(Calendar.YEAR, year);
                calendarTOILDate.set(Calendar.MONTH, monthOfYear);
                calendarTOILDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etTOILDate.setText(sdf.format(calendarTOILDate.getTime()));
            }

        };
        etTOILDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), TOILDate, calendarTOILDate
                        .get(Calendar.YEAR), calendarTOILDate.get(Calendar.MONTH),
                        calendarTOILDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmitTOIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTOILDate = String.valueOf(etTOILDate.getText());
                fStartTime = String.valueOf(etStartTime.getText());
                fEndTime = String.valueOf(etEndTime.getText());
                fReason = String.valueOf(etReason.getText());
                if (fTOILDate.isEmpty() || fStartTime.isEmpty()|| fEndTime.isEmpty()|| fReason.isEmpty()){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            R.string.pleasefillup, Snackbar.LENGTH_LONG);
                    View snackBarView = snackBar.getView();snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                    textView.setTextSize(18);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                    snackBar.show();
                }else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_form_toil);
                    dialog.setTitle("Custom Dialog");

                    txtTOILDate = dialog.findViewById(R.id.TxtTOILDate) ;
                    txtStartTime = dialog.findViewById(R.id.TxtTOILStartTime);
                    txtEndTime = dialog.findViewById(R.id.TxtTOILEndTime);
                    txtReason = dialog.findViewById(R.id.TxtTOILReason);
                    dialogSubmit= dialog.findViewById(R.id.DialogSubmit);
                    dialogClose = dialog.findViewById(R.id.DialogCancel);

                    txtTOILDate.setText(fTOILDate);
                    txtStartTime.setText(fStartTime);
                    txtEndTime.setText(fEndTime);
                    txtReason.setText(fReason);
                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            Date DateTOIL = null;
                            try {
                                DateTOIL = myformat.parse(fTOILDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            HashMap<String, String> params = new HashMap<>();
                            params.put("employee_id", employee_Id);
                            params.put("user_id", employee_User_Id);
                            params.put("form_type_id", "9");
                            params.put("toil_date", DateFormat.format(DateTOIL));
                            params.put("time_from" , fStartTime);
                            params.put("time_to" , fEndTime);
                            params.put("reason", fReason);
                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_TOIL, params, CODE_POST_REQUEST);
                            dialog.dismiss();
                            request.execute();
                        }
                    });

                    dialogClose.setOnClickListener(new View.OnClickListener() {
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
