package com.applicationcourse.mobile.graphmaster.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.applicationcourse.mobile.graphmaster.Util.FileUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiping Xiao on 2016-02-21.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Makes a singleton Database
    private static DatabaseHandler databaseHandler = null;
    private static Context mContext = null;
    private String TAG = "Database Handler";

    // Database Directories
    private static String DB_PATH = null;
    private static String BACKUP_DB_DIR = null;
    private static final int DATABASE_VERSION = 1;

    // Define Database Parameters
    private static final String DATABASE_NAME = "GraphMaster.db";
    /////////////////////////////////////////////////////////////////////////////
    /////                   Question Table
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////Main Question Table//////////////////////////////////////
    public static final String TABLE_ENTRIES = "MainQuestion"; // Create graph question table
    public static final String COLUMN_MAIN_ID = "mainId";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_FUNCTION = "function";

    ///////////////////////SubQuestion Table////////////////////////////////////
    public static final String TABLE_NAME = "Subquestion";
    public static final String COLUMN_SUB_ID = "subId";
    public static final String COLUMN_SUB_FUNCTION = "function";
    public static final String COLUMN_SUB_TYPE = "type";
    public static final String COLUMN_SUBQUESTION = "subquestion";

    ////////////////////////Option Table//////////////////////////////////////////
    public static final String TABLE_OPTION_NAME = "option";
    public static final String COLUMN_MAINQ_ID = "mainID";
    public static final String COLUMN_SUBQ_ID = "subID";
    public static final String COLUMN_OPTION_ID = "optionID";
    public static final String COLUMN_OPTION_VALUE = "optionValue";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_EXPLAIN = "explanation";

    /////////////////////////Heading Table//////////////////////////////////////////
    public static final String TABLE_HEADING_NAME = "heading";
    public static final String COLUMN_HEADING_ID = "headingId";
    public static final String COLUMN_MAINH_ID = "mainId";
    public static final String COLUMN_HEADING = "heading";

    /////////////////////////Table Heading data/////////////////////////////////////////////
    public static final String TABLE_HDATA_NAME = "headingValue";
    public static final String COLUMN_MAIND_ID = "mainId";
    public static final String COLUMN_HEADINGD_ID = "headingId";
    public static final String COLUMN_ORDER = "ordering";
    public static final String COLUMN_DATA = "data";

    // Database creation sql statement
    private static final String DATABASE_MAIN_CREATE = "CREATE TABLE "
            + TABLE_ENTRIES
            + "("
            + COLUMN_MAIN_ID + " integer primary key autoincrement,"
            + COLUMN_QUESTION + " text not null,"
            + COLUMN_TYPE + " text not null,"
            + COLUMN_FUNCTION + " text not null"
            + ");";

    private static final String DATABASE_SUB_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + COLUMN_SUB_ID + " integer primary key autoincrement ,"
            + COLUMN_SUB_FUNCTION + " text not null,"
            + COLUMN_SUB_TYPE + " text not null,"
            + COLUMN_SUBQUESTION + " text not null"
            + ");";

    private static final String DATABASE_OPTION_CREATE = "CREATE TABLE "
            + TABLE_OPTION_NAME
            + "("
            + COLUMN_MAINQ_ID + " integer not null,"
            + COLUMN_SUBQ_ID + " integer not null,"
            + COLUMN_OPTION_ID + " integer primary key autoincrement,"
            + COLUMN_OPTION_VALUE + " text not null,"
            + COLUMN_ANSWER + " text not null,"
            + COLUMN_EXPLAIN + " text not null"
            + ");";

    private static final String DATABASE_HEADING_CREATE = "CREATE TABLE "
            + TABLE_HEADING_NAME
            + "("
            + COLUMN_HEADING_ID + " integer primary key autoincrement,"
            + COLUMN_MAINH_ID + " integer not null,"
            + COLUMN_HEADING + " text not null"
            + ");";

    private static final String DATABASE_DATA_CREATE = "CREATE TABLE "
            + TABLE_HDATA_NAME
            + "("
            + COLUMN_MAIND_ID + " integer not null,"
            + COLUMN_HEADINGD_ID + " integer not null,"
            + COLUMN_ORDER + " integer not null,"
            + COLUMN_DATA + " text not null"
            + ");";
    ////////////////////////////////////////////////////////////////////////////////
    ///////                 Create the Graph Finish
    ///////////////////////////////////////////////////////////////////////////////


    /* Sets the static function helpers for the initialization of the database */
    // Private constructor to prevent other contexts from creating a new instance
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        databaseHandler = this;
    }

    // Public function for other contexts to get access to database
    public static DatabaseHandler getHandler(Context context) {
        if (databaseHandler == null) {
            return initHandler(context);
        }
        return databaseHandler;
    }

    // Call this to initialize the database
    public static DatabaseHandler initHandler(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);

            // Store the path of the backup databases so it is easy to access
            BACKUP_DB_DIR = "/data/data/" + mContext.getPackageName() + "/databases/backup/";
            File backupDir = new File(BACKUP_DB_DIR);
            if (!backupDir.exists()) {
                backupDir.mkdir();
            }

            // Store the path of the database so it is easy to access
            SQLiteDatabase db = databaseHandler.getReadableDatabase();
            DB_PATH = db.getPath();
            db.close();
        }
        return databaseHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Added now so that data is not repeated.  Remove later
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HEADING_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HDATA_NAME);

        database.execSQL(DATABASE_MAIN_CREATE);
        database.execSQL(DATABASE_SUB_CREATE);
        database.execSQL(DATABASE_OPTION_CREATE);
        database.execSQL(DATABASE_HEADING_CREATE);
        database.execSQL(DATABASE_DATA_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEADING_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HDATA_NAME);

        onCreate(db);
    }

    // This deletes a database, but also sets up an empty database after
    public static  void deleteDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
        databaseHandler = null;
        DatabaseHandler.initHandler(mContext);
        //Log.d(TAG, "delete Database --> delete and reinitialization Sucessful");
    }

    // Use this to set the Backup storage location of the backups - static since before initialized
    public static void setBACKUP_DB_DIR(String path) {
        // Only do this before the database has been initialized
        if (databaseHandler == null) {
            BACKUP_DB_DIR = path;
        }
    }

    // Gets the path of the database
    public static String getDB_PATH() {
        return DB_PATH;
    }

    // Export Database
    public void exportDatabase(String name) throws IOException {
        // Closes all database connections to commit cache changes to memory
        databaseHandler.close();

        // Move the file over, but deletes using own method to avoid database link breaking
        String outFileName = BACKUP_DB_DIR + name + ".db";
        FileUtility.copyFile(DB_PATH, outFileName);
        Log.d(TAG, "Export Database --> Copy Sucessful");
        deleteDatabase();
    }

    // OverWrites existing Database with chosen one! --> will lose current DB info
    public void importDatabase(String name) throws IOException {
        // Closes all database connections to commit to mem
        databaseHandler.close();
        // Determines paths
        String inFileName = "";
        if (name.contains(".db")) {
            inFileName = BACKUP_DB_DIR + name;
        } else {
            inFileName = BACKUP_DB_DIR + name + ".db";
        }
        // Copy and log success
        FileUtility.copyFile(inFileName, DB_PATH);
        Log.d(TAG, "Import Database succeeded");
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Add Data into Database////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Adding new Question Table Entry Value to database
    public void addQuestionValue(MainQues mainQues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, mainQues.getQuestion());
        values.put(COLUMN_TYPE, mainQues.getType());
        values.put(COLUMN_FUNCTION, mainQues.getFunction());

        // Inserting into database
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
    }

    ////////////////////////////////SUBQUESTION//////////////////////////////////////////////////
    //Adding a new Subquestion Value into database
    public void addSubQValue(SubQuestion subQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBQUESTION, subQuestion.getSubQuestion());
        values.put(COLUMN_SUB_FUNCTION, subQuestion.getFunction());
        values.put(COLUMN_SUB_TYPE, subQuestion.getType());
        // Inserting into database
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //////////////////////////////////OPTION///////////////////////////////////////////////////
    //Adding a OPTION into database
    public void addOption(Options options1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAINQ_ID, options1.getMqId());
        values.put(COLUMN_SUBQ_ID, options1.getSubQuesId());
        values.put(COLUMN_OPTION_VALUE, options1.getOptionValue());
        values.put(COLUMN_ANSWER, options1.getAnswer());
        values.put(COLUMN_EXPLAIN, options1.getExplanation());


        // Inserting into database
        db.insert(TABLE_OPTION_NAME, null, values);
        db.close();
    }

    //////////////////////////////////HEADING////////////////////////////////////////////////
    //Add a Heading value into database
    public void addHeading(MainQuesHeading heading) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAINH_ID, heading.getMqId());
        values.put(COLUMN_HEADING, heading.getHeading());

        // Inserting into database
        db.insert(TABLE_HEADING_NAME, null, values);
        db.close();
    }

    ///////////////////////////////////DATA///////////////////////////////////////////////////
    //Add a Data into database
    public void addHData(MainQuesHData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAIND_ID, data.getMqId());
        values.put(COLUMN_HEADINGD_ID, data.gethId());
        values.put(COLUMN_ORDER, data.getOrdering());
        values.put(COLUMN_DATA, data.getData());

        // Inserting into database
        db.insert(TABLE_HDATA_NAME, null, values);
        db.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Get Data from Database///////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getting single Question value
    public MainQues getQuestionValue(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_ENTRIES, new String[]{
                COLUMN_MAIN_ID,
                COLUMN_QUESTION,
                COLUMN_TYPE,
                COLUMN_FUNCTION
        }
                , COLUMN_MAIN_ID + "=?", new String[]{
                String.valueOf(id)
        }
                , null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        MainQues entry = new MainQues(
                Long.parseLong(cursor.getString(0)),    // ID
                cursor.getString(1),                    // Question
                cursor.getString(2),                    // Type
                cursor.getString(3)                    // Function

        );

        db.close();
        return entry;
    }
    //Get list of all Main Questions under a function and bar type
    public static ArrayList<MainQues> getAllMainQVal( String function, String type) {
        ArrayList<MainQues> mainQuesArrayList = new ArrayList<MainQues>();

        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        Cursor cursor = db.query(TABLE_ENTRIES, new String[]{
                COLUMN_MAIN_ID,
                COLUMN_QUESTION,
                COLUMN_TYPE,
                COLUMN_FUNCTION
        }
                , COLUMN_TYPE + "=? AND "+ COLUMN_FUNCTION + "=?", new String[]{
                String.valueOf(type),String.valueOf(function)
        }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MainQues mainQues = new MainQues(
                        Integer.parseInt(cursor.getString(0)),    // ID
                        cursor.getString(1),                    // question
                        cursor.getString(2),                    // type
                        cursor.getString(3)                     // function
                );
                // Adding contact to list
                mainQuesArrayList.add(mainQues);
            } while (cursor.moveToNext());
        }

        db.close();
        // return main ques list
        return mainQuesArrayList;
    }



    /////////////////////////////////////////SUBQUESTION//////////////////////////////////////////////////////////
    // Getting single sub Question value
    public SubQuestion getSubQValue(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        Cursor cursor = db.query(TABLE_NAME, new String[]{
                COLUMN_SUB_ID,
                COLUMN_SUB_FUNCTION,
                COLUMN_SUB_TYPE,
                COLUMN_SUBQUESTION
        }
                , COLUMN_SUB_ID + "=?", new String[]{
                String.valueOf(id)
        }
                , null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        SubQuestion entry = new SubQuestion(
                Long.parseLong(cursor.getString(0)),    // ID
                cursor.getString(1),                    // Type
                cursor.getString(2),                    //Function
                cursor.getString(3)                     //subquestion
        );

        db.close();
        return entry;
    }

    // Getting  sub Question list where function and type mathc the main question
    public static List<SubQuestion> getSubQValueList(String function, String type) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<SubQuestion> subList = new ArrayList<SubQuestion>();;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUB_FUNCTION + " =? AND " + COLUMN_SUB_TYPE + " =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{function, type});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            SubQuestion entry = new SubQuestion(
                    Long.parseLong(cursor.getString(0)),    // ID
                    cursor.getString(1),                    // Type
                    cursor.getString(2),                    //Function
                    cursor.getString(3)                     //subquestion
            );

            subList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return subList;
    }

    ////////////////////////////////////////////////OPTION//////////////////////////////////////////////////////////////
    // Getting single  option value
    public Options getOption(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_OPTION_NAME, new String[]{
                COLUMN_MAINQ_ID,
                COLUMN_SUBQ_ID,
                COLUMN_OPTION_ID,
                COLUMN_OPTION_VALUE,
                COLUMN_ANSWER,
                COLUMN_EXPLAIN
        }
                , COLUMN_OPTION_ID + "=?", new String[]{
                String.valueOf(id)
        }
                , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Options entry = new Options(
                Long.parseLong(cursor.getString(0)),    // MainID
                Long.parseLong(cursor.getString(1)),    //SubID
                Long.parseLong(cursor.getString(2)),    //OptionID
                cursor.getString(3),                    // Option value
                cursor.getString(4),                     // Answer
                cursor.getString(5)                    // explanation
        );

        db.close();
        return entry;
    }

    //Get the option list for specific mainID and sunID
    public static List<Options> getOptionList(long mainID, long subID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<Options> optionList = new ArrayList<Options>();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND " + COLUMN_SUBQ_ID + " =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(subID)});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            Options entry = new Options(
                    Long.parseLong(cursor.getString(0)),    // MainID
                    Long.parseLong(cursor.getString(1)),    //SubID
                    Long.parseLong(cursor.getString(2)),    //OptionID
                    cursor.getString(3),                    // Option value
                    cursor.getString(4),                     // Answer
                    cursor.getString(5)                    // explanation
            );

            optionList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return optionList;
    }

    //Get the answers for mainID and sunID
    public static List<String> getAnswerList(long mainID, long subID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<String> answerList = new ArrayList<String>();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT optionValue FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND " + COLUMN_SUBQ_ID + " =? AND "+COLUMN_ANSWER+" =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(subID),"T"});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            answerList.add(cursor.getString(0));//Option Value that's correct
            cursor.moveToNext();
        }

        db.close();
        return answerList;
    }
    //Get the explanation for mainID and sunID
    public static List<String> getExplanationList(long mainID, long subID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<String> answerList = new ArrayList<String>();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT explanation FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND " + COLUMN_SUBQ_ID + " =? AND "+COLUMN_ANSWER+" =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(subID),"F"});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            answerList.add(cursor.getString(0));//answer
            cursor.moveToNext();
        }

        db.close();
        return answerList;
    }

    ////////////////////////////////////////////////////////////Heading/////////////////////////////////////////////////////////////////////////////
    //Get single heading row value from db
    public MainQuesHeading getHeading(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_HEADING_NAME, new String[]{
                COLUMN_HEADING_ID,
                COLUMN_MAINH_ID,
                COLUMN_HEADING
        }
                , COLUMN_HEADING_ID + "=?", new String[]{
                String.valueOf(id)
        }
                , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MainQuesHeading entry = new MainQuesHeading(
                Long.parseLong(cursor.getString(0)),    // HeadingID
                Long.parseLong(cursor.getString(1)),    //MainID
                cursor.getString(2)                    // Heading
        );

        db.close();
        return entry;
    }

    //Get the Heading list for specific mainID
    public static List<MainQuesHeading> getHeadingList(long mainID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<MainQuesHeading> headingList = new ArrayList<MainQuesHeading>();;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_HEADING_NAME + " WHERE " + COLUMN_MAINH_ID + " =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID)});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            MainQuesHeading entry = new MainQuesHeading(
                    Long.parseLong(cursor.getString(0)),    // HeadingID
                    Long.parseLong(cursor.getString(1)),    //MainID
                    cursor.getString(2)                    // Heading
            );

            headingList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return headingList;
    }

    //////////////////////////////////////////Table Data/////////////////////////////////////////////////////////
    //Get single Data row value from db
    public MainQuesHData getHData(int order) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_HDATA_NAME, new String[]{
                COLUMN_MAIND_ID,
                COLUMN_HEADINGD_ID,
                COLUMN_ORDER,
                COLUMN_DATA
        }
                , COLUMN_ORDER + "=?", new String[]{
                String.valueOf(order)
        }
                , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MainQuesHData entry = new MainQuesHData(
                Long.parseLong(cursor.getString(0)),    // MainID
                Long.parseLong(cursor.getString(1)),    //HeadingID
                Long.parseLong(cursor.getString(2)),    // order
                cursor.getString(3)                     //Data
        );

        db.close();
        return entry;
    }

    //Get the Data list for specific mainID and Heading ID
    public static List<MainQuesHData> getHDataList(long mainID, long HeadingID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<MainQuesHData> dataList = new ArrayList<MainQuesHData>();;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_HDATA_NAME + " WHERE " + COLUMN_MAIND_ID + " =? AND " + COLUMN_HEADINGD_ID + " =? ORDER BY "+COLUMN_ORDER+" ASC";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(HeadingID)});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            MainQuesHData entry = new MainQuesHData(
                    Long.parseLong(cursor.getString(0)),    // MainID
                    Long.parseLong(cursor.getString(1)),    //HeadingID
                    Long.parseLong(cursor.getString(2)),    // order
                    cursor.getString(3)                     //Data
            );


            dataList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return dataList;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getting All Options
    /*
    public ArrayList<CGQuestion> getAllValues() {
        ArrayList<CGQuestion> entryList = new ArrayList<CGQuestion>();

        SQLiteDatabase db = this.getWritableDatabase();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_ENTRIES, new String[] {
                COLUMN_ID,
                COLUMN_QUESTION,
                COLUMN_TYPE,
                //COLUMN_TABLE_PATH,
                COLUMN_SUBQUESTION_ID
        }
                , null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //change cursor.getString(4) into int[]
                String subIDString = cursor.getString(4);

                String[] str =subIDString.split(",");
                int[] subIdInt = new int[str.length];
                //loop
                for (int i = 0;i<subIdInt.length; i++){
                    subIdInt[i] = Integer.parseInt(str[i]);
                }
                CGQuestion tableEntry = new CGQuestion(
                        Long.parseLong(cursor.getString(0)),    // ID
                        cursor.getString(1),                    // Question
                        cursor.getString(2),                    // Type
                        cursor.getString(3),                    // table
                        subIdInt                                //subquestion ID
                );
                // Adding contact to list
                entryList.add(tableEntry);
            } while (cursor.moveToNext());
        }

        db.close();
        // return contact list
        return entryList;
    }
  */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////Count///////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getting Question Count
    public int getQuestionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Getting SubQuestion Count of specific type and function
    public int getSubQCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUB_FUNCTION + " =? AND " + COLUMN_SUB_TYPE + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Getting option Count
    public int getOptionCount() {
        String countQuery = "SELECT * FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND " + COLUMN_SUBQ_ID + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Getting Heading Count
    public int getHeadingCount() {
        String countQuery = "SELECT * FROM " + TABLE_HEADING_NAME + " WHERE " + COLUMN_MAINH_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Getting Data Count
    public int getDataCount() {
        String countQuery = "SELECT * FROM " + TABLE_HDATA_NAME + " WHERE " + COLUMN_MAIND_ID + " =? AND " + COLUMN_HEADINGD_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////UPDATE///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Updating single Question
    public int updateQuestion(MainQues entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, entry.getQuestion());
        values.put(COLUMN_TYPE, entry.getType());
        values.put(COLUMN_FUNCTION, entry.getFunction());

        // updating row
        int result = db.update(TABLE_ENTRIES, values, COLUMN_MAIN_ID + " = ?",
                new String[]{String.valueOf(entry.getMqId())});

        db.close();
        return result;
    }

    // Updating single SubQuestion according to subquestion id
    public int updateSubQ(SubQuestion entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_SUBQUESTION, entry.getSubQuestion());
        values.put(COLUMN_SUB_TYPE, entry.getType());
        values.put(COLUMN_SUB_FUNCTION, entry.getFunction());

        // updating row
        int result = db.update(TABLE_NAME, values, COLUMN_SUB_ID + " = ?",
                new String[]{String.valueOf(entry.getSubQuesId())});

        db.close();
        return result;
    }

    // Updating single OPTION value
    public int updateOption(Options entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAINQ_ID, entry.getMqId());
        values.put(COLUMN_SUBQ_ID, entry.getSubQuesId());
        values.put(COLUMN_OPTION_VALUE, entry.getOptionValue());
        values.put(COLUMN_ANSWER, entry.getAnswer());
        values.put(COLUMN_EXPLAIN, entry.getExplanation());

        // updating row
        int result = db.update(TABLE_OPTION_NAME, values, COLUMN_OPTION_ID + " = ?",
                new String[]{String.valueOf(entry.getOptionId())});

        db.close();
        return result;
    }

    //update single heading value
    public int updateHeading(MainQuesHeading entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAINH_ID, entry.getMqId());
        values.put(COLUMN_HEADING, entry.getHeading());

        // updating row
        int result = db.update(TABLE_HEADING_NAME, values, COLUMN_HEADING_ID + " = ?",
                new String[]{String.valueOf(entry.gethId())});

        db.close();
        return result;
    }

    //update single data value
    public int updateData(MainQuesHData entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MAIND_ID, entry.getMqId());
        values.put(COLUMN_HEADINGD_ID, entry.gethId());
        values.put(COLUMN_DATA, entry.getData());

        // updating row
        int result = db.update(TABLE_HDATA_NAME, values, COLUMN_ORDER + " = ?",
                new String[]{String.valueOf(entry.getOrdering())});

        db.close();
        return result;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////Delete/////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Deleting single Main Question
    public void deleteValue(MainQues entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRIES, COLUMN_MAIN_ID + " = ?",
                new String[]{String.valueOf(entry.getMqId())});
        //IF WE DELETE A MAIN QUESTION ,WE DO NOT NEED TO DELETE THE SUBQUESTION
        db.delete(TABLE_OPTION_NAME, COLUMN_MAINQ_ID + " = ? ", new String[]{String.valueOf(entry.getMqId())});
        db.delete(TABLE_HEADING_NAME, COLUMN_MAINH_ID + " = ? ", new String[]{String.valueOf(entry.getMqId())});
        db.delete(TABLE_HDATA_NAME, COLUMN_MAIND_ID + " = ? ", new String[]{String.valueOf(entry.getMqId())});
        db.close();
    }

}
