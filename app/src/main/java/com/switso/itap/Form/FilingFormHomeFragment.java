package com.switso.itap.Form;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.switso.itap.Api;
import com.switso.itap.GetPostRequest;
import com.switso.itap.GlobalFunction;
import com.switso.itap.HomeFragment;
import com.switso.itap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FilingFormHomeFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    public FilingFormHomeFragment() {
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private FragmentActivity myContext;
    Button btnFileForm;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;
    private List<FormList> formData = new ArrayList<>();
    private FormListAdapter adapter;
    RecyclerView rvForms;
    private LinearLayoutManager layoutManager;
    private SwipyRefreshLayout swipeContainer;
    ProgressDialog pd;
    int limitcount = 50 ,startcount = 0, itemcount = 0;
    GlobalFunction globalFunction;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filing_form_home, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        btnFileForm = view.findViewById(R.id.BtnFileForm);
        rvForms = view.findViewById(R.id.RvForms) ;
        rvForms.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvForms.setLayoutManager(layoutManager);
        rvForms.setItemAnimator(new DefaultItemAnimator());
        adapter = new FormListAdapter(getActivity(), formData);
        rvForms.setAdapter(adapter);
        swipeContainer = view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,R.color.colorPrimaryDark);

                if (getActivity().getIntent().getExtras() != null) {
                    employee_Id = getActivity().getIntent().getStringExtra("employee_id");
                    employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
                    employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
                    employee_Email = getActivity().getIntent().getStringExtra("employee_email");
                    employee_Image = getActivity().getIntent().getStringExtra("employee_image");
                    employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
                    employee_Role = getActivity().getIntent().getStringExtra("employee_role");
                }


                btnFileForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        SetFragment(new FilingFormFragment(),"FilingForm", bundle);
                    }
                });

                swipeContainer.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(SwipyRefreshLayoutDirection direction) {

                        if ((direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom").equals("top")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startcount = 0;
                                    HashMap<String, String> params = new HashMap<>();
                                    GetFormbyEmployeeId requestGetFormbyEmployeeId = new GetFormbyEmployeeId(Api.URL_GET_FORM_BY_EMP_ID+"employee_id="+ employee_Id+"&subdomain="+Api.SUBDOMAIN+ "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
                                    requestGetFormbyEmployeeId.execute();
                                    swipeContainer.setRefreshing(false);
                                }
                            }, 2000);
                        }else if ((direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom").equals("bottom")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    startcount = itemcount;
                                    HashMap<String, String> params = new HashMap<>();
                                    AppendFormbyEmployeeId requestAppendFormbyEmployeeId = new AppendFormbyEmployeeId(Api.URL_GET_FORM_BY_EMP_ID+"employee_id="+ employee_Id+"&subdomain="+Api.SUBDOMAIN + "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
                                    requestAppendFormbyEmployeeId.execute();
                                    swipeContainer.setRefreshing(false);
                                }
                            }, 500);

                        }
                    }
                });

        return view;
    }


    private class AppendFormbyEmployeeId extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        AppendFormbyEmployeeId(String url, HashMap<String, String> params, int requestCode) {
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

            if(s != null){
                if (s.length() != 0){
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_obj = jsonArray.getJSONObject(i);
                            FormList formList = new FormList();

                            formList.setForm_id(json_obj.getString("form_id"));
                            formList.setForm_type_id(json_obj.getString("form_type_id"));
                            formList.setUser_id(json_obj.getString("user_id"));
                            formList.setEmployee_id(json_obj.getString("employee_id"));
                            formList.setApproval_action_id(json_obj.getString("approval_action_id"));
                            formList.setApproval_route_step_id(json_obj.getString("approval_route_step_id"));
                            formList.setTimekeeping_inandout_id(json_obj.getString("timekeeping_inandout_id"));
                            formList.setShift_time_type_id(json_obj.getString("shift_time_type_id"));
                            formList.setTime_code_id(json_obj.getString("time_code_id"));
                            formList.setLeave_type_id(json_obj.getString("leave_type_id"));
                            formList.setLeave_hours(json_obj.getString("leave_hours"));
                            formList.setAwol_leave_hours(json_obj.getString("awol_leave_hours"));
                            formList.setShift_code_id(json_obj.getString("shift_code_id"));
                            formList.setShift_type_id(json_obj.getString("shift_type_id"));
                            formList.setRest_day_type_id(json_obj.getString("rest_day_type_id"));
                            formList.setOld_superior_id(json_obj.getString("old_superior_id"));
                            formList.setDay_id(json_obj.getString("day_id"));
                            formList.setAction(json_obj.getString("action"));
                            formList.setReason(json_obj.getString("reason"));
                            formList.setReason_for_cancellation(json_obj.getString("reason_for_cancellation"));
                            formList.setReason_for_disapproving(json_obj.getString("reason_for_disapproving"));
                            formList.setOld_date(json_obj.getString("old_date"));
                            formList.setNew_date(json_obj.getString("new_date"));
                            formList.setToil_date(json_obj.getString("toil_date"));
                            formList.setExp_date(json_obj.getString("exp_date"));
                            formList.setIs_halfday(json_obj.getString("is_halfday"));
                            formList.setIs_quarterday(json_obj.getString("is_quarterday"));
                            formList.setIs_rest_day(json_obj.getString("is_rest_day"));
                            formList.setIs_processed(json_obj.getString("is_processed"));
                            formList.setIs_compound(json_obj.getString("is_compound"));
                            formList.setIs_dispute(json_obj.getString("is_dispute"));
                            formList.setIs_last_step(json_obj.getString("is_last_step"));
                            formList.setIs_deleted(json_obj.getString("is_deleted"));
                            formList.setIs_expired(json_obj.getString("is_expired"));
                            formList.setDt_authorized_from(json_obj.getString("dt_authorized_from"));
                            formList.setDt_authorized_to(json_obj.getString("dt_authorized_to"));
                            formList.setDt_posted(json_obj.getString("dt_posted"));
                            formList.setDt_from(json_obj.getString("dt_from"));
                            formList.setDt_to(json_obj.getString("dt_to"));
                            formList.setDt_failure_timeinout(json_obj.getString("dt_failure_timeinout"));
                            formList.setDt_workdate(json_obj.getString("dt_workdate"));
                            formList.setTime_from(json_obj.getString("time_from"));
                            formList.setTime_to(json_obj.getString("time_to"));
                            formList.setTm_time(json_obj.getString("tm_time"));
                            formList.setToil_hours(json_obj.getString("toil_hours"));
                            formList.setFiled_hours(json_obj.getString("filed_hours"));
                            formList.setDt_created(json_obj.getString("dt_created"));
                            formList.setDt_updated(json_obj.getString("dt_updated"));
                            formList.setDt_reminded(json_obj.getString("dt_reminded"));
                            formList.setCreated_by(json_obj.getString("created_by"));
                            formList.setUpdated_by(json_obj.getString("updated_by"));
                            formList.setForm_name(json_obj.getString("form_name"));
                            formList.setForm_template_name(json_obj.getString("form_template_name"));
                            formList.setIs_active(json_obj.getString("is_active"));
                            formList.setIs_upload_attachment(json_obj.getString("is_upload_attachment"));
                            formList.setLeave_type_code(json_obj.getString("leave_type_code"));
                            formList.setDescription(json_obj.getString("description"));
                            formList.setIs_leave_advance_notice(json_obj.getString("is_leave_advance_notice"));
                            formList.setLeave_advance_notice(json_obj.getString("leave_advance_notice"));
                            formList.setIs_expire(json_obj.getString("is_expire"));
                            formList.setIs_default(json_obj.getString("is_default"));
                            formList.setApproval_action(json_obj.getString("approval_action"));
                            formList.setApproval_status(json_obj.getString("approval_status"));
                            formList.setApproval_label(json_obj.getString("approval_label"));
                            formList.setPending_approvers(json_obj.getString("pending_approvers"));
                            formList.setForm_date_from(json_obj.getString("form_date_from"));
                            formList.setForm_date_to(json_obj.getString("form_date_to"));
                            formList.setShift_time_type(json_obj.getString("shift_time_type"));
                            formList.setShift_code(json_obj.getString("shift_code"));


                            ArrayList<String> shiftDays = new ArrayList<>();
                            shiftDays.add(json_obj.getString("shift_days"));
                            formList.setShift_days(shiftDays);

                            formData.add(formList);
                        }

                        if (itemcount == formData.size()){
                            globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.nomoreforms));
                        }else {
                            itemcount = formData.size();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                    }
                }else {
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.sbNoData));
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


    private class GetFormbyEmployeeId extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetFormbyEmployeeId(String url, HashMap<String, String> params, int requestCode) {
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

            int count = globalFunction.getErrorCount();
            if(s != null){
                formData.clear();
                if (s.length() != 0){
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_obj = jsonArray.getJSONObject(i);
                            FormList formList = new FormList();

                            formList.setForm_id(json_obj.getString("form_id"));
                            formList.setForm_type_id(json_obj.getString("form_type_id"));
                            formList.setUser_id(json_obj.getString("user_id"));
                            formList.setEmployee_id(json_obj.getString("employee_id"));
                            formList.setApproval_action_id(json_obj.getString("approval_action_id"));
                            formList.setApproval_route_step_id(json_obj.getString("approval_route_step_id"));
                            formList.setTimekeeping_inandout_id(json_obj.getString("timekeeping_inandout_id"));
                            formList.setShift_time_type_id(json_obj.getString("shift_time_type_id"));
                            formList.setTime_code_id(json_obj.getString("time_code_id"));
                            formList.setLeave_type_id(json_obj.getString("leave_type_id"));
                            formList.setLeave_hours(json_obj.getString("leave_hours"));
                            formList.setAwol_leave_hours(json_obj.getString("awol_leave_hours"));
                            formList.setShift_code_id(json_obj.getString("shift_code_id"));
                            formList.setShift_type_id(json_obj.getString("shift_type_id"));
                            formList.setRest_day_type_id(json_obj.getString("rest_day_type_id"));
                            formList.setOld_superior_id(json_obj.getString("old_superior_id"));
                            formList.setDay_id(json_obj.getString("day_id"));
                            formList.setAction(json_obj.getString("action"));
                            formList.setReason(json_obj.getString("reason"));
                            formList.setReason_for_cancellation(json_obj.getString("reason_for_cancellation"));
                            formList.setReason_for_disapproving(json_obj.getString("reason_for_disapproving"));
                            formList.setOld_date(json_obj.getString("old_date"));
                            formList.setNew_date(json_obj.getString("new_date"));
                            formList.setToil_date(json_obj.getString("toil_date"));
                            formList.setExp_date(json_obj.getString("exp_date"));
                            formList.setIs_halfday(json_obj.getString("is_halfday"));
                            formList.setIs_quarterday(json_obj.getString("is_quarterday"));
                            formList.setIs_rest_day(json_obj.getString("is_rest_day"));
                            formList.setIs_processed(json_obj.getString("is_processed"));
                            formList.setIs_compound(json_obj.getString("is_compound"));
                            formList.setIs_dispute(json_obj.getString("is_dispute"));
                            formList.setIs_last_step(json_obj.getString("is_last_step"));
                            formList.setIs_deleted(json_obj.getString("is_deleted"));
                            formList.setIs_expired(json_obj.getString("is_expired"));
                            formList.setDt_authorized_from(json_obj.getString("dt_authorized_from"));
                            formList.setDt_authorized_to(json_obj.getString("dt_authorized_to"));
                            formList.setDt_posted(json_obj.getString("dt_posted"));
                            formList.setDt_from(json_obj.getString("dt_from"));
                            formList.setDt_to(json_obj.getString("dt_to"));
                            formList.setDt_failure_timeinout(json_obj.getString("dt_failure_timeinout"));
                            formList.setDt_workdate(json_obj.getString("dt_workdate"));
                            formList.setTime_from(json_obj.getString("time_from"));
                            formList.setTime_to(json_obj.getString("time_to"));
                            formList.setTm_time(json_obj.getString("tm_time"));
                            formList.setToil_hours(json_obj.getString("toil_hours"));
                            formList.setFiled_hours(json_obj.getString("filed_hours"));
                            formList.setDt_created(json_obj.getString("dt_created"));
                            formList.setDt_updated(json_obj.getString("dt_updated"));
                            formList.setDt_reminded(json_obj.getString("dt_reminded"));
                            formList.setCreated_by(json_obj.getString("created_by"));
                            formList.setUpdated_by(json_obj.getString("updated_by"));
                            formList.setForm_name(json_obj.getString("form_name"));
                            formList.setForm_template_name(json_obj.getString("form_template_name"));
                            formList.setIs_active(json_obj.getString("is_active"));
                            formList.setIs_upload_attachment(json_obj.getString("is_upload_attachment"));
                            formList.setLeave_type_code(json_obj.getString("leave_type_code"));
                            formList.setDescription(json_obj.getString("description"));
                            formList.setIs_leave_advance_notice(json_obj.getString("is_leave_advance_notice"));
                            formList.setLeave_advance_notice(json_obj.getString("leave_advance_notice"));
                            formList.setIs_expire(json_obj.getString("is_expire"));
                            formList.setIs_default(json_obj.getString("is_default"));
                            formList.setApproval_action(json_obj.getString("approval_action"));
                            formList.setApproval_status(json_obj.getString("approval_status"));
                            formList.setApproval_label(json_obj.getString("approval_label"));
                            formList.setPending_approvers(json_obj.getString("pending_approvers"));
                            formList.setForm_date_from(json_obj.getString("form_date_from"));
                            formList.setForm_date_to(json_obj.getString("form_date_to"));
                            formList.setShift_time_type(json_obj.getString("shift_time_type"));
                            formList.setShift_code(json_obj.getString("shift_code"));


                            ArrayList<String> shiftDays = new ArrayList<>();
                            shiftDays.add(json_obj.getString("shift_days"));
                            formList.setShift_days(shiftDays);

                            formData.add(formList);
                        }
                        itemcount = formData.size();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                    }
                }else {
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.sbNoData));
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
    public void onResume() {
        startcount = 0;
        HashMap<String, String> params = new HashMap<>();
        GetFormbyEmployeeId requestGetFormbyEmployeeId = new GetFormbyEmployeeId(Api.URL_GET_FORM_BY_EMP_ID+"employee_id="+ employee_Id+"&subdomain="+Api.SUBDOMAIN+ "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
        requestGetFormbyEmployeeId.execute();
        super.onResume();
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
