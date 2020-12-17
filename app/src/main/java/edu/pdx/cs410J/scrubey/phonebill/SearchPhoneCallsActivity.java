package edu.pdx.cs410J.scrubey.phonebill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.*;
import android.widget.*;

public class SearchPhoneCallsActivity extends AppCompatActivity {
    EditText custBox;
    EditText startDateBox;
    EditText startTimeBox;
    EditText startSuffixBox;
    EditText endDateBox;
    EditText endTimeBox;
    EditText endSuffixBox;
    Button submit;
    PhoneBillCollection pBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_phonecalls);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        custBox = findViewById(R.id.customerTextBox);
        startDateBox = findViewById(R.id.startDateTextBox);
        startTimeBox = findViewById(R.id.startTimeTextBox);
        startSuffixBox = findViewById(R.id.startSuffixTextBox);
        endDateBox = findViewById(R.id.endDateTextBox);
        endTimeBox = findViewById(R.id.endTimeTextBox);
        endSuffixBox = findViewById(R.id.endSuffixTextBox);
        submit = findViewById(R.id.submit_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        pBills = (PhoneBillCollection) getIntent().getSerializableExtra("PhoneBillCollection");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle EditText errors
                boolean custOK = ErrorChecker.custChk(custBox);
                boolean startDateOK = ErrorChecker.dateChk(startDateBox);
                boolean startTimeOK = ErrorChecker.timeChk(startTimeBox);
                boolean startSuffixOK = ErrorChecker.suffixChk(startSuffixBox);
                boolean endDateOK = ErrorChecker.dateChk(endDateBox);
                boolean endTimeOK = ErrorChecker.timeChk(endTimeBox);
                boolean endSuffixOK = ErrorChecker.suffixChk(endSuffixBox);

                boolean allChksPass = custOK && startDateOK && startTimeOK &&
                        startSuffixOK && endDateOK && endTimeOK && endSuffixOK;

                if(allChksPass) {
                    Context context = view.getContext();
                    String customer = custBox.getText().toString();
                    String startDate = startDateBox.getText().toString();
                    String startTime = startTimeBox.getText().toString();
                    String startSuffix = startSuffixBox.getText().toString();
                    String endDate = endDateBox.getText().toString();
                    String endTime = endTimeBox.getText().toString();
                    String endSuffix = endSuffixBox.getText().toString();

                    //combine date/time info into single start and end strings
                    String start = startDate + " " + startTime + " " + startSuffix;
                    String end = endDate + " " + endTime + " " + endSuffix;

                    PhoneBill bill = pBills.searchPhoneBills(customer, start, end);

                    //if no bills under the provided customer name...
                    if (bill == null) {
                        Toast toast = Toast.makeText(context, "No phonebills under that name", Toast.LENGTH_SHORT);
                        toast.show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                    //if there is a bill under the given name, go to next activity to print it
                    else if (bill.getPhoneCalls().size() > 0) {
                        Intent intent = new Intent(context, SearchPhoneCallsToScreenActivity.class);
                        intent.putExtra("PhoneBill", bill);
                        startActivity(intent);
                    }
                    //if there are no bills that fit the search criteria, print error, return to main activity
                    else {
                        Toast toast = Toast.makeText(context, "No calls meet this criteria", Toast.LENGTH_SHORT);
                        toast.show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
