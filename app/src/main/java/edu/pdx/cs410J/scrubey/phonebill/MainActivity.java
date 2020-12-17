package edu.pdx.cs410J.scrubey.phonebill;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import java.io.*;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int MAIN_ACTIVITY_REQUEST_CODE = 43;
    static PhoneBillCollection pBills;
    Button createPhoneCall;
    Button displayPhoneBill;
    Button searchPhoneCalls;
    Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File dir = getDataDir();
        File data = new File(dir, "Phonebills.txt");
        boolean exists = data.exists();

        if(exists)
            loadPhonebills();
        else
            pBills = new PhoneBillCollection();
    }

    @Override
    public void onStart(){
        super.onStart();

        createPhoneCall = findViewById(R.id.create_new_phonecall_button);
        displayPhoneBill = findViewById(R.id.display_phonebill_button);
        searchPhoneCalls = findViewById(R.id.search_phonecalls_button);
        help = findViewById(R.id.help_button);
    }

    @Override
    public void onResume(){
        super.onResume();

        createPhoneCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, AddPhonecallActivity.class);
                intent.putExtra("PhoneBillCollection", pBills);
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });

        displayPhoneBill.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, DisplayPhoneBillActivity.class);
                intent.putExtra("PhoneBillCollection", pBills);
                context.startActivity(intent);
            }
        });

        searchPhoneCalls.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, SearchPhoneCallsActivity.class);
                intent.putExtra("PhoneBillCollection", pBills);
                context.startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, HelpActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, @Nullable Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if(reqCode == MAIN_ACTIVITY_REQUEST_CODE && resCode == RESULT_OK) {
            if (data != null && data.hasExtra("PhoneBillCollection")) {
                pBills = (PhoneBillCollection) data.getSerializableExtra("PhoneBillCollection");

                try{
                    saveResults();
                }catch(IOException e){
                    Toast.makeText(this, "While writing file " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveResults() throws IOException {
        File dir = getDataDir();
        File file = new File(dir, "Phonebills.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file), true)) {
            for(Map.Entry<String, PhoneBill> entry: pBills.getPhoneBills().entrySet()){
                PhoneBill bill = pBills.getPhoneBill(entry.getKey());
                String customer = bill.getCustomer();
                pw.println(customer);

                //iterate through each phone call, extracting and adding data to file
                for(PhoneCall call:bill.getPhoneCalls()) {
                    String caller = call.callerNumber;
                    String callee = call.calleeNumber;
                    String start = call.start;
                    String end = call.end;

                    pw.print(caller + ", " + callee + ", " + start + ", " + end + "\n");
                }
                pw.print("\n");
            }
        }
    }

    private void loadPhonebills() {
        BufferedReader reader;
        File dir = getDataDir();
        File file = new File(dir, "Phonebills.txt");

        try{
            reader = new BufferedReader(new FileReader(file));
            TextParser parser = new TextParser(reader);
            pBills = parser.parseCollection();
        }catch(IOException e){
            Toast toast = Toast.makeText(this, "Could not connect to data file", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("PhoneBillCollection", pBills);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        pBills = (PhoneBillCollection)savedInstanceState.getSerializable("PhoneBillCollection");
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