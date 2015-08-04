package org.secure.sms;

import java.util.ArrayList;
import java.util.HashMap;

import org.secure.sms.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SecureMessagesActivity extends Activity implements OnClickListener, OnItemClickListener
{
   private static String LOG_TAG = "MonsterSecureMessagesActivity";
    public void onCreate(Bundle savedInstanceState)
    {

        Log.d(LOG_TAG,"onCreate");
        super.onCreate(savedInstanceState);
        
        setTheme( android.R.style.Theme_Light );
        setContentView(R.layout.main);
        
        /**
         * You can also register your intent filter here.
         * And here is example how to do this.
         *
         * IntentFilter filter = new IntentFilter( "android.provider.Telephony.SMS_RECEIVED" );
         * filter.setPriority( IntentFilter.SYSTEM_HIGH_PRIORITY );
         * registerReceiver( new SmsReceiver(), filter );
        **/
        
        this.findViewById( R.id.UpdateList ).setOnClickListener( this );
    }

    ArrayList<String> smsList = new ArrayList<String>();
    HashMap<String, Boolean> smsCheckList =  new HashMap<String, Boolean>();
    
	public void onItemClick( AdapterView<?> parent, View view, int pos, long id ) 
	{
        Log.d(LOG_TAG,"ItemClicked pos = " + pos + "id = " + id);
        try
		{
		    String[] splitted = smsList.get( pos ).split("\n"); 
			String sender = splitted[0];

			Toast.makeText( this, "test", Toast.LENGTH_SHORT ).show();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onClick( View v )
	{

        Log.d(LOG_TAG, "onClick");
        fillSmsList();

		Log.i(LOG_TAG, "I am here");
        Cursor cursor  = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        smsList.clear();
        int indexBody = cursor.getColumnIndex(SmsReceiver.BODY);
        int indexAddr = cursor.getColumnIndex(SmsReceiver.ADDRESS);
        if(indexBody < 0 || !cursor.moveToFirst()){
            Log.i(LOG_TAG,"indexBody is " + indexBody + "Cursor is" + cursor + "so returning");
            return;
        }
        do {
            String contactNumber = cursor.getString(indexAddr);
            if(smsCheckList.get(contactNumber) == null){
                String contactName = getContactName(contactNumber);
                if(contactName.length()>0) {
                    smsCheckList.put(contactNumber, new Boolean("True"));
                }
                else{
                    contactName = contactNumber;
                }
                String str = "Sender: " + contactName + "\n" + cursor.getString( indexBody );
                smsList.add( str );
            }

        }
        while(cursor.moveToNext());

        ListView smsListView = (ListView) findViewById(R.id.SMSList);
        smsListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList));
        smsListView.setOnItemClickListener(this);



	}
    public String getContactName(final String phoneNumber)
    {
        Uri uri;
        String[] projection;
        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
        projection = new String[] { android.provider.Contacts.People.NAME };
        try {
            Class<?> c =Class.forName("android.provider.ContactsContract$PhoneLookup");
            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
            projection = new String[] { "display_name" };
        }
        catch (Exception e) {
        }


        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

        String contactName = "";

        if (cursor.moveToFirst())
        {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }

    
}