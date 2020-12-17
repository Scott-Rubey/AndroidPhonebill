package edu.pdx.cs410J.scrubey.phonebill;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.widget.*;

import java.io.*;

public class HelpActivity extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        text = findViewById(R.id.helpText);
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onResume() {
        super.onResume();
        text.setText(openReadMe());
    }

    /**
     * Opens readme.txt file and appends to <code>StringBuilder</code>
     * @return string containing readme.txt file
     */
    protected String openReadMe() {
        String line;
        BufferedReader reader;
        InputStream readme;
        StringBuilder sb = new StringBuilder();

        try {
            readme = getApplicationContext().getResources().openRawResource(R.raw.readme);
            reader = new BufferedReader(new InputStreamReader(readme));
            line = reader.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e){
            Toast toast = Toast.makeText(this, "Error connecting to readme.txt file", Toast.LENGTH_SHORT);
            toast.show();
        }

        return sb.toString();
    }
}
