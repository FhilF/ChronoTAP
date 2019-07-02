package com.switso.itap.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


public class FormLeaveOfAbsenceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormLeaveOfAbsenceFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    TextView txtLeaveType, txtDateFrom, txtDateTo , txtHalfDay, txtReason;
    EditText etLeaveType,etLeaveFrom, etLeaveTo, etReason;
    Button btnConfirmLeaveType, btnSubmitLeave ;
    RelativeLayout dialogSubmit, dialogCancel;
    RadioGroup leave, halfday;
    RadioButton leaveItem, halfdayitem;
    Calendar calendarLeaveFrom,calendarLeaveTo;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;
    String fType, fDateFrom, fDateTo, fHalfDay, fReason, employee_fname, employee_email, employee_image,employee_deparment,employee_role,LeaveTypeResult;
    Integer employee_id;
    ProgressDialog pd;
    private List<LeaveTypeList> formLeaveTypeListData = new ArrayList<>();
    List<String> arrayAdapterItem  = new ArrayList<String>();
    GlobalFunction globalFunction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_form_leave_of_absence, container, false);
        pd = new ProgressDialog(getActivity());
        globalFunction = new GlobalFunction(getActivity());
        etLeaveType= view.findViewById(R.id.EtLeaveType);
        btnSubmitLeave = view.findViewById(R.id.BtnSubmit);
        etReason = view.findViewById(R.id.EtReason);
        etLeaveFrom = view.findViewById(R.id.EtLeaveDateFrom);
        etLeaveTo = view.findViewById(R.id.EtLeaveDateTo);
        halfday = view.findViewById(R.id.RadioDayType);
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
        GetLeaveTypes requestGetLeaveTypes = new GetLeaveTypes(Api.URL_GET_LEAVE_TYPES, params, CODE_GET_REQUEST);
        requestGetLeaveTypes.execute();


        etLeaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_leavetype);
                dialog.setTitle("Custom Dialog");

                ListView mylistview = dialog.findViewById(R.id.LvLeaveType);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                        arrayAdapterItem );
                mylistview.setAdapter(arrayAdapter);
                mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mylistview.setItemsCanFocus(false);
                LeaveTypeResult = "";
                mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LeaveTypeResult = (String) mylistview.getItemAtPosition(position);
                    }
                });

                Button btnConfirm= dialog.findViewById(R.id.BtnConfirmLeaveType);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (LeaveTypeResult.isEmpty()){
                            etLeaveType.setText("");
                            dialog.dismiss();

                        }else {
                            etLeaveType.setText(LeaveTypeResult);
                            dialog.dismiss();
                        }
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        calendarLeaveFrom = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendarLeaveFrom.set(Calendar.YEAR, year);
                calendarLeaveFrom.set(Calendar.MONTH, monthOfYear);
                calendarLeaveFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFrom();
            }

        };

        etLeaveFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateFrom, calendarLeaveFrom
                        .get(Calendar.YEAR), calendarLeaveFrom.get(Calendar.MONTH),
                        calendarLeaveFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        calendarLeaveTo = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendarLeaveTo.set(Calendar.YEAR, year);
                calendarLeaveTo.set(Calendar.MONTH, monthOfYear);
                calendarLeaveTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTo();
            }

        };

        etLeaveTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateTo, calendarLeaveTo
                        .get(Calendar.YEAR), calendarLeaveTo.get(Calendar.MONTH),
                        calendarLeaveTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        btnSubmitLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = halfday.getCheckedRadioButtonId();
                halfdayitem = getActivity().findViewById(selectedId);
                fHalfDay = String.valueOf(halfdayitem.getText());
                fType = etLeaveType.getText().toString();
                fDateFrom = etLeaveFrom.getText().toString();
                fDateTo = etLeaveTo.getText().toString();
                fReason = etReason.getText().toString();


                if (fType.isEmpty() || fDateFrom.isEmpty() || fDateTo.isEmpty() || fReason.isEmpty() || etReason.getText().toString().trim().length() == 0){
                    globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                }else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_form_leave);
                    dialog.setTitle("Custom Dialog");


                    txtLeaveType = dialog.findViewById(R.id.TxtLeaveType);
                    txtLeaveType.setText(fType);
                    txtDateFrom = dialog.findViewById(R.id.TxtLeaveDateFrom);
                    txtDateFrom.setText(fDateFrom);
                    txtDateTo = dialog.findViewById(R.id.TxtLeaveDateTo);
                    txtDateTo.setText(fDateTo);
                    txtHalfDay = dialog.findViewById(R.id.TxtLeaveHalfDay);
                    txtHalfDay.setText(fHalfDay);
                    txtReason = dialog.findViewById(R.id.TxtLeaveReason);
                    txtReason.setText(fReason);

                    dialogCancel = dialog.findViewById(R.id.DialogCancel);
                    dialogSubmit = dialog.findViewById(R.id.DialogSubmit);


                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date DateFrom = null;
                            Date DateTo = null;
                            try {
                                DateFrom = myformat.parse(fDateFrom);
                                DateTo = myformat.parse(fDateTo);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String fLeaveTypeValue = "0";
                            for (int i = 0; i < formLeaveTypeListData.size(); i++) {
                                if (formLeaveTypeListData.get(i).getDescription().equals(fType)){
                                    fLeaveTypeValue = formLeaveTypeListData.get(i).getLeave_type_id();
                                }
                            }

                            String fHalfDayValue;
                            if (fHalfDay.equals("Yes")){
                                fHalfDayValue = "1";
                            }else {
                                fHalfDayValue = "2";
                            }


                            HashMap<String, String> params = new HashMap<>();
                            params.put("employee_id", employee_Id);
                            params.put("user_id", employee_User_Id);
                            params.put("form_type_id", "2");
                            params.put("leave_type_id",fLeaveTypeValue);
                            params.put("is_halfday",fHalfDayValue);
                            params.put("dt_from_leave", String.valueOf(DateFormat.format(DateFrom)));
                            params.put("dt_to_leave", String.valueOf(DateFormat.format(DateTo)));
                            params.put("reason", fReason);

                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_LEAVE_OF_ABSENCE, params, CODE_POST_REQUEST);
                            dialog.cancel();
                            request.execute();
                        }
                    });

                    dialogCancel.setOnClickListener(new View.OnClickListener() {
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







    private void updateLabelFrom() {
        String myFormat = "MMMM dd, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etLeaveFrom.setText(sdf.format(calendarLeaveFrom.getTime()));
    }


    private void updateLabelTo() {
        String myFormat = "MMMM dd, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etLeaveTo.setText(sdf.format(calendarLeaveTo.getTime()));

    }



    private class GetLeaveTypes extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetLeaveTypes(String url, HashMap<String, String> params, int requestCode) {
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
                formLeaveTypeListData.clear();
                arrayAdapterItem.clear();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);
                        LeaveTypeList leaveTypeList = new LeaveTypeList();

                        leaveTypeList.setLeave_type_id(json_obj.getString("leave_type_id"));
                        leaveTypeList.setLeave_type_code(json_obj.getString("leave_type_code"));
                        leaveTypeList.setDescription(json_obj.getString("description"));
                        leaveTypeList.setIs_leave_advance_notice(json_obj.getString("is_leave_advance_notice"));
                        leaveTypeList.setLeave_advance_notice(json_obj.getString("leave_advance_notice"));
                        leaveTypeList.setIs_expire(json_obj.getString("is_expire"));
                        leaveTypeList.setIs_active(json_obj.getString("is_active"));
                        leaveTypeList.setIs_deleted(json_obj.getString("is_deleted"));
                        leaveTypeList.setIs_default(json_obj.getString("is_default"));
                        leaveTypeList.setStatus(json_obj.getString("status"));

                        formLeaveTypeListData.add(leaveTypeList);
                        arrayAdapterItem.add(json_obj.getString("description"));
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