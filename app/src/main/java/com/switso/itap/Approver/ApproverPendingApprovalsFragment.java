package com.switso.itap.Approver;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.switso.itap.Api;
import com.switso.itap.Form.FormList;
import com.switso.itap.GetPostRequest;
import com.switso.itap.GlobalFunction;
import com.switso.itap.HomeFragment;
import com.switso.itap.R;
import com.switso.itap.RawLogsAdapter;
import com.switso.itap.RawLogsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ApproverPendingApprovalsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private List<FormList> formListsData = new ArrayList<>();
    private ApproverApprovalListAdapter adapter;
    RecyclerView rvApproval;
    private LinearLayoutManager layoutManager;
    private SwipyRefreshLayout swipeContainer;
    ProgressDialog pd;
    GlobalFunction globalFunction;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;


    public ApproverPendingApprovalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approver_pending_approvals, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        rvApproval = view.findViewById(R.id.RvForms) ;
        rvApproval.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvApproval.setLayoutManager(layoutManager);
        rvApproval.setItemAnimator(new DefaultItemAnimator());
        adapter = new ApproverApprovalListAdapter(getActivity(), formListsData);
        rvApproval.setAdapter(adapter);
        swipeContainer = view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,R.color.colorPrimaryDark);

        swipeContainer.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if ((direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom").equals("top")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> params = new HashMap<>();
                            GetForApprovalForm requestGetForApprovalForm = new GetForApprovalForm(Api.URL_GET_FOR_APPROVAL_FORM + employee_Id, params, CODE_GET_REQUEST);
                            requestGetForApprovalForm.execute();
                            swipeContainer.setRefreshing(false);
                        }
                    }, 2000);
                }
            }
        });
        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }

        return view;
    }


    private class GetForApprovalForm extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetForApprovalForm(String url, HashMap<String, String> params, int requestCode) {
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
                formListsData.clear();
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
                            formList.setApproval_status(json_obj.getString("form_status"));
                            formList.setEmployee_name(json_obj.getString("employee_name"));
                            formList.setEmployee_num(json_obj.getString("employee_num"));
                            formList.setLeave_type_code(json_obj.getString("leave_type_code"));
                            formList.setShift_time_type(json_obj.getString("shift_time_type"));

                            if (json_obj.has("shift_code_details")) {
                                ArrayList<String> shiftDays = new ArrayList<>();
                                shiftDays.add(json_obj.getString("shift_code_details"));
                                formList.setShift_days(shiftDays);
                            }




                            formListsData.add(formList);
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

    @Override
    public void onDestroy() {
        pd.dismiss();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        HashMap<String, String> params = new HashMap<>();
        GetForApprovalForm requestGetForApprovalForm = new GetForApprovalForm(Api.URL_GET_FOR_APPROVAL_FORM + employee_Id, params, CODE_GET_REQUEST);
        requestGetForApprovalForm.execute();
        super.onResume();
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
