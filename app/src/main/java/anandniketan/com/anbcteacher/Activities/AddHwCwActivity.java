package anandniketan.com.anbcteacher.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import anandniketan.com.anbcteacher.AsyncTasks.GetGradeAsyncTask;
import anandniketan.com.anbcteacher.AsyncTasks.GetSectionAsyncTask;
import anandniketan.com.anbcteacher.AsyncTasks.GetSubjectAsyncTask;
import anandniketan.com.anbcteacher.AsyncTasks.GetTermAsyncTask;
import anandniketan.com.anbcteacher.AsyncTasks.InsertDailyEntryAsyncTask;
import anandniketan.com.anbcteacher.Models.LeaveModel.LeaveFinalArray;
import anandniketan.com.anbcteacher.Models.LeaveModel.LeaveMainModel;
import anandniketan.com.anbcteacher.R;
import anandniketan.com.anbcteacher.Utility.Utility;

public class AddHwCwActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static String dateFinal;
    HashMap<Integer, String> spinnerTermIdNameMap, spinnerGradeIdNameMap, spinnerSubjectIdNameMap;
    String termNameStr, termIdStr, gradeNameStr, gradeIdStr = "0", subjectIdStr;
    private Spinner spTerm, spGrade, spSubject;
    private GridView sectionGridView;
    private TextView etDate;
    private EditText etHw, etCw;
    private Button btnSubmit;
    private GetTermAsyncTask getTermAsyncTask = null;
    private ProgressDialog progressDialog = null;
    private DatePickerDialog datePickerDialog;
    private LeaveMainModel termDetailResponse, gradeDetailResponse, sectionDetailResponse, subjectDetailResponse, insertDetailResponse;
    private GetGradeAsyncTask getgradeAsyncTask = null;
    private GetSectionAsyncTask sectionAsyncTask = null;
    private GetSubjectAsyncTask subjectAsyncTask = null;
    private InsertDailyEntryAsyncTask insertDailyEntryAsyncTask = null;
    private SectionAdapter sectionAdapter;
    private Calendar calendar;
    private int Year, Month, Day;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hw_cw);

        spTerm = findViewById(R.id.term_spinner);
        spGrade = findViewById(R.id.grade_spinner);
        sectionGridView = findViewById(R.id.section_grid_view);
        spSubject = findViewById(R.id.subject_spinner);
        btnSubmit = findViewById(R.id.btnsubmit);
        etDate = findViewById(R.id.etdate_txt);
        etHw = findViewById(R.id.homework_add_edt);
        etCw = findViewById(R.id.classwork_add_edt);
        btnBack = findViewById(R.id.btnBack_homework);

        setListener();
        getTermData();

    }

    private void setListener() {

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String termName = spTerm.getSelectedItem().toString();
                String termId = spinnerTermIdNameMap.get(spTerm.getSelectedItemPosition());

                Log.d("value", termName + " " + termId);
                termIdStr = termId.toString();
                termNameStr = termName;
                Log.d("termIdStr", termIdStr);
                getGradeData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String gradeName = spGrade.getSelectedItem().toString();
                String gradeId = spinnerGradeIdNameMap.get(spGrade.getSelectedItemPosition());

                Log.d("value", gradeName + " " + gradeId);
                gradeIdStr = gradeId.toString();
                gradeNameStr = gradeName;
                Log.d("termIdStr", gradeIdStr);
                getSectionData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subjectName = spSubject.getSelectedItem().toString();
                String gsubjectId = spinnerSubjectIdNameMap.get(spSubject.getSelectedItemPosition());

                Log.d("value", subjectName + " " + gsubjectId);
                subjectIdStr = gsubjectId.toString();
//                sub = gradeName;
                Log.d("termIdStr", gradeIdStr);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(AddHwCwActivity.this, Year, Month, Day);
                datePickerDialog.setMaxDate(calendar);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#1B88C8"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()) {
                    if (!etCw.getText().toString().contains("\\|")) {
                        if (!etHw.getText().toString().contains("\\|")) {
                            insertHwCw();
                        } else {
                            etCw.setError("Character | not allowed in homework");
                        }
                    } else {
                        etHw.setError("Character | not allowed in homework");
                    }
                }
            }
        });

    }

    public boolean isValid() {

        boolean isvalid = true;

        if (TextUtils.isEmpty(etDate.getText().toString())) {
            etDate.setError("Please select proper date");
            isvalid = false;
        }

        if (TextUtils.isEmpty(etCw.getText().toString())) {
            etCw.setError("Please enter classwork");
            isvalid = false;
        }

        if (TextUtils.isEmpty(etHw.getText().toString())) {
            etHw.setError("Please enter homework");
            isvalid = false;
        }

        if (spGrade.getSelectedItem().toString().equalsIgnoreCase("") || spSubject.getSelectedItem().toString().equalsIgnoreCase("")
        || sectionDetailResponse.getFinalArray().size() < 0) {

            Utility.ping(AddHwCwActivity.this, "Please select another term");
            isvalid = false;
        }

        return isvalid;
    }

    public void getTermData() {
        if (Utility.isNetworkConnected(AddHwCwActivity.this)) {
            progressDialog = new ProgressDialog(AddHwCwActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("StaffID", Utility.getPref(AddHwCwActivity.this, "StaffID"));
                        getTermAsyncTask = new GetTermAsyncTask(params);
                        termDetailResponse = getTermAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                if (termDetailResponse.getFinalArray().size() > 0) {
                                    filltermspinner();
                                } else {
                                    progressDialog.dismiss();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utility.ping(AddHwCwActivity.this, "Network not available");
        }
    }

    public void filltermspinner() {
        ArrayList<Integer> termId = new ArrayList<Integer>();
        ArrayList<String> termname = new ArrayList<String>();

        for (int j = 0; j < termDetailResponse.getFinalArray().size(); j++) {
            termId.add(termDetailResponse.getFinalArray().get(j).getTermId());
        }

        for (int k = 0; k < termDetailResponse.getFinalArray().size(); k++) {
            termname.add(termDetailResponse.getFinalArray().get(k).getTerm());
        }
        String[] spinnertermIdArray = new String[termId.size()];
        spinnertermIdArray = new String[termname.size()];

        spinnerTermIdNameMap = new HashMap<Integer, String>();
        for (int i = 0; i < termId.size(); i++) {
            spinnerTermIdNameMap.put(i, String.valueOf(termId.get(i)));
            spinnertermIdArray[i] = termname.get(i).trim();
        }
        Log.d("TestArray", "" + termname);
//        HashSet hs = new HashSet();
//        hs.addAll(testname);
//        testname.clear();
//        testname.addAll(hs);

        ArrayAdapter<String> adaptertest = new ArrayAdapter<String>(AddHwCwActivity.this, R.layout.spinner_layout, spinnertermIdArray);
        spTerm.setAdapter(adaptertest);
    }

    public void getGradeData() {
        if (Utility.isNetworkConnected(AddHwCwActivity.this)) {
            progressDialog = new ProgressDialog(AddHwCwActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("EmployeeID", Utility.getPref(AddHwCwActivity.this, "StaffID"));
                        params.put("TermID", termIdStr);
                        getgradeAsyncTask = new GetGradeAsyncTask(params);
                        gradeDetailResponse = getgradeAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                if (gradeDetailResponse != null && gradeDetailResponse.getFinalArray().size() > 0) {
                                    fillgradepinner();
                                } else {

                                    gradeIdStr = "0";
                                    ArrayAdapter<String> adaptertest = new ArrayAdapter<>(AddHwCwActivity.this, R.layout.spinner_layout, new ArrayList<String>());
                                    spGrade.setAdapter(adaptertest);
                                    progressDialog.dismiss();

                                    getSectionData();

                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utility.ping(AddHwCwActivity.this, "Network not available");
        }
    }

    public void fillgradepinner() {
        ArrayList<Integer> gradeId = new ArrayList<>();
        ArrayList<String> gradename = new ArrayList<>();

        for (int j = 0; j < gradeDetailResponse.getFinalArray().size(); j++) {
            gradeId.add(gradeDetailResponse.getFinalArray().get(j).getStandard_id());
        }

        for (int k = 0; k < gradeDetailResponse.getFinalArray().size(); k++) {
            gradename.add(gradeDetailResponse.getFinalArray().get(k).getStandard());
        }
        String[] spinnergradeIdArray = new String[gradeId.size()];
        spinnergradeIdArray = new String[gradename.size()];

        spinnerGradeIdNameMap = new HashMap<>();
        for (int i = 0; i < gradeId.size(); i++) {
            spinnerGradeIdNameMap.put(i, String.valueOf(gradeId.get(i)));
            spinnergradeIdArray[i] = gradename.get(i).trim();
        }
        Log.d("TestArray", "" + gradename);
//        HashSet hs = new HashSet();
//        hs.addAll(testname);
//        testname.clear();
//        testname.addAll(hs);

        ArrayAdapter<String> adaptertest = new ArrayAdapter<>(AddHwCwActivity.this, R.layout.spinner_layout, spinnergradeIdArray);
        spGrade.setAdapter(adaptertest);
    }

    public void getSectionData() {
        if (Utility.isNetworkConnected(AddHwCwActivity.this)) {
            progressDialog = new ProgressDialog(AddHwCwActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("EmployeeID", Utility.getPref(AddHwCwActivity.this, "StaffID"));
                        params.put("TermID", termIdStr);
                        params.put("StandardID", gradeIdStr);
                        sectionAsyncTask = new GetSectionAsyncTask(params);
                        sectionDetailResponse = sectionAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                if (sectionDetailResponse != null && sectionDetailResponse.getFinalArray().size() > 0) {

                                    for (int i = 0; i < sectionDetailResponse.getFinalArray().size(); i++) {
                                        sectionDetailResponse.getFinalArray().get(i).setCheckedStatus("1");
                                    }
                                    sectionAdapter = new SectionAdapter(AddHwCwActivity.this, sectionDetailResponse.getFinalArray());

                                    sectionGridView.setAdapter(sectionAdapter);

                                    String selectedGradeIds = "";
                                    if (sectionAdapter != null) {
                                        selectedGradeIds = TextUtils.join("|", sectionAdapter.getCheckedStandards());
                                    }

                                    getSubjectData(selectedGradeIds);

                                } else {

                                    ArrayAdapter<String> adaptertest = new ArrayAdapter<>(AddHwCwActivity.this, R.layout.spinner_layout, new ArrayList<String>());
                                    sectionGridView.setAdapter(adaptertest);
                                    progressDialog.dismiss();

                                    getSubjectData("0");

                                }


                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utility.ping(AddHwCwActivity.this, "Network not available");
        }
    }

    public void getSubjectData(final String clsId) {
        if (Utility.isNetworkConnected(AddHwCwActivity.this)) {
            progressDialog = new ProgressDialog(AddHwCwActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("EmployeeID", Utility.getPref(AddHwCwActivity.this, "StaffID"));
                        params.put("TermID", termIdStr);
                        params.put("StandardID", gradeIdStr);
//                        params.put("ClassIDs", "5|6|7");
                        params.put("ClassIDs", clsId);

                        subjectAsyncTask = new GetSubjectAsyncTask(params);
                        subjectDetailResponse = subjectAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                if (subjectDetailResponse != null && subjectDetailResponse.getFinalArray().size() > 0) {
                                    fillsubjectspinner();
                                } else {

                                    ArrayAdapter<String> adaptertest = new ArrayAdapter<>(AddHwCwActivity.this, R.layout.spinner_layout, new ArrayList<String>());
                                    spSubject.setAdapter(adaptertest);
                                    progressDialog.dismiss();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utility.ping(AddHwCwActivity.this, "Network not available");
        }
    }

    public void fillsubjectspinner() {
        ArrayList<Integer> subjectId = new ArrayList<>();
        ArrayList<String> subjectname = new ArrayList<>();

        for (int j = 0; j < subjectDetailResponse.getFinalArray().size(); j++) {
            subjectId.add(Integer.valueOf(subjectDetailResponse.getFinalArray().get(j).getSubjectID()));
        }

        for (int k = 0; k < subjectDetailResponse.getFinalArray().size(); k++) {
            subjectname.add(subjectDetailResponse.getFinalArray().get(k).getSubjectName());
        }
        String[] spinnersubjectIdArray = new String[subjectId.size()];
        spinnersubjectIdArray = new String[subjectname.size()];

        spinnerSubjectIdNameMap = new HashMap<>();
        for (int i = 0; i < subjectId.size(); i++) {
            spinnerSubjectIdNameMap.put(i, String.valueOf(subjectId.get(i)));
            spinnersubjectIdArray[i] = subjectname.get(i).trim();
        }
        Log.d("TestArray", "" + subjectname);
//        HashSet hs = new HashSet();
//        hs.addAll(testname);
//        testname.clear();
//        testname.addAll(hs);

        ArrayAdapter<String> adaptertest = new ArrayAdapter<>(AddHwCwActivity.this, R.layout.spinner_layout, spinnersubjectIdArray);
        spSubject.setAdapter(adaptertest);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        populateSetDate(year, monthOfYear + 1, dayOfMonth);
    }

    public void populateSetDate(int year, int month, int day) {
        String d, m, y;
        d = Integer.toString(day);
        m = Integer.toString(month);
        y = Integer.toString(year);
        if (day < 10) {
            d = "0" + d;
        }
        if (month < 10) {
            m = "0" + m;
        }

        dateFinal = d + "/" + m + "/" + y;

        etDate.setText(dateFinal);

    }

    public void insertHwCw() {
        if (Utility.isNetworkConnected(AddHwCwActivity.this)) {
            progressDialog = new ProgressDialog(AddHwCwActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("EmployeeID", Utility.getPref(AddHwCwActivity.this, "StaffID"));
                        params.put("TermID", termIdStr);
                        params.put("StandardID", gradeIdStr);
                        String selectedGradeIds = TextUtils.join("|", sectionAdapter.getCheckedStandards());
                        params.put("ClassIDs", selectedGradeIds);
                        params.put("SubjectID", subjectIdStr);
                        params.put("Date", etDate.getText().toString());
                        params.put("HomeWork", etHw.getText().toString());
                        params.put("ClassWork", etCw.getText().toString());
                        insertDailyEntryAsyncTask = new InsertDailyEntryAsyncTask(params);
                        insertDetailResponse = insertDailyEntryAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                if (insertDetailResponse.getSuccess().equalsIgnoreCase("true")) {
                                    Utility.ping(AddHwCwActivity.this, insertDetailResponse.getMessage());
                                    onBackPressed();
                                } else {
                                    Utility.ping(AddHwCwActivity.this, insertDetailResponse.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utility.ping(AddHwCwActivity.this, "Network not available");
        }
    }

    public class SectionAdapter extends BaseAdapter {
        private Context mContext;
        private List<LeaveFinalArray> standardModel;
        private ArrayList<LeaveFinalArray> arrayList = new ArrayList<>();
        private ArrayList<String> checkedItemsIds;

        // Constructor
        public SectionAdapter(Context c, List<LeaveFinalArray> standardModel) {
            mContext = c;
            this.standardModel = standardModel;
        }

        @Override
        public int getCount() {
            return standardModel.size();
        }

        @Override
        public Object getItem(int position) {
            return standardModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            View view = null;
            convertView = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_row_standard_checkbox, null);
                viewHolder.check_standard = convertView.findViewById(R.id.check_standard);
                final LeaveFinalArray standarObj = standardModel.get(position);

                try {
                    viewHolder.check_standard.setText(standarObj.getClassName());
                    viewHolder.check_standard.setTag(standarObj.getClassID());

                    if (standarObj.getCheckedStatus().equalsIgnoreCase("1")) {
                        viewHolder.check_standard.setChecked(true);
                    } else {
                        viewHolder.check_standard.setChecked(false);
                    }

                    viewHolder.check_standard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            String selectedGradeIds = TextUtils.join("|", sectionAdapter.getCheckedStandards());
                            Log.e("ClassIDs", selectedGradeIds);

                            getSubjectData(selectedGradeIds);

                            if (isChecked) {
                                standarObj.setCheckedStatus("1");
                                standardModel.get(position).setCheckedStatus("1");
                            } else {
                                standarObj.setCheckedStatus("0");
                                standardModel.get(position).setCheckedStatus("0");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return convertView;
        }

        public List<LeaveFinalArray> getDatas() {
            return standardModel;
        }

        public ArrayList<String> getCheckedStandards() {
            checkedItemsIds = new ArrayList<String>();
            if (standardModel != null) {
                if (standardModel.size() > 0) {
                    for (int count = 0; count < standardModel.size(); count++) {
                        if (standardModel.get(count).getCheckedStatus().equalsIgnoreCase("1")) {
                            checkedItemsIds.add(String.valueOf(standardModel.get(count).getClassID()));
                        }
                    }
                }
            }
            return checkedItemsIds;
        }

        public void disableSelection() {
            try {
                ViewHolder viewHolder = new ViewHolder();

                if (viewHolder != null) {
                    viewHolder.check_standard.setEnabled(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void enableSelection() {
            try {
                ViewHolder viewHolder = new ViewHolder();

                if (viewHolder != null) {
                    viewHolder.check_standard.setEnabled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private class ViewHolder {
            CheckBox check_standard;
        }
    }
}
