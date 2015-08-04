package org.secure.sms;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.ContentResolver;

import java.util.ArrayList;

/**
 * Created by monster on 19/7/15.
 */
public class FetchFromDatabase extends AsyncQueryHandler {


    public FetchFromDatabase(ContentResolver cr) {
        super(cr);
    }
}
