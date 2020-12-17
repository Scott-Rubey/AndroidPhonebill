package edu.pdx.cs410J.scrubey.phonebill;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.widget.*;

import java.io.PrintWriter;

public class PrintPhoneBillToScreenActivity extends AppCompatActivity {
    PhoneBill bill;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_phonebill_to_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        text = findViewById(R.id.billText);
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onResume() {
        super.onResume();
        bill = (PhoneBill) getIntent().getSerializableExtra("PhoneBill");
        PrintWriter pw = new PrintWriter(System.out, true);
        PrettyPrinter pretty = new PrettyPrinter(pw);
        text.setText(pretty.getBillText(bill));
    }
}
