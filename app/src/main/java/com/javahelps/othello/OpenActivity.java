package com.javahelps.othello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpenActivity extends AppCompatActivity {

    TextView white ,black ;
    Button start ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        white = (TextView)findViewById(R.id.whitePlayer);
        black = (TextView)findViewById(R.id.blackPlayer);
        start = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OpenActivity.this , MainActivity.class);

                Bundle b = new Bundle();
                b.putString("White" , white.getText().toString());
                b.putString("Black" , black.getText().toString());
                intent.putExtras(b);
                startActivity(intent);


            }
        });
    }
}
