package com.example.my_app;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class My_Activity extends ActionBarActivity {

	private SocketIO socket;
	public String reading = "";
	private void setupSocket(){
    	try{
    		socket = new SocketIO("http://192.168.2.4:8080/");
    		
    		socket.connect(new IOCallback() {
                @Override
                public void onMessage(JSONObject json, IOAcknowledge ack) {
                    try {
                        System.out.println("Server said:" + json.toString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onMessage(String data, IOAcknowledge ack) {
                    System.out.println("Server said: " + data);
                }

                @Override
                public void onError(SocketIOException socketIOException) {
                    System.out.println("an Error occured");
                    socketIOException.printStackTrace();
                }

                @Override
                public void onDisconnect() {
                    System.out.println("Connection terminated.");
                }

                @Override
                public void onConnect() {
                    System.out.println("Connection established");
                }
                
                
                @Override
                public void on(String event, IOAcknowledge ack, Object... args) {
                    System.out.println("Server triggered event '" + event + "'");
                    reading += args[0].toString();
                   
                }
            });

            // This line is cached until the connection is establisched.
            socket.send("Hello Server!");
    		
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
          
    
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_);
        setupSocket();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
    }
public void button1(View v){
	final EditText namefield = (EditText) findViewById(R.id.editText1);
	final TextView filetext = (TextView) findViewById(R.id.textView2);
	socket.emit("temp", '2');
	String temp = "";
	temp += namefield.getText();
	socket.emit("temp", temp);
	filetext.setText(reading);
}

public void button2(View v){
	final EditText namefield = (EditText) findViewById(R.id.editText2);
	socket.emit("temp", '3');
	String temp = "";
	temp += namefield.getText();
	socket.emit("temp", temp);
	socket.emit("temp", namefield.getText());
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_, container, false);
            return rootView;
        }
    }

}
