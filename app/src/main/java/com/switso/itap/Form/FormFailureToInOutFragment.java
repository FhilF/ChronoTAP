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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FormFailureToInOutFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormFailureToInOutFragment() {
    }


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    Button btnSubmitFailureToInOut, btnConfirmTimeType;
    EditText etWorkDate, etDateFailure, etTime, etType, etReason;
    Calendar calendarWorkDate, calendarDateFailure;
    RadioGroup rgTimeType;
    RadioButton rbTimeTypeItem;
    String fWorkDate, fDateFailure, fTime, fType, fReason, TimeTypeResult;
    TextView txtWorkDate, txtDateFailure, txtTime, txtType, txtReason;
    RelativeLayout dialogSubmit, dialogClose;
    ProgressDialog pd;
    private List<FormShiftTypeList> formShiftTypeListsData = new ArrayList<>();
    List<String> arrayAdapterItem  = new ArrayList<String>();
    GlobalFunction globalFunction;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_failure_to_in_out, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        btnSubmitFailureToInOut = view.findViewById(R.id.BtnSubmitFailure);
        etWorkDate = view.findViewById(R.id.EtFailureWorkDate);
        etDateFailure = view.findViewById(R.id.EtDateOfFailure);
        etTime = view.findViewById(R.id.EtTimeFailure);
        etType = view.findViewById(R.id.EtFailureTimeType);
        etReason = view.findViewById(R.id.EtFailureReason);

        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }



        HashMap<String, String> params = new HashMap<>();
        GetShiftTimeTypes requestGetShiftTimeTypes = new GetShiftTimeTypes(Api.URL_GET_FORM_SHIFT_TIME_TYPES, params, CODE_GET_REQUEST);
        requestGetShiftTimeTypes.execute();

        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_failuretimetype);
                dialog.setTitle("Custom Dialog");

                ListView mylistview = dialog.findViewById(R.id.LvTimeType);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                        arrayAdapterItem );
                mylistview.setAdapter(arrayAdapter);
                mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mylistview.setItemsCanFocus(false);
                TimeTypeResult = "";
                mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TimeTypeResult = (String) mylistview.getItemAtPosition(position);
                    }
                });

                Button btnConfirm= dialog.findViewById(R.id.BtnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TimeTypeResult.isEmpty()){
                            etType.setText("");
                            dialog.dismiss();

                        }else {
                            etType.setText(TimeTypeResult);
                            dialog.dismiss();
                        }
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
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
                        etTime.setText( convertedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        calendarDateFailure = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateFailure = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarDateFailure.set(Calendar.YEAR, year);
                calendarDateFailure.set(Calendar.MONTH, monthOfYear);
                calendarDateFailure.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etDateFailure.setText(sdf.format(calendarDateFailure.getTime()));
            }

        };
        etDateFailure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateFailure, calendarDateFailure
                        .get(Calendar.YEAR), calendarDateFailure.get(Calendar.MONTH),
                        calendarDateFailure.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        calendarWorkDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener workDate = new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(getActivity(), workDate, calendarWorkDate
                        .get(Calendar.YEAR), calendarWorkDate.get(Calendar.MONTH),
                        calendarWorkDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnSubmitFailureToInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fWorkDate = String.valueOf(etWorkDate.getText());
                fDateFailure = String.valueOf(etDateFailure.getText());
                fTime = String.valueOf(etTime.getText());
                fType = String.valueOf(etType.getText());
                fReason = String.valueOf(etReason.getText());
                if (fWorkDate.isEmpty() || fDateFailure.isEmpty() || fTime.isEmpty() || fType.isEmpty() || fReason.trim().length() == 0){
                    globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));

                }else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_form_failureinout);
                    dialog.setTitle("Custom Dialog");

                    txtWorkDate = dialog.findViewById(R.id.TxtFailureWorkDate);
                    txtDateFailure = dialog.findViewById(R.id.TxtFailureDateOfFailure);
                    txtTime = dialog.findViewById(R.id.TxtFailureTime);
                    txtType = dialog.findViewById(R.id.TxtFailureTimeType);
                    txtReason = dialog.findViewById(R.id.TxtFailureReason);
                    dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                    dialogClose = dialog.findViewById(R.id.DialogCancel);

                    txtDateFailure.setText(fDateFailure);
                    txtWorkDate.setText(fWorkDate);
                    txtTime.setText(fTime);
                    txtType.setText(fType);
                    txtReason.setText(fReason);

                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date DateWork = null;
                            Date DateFailure = null;
                            try {
                                DateWork = myformat.parse(fWorkDate);
                                DateFailure = myformat.parse(fDateFailure);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String ftimetypevalue = "0";
                            for (int i = 0; i < formShiftTypeListsData.size(); i++) {
                                if (formShiftTypeListsData.get(i).getShift_time_type().equals(fType)){
                                    ftimetypevalue = formShiftTypeListsData.get(i).getShift_time_type_id();
                                }

                            }

                            HashMap<String, String> params = new HashMap<>();
                            params.put("employee_id", employee_Id);
                            params.put("user_id", employee_User_Id);
                            params.put("form_type_id", "1");
                            params.put("dt_workdate", String.valueOf(DateFormat.format(DateWork)));
                            params.put("dt_failure_timeinout", String.valueOf(DateFormat.format(DateFailure)));
                            params.put("tm_time", fTime);
                            params.put("shift_time_type_id", ftimetypevalue);
                            params.put("reason", fReason);
                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_FAILURE_TO_IN_OUT, params, CODE_POST_REQUEST);
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

    private class GetShiftTimeTypes extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetShiftTimeTypes(String url, HashMap<String, String> params, int requestCode) {
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
                formShiftTypeListsData.clear();
                arrayAdapterItem.clear();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);
                        FormShiftTypeList formShiftTypeList = new FormShiftTypeList();
                        formShiftTypeList.setShift_time_type_id(json_obj.getString("shift_time_type_id"));
                        formShiftTypeList.setShift_time_type(json_obj.getString("shift_time_type"));
                        formShiftTypeList.setField_name(json_obj.getString("field_name"));
                        formShiftTypeListsData.add(formShiftTypeList);
                        arrayAdapterItem.add(json_obj.getString("shift_time_type"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                    Bundle bundle = new Bundle();
                    SetFragment(new FilingFormFragment(),"FilingForm", bundle);
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
            String result = "" , message = "";
            if (s != null){
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
