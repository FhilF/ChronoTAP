package com.switso.itap.Form;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
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


public class FormRestdayFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormRestdayFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressDialog pd;
    RadioGroup rgOption;
    Button btnConfirmOptionRestday, btnSubmitRestday;
    RadioButton rbRestdayItem;
    RelativeLayout relOptionAdd, relOptionChange, relOptionRemove, dialogSubmit, dialogClose;
    LinearLayout  linDialogConfirmAdd, linDialogConfirmChange, linDialogConfirmRemove;
    String optionSelected, fOption, fAddNewRestDay, fChangeNewRestDay, fChangeOldRestDay, fRemoveOldRestDay, fReason, RestdayResult;
    TextView txtOption, txtAddNewRestDay, txtChangeNewRestday, txtChangeOldRestday, txtRemoveOldRestDay, txtReason;
    EditText etOption, etAddNewRestDay, etChangeNewRestDay, etChangeOldRestDay, etRemoveOldRestDay, etReason;
    Calendar calendarRemoveOldRestDay, calendarAddNewRestDay, calendarChangeOldRestDay, calendarChangeNewRestDay;
    GlobalFunction globalFunction;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_restday, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        relOptionAdd = view.findViewById(R.id.RelRestdayOptionAdd);
        relOptionChange = view.findViewById(R.id.RelRestdayOptionChange);
        relOptionRemove = view.findViewById(R.id.RelRestdayOptionRemove);
        btnSubmitRestday = view.findViewById(R.id.BtnSubmitRestday);
        etOption = view.findViewById(R.id.EtRestdayOption);
        etAddNewRestDay = view.findViewById(R.id.EtAddRestdayNew);
        etChangeNewRestDay = view.findViewById(R.id.EtChangeRestDayNew);
        etChangeOldRestDay = view.findViewById(R.id.EtChangeRestDayOld);
        etRemoveOldRestDay = view.findViewById(R.id.EtRemoveOldRestDay);
        etReason = view.findViewById(R.id.EtRestdayReason);
        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }


        calendarAddNewRestDay = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateAddNewRestDay = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarAddNewRestDay.set(Calendar.YEAR, year);
                calendarAddNewRestDay.set(Calendar.MONTH, monthOfYear);
                calendarAddNewRestDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etAddNewRestDay.setText(sdf.format(calendarAddNewRestDay.getTime()));
            }

        };
        etAddNewRestDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateAddNewRestDay, calendarAddNewRestDay
                        .get(Calendar.YEAR), calendarAddNewRestDay.get(Calendar.MONTH),
                        calendarAddNewRestDay.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarChangeNewRestDay = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateChangeNewRestDay = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarChangeNewRestDay.set(Calendar.YEAR, year);
                calendarChangeNewRestDay.set(Calendar.MONTH, monthOfYear);
                calendarChangeNewRestDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etChangeNewRestDay.setText(sdf.format(calendarChangeNewRestDay.getTime()));
            }

        };
        etChangeNewRestDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateChangeNewRestDay, calendarChangeNewRestDay
                        .get(Calendar.YEAR), calendarChangeNewRestDay.get(Calendar.MONTH),
                        calendarChangeNewRestDay.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarChangeOldRestDay = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateChangeOldRestday = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarChangeOldRestDay.set(Calendar.YEAR, year);
                calendarChangeOldRestDay.set(Calendar.MONTH, monthOfYear);
                calendarChangeOldRestDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etChangeOldRestDay.setText(sdf.format(calendarChangeOldRestDay.getTime()));
            }

        };
        etChangeOldRestDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateChangeOldRestday, calendarChangeOldRestDay
                        .get(Calendar.YEAR), calendarChangeOldRestDay.get(Calendar.MONTH),
                        calendarChangeOldRestDay.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarRemoveOldRestDay = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateRemoveOldRestday = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarRemoveOldRestDay.set(Calendar.YEAR, year);
                calendarRemoveOldRestDay.set(Calendar.MONTH, monthOfYear);
                calendarRemoveOldRestDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etRemoveOldRestDay.setText(sdf.format(calendarRemoveOldRestDay.getTime()));
            }

        };
        etRemoveOldRestDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateRemoveOldRestday, calendarRemoveOldRestDay
                        .get(Calendar.YEAR), calendarRemoveOldRestDay.get(Calendar.MONTH),
                        calendarRemoveOldRestDay.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        etOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_restday_option);
                dialog.setTitle("Custom Dialog");


                ListView mylistview = dialog.findViewById(R.id.LvRestdayOption);
                List<String> arrayAdapterItem  = new ArrayList<String>();
                arrayAdapterItem.clear();
                arrayAdapterItem.add("Add");
                arrayAdapterItem.add("Change");
                arrayAdapterItem.add("Remove");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                        arrayAdapterItem );
                mylistview.setAdapter(arrayAdapter);
                mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mylistview.setItemsCanFocus(false);
                RestdayResult = "";
                mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RestdayResult = (String) mylistview.getItemAtPosition(position);
                    }
                });

                Button btnConfirm = dialog.findViewById(R.id.BtnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (RestdayResult.isEmpty()){
                            etOption.setText("");
                            relOptionRemove.setVisibility(View.GONE);
                            relOptionAdd.setVisibility(View.GONE);
                            relOptionChange.setVisibility(View.GONE);
                            dialog.dismiss();

                        }else {
                            etOption.setText(RestdayResult);
                            if (RestdayResult.equals("Add")){
                                relOptionAdd.setVisibility(View.VISIBLE);
                                relOptionChange.setVisibility(View.GONE);
                                relOptionRemove.setVisibility(View.GONE);
                            }else if(RestdayResult.equals("Change")){
                                relOptionChange.setVisibility(View.VISIBLE);
                                relOptionRemove.setVisibility(View.GONE);
                                relOptionAdd.setVisibility(View.GONE);

                            }else if(RestdayResult.equals("Remove")){
                                relOptionRemove.setVisibility(View.VISIBLE);
                                relOptionAdd.setVisibility(View.GONE);
                                relOptionChange.setVisibility(View.GONE);

                            }
                            dialog.dismiss();
                        }
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });



        btnSubmitRestday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(etOption.getText()).isEmpty()){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Please choose an option", Snackbar.LENGTH_LONG);
                    View snackBarView = snackBar.getView();
                    snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                    TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                    textView.setTextSize(18);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                    snackBar.show();

                }else {
                    if (RestdayResult.equals("Add")){
                        fOption = String.valueOf(etOption.getText());
                        fReason = String.valueOf(etReason.getText());
                        fAddNewRestDay = String.valueOf(etAddNewRestDay.getText());
                        if(fOption.isEmpty() || fReason.isEmpty() || fAddNewRestDay.isEmpty()){
                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    R.string.pleasefillup, Snackbar.LENGTH_LONG);
                            View snackBarView = snackBar.getView();snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                            textView.setTextSize(18);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                            snackBar.show();

                        }else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_form_restday);
                            dialog.setTitle("Custom Dialog");
                            linDialogConfirmAdd = dialog.findViewById(R.id.LinDialogOptionAdd);
                            linDialogConfirmChange = dialog.findViewById(R.id.LinDialogOptionChange);
                            linDialogConfirmRemove = dialog.findViewById(R.id.LinDialogOptionRemove);
                            txtOption = dialog.findViewById(R.id.txtConfirmOption);
                            txtReason = dialog.findViewById(R.id.txtConfirmRestdayReason);

                            txtOption.setText(fOption);
                            txtReason.setText(fReason);
                            dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                            dialogClose = dialog.findViewById(R.id.DialogCancel);
                            dialogClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            linDialogConfirmAdd.setVisibility(View.VISIBLE);
                            linDialogConfirmChange.setVisibility(View.GONE);
                            linDialogConfirmRemove.setVisibility(View.GONE);
                            txtAddNewRestDay = dialog.findViewById(R.id.txtConfirmAddNewRestDay);
                            txtAddNewRestDay.setText(fAddNewRestDay);


                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                                    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                    Date DateNew = null;
                                    try {
                                        DateNew = myformat.parse(fAddNewRestDay);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("employee_id", employee_Id);
                                    params.put("user_id", employee_User_Id);
                                    params.put("form_type_id", "5");
                                    params.put("rest_day", "1");
                                    params.put("new_date" , DateFormat.format(DateNew));
                                    params.put("old_date" , "");
                                    params.put("reason", fReason);
                                    SubmitForm request = new SubmitForm(Api.URL_SUBMIT_RESTDAY, params, CODE_POST_REQUEST);
                                    request.execute();
                                    dialog.dismiss();



                                }
                            });

                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        }


                    }else if(RestdayResult.equals("Change")){

                        fOption = String.valueOf(etOption.getText());
                        fReason = String.valueOf(etReason.getText());
                        fChangeNewRestDay = String.valueOf(etChangeNewRestDay.getText());
                        fChangeOldRestDay = String.valueOf(etChangeOldRestDay.getText());

                        if (fOption.isEmpty() || fReason.isEmpty() || fChangeNewRestDay.isEmpty() || fChangeOldRestDay.isEmpty()){
                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    R.string.pleasefillup, Snackbar.LENGTH_LONG);
                            View snackBarView = snackBar.getView();snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                            textView.setTextSize(18);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                            snackBar.show();

                        }else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_form_restday);
                            dialog.setTitle("Custom Dialog");
                            linDialogConfirmAdd = dialog.findViewById(R.id.LinDialogOptionAdd);
                            linDialogConfirmChange = dialog.findViewById(R.id.LinDialogOptionChange);
                            linDialogConfirmRemove = dialog.findViewById(R.id.LinDialogOptionRemove);
                            txtOption = dialog.findViewById(R.id.txtConfirmOption);
                            txtReason = dialog.findViewById(R.id.txtConfirmRestdayReason);

                            txtOption.setText(fOption);
                            txtReason.setText(fReason);

                            dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                            dialogClose = dialog.findViewById(R.id.DialogCancel);
                            dialogClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            linDialogConfirmChange.setVisibility(View.VISIBLE);
                            linDialogConfirmAdd.setVisibility(View.GONE);
                            linDialogConfirmRemove.setVisibility(View.GONE);
                            txtChangeNewRestday = dialog.findViewById(R.id.txtConfirmChangeNewRestDay);
                            txtChangeOldRestday = dialog.findViewById(R.id.txtConfirmChangeOldRestDay);
                            txtChangeOldRestday.setText(fChangeOldRestDay);
                            txtChangeNewRestday.setText(fChangeNewRestDay);
                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                                    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                    Date DateNew = null;
                                    Date DateOld = null;
                                    try {
                                        DateNew = myformat.parse(fChangeNewRestDay);
                                        DateOld = myformat.parse(fChangeOldRestDay);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("employee_id", employee_Id);
                                    params.put("user_id", employee_User_Id);
                                    params.put("form_type_id", "5");
                                    params.put("rest_day", "2");
                                    params.put("new_date" , DateFormat.format(DateNew));
                                    params.put("old_date" , DateFormat.format(DateOld));
                                    params.put("reason", fReason);
                                    SubmitForm request = new SubmitForm(Api.URL_SUBMIT_RESTDAY, params, CODE_POST_REQUEST);
                                    request.execute();
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        }

                    }else if(RestdayResult.equals("Remove")){
                        fOption = String.valueOf(etOption.getText());
                        fReason = String.valueOf(etReason.getText());
                        fRemoveOldRestDay = String.valueOf(etRemoveOldRestDay.getText());
                        if (fOption.isEmpty() || fReason.isEmpty() || fRemoveOldRestDay.isEmpty()){
                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    R.string.pleasefillup, Snackbar.LENGTH_LONG);
                            View snackBarView = snackBar.getView();snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                            textView.setTextSize(18);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                            snackBar.show();
                        }else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_form_restday);
                            dialog.setTitle("Custom Dialog");
                            linDialogConfirmAdd = dialog.findViewById(R.id.LinDialogOptionAdd);
                            linDialogConfirmChange = dialog.findViewById(R.id.LinDialogOptionChange);
                            linDialogConfirmRemove = dialog.findViewById(R.id.LinDialogOptionRemove);
                            txtOption = dialog.findViewById(R.id.txtConfirmOption);
                            txtReason = dialog.findViewById(R.id.txtConfirmRestdayReason);
                            txtOption.setText(fOption);
                            txtReason.setText(fReason);

                            dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                            dialogClose = dialog.findViewById(R.id.DialogCancel);
                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            linDialogConfirmRemove.setVisibility(View.VISIBLE);
                            linDialogConfirmAdd.setVisibility(View.GONE);
                            linDialogConfirmChange.setVisibility(View.GONE);
                            txtRemoveOldRestDay = dialog.findViewById(R.id.txtConfirmRemoveOldRestDay);
                            txtRemoveOldRestDay.setText(fRemoveOldRestDay);
                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                                    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                    Date DateOld = null;
                                    try {
                                        DateOld = myformat.parse(fRemoveOldRestDay);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("employee_id", employee_Id);
                                    params.put("user_id", employee_User_Id);
                                    params.put("form_type_id", "5");
                                    params.put("rest_day", "3");
                                    params.put("new_date" , "");
                                    params.put("old_date" , DateFormat.format(DateOld));
                                    params.put("reason", fReason);
                                    SubmitForm request = new SubmitForm(Api.URL_SUBMIT_RESTDAY, params, CODE_POST_REQUEST);
                                    request.execute();
                                    dialog.dismiss();

                                }
                            });

                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }

                    }


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


    public void SetFragment(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
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
}
