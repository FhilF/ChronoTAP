package com.switso.itap.Form;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.switso.itap.Form.FormFailureToInOutFragment;
import com.switso.itap.Form.FormLeaveOfAbsenceFragment;
import com.switso.itap.Form.FormOffSiteAssignment;
import com.switso.itap.Form.FormOvertimeFragment;
import com.switso.itap.Form.FormRestdayFragment;
import com.switso.itap.Form.FormShiftCodeFragment;
import com.switso.itap.Form.FormTOILCreditFragment;
import com.switso.itap.GetPostRequest;
import com.switso.itap.GlobalFunction;
import com.switso.itap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FilingFormFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FilingFormFragment() {
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    RelativeLayout relBtnFailureInOut, relBtnLeaveOfAbsence, relBtnTOILCredit, relBtnOvertime, relBtnRestDay, relBtnShiftCode, relBtnOffsiteAssignment;
    ProgressDialog pd;
    GlobalFunction globalFunction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filing_form, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        relBtnFailureInOut = view.findViewById(R.id.RelBtnFailureInOut);
        relBtnLeaveOfAbsence = view.findViewById(R.id.RelBtnLeaveOfAbsence);
        relBtnTOILCredit = view.findViewById(R.id.RelBtnTOILCredit);
        relBtnOvertime = view.findViewById(R.id.RelBtnOvertime);
        relBtnRestDay = view.findViewById(R.id.RelBtnRestDay);
        relBtnShiftCode = view.findViewById(R.id.RelBtnShiftCode);
        relBtnOffsiteAssignment = view.findViewById(R.id.RelBtnOffsiteAssignment);

        relBtnFailureInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormFailureToInOutFragment(),"Form Failure to in/out",bundle);
                }
            }
        });

        relBtnLeaveOfAbsence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormLeaveOfAbsenceFragment(),"Form Leave Of Absence",bundle);
                }

            }
        });

        relBtnOffsiteAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormOffSiteAssignment(),"Form Off-site Assignment",bundle);
                }

            }
        });

        relBtnTOILCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormTOILCreditFragment(),"Form TOIL Credit",bundle);
                }

            }
        });

        relBtnOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormOvertimeFragment(),"Form Overtime",bundle);
                }

            }
        });

        relBtnRestDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormRestdayFragment(),"Form Restday",bundle);
                }

            }
        });

        relBtnShiftCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!globalFunction.isNetworkAvailable()){
                    globalFunction.ShowErrorSnackbar(getActivity(), getResources().getString(R.string.nonet));
                }else {
                    Bundle bundle = new Bundle();
                    SetFragment(new FormShiftCodeFragment(),"Form Shift Code",bundle);
                }

            }
        });


        return view;
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
