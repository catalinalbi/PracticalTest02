package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ServerThread extends Thread {

    int port;

    public ServerThread(int port) {
        this.port = port;
    }

    HashMap<String, String> map = new HashMap<>();

    @Override
    public void run() {

        try {

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {

                Socket socket = serverSocket.accept();

                BufferedReader reader = Utilities.getReader(socket);
                PrintWriter writer = Utilities.getWriter(socket);

                String buttonType = reader.readLine();

                if (buttonType.compareTo("set") == 0) {
                    String ora = reader.readLine();
                    String minut = reader.readLine();
                    Log.v("SERVER", socket.getRemoteSocketAddress().toString().split(":")[0].substring(1) + " " + ora + " " + minut);
                    if (map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        map.remove(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));
                    }
                    map.put(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1), ora + " " + minut);
                    writer.println("Alarma setata!");
                }
                else if (buttonType.compareTo("reset") == 0) {
                    if (map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        map.remove(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));
                    }
                    writer.println("Alarma stearsa!");
                }
                else if (buttonType.compareTo("poll") == 0) {
                    Log.v("SEVER", "POLL");

                    if (!map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        writer.println("none");
                    }
                    else if (map.get(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1)).compareTo("active") == 0) {
                        writer.println("active");
                        Log.v("SERVER", "Deja activa");
                    }
                    else {
                        Socket serviceSocket = new Socket("utcnist.colorado.edu", 13);
                        BufferedReader serviceReader = Utilities.getReader(serviceSocket);

                        //Log.v("SERVER", socket.getRemoteSocketAddress().toString().split(":")[0].substring(1) + " " + map.get(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1)));

                        String time = serviceReader.readLine();

                        while (time == null || time.isEmpty()) {
                            time = serviceReader.readLine();
                        }

                        serviceSocket.close();

                        Date date = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(time.substring(5, 22));

                        String ipTime = map.get(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));

                        if (date.getHours() > Integer.parseInt(ipTime.split(" ")[0]) || (date.getHours() == Integer.parseInt(ipTime.split(" ")[0]) && date.getMinutes() > Integer.parseInt(ipTime.split(" ")[1]))) {
                            writer.println("active");
                            map.remove(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));
                            map.put(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1), "active");
                        }
                        else {
                            writer.println("inactive");
                        }
                    }

                }

                socket.close();
            }


        } catch (Exception e) {
            Log.v("SERVER", e.toString());
        }


    }

}
