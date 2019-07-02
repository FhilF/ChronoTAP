package com.switso.itap.Approver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.switso.itap.Api;
import com.switso.itap.GetPostRequest;
import com.switso.itap.GlobalFunction;
import com.switso.itap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ApproverShiftCodeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ApproverShiftCodeFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    GlobalFunction globalFunction;
    ProgressDialog pd;
    EditText etAction, etDissapprovedReason;
    LinearLayout LinDisapproved;
    Button btnSubmit;
    TextView txtShiftType, txtDateFrom, txtDateTo, txtSingleCompound, txtShiftCode, txtReason, txtFormStatus, txtDateSubmitted, txtSunday, txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday, txtEmployeeName;
    String fFormId, fShiftType, fDateFrom, fDateTo, fSingleCompound, fShiftCode, fReason, fFormStatus, fDateSubmitted, fSunday, fMonday, fTuesday, fWednesday, fThursday, fFriday, fSaturday, fEmployeeName;
    LinearLayout linSingle, linCompound;
    List<String> arrayAdapterItem  = new ArrayList<String>();
    String fApproverActionResult;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approver_shift_code, container, false);
        pd = new ProgressDialog(getActivity());
        globalFunction = new GlobalFunction(getActivity());
        etAction = view.findViewById(R.id.AEtAction);
        LinDisapproved = view.findViewById(R.id.LinDisapprovalReason);
        etDissapprovedReason = view.findViewById(R.id.AEtDisapprovalReason);
        btnSubmit = view.findViewById(R.id.BtnSubmitAction);
        txtShiftType = view.findViewById(R.id.TxtShiftCodeType);
        txtDateFrom = view.findViewById(R.id.TxtShiftCodeDateFrom);
        txtDateTo = view.findViewById(R.id.TxtShiftCodeDateTo);
        txtSingleCompound = view.findViewById(R.id.TxtShiftCodeSingleCompound);
        txtShiftCode = view.findViewById(R.id.TxtShiftCode);
        txtReason = view.findViewById(R.id.TxtShiftCodeReason);
        txtFormStatus = view.findViewById(R.id.TxtFormStatus);
        txtDateSubmitted = view.findViewById(R.id.TxtFormDateSubmitted);
        txtSunday = view.findViewById(R.id.TxtShiftSunday);
        txtMonday = view.findViewById(R.id.TxtShiftMonday);
        txtTuesday = view.findViewById(R.id.TxtShiftTuesday);
        txtWednesday = view.findViewById(R.id.TxtShiftWednesday);
        txtThursday = view.findViewById(R.id.TxtShiftThursday);
        txtFriday = view.findViewById(R.id.TxtShiftFriday);
        txtSaturday = view.findViewById(R.id.TxtShiftSaturday);
        linCompound = view.findViewById(R.id.LinCompound);
        linSingle = view.findViewById(R.id.LinSingle);
        txtEmployeeName = view.findViewById(R.id.TxtEmployeeName);

        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }


        Bundle bundle = this.getArguments();
        fFormId = bundle.getString("FormId");
        fEmployeeName = bundle.getString("EmployeeName");
        fShiftType = bundle.getString("ShiftType");
        fDateFrom = bundle.getString("DateFrom");
        fDateTo = bundle.getString("DateTo");
        fSingleCompound = bundle.getString("SingleCompound");
        fShiftCode = bundle.getString("ShiftCode");
        fReason = bundle.getString("Reason");
        fFormStatus = bundle.getString("Status");
        fDateSubmitted = bundle.getString("DateSubmitted");
        fSunday = bundle.getString("Sunday");
        fMonday = bundle.getString("Monday");
        fTuesday = bundle.getString("Tuesday");
        fWednesday = bundle.getString("Wednesday");
        fThursday = bundle.getString("Thursday");
        fFriday = bundle.getString("Friday");
        fSaturday = bundle.getString("Saturday");

        txtDateFrom.setText(fDateFrom);
        txtDateTo.setText(fDateTo);
        txtShiftCode.setText(fShiftCode);
        txtReason.setText(fReason);
        txtFormStatus.setText(fFormStatus);
        txtDateSubmitted.setText(fDateSubmitted);
        if (fShiftType.equals("2")){
            txtShiftType.setText("Change");
        }
        txtEmployeeName.setText(fEmployeeName);

        if (fSingleCompound.equals("0")){
            txtSingleCompound.setText("Single");
            linSingle.setVisibility(View.VISIBLE);
            linCompound.setVisibility(View.GONE);
            txtShiftCode.setText(fShiftCode);
        }else if (fSingleCompound.equals("1")){
            txtSingleCompound.setText("Compound");
            linSingle.setVisibility(View.GONE);
            linCompound.setVisibility(View.VISIBLE);
            txtSunday.setText(parseShiftCode(fSunday));
            txtMonday.setText(parseShiftCode(fMonday));
            txtTuesday.setText(parseShiftCode(fTuesday));
            txtWednesday.setText(parseShiftCode(fWednesday));
            txtThursday.setText(parseShiftCode(fThursday));
            txtFriday.setText(parseShiftCode(fFriday));
            txtSaturday.setText(parseShiftCode(fSaturday));
        }
        txtDateFrom = view.findViewById(R.id.TxtShiftCodeDateFrom);
        txtDateTo = view.findViewById(R.id.TxtShiftCodeDateTo);
        txtSingleCompound = view.findViewById(R.id.TxtShiftCodeSingleCompound);
        txtShiftCode = view.findViewById(R.id.TxtShiftCode);
        txtReason = view.findViewById(R.id.TxtShiftCodeReason);
        txtFormStatus = view.findViewById(R.id.TxtFormStatus);
        txtDateSubmitted = view.findViewById(R.id.TxtDateSubmitted);
        txtShiftType = view.findViewById(R.id.TxtShiftCodeType);
        txtEmployeeName = view.findViewById(R.id.TxtEmployeeName);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etAction.getText().toString().length() == 0){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.chooseaction));
                }else {
                    if (etAction.getText().toString().equals("Approve")){
                        HashMap<String, String> params = new HashMap<>();
                        params.put("approver_id", employee_Id);
                        params.put("form_id", fFormId);
                        params.put("approval_action_id", "5");
                        SubmitForm request = new SubmitForm(Api.URL_SUBMIT_FORM_APPROVE, params, CODE_POST_REQUEST);
                        request.execute();
                    }else if (etAction.getText().toString().equals("Disapprove")){
                        if (etDissapprovedReason.getText().toString().trim().equals("")){
                            globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.statereason));
                        }else {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("approver_id", employee_Id);
                            params.put("form_id", fFormId);
                            params.put("approval_action_id", "6");
                            params.put("reason_for_disapprove", etDissapprovedReason.getText().toString());
                            SubmitForm request = new SubmitForm(Api.URL_SUBMIT_FORM_APPROVE, params, CODE_POST_REQUEST);
                            request.execute();
                        }
                    }
                }
            }
        });

        etAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_approveraction);
                dialog.setTitle("Custom Dialog");

                arrayAdapterItem.clear();
                ListView mylistview = dialog.findViewById(R.id.LvApproverAction);
                arrayAdapterItem.add("Approve");
                arrayAdapterItem.add("Disapprove");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                        arrayAdapterItem );
                mylistview.setAdapter(arrayAdapter);
                mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mylistview.setItemsCanFocus(false);
                fApproverActionResult = "";
                mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        fApproverActionResult = (String) mylistview.getItemAtPosition(position);
                    }
                });

                Button btnConfirmAction = dialog.findViewById(R.id.btnConfirmAction);
                btnConfirmAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fApproverActionResult.isEmpty()){
                            etAction.setText("");
                            LinDisapproved.setVisibility(View.GONE);
                        }else {
                            etAction.setText(fApproverActionResult);
                            if (fApproverActionResult.equals("Disapprove")){
                                LinDisapproved.setVisibility(View.VISIBLE);
                            }else {
                                LinDisapproved.setVisibility(View.GONE);
                            }
                        }

                        dialog.dismiss();

                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
            String result = "" , message = "";
            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    result = jsonObject.getString("status");
                    message = jsonObject.getString("msg");
                    if (result.equals("true")){
                        globalFunction.ShowSucessSnackbar(getActivity(),message);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    }else {
                        globalFunction.ShowErrorSnackbar(getActivity(),message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                }
            }else {
                globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredCannotConnect));
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
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


    public String parseShiftCode(String string){
        if (string.equals("")){
            string = "Rest day";
            return string;
        }else {
            return string;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
