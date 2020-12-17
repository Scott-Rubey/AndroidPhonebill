package edu.pdx.cs410J.scrubey.phonebill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.*;
import android.widget.*;

public class DisplayPhoneBillActivity extends AppCompatActivity {
    EditText custBox;
    Button submit;
    PhoneBillCollection pBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phonebill);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        custBox = findViewById(R.id.customerTextBox);
        submit = findViewById(R.id.submit_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        pBills = (PhoneBillCollection) getIntent().getSerializableExtra("PhoneBillCollection");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle input errors
                boolean custOK = ErrorChecker.custChk(custBox);

                if(custOK) {
                    Context context = view.getContext();
                    String customer = custBox.getText().toString();

                    PhoneBill bill = pBills.getPhoneBill(customer);
                    //if there is a bill under the given name, go to next activity to print it
                    if (bill != null) {
                        Intent intent = new Intent(context, PrintPhoneBillToScreenActivity.class);
                        intent.putExtra("PhoneBill", bill);
                        startActivity(intent);
                    }
                    //if there are no bills under that name, print error, return to main activity
                    else {
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

