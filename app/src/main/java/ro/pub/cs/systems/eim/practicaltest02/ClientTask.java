package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTask extends AsyncTask<Void, String, Void> {

    int port;
    String ip, ora, minut, buttonType;
    TextView info;

    public ClientTask(int port, String ip, String ora, String minut, TextView info, String buttonType) {
        this.port = port;
        this.ip = ip;
        this.ora = ora;
        this.minut = minut;
        this.info = info;
        this.buttonType = buttonType;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            Socket socket = new Socket(ip, port);
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            writer.println(buttonType);
            writer.println(ora);
            writer.println(minut);

            publishProgress(reader.readLine());

            socket.close();

        } catch (Exception e) {
            Log.v("CLIENT", e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        info.append(progress[0] + " ");
    }

    @Override
    protected void onPostExecute(Void result) {
    }
    @Override
    protected  void onPreExecute() {
        info.setText("");
    }
}
