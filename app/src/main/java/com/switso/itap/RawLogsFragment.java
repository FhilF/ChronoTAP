package com.switso.itap;

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
import com.switso.itap.Form.FilingFormHomeFragment;
import com.switso.itap.Form.FormList;
import com.switso.itap.Form.FormListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RawLogsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private List<RawLogsList> rawLogData = new ArrayList<>();
    private RawLogsAdapter adapter;
    RecyclerView rvRawLogs;
    private LinearLayoutManager layoutManager;
    private SwipyRefreshLayout swipeContainer;
    ProgressDialog pd;
    GlobalFunction globalFunction;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;
    int limitcount = 50 ,startcount = 0, itemcount = 0;

    public RawLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raw_logs, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        rvRawLogs = view.findViewById(R.id.RvRawLogs) ;
        rvRawLogs.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRawLogs.setLayoutManager(layoutManager);
        rvRawLogs.setItemAnimator(new DefaultItemAnimator());
        adapter = new RawLogsAdapter(getActivity(), rawLogData);
        rvRawLogs.setAdapter(adapter);
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

        swipeContainer.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if ((direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom").equals("top")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startcount = 0;
                            HashMap<String, String> params = new HashMap<>();
                            GetRawLogsEmployeeId requestGetRawLogsEmployeeId = new GetRawLogsEmployeeId(Api.URL_GET_RAW_LOGS+"employee_id="+ employee_Id  + "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
                            requestGetRawLogsEmployeeId.execute();
                            swipeContainer.setRefreshing(false);
                        }
                    }, 2000);
                }else if ((direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom").equals("bottom")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startcount = itemcount;
                            HashMap<String, String> params = new HashMap<>();
                            AppendRawLogsEmployeeId requestAppendRawLogsEmployeeId = new AppendRawLogsEmployeeId(Api.URL_GET_RAW_LOGS+"employee_id="+ employee_Id  + "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
                            requestAppendRawLogsEmployeeId.execute();
                            swipeContainer.setRefreshing(false);
                        }
                    }, 500);

                }
            }
        });




        return view;
    }



    private class AppendRawLogsEmployeeId extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        AppendRawLogsEmployeeId(String url, HashMap<String, String> params, int requestCode) {
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
                            RawLogsList rawLogsList = new RawLogsList();

                            rawLogsList.setWorkdate(json_obj.getString("workdate"));
                            rawLogsList.setIndicator(json_obj.getString("indicator"));
                            rawLogData.add(rawLogsList);
                        }

                        if (itemcount == rawLogData.size()){
                            globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.nomoreforms));
                        }else {
                            itemcount = rawLogData.size();
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
                Bundle bundle = new Bundle();
                SetFragment(new HomeFragment(),"Home Fragment", bundle);
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


    private class GetRawLogsEmployeeId extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetRawLogsEmployeeId(String url, HashMap<String, String> params, int requestCode) {
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
                rawLogData.clear();
                if (s.length() != 0){
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_obj = jsonArray.getJSONObject(i);
                            RawLogsList rawLogsList = new RawLogsList();

                            rawLogsList.setWorkdate(json_obj.getString("workdate"));
                            rawLogsList.setIndicator(json_obj.getString("indicator"));
                            rawLogData.add(rawLogsList);
                        }

                        itemcount = rawLogData.size();
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
        super.onResume();
        startcount = 0;
        HashMap<String, String> params = new HashMap<>();
        GetRawLogsEmployeeId requestGetRawLogsEmployeeId = new GetRawLogsEmployeeId(Api.URL_GET_RAW_LOGS+"employee_id="+ employee_Id  + "&start=" + startcount + "&length=" + limitcount, params, CODE_GET_REQUEST);
        requestGetRawLogsEmployeeId.execute();
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
