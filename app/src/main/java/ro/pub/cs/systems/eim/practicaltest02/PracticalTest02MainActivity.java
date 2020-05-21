package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText serverPort, clientPort, serverIP, ora, minut;
    TextView info;
    Button serverStart, set, reset, poll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPort = findViewById(R.id.editText);
        clientPort = findViewById(R.id.editText2);
        serverIP = findViewById(R.id.editText3);
        ora = findViewById(R.id.editText4);
        minut = findViewById(R.id.editText5);

        info = findViewById(R.id.textView);

        serverStart = findViewById(R.id.button);
        set = findViewById(R.id.button2);
        reset = findViewById(R.id.button3);
        poll = findViewById(R.id.button4);

        serverStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerThread st = new ServerThread(Integer.parseInt(serverPort.getText().toString()));
                st.start();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientTask ct = new ClientTask(Integer.parseInt(clientPort.getText().toString()), serverIP.getText().toString(), ora.getText().toString(), minut.getText().toString(), info, "set");
                ct.execute();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientTask ct = new ClientTask(Integer.parseInt(clientPort.getText().toString()), serverIP.getText().toString(), ora.getText().toString(), minut.getText().toString(), info, "reset");
                ct.execute();
            }
        });

        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientTask ct = new ClientTask(Integer.parseInt(clientPort.getText().toString()), serverIP.getText().toString(), ora.getText().toString(), minut.getText().toString(), info, "poll");
                ct.execute();
            }
        });

    }
}
