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
import android.widget.CheckBox;
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


public class FormShiftCodeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public FormShiftCodeFragment() {
        // Required empty public constructor
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    Button btnSubmitShiftCode, btnConfirmShiftType, btnCancelShiftType, btnConfirmShiftCode;
    String fShiftType, fDateFrom, fDateTo, fSingleCompound, fShiftCode, fReason, ShiftCodeResult = "", ShiftTypeResult = "", fSunday, fMonday, fTuesday, fWednesday, fThursday, fFriday, fSaturday;
    TextView txtShiftType, txtDateFrom, txtDateTo, txtSingleCompound, txtShiftCode, txtReason, txtSunday, txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday;
    EditText etShiftType, etDateFrom, etDateTo, etShiftCode, etReason, etShiftCodeSun,etShiftCodeMon,etShiftCodeTue,etShiftCodeWed,etShiftCodeThu,etShiftCodeFri,etShiftCodeSat;
    RadioGroup rgSingleCompound, rgShiftType;
    RadioButton rbSingleCompoundItem;
    Calendar calendarDateFrom, calendarDateTo;
    RelativeLayout dialogSubmit, dialogClose;
    ProgressDialog pd;
    GlobalFunction globalFunction;
    private List<FormShiftCodeList> formShiftCodeListsData = new ArrayList<>();
    List<String> arrayAdapterItem  = new ArrayList<String>();
    LinearLayout linSingle,linCompound;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;
    ArrayList<String> fCompoundIsRestDay = new ArrayList<String>();
    ArrayList<String> fCompoundShiftCode = new ArrayList<String>();
    CheckBox cbSundayRestDay, cbMondayRestDay, cbTuesdayRestDay, cbWednesdayRestDay, cbThursdayRestDay, cbFridayRestDay, cbSaturdayRestDay, cbShiftCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_shift_code, container, false);
        globalFunction = new GlobalFunction(getActivity());
        pd = new ProgressDialog(getActivity());
        btnSubmitShiftCode = view.findViewById(R.id.BtnSubmitShiftCode);
        etShiftType = view.findViewById(R.id.EtShiftType);
        etDateFrom = view.findViewById(R.id.EtShiftDateFrom);
        etDateTo = view.findViewById(R.id.EtShiftDateTo);
        etShiftCode = view.findViewById(R.id.EtShiftCode);
        etReason = view.findViewById(R.id.EtShiftCodeReason);
        rgSingleCompound = view.findViewById(R.id.RgSingleCompound);
        linSingle = view.findViewById(R.id.LinSingle);
        linCompound = view.findViewById(R.id.LinCompound);
        etShiftCodeSun = view.findViewById(R.id.EtShiftCodeSun);
        etShiftCodeMon = view.findViewById(R.id.EtShiftCodeMon);
        etShiftCodeTue = view.findViewById(R.id.EtShiftCodeTue);
        etShiftCodeWed = view.findViewById(R.id.EtShiftCodeWed);
        etShiftCodeThu = view.findViewById(R.id.EtShiftCodeThu);
        etShiftCodeFri = view.findViewById(R.id.EtShiftCodeFri);
        etShiftCodeSat = view.findViewById(R.id.EtShiftCodeSat);
        cbSundayRestDay = view.findViewById(R.id.CbRestdaySunday);
        cbMondayRestDay = view.findViewById(R.id.CbRestdayMonday);
        cbTuesdayRestDay = view.findViewById(R.id.CbRestdayTuesday);
        cbWednesdayRestDay = view.findViewById(R.id.CbRestdayWednesday);
        cbThursdayRestDay = view.findViewById(R.id.CbRestdayThursday);
        cbFridayRestDay = view.findViewById(R.id.CbRestdayFriday);
        cbSaturdayRestDay = view.findViewById(R.id.CbRestdaySaturday);
        cbShiftCode = view.findViewById(R.id.CbShiftCode);


        if (getActivity().getIntent().getExtras() != null) {
            employee_Id = getActivity().getIntent().getStringExtra("employee_id");
            employee_User_Id = getActivity().getIntent().getStringExtra("user_id");
            employee_Fname = getActivity().getIntent().getStringExtra("employee_fname");
            employee_Email = getActivity().getIntent().getStringExtra("employee_email");
            employee_Image = getActivity().getIntent().getStringExtra("employee_image");
            employee_Deparment = getActivity().getIntent().getStringExtra("employee_department");
            employee_Role = getActivity().getIntent().getStringExtra("employee_role");
        }


        CheckBoxOnClick(cbSundayRestDay,etShiftCodeSun);
        CheckBoxOnClick(cbMondayRestDay,etShiftCodeMon);
        CheckBoxOnClick(cbTuesdayRestDay,etShiftCodeTue);
        CheckBoxOnClick(cbWednesdayRestDay,etShiftCodeWed);
        CheckBoxOnClick(cbThursdayRestDay,etShiftCodeThu);
        CheckBoxOnClick(cbFridayRestDay,etShiftCodeFri);
        CheckBoxOnClick(cbSaturdayRestDay,etShiftCodeSat);

        HashMap<String, String> params = new HashMap<>();
        GetShiftCodes requestGetShiftCodes = new GetShiftCodes(Api.URL_GET_FORM_SHIFT_CODES, params, CODE_GET_REQUEST);
        requestGetShiftCodes.execute();

        rgSingleCompound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.RadioSingle:
                        linSingle.setVisibility(View.VISIBLE);
                        linCompound.setVisibility(View.GONE);
                        break;
                    case R.id.RadioCompound:
                        linSingle.setVisibility(View.GONE);
                        linCompound.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        etShiftCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCode,cbShiftCode);
            }
        });

        etShiftCodeSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeSun,cbSundayRestDay);
            }
        });

        etShiftCodeMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeMon,cbMondayRestDay);
            }
        });

        etShiftCodeTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeTue,cbTuesdayRestDay);
            }
        });

        etShiftCodeWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeWed,cbWednesdayRestDay);
            }
        });

        etShiftCodeThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeThu,cbThursdayRestDay);
            }
        });

        etShiftCodeFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeFri,cbFridayRestDay);
            }
        });

        etShiftCodeSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowShiftCodeDialog(etShiftCodeSat,cbSaturdayRestDay);
            }
        });

        etShiftType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_shifttype);
                dialog.setTitle("Custom Dialog");
                ListView mylistview = dialog.findViewById(R.id.LvShiftType);
                List<String> arrayAdapterItem  = new ArrayList<String>();
                arrayAdapterItem.clear();
                arrayAdapterItem.add("Change");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                        arrayAdapterItem );
                mylistview.setAdapter(arrayAdapter);
                mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mylistview.setItemsCanFocus(false);
                ShiftTypeResult = "";
                mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ShiftTypeResult = (String) mylistview.getItemAtPosition(position);
                    }
                });

                btnConfirmShiftType = dialog.findViewById(R.id.BtnConfirmShiftType);
                btnConfirmShiftType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ShiftTypeResult.isEmpty()){
                            etShiftType.setText("");
                            dialog.dismiss();

                        }else {
                            etShiftType.setText(ShiftTypeResult);
                            dialog.dismiss();
                        }
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        calendarDateFrom = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateDateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarDateFrom.set(Calendar.YEAR, year);
                calendarDateFrom.set(Calendar.MONTH, monthOfYear);
                calendarDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etDateFrom.setText(sdf.format(calendarDateFrom.getTime()));
            }

        };
        etDateFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDateFrom, calendarDateFrom
                        .get(Calendar.YEAR), calendarDateFrom.get(Calendar.MONTH),
                        calendarDateFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        calendarDateTo = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateDateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarDateTo.set(Calendar.YEAR, year);
                calendarDateTo.set(Calendar.MONTH, monthOfYear);
                calendarDateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                if (calendarDateTo.getTime().before(calendarDateFrom.getTime())){
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            R.string.sbSelectValidDate, Snackbar.LENGTH_LONG);
                    View snackBarView = snackBar.getView();snackBarView.setBackgroundColor(getActivity().getColor(R.color.colorAccent));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getActivity().getColor(R.color.colorWhite));
                    textView.setTextSize(18);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
                    snackBar.show();
                    etDateTo.setText("");

                }else {
                    etDateTo.setText(sdf.format(calendarDateTo.getTime()));
                }

            }

        };
        etDateTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDateTo, calendarDateTo
                        .get(Calendar.YEAR), calendarDateTo.get(Calendar.MONTH),
                        calendarDateTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        btnSubmitShiftCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rgSingleCompound.getCheckedRadioButtonId();
                rbSingleCompoundItem = getActivity().findViewById(selectedId);
                fSingleCompound = String.valueOf(rbSingleCompoundItem.getText());

                fShiftType = String.valueOf(etShiftType.getText());
                fDateFrom = String.valueOf(etDateFrom.getText());
                fDateTo = String.valueOf(etDateTo.getText());
                fSingleCompound = String.valueOf(rbSingleCompoundItem.getText());
                fShiftCode = String.valueOf(etShiftCode.getText());
                fReason = String.valueOf(etReason.getText());


                if (fShiftType.isEmpty() || fDateFrom.isEmpty() || fDateTo.isEmpty() || fReason.isEmpty() || fReason.trim().length() == 0){
                    globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                }else {
                    if (fSingleCompound.equals("Single")){
                        if (fShiftCode.equals("") || (fShiftCode.trim().length()== 0)){
                            globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                        }else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_form_shiftcode);
                            dialog.setTitle("Custom Dialog");
                            txtShiftType = dialog.findViewById(R.id.TxtShiftCodeType);
                            txtDateFrom = dialog.findViewById(R.id.TxtShiftCodeDateFrom);
                            txtDateTo = dialog.findViewById(R.id.TxtShiftCodeDateTo);
                            txtSingleCompound = dialog.findViewById(R.id.TxtShiftCodeSingleCompound);
                            txtShiftCode = dialog.findViewById(R.id.TxtShiftCodeData);
                            txtReason = dialog.findViewById(R.id.TxtShiftCodeReason);
                            dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                            dialogClose = dialog.findViewById(R.id.DialogCancel);
                            LinearLayout linSingle = dialog.findViewById(R.id.LinSingle);
                            LinearLayout linCompound = dialog.findViewById(R.id.LinCompound);
                            linSingle.setVisibility(View.VISIBLE);
                            linCompound.setVisibility(View.GONE);

                            txtShiftType.setText(fShiftType);
                            txtDateFrom.setText(fDateFrom);
                            txtDateTo.setText(fDateTo);
                            txtSingleCompound.setText(fSingleCompound);
                            txtShiftCode.setText(fShiftCode);
                            txtReason.setText(fReason);

                            dialogClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                                    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                    Date DateFrom = null;
                                    Date DateTo = null;
                                    try {
                                        DateFrom = myformat.parse(fDateFrom);
                                        DateTo = myformat.parse(fDateTo);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    String fShiftTypeValue = "0";
                                    if (fShiftType.equals("Change")){
                                        fShiftTypeValue = "2";
                                    }


                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("employee_id", employee_Id);
                                    params.put("user_id", employee_User_Id);
                                    params.put("form_type_id", "6");
                                    params.put("shift_type_id", fShiftTypeValue);
                                    params.put("dt_from",DateFormat.format(DateFrom));
                                    params.put("dt_to",DateFormat.format(DateTo));
                                    params.put("shift_code_id",GetShiftCodeValue(fShiftCode));
                                    params.put("is_compound", "0");
                                    params.put("reason", fReason);
                                    params.put("comp_shift_code_id", "");
                                    params.put("is_rest_day", "");

                                    SubmitForm request = new SubmitForm(Api.URL_SUBMIT_SHIFTCODE, params, CODE_POST_REQUEST);
                                    request.execute();

                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }
                    }else if (fSingleCompound.equals("Compound")){
                        fCompoundIsRestDay.clear();
                        fCompoundShiftCode.clear();
                        fSunday = String.valueOf(etShiftCodeSun.getText());
                        fMonday = String.valueOf(etShiftCodeMon.getText());
                        fTuesday = String.valueOf(etShiftCodeTue.getText());
                        fWednesday = String.valueOf(etShiftCodeWed.getText());
                        fThursday = String.valueOf(etShiftCodeThu.getText());
                        fFriday = String.valueOf(etShiftCodeFri.getText());
                        fSaturday = String.valueOf(etShiftCodeSat.getText());

                        if (fSunday.equals("") && !cbSundayRestDay.isChecked() ||
                                fMonday.equals("") && !cbMondayRestDay.isChecked() ||
                                    fTuesday.equals("") && !cbTuesdayRestDay.isChecked() ||
                                        fWednesday.equals("") && !cbWednesdayRestDay.isChecked() ||
                                            fThursday.equals("") && !cbThursdayRestDay.isChecked() ||
                                                fFriday.equals("") && !cbFridayRestDay.isChecked() ||
                                                    fSaturday.equals("") && !cbSaturdayRestDay.isChecked()){

                            globalFunction.ShowErrorSnackbar(getActivity(),getResources().getString(R.string.pleasefillup));
                        }else {

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_form_shiftcode);
                            dialog.setTitle("Custom Dialog");
                            LinearLayout linSingle = dialog.findViewById(R.id.LinSingle);
                            LinearLayout linCompound = dialog.findViewById(R.id.LinCompound);
                            linSingle.setVisibility(View.GONE);
                            linCompound.setVisibility(View.VISIBLE);
                            txtShiftType = dialog.findViewById(R.id.TxtShiftCodeType);
                            txtDateFrom = dialog.findViewById(R.id.TxtShiftCodeDateFrom);
                            txtDateTo = dialog.findViewById(R.id.TxtShiftCodeDateTo);
                            txtSingleCompound = dialog.findViewById(R.id.TxtShiftCodeSingleCompound);
                            txtShiftCode = dialog.findViewById(R.id.TxtShiftCodeData);
                            txtReason = dialog.findViewById(R.id.TxtShiftCodeReason);
                            dialogSubmit = dialog.findViewById(R.id.DialogSubmit);
                            dialogClose = dialog.findViewById(R.id.DialogCancel);
                            txtSunday = dialog.findViewById(R.id.TxtSundayData);
                            txtMonday = dialog.findViewById(R.id.TxtMondayData);
                            txtTuesday = dialog.findViewById(R.id.TxtTuesdayData);
                            txtWednesday = dialog.findViewById(R.id.TxtWednesdayData);
                            txtThursday = dialog.findViewById(R.id.TxtThursdayData);
                            txtFriday = dialog.findViewById(R.id.TxtFridayData);
                            txtSaturday = dialog.findViewById(R.id.TxtSaturdayData);

                            txtShiftType.setText(fShiftType);
                            txtDateFrom.setText(fDateFrom);
                            txtDateTo.setText(fDateTo);
                            txtSingleCompound.setText(fSingleCompound);
                            txtReason.setText(fReason);
                            ShiftCodeResultCheck(fSunday,txtSunday);
                            ShiftCodeResultCheck(fMonday,txtMonday);
                            ShiftCodeResultCheck(fTuesday,txtTuesday);
                            ShiftCodeResultCheck(fWednesday,txtWednesday);
                            ShiftCodeResultCheck(fThursday,txtThursday);
                            ShiftCodeResultCheck(fFriday,txtFriday);
                            ShiftCodeResultCheck(fSaturday,txtSaturday);

                            RestdayResultCheck(cbSundayRestDay);
                            RestdayResultCheck(cbMondayRestDay);
                            RestdayResultCheck(cbTuesdayRestDay);
                            RestdayResultCheck(cbWednesdayRestDay);
                            RestdayResultCheck(cbThursdayRestDay);
                            RestdayResultCheck(cbFridayRestDay);
                            RestdayResultCheck(cbSaturdayRestDay);


                            fCompoundShiftCode.add(GetShiftCodeValue(fSunday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fMonday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fTuesday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fWednesday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fThursday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fFriday));
                            fCompoundShiftCode.add(GetShiftCodeValue(fSaturday));

                            dialogClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat myformat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                                    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                    Date DateFrom = null;
                                    Date DateTo = null;
                                    try {
                                        DateFrom = myformat.parse(fDateFrom);
                                        DateTo = myformat.parse(fDateTo);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    String fShiftTypeValue = "0";
                                    if (fShiftType.equals("Change")){
                                        fShiftTypeValue = "2";
                                    }


                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("employee_id", employee_Id);
                                    params.put("user_id", employee_User_Id);
                                    params.put("form_type_id", "6");
                                    params.put("shift_type_id", fShiftTypeValue);
                                    params.put("dt_from",DateFormat.format(DateFrom));
                                    params.put("dt_to",DateFormat.format(DateTo));
                                    params.put("shift_code_id","");
                                    params.put("is_compound", "1");
                                    params.put("reason", fReason);
                                    params.put("comp_shift_code_id[0]", fCompoundShiftCode.get(0).toString());
                                    params.put("comp_shift_code_id[1]", fCompoundShiftCode.get(1).toString());
                                    params.put("comp_shift_code_id[2]", fCompoundShiftCode.get(2).toString());
                                    params.put("comp_shift_code_id[3]", fCompoundShiftCode.get(3).toString());
                                    params.put("comp_shift_code_id[4]", fCompoundShiftCode.get(4).toString());
                                    params.put("comp_shift_code_id[5]", fCompoundShiftCode.get(5).toString());
                                    params.put("comp_shift_code_id[6]", fCompoundShiftCode.get(6).toString());
                                    params.put("is_rest_day[0]", fCompoundIsRestDay.get(0).toString());
                                    params.put("is_rest_day[1]", fCompoundIsRestDay.get(1).toString());
                                    params.put("is_rest_day[2]", fCompoundIsRestDay.get(2).toString());
                                    params.put("is_rest_day[3]", fCompoundIsRestDay.get(3).toString());
                                    params.put("is_rest_day[4]", fCompoundIsRestDay.get(4).toString());
                                    params.put("is_rest_day[5]", fCompoundIsRestDay.get(5).toString());
                                    params.put("is_rest_day[6]", fCompoundIsRestDay.get(6).toString());

                                    SubmitForm request = new SubmitForm(Api.URL_SUBMIT_SHIFTCODE, params, CODE_POST_REQUEST);
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



    public String GetShiftCodeValue(String value){

        if (value.equals("")){
            value="0";
        }else {
            for (int i = 0; i < formShiftCodeListsData.size(); i++) {
                if (formShiftCodeListsData.get(i).shift_code.equals(value.substring(0,1))){
                    value = formShiftCodeListsData.get(i).getShift_code_id();
                }
            }
        }
        return value;
    }

    private void RestdayResultCheck(CheckBox cb){
        if (cb.isChecked()){
            fCompoundIsRestDay.add("1");
        }else {
            fCompoundIsRestDay.add("0");
        }
    }

    private void ShiftCodeResultCheck(String s,TextView textView){
        if (s.equals("")){
            textView.setText("Rest day");
        }else {
            textView.setText(s);
        }
    }


    private void CheckBoxOnClick(CheckBox cb, EditText et){

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked()){
                    et.setText("");
                }
            }
        });
    }


    private void ShowShiftCodeDialog(EditText et, CheckBox cb){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_shiftcode);
        dialog.setTitle("Custom Dialog");

        ListView mylistview = dialog.findViewById(R.id.LvShiftCode);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,
                arrayAdapterItem );
        mylistview.setAdapter(arrayAdapter);
        mylistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mylistview.setItemsCanFocus(false);
        ShiftCodeResult = "";
        mylistview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShiftCodeResult = (String) mylistview.getItemAtPosition(position);
            }
        });

        btnConfirmShiftCode = dialog.findViewById(R.id.BtnConfirmShiftCode);
        btnConfirmShiftCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShiftCodeResult.isEmpty()){
                    et.setText("");
                    dialog.dismiss();

                }else {
                    et.setText(ShiftCodeResult);
                    if (cb.isChecked()){
                        cb.toggle();
                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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


    private class GetShiftCodes extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        GetShiftCodes(String url, HashMap<String, String> params, int requestCode) {
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

            if (s != null) {
                formShiftCodeListsData.clear();
                arrayAdapterItem.clear();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);
                        FormShiftCodeList formShiftCodeList = new FormShiftCodeList();
                        formShiftCodeList.setTime_in(json_obj.getString("time_in"));
                        formShiftCodeList.setTime_out(json_obj.getString("time_out"));
                        formShiftCodeList.setDescription(json_obj.getString("description"));
                        formShiftCodeList.setShift_code(json_obj.getString("shift_code"));
                        formShiftCodeList.setShift_code_id(json_obj.getString("shift_code_id"));
                        formShiftCodeList.setWork_hours(json_obj.getString("work_hours"));
                        formShiftCodeList.setBreak_1(json_obj.getString("break_1"));
                        formShiftCodeList.setStatus(json_obj.getString("status"));
                        formShiftCodeListsData.add(formShiftCodeList);
                        arrayAdapterItem.add(json_obj.getString("shift_code") + " (" + json_obj.getString("description") + "[" + json_obj.getString("time_in") + " - " + json_obj.getString("time_out") + "])");
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
