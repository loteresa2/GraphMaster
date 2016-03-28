package com.applicationcourse.mobile.graphmaster.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.applicationcourse.mobile.graphmaster.R;
import com.applicationcourse.mobile.graphmaster.Util.FileUtility;

import java.io.ByteArrayOutputStream;
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
    public static final String COLUMN_GRADE = "level";

    ///////////////////////SubQuestion Table////////////////////////////////////
    public static final String TABLE_NAME = "Subquestion";
    public static final String COLUMN_SUB_ID = "subId";
    public static final String COLUMN_SUB_FUNCTION = "function";
    public static final String COLUMN_SUB_TYPE = "type";
    public static final String COLUMN_SUBQUESTION = "subquestion";
    public static final String COLUMN_OPTION_TYPE = "optionType";

    //////////////////////Grading///////////////////////////////////
    //Since its same grade for all subquestion irrespective of level , this table is created
    public static final String TABLE_NAME_MARK= "marks";
    public static final String COLUMN_SUBQ_ID_MARK = "subId";
    public static final String COLUMN_WEIGHT = "weight";

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
    public static final String COLUMN_MAINQH_ID = "mainId";
    public static final String COLUMN_HEADING_ID = "headingId";
    public static final String COLUMN_HEADING_AXIS = "headingAxis";
    public static final String COLUMN_HEADING = "heading";

    /////////////////////////Table Heading data/////////////////////////////////////////////
    public static final String TABLE_HDATA_NAME = "headingValue";
    public static final String COLUMN_MAIND_ID = "mainId";
    public static final String COLUMN_HEADINGD_ID = "headingId";
    public static final String COLUMN_ORDER = "ordering";
    public static final String COLUMN_DATA = "data";

    /////////////////////////Table STUDENT data/////////////////////////////////////////////
    public static final String TABLE_STUDENT_NAME = "student";
    public static final String COLUMN_STUDENT_ID = "studentId";
    public static final String COLUMN_STUDENT_NAME = "studentName";
    public static final String COLUMN_TEACHER_ID = "teacherId";
    public static final String COLUMN_FUNCTION_TYPE = "functionType";
    public static final String  COLUMN_LEVEL_REACHED="levelReached";
    /////////////////////////Table PROGRESS data/////////////////////////////////////////////
    public static final String TABLE_PROGRESS_NAME = "progress";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STUD_ID = "studentId";
    public static final String COLUMN_FUNCT_TYPE = "functionType";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_ATTEMPT_COUNT = "attemptCount";
    public static final String COLUMN_TIME_TAKEN = "timeTaken";
    public static final String COLUMN_NUM_WRONG = "no_of_wrong_ans";
    // Database creation sql statement
    /////////////////////////Video&text&picture/////////////////////////////////////////////
    public static final String TABLE_HELP = "help";
    public static final String COLUMN_MID = "mid";
    public static final String COLUMN_SID = "sid";
    public static final String COLUMN_TY = "type";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_IMAGE = "image";
    // Database creation sql statement

    private static final String DATABASE_HELP_CREATE = "CREATE TABLE "
            + TABLE_HELP
            + "("
            + COLUMN_MID + " integer not null,"
            + COLUMN_SID + " integer not null,"
            + COLUMN_TY + " text not null,"
            + COLUMN_VALUE + " text not null,"
            + COLUMN_IMAGE + " blob not null"
            + ");";


    private static final String DATABASE_STUDENT_CREATE = "CREATE TABLE "
            + TABLE_STUDENT_NAME
            + "("
            + COLUMN_STUDENT_ID + " integer primary key autoincrement,"
            + COLUMN_STUDENT_NAME + " string not null,"
            + COLUMN_TEACHER_ID + " integer not null,"
            + COLUMN_FUNCTION_TYPE + " text not null,"
            + COLUMN_LEVEL_REACHED + " integer not null"
            + ");";

    private static final String DATABASE_MAIN_CREATE = "CREATE TABLE "
            + TABLE_ENTRIES
            + "("
            + COLUMN_MAIN_ID + " integer primary key autoincrement,"
            + COLUMN_QUESTION + " text not null,"
            + COLUMN_TYPE + " text not null,"
            + COLUMN_FUNCTION + " text not null,"
            + COLUMN_GRADE +" text not null "
            + ");";

    private static final String DATABASE_SUB_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + COLUMN_SUB_ID + " integer primary key autoincrement ,"
            + COLUMN_SUB_FUNCTION + " text not null,"
            + COLUMN_SUB_TYPE + " text not null,"
            + COLUMN_SUBQUESTION + " text not null,"
            + COLUMN_OPTION_TYPE + " text not null"
            + ");";
    private static final String DATABASE_MARK_CREATE = " CREATE TABLE "
            + TABLE_NAME_MARK
            + "("
            + COLUMN_SUBQ_ID_MARK + " integer not null,"
            + COLUMN_WEIGHT + " integer not null"
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
            + COLUMN_MAINQH_ID + " integer not null,"
            + COLUMN_HEADING_ID + " integer primary key autoincrement,"
            + COLUMN_HEADING_AXIS + " text not null,"
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


    private static final String DATABASE_PROGRESS_CREATE = "CREATE TABLE "
            + TABLE_PROGRESS_NAME
            + "("
            + COLUMN_DATE + " text not null,"
            + COLUMN_STUD_ID + " integer not null,"
            + COLUMN_FUNCT_TYPE + " text not null,"
            + COLUMN_LEVEL + " integer not null,"
            + COLUMN_ATTEMPT_COUNT + " integer primary key autoincrement,"
            + COLUMN_TIME_TAKEN +" text not null,"
            + COLUMN_NUM_WRONG+" integer not null"
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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MARK);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HEADING_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HDATA_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP);

        database.execSQL(DATABASE_MAIN_CREATE);
        database.execSQL(DATABASE_SUB_CREATE);
        database.execSQL(DATABASE_MARK_CREATE);
        database.execSQL(DATABASE_OPTION_CREATE);
        database.execSQL(DATABASE_HEADING_CREATE);
        database.execSQL(DATABASE_DATA_CREATE);
        database.execSQL(DATABASE_PROGRESS_CREATE);
        database.execSQL(DATABASE_STUDENT_CREATE);
        database.execSQL(DATABASE_HELP_CREATE);

        String insert1 = "INSERT INTO "+TABLE_PROGRESS_NAME+" ("+COLUMN_DATE+","+COLUMN_STUD_ID+","+COLUMN_FUNCT_TYPE+","+COLUMN_LEVEL+","+COLUMN_TIME_TAKEN+","+COLUMN_NUM_WRONG+") VALUES ('4/3/2016','1','create','1','00:02:00','0')";
        database.execSQL(insert1);
        database.execSQL(insert1);
        //String insert2 = "INSERT INTO "+TABLE_PROGRESS_NAME+" ("+COLUMN_DATE+","+COLUMN_STUD_ID+","+COLUMN_FUNCT_TYPE+","+COLUMN_LEVEL+","+COLUMN_TIME_TAKEN+","+COLUMN_NUM_WRONG+") VALUES ('4/3/2016','1','create','1','3','4')";
        //database.execSQL(insert2);
        insertLevel1AndMarks(database);
        insertLevel2(database);
        insertLevel3(database);
        insertLevel4(database);
        insertHelp(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MARK);
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
        values.put(COLUMN_GRADE, mainQues.getGrade());
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
        values.put(COLUMN_OPTION_TYPE, subQuestion.getOptionType());
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
        values.put(COLUMN_MAINQH_ID, heading.getMqId());
        values.put(COLUMN_HEADING_AXIS, heading.getAxis());
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
    //Add STUDENT data into database
    public void addStudentData(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudId());
        values.put(COLUMN_STUDENT_NAME, student.getName());
        values.put(COLUMN_TEACHER_ID, student.getTeachId());
        values.put(COLUMN_FUNCTION_TYPE, student.getFunctType());
        values.put(COLUMN_LEVEL_REACHED, student.getLevelReached());

        // Inserting into database
        db.insert(TABLE_STUDENT_NAME, null, values);
        db.close();
    }

    //Add PROGRESS data into database
    public static void addProgressData(Progress progress) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, progress.getDateTime());
        values.put(COLUMN_STUD_ID, progress.getStudId());
        values.put(COLUMN_FUNCT_TYPE, progress.getFuncType());
        values.put(COLUMN_LEVEL, progress.getLevel());
        values.put(COLUMN_TIME_TAKEN, progress.getTimeTaken());
        values.put(COLUMN_NUM_WRONG, progress.getNo_wrong_ans());

        // Inserting into database
        db.insert(TABLE_PROGRESS_NAME, null, values);
        db.close();
    }

    public static List<Progress> getAllProgress(){
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<Progress> dataList = new ArrayList<Progress>();;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_PROGRESS_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            Progress entry = new Progress(
                    (cursor.getString(0)),    // Time
                    Integer.parseInt(cursor.getString(1)),    //StudentID
                    (cursor.getString(2)),    // funcType
                    Integer.parseInt(cursor.getString(3)), //Level
                    Integer.parseInt(cursor.getString(4)), //attemptcount
                    cursor.getString(5),//Time taken
                    Integer.parseInt(cursor.getString(6))//No of wrong

            );


            dataList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return dataList;
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
                COLUMN_FUNCTION,
                COLUMN_GRADE
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
                cursor.getString(3),                    // Function
                cursor.getString(4)                     //Grade
        );

        db.close();
        return entry;
    }
    //Get list of all Main Questions under a function and bar type
    public static ArrayList<MainQues> getAllMainQVal( String function, String type, int grade) {
        ArrayList<MainQues> mainQuesArrayList = new ArrayList<MainQues>();

        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        Cursor cursor = db.query(TABLE_ENTRIES, new String[]{
                COLUMN_MAIN_ID,
                COLUMN_QUESTION,
                COLUMN_TYPE,
                COLUMN_FUNCTION,
                COLUMN_GRADE
        }
                , COLUMN_TYPE + "=? AND "+ COLUMN_FUNCTION + "=? AND "+ COLUMN_GRADE + "=? ", new String[]{
                String.valueOf(type),String.valueOf(function), String.valueOf(grade)
        }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MainQues mainQues = new MainQues(
                        Integer.parseInt(cursor.getString(0)),    // ID
                        cursor.getString(1),                    // question
                        cursor.getString(2),                    // type
                        cursor.getString(3),                     // function
                        cursor.getString(4)                     //grade
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
                COLUMN_SUBQUESTION,
                COLUMN_OPTION_TYPE
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
                cursor.getString(3),                     //subquestion
                cursor.getString(4)                     //Option Type
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

        for (int i = 0; i < cursor.getCount()-1; i++) {
            SubQuestion entry = new SubQuestion(
                    Long.parseLong(cursor.getString(0)),    // ID
                    cursor.getString(1),                    // Type
                    cursor.getString(2),                    //Function
                    cursor.getString(3),                     //subquestion
                    cursor.getString(4)                     //Option type
            );

            subList.add(entry);
            cursor.moveToNext();
        }

        db.close();
        return subList;
    }
    // Getting  sub Question list where function and type mathc the main question
    public static List<SubQuestion> getSubQValueListGrade4(String function, String type) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<SubQuestion> subList = new ArrayList<SubQuestion>();;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUB_FUNCTION + " =? AND " + COLUMN_SUB_TYPE + " =? ORDER BY "+COLUMN_SUB_ID+" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(countQuery, new String[]{function, type});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            SubQuestion entry = new SubQuestion(
                    Long.parseLong(cursor.getString(0)),    // ID
                    cursor.getString(1),                    // Type
                    cursor.getString(2),                    //Function
                    cursor.getString(3),                     //subquestion
                    cursor.getString(4)                     //Option type
            );

            subList.add(entry);
            cursor.moveToNext();
        }
        countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUB_FUNCTION + " =? AND " + COLUMN_SUB_TYPE + " =?";
        cursor = db.rawQuery(countQuery, new String[]{function, type});

        if (cursor != null)
            cursor.moveToFirst();
        cursor.moveToNext(); //omit the first one
        for (int i = 0; i < cursor.getCount()-2; i++) {
            SubQuestion entry = new SubQuestion(
                    Long.parseLong(cursor.getString(0)),    // ID
                    cursor.getString(1),                    // Type
                    cursor.getString(2),                    //Function
                    cursor.getString(3),                     //subquestion
                    cursor.getString(4)                     //Option type
            );

            subList.add(entry);
            cursor.moveToNext();
        }


        db.close();
        return subList;
    }
    //Get List of marks
    public static Float[] getAllMarksList() {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();


        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT * FROM " + TABLE_NAME_MARK;
        Cursor cursor = db.rawQuery(countQuery,null);
        Float[] marksList = new Float[cursor.getCount()];
        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            marksList[i] =
                    Float.parseFloat(cursor.getString(1));               // Weight
            cursor.moveToNext();
        }

        db.close();
        return marksList;
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

    //Get the answers for mainID and sunID and option is none for exemplar
    public static List<String> getExemplar(long mainID, long subID) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<String> answerList = new ArrayList<String>();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT "+COLUMN_EXPLAIN +" FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND "
                + COLUMN_SUBQ_ID + " =? AND "
                +COLUMN_OPTION_VALUE+" =? AND "+COLUMN_ANSWER+" =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(subID),"none","T"});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            answerList.add(cursor.getString(0));//Option Value that's correct
            cursor.moveToNext();
        }

        db.close();
        return answerList;
    }
    //Get the explanation for mainID and subId and chosen option

    public static String getOptionExpl(long mainID, long subID, String optionValue) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        String optionExpl;

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters

        String countQuery = "SELECT explanation FROM " + TABLE_OPTION_NAME + " WHERE " + COLUMN_MAINQ_ID + " =? AND " + COLUMN_SUBQ_ID + " =? AND " + COLUMN_OPTION_VALUE + " =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainID), String.valueOf(subID), optionValue});

        if (cursor != null)
            cursor.moveToFirst();

        optionExpl = cursor.getString(0);

        db.close();
        return optionExpl;
    }

    ////////////////////////////////////////////////////////////Heading/////////////////////////////////////////////////////////////////////////////
    //Get single heading row value from db
    public static List<MainQuesHeading> getHeadingList(long mainQid) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<MainQuesHeading> headingList = new ArrayList<MainQuesHeading>();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        String countQuery = "SELECT * FROM " + TABLE_HEADING_NAME + " WHERE " + COLUMN_MAINQH_ID + " =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainQid)});

        if (cursor != null)
            cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            MainQuesHeading entry = new MainQuesHeading(
                    Long.parseLong(cursor.getString(0)),//mainId
                    Long.parseLong(cursor.getString(1)),//headingId
                    cursor.getString(2),             // Axis
                    cursor.getString(3)             // Heading
            );
            headingList.add(entry);
            cursor.moveToNext();
        }
        db.close();
        return headingList;
    }

    //Get single heading value for an axis
    public static String getHeadingName(long mainQid, String axis) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<MainQuesHeading> headingList = new ArrayList<MainQuesHeading>();
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        String countQuery = "SELECT "+COLUMN_HEADING+" FROM " + TABLE_HEADING_NAME + " WHERE " + COLUMN_MAINQH_ID
                + " =? AND "+COLUMN_HEADING_AXIS+" =? ";
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(mainQid), axis});

        if (cursor != null)
            cursor.moveToFirst();

        String headingName = cursor.getString(0);
        db.close();
        return headingName;
    }

    //////////////////////////////////////////Table Data/////////////////////////////////////////////////////////
    //Check if the x, y value touched within the axis is present in the heading data table
    public static List<HeadingData> getAllHeadingData(int mainId ){
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        List<HeadingData> pList = new ArrayList<HeadingData>();
        String query;
        //GETS THE VALUES ONLY FOR X AXIS FOR A PARTICULAR QUESTION
        query = "SELECT "+COLUMN_DATA+" FROM "+TABLE_HDATA_NAME+" d INNER JOIN "+TABLE_HEADING_NAME
                +" h ON ( h."+COLUMN_MAINQH_ID+" = d." +COLUMN_MAIND_ID
                +" AND h."+COLUMN_HEADINGD_ID+" = d."+COLUMN_HEADING_ID+" ) WHERE h."+COLUMN_MAIND_ID+" =? AND h."+COLUMN_HEADING_AXIS+"= 'x' ORDER BY d."+COLUMN_ORDER;

        Cursor cursor = db.rawQuery( query, new String[]{String.valueOf(mainId) });
        if(cursor != null)
            cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HeadingData p = new HeadingData();
            p.x = Float.parseFloat(cursor.getString(0));
            pList.add(p);
            cursor.moveToNext();
        }
        //GETS THE VALUES ONLY FOR Y AXIS FOR A PARTICULAR QUESTION
        query = "SELECT "+COLUMN_DATA+" FROM "+TABLE_HDATA_NAME+" d INNER JOIN "+TABLE_HEADING_NAME
                +" h ON (h."+COLUMN_MAINQH_ID+" = d." +COLUMN_MAIND_ID
                +" AND h."+COLUMN_HEADINGD_ID+" = d."+COLUMN_HEADING_ID+" ) WHERE h."+COLUMN_MAIND_ID+" =? AND h."+COLUMN_HEADING_AXIS+"= 'y' ORDER BY d."+COLUMN_ORDER;

        cursor = db.rawQuery( query, new String[]{String.valueOf(mainId) });
        if(cursor != null)
            cursor.moveToFirst();
        int j = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            HeadingData p = pList.get(j++);
            p.y = Float.parseFloat(cursor.getString(0));
            cursor.moveToNext();
        }
        return pList;
    }
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
    //Get single Data row value from db
    public Student getStudData() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        Cursor cursor = db.query(TABLE_STUDENT_NAME, new String[]{
                COLUMN_STUDENT_ID,
                COLUMN_STUDENT_NAME,
                COLUMN_TEACHER_ID,
                COLUMN_FUNCTION_TYPE,
                COLUMN_LEVEL_REACHED
        }
                , null
                , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Student entry = new Student(
                Integer.parseInt(cursor.getString(0)),    // studId
                (cursor.getString(1)),                  //studName
                Integer.parseInt(cursor.getString(2)),    // teachId
                cursor.getString(3),                  // FuncType
                Integer.parseInt(cursor.getString(4))  //Level Reached
        );

        db.close();
        return entry;
    }
    public static float getTotalScore(int mainId){
        int headingDataCount = DatabaseHandler.getAllHeadingData(mainId).size();
        int axisIntervalNo = 7;
        float totalScore=0;
        Float[] marksArray = DatabaseHandler.getAllMarksList();
        for(int i = 0; i < marksArray.length; i++){
            if(i==7){
                //Points of all x axis interval and y axis interval
                totalScore+= (marksArray[i]*axisIntervalNo*2);
                //Points that's to be plotted
                totalScore+=(marksArray[i] * headingDataCount);
            }
            else {
                totalScore += marksArray[i];
            }
        }
        return totalScore;
    }

    //Get Progress Data row value from db
    public static String getProgressResult(int studId,int mainId,String function, int level,String timeTaken,String timeThreshold) {
        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        //IF student finishes fast and no mistake
        Cursor cursor;
        String countQuery;
        float score;
        //total score
        float total = DatabaseHandler.getTotalScore(mainId);

        //TODO: count the number of attempt for each function: if count > 2 then only do below
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        countQuery = "SELECT * FROM "+TABLE_PROGRESS_NAME+" WHERE "+COLUMN_LEVEL+" =?";
        cursor = db.rawQuery(countQuery,new String[]{ String.valueOf(level)});
        if(cursor.getCount() >= 1) {
            if (timeTaken.compareTo(timeThreshold) < 0) {
              /*  countQuery = "SELECT SUM( A." + COLUMN_NUM_WRONG + ") FROM ( SELECT * FROM " + TABLE_PROGRESS_NAME
                        + " WHERE " + COLUMN_STUD_ID + " =? AND "
                        + COLUMN_FUNCT_TYPE + " =? AND " + COLUMN_LEVEL + " =? " +
                        " ORDER BY " + COLUMN_ATTEMPT_COUNT + " DESC LIMIT 2 ) A WHERE A." + COLUMN_TIME_TAKEN + " <= ?  GROUP BY A." + COLUMN_LEVEL;
               */
                countQuery = "SELECT A." + COLUMN_NUM_WRONG + " FROM ( SELECT * FROM " + TABLE_PROGRESS_NAME
                        + " WHERE " + COLUMN_STUD_ID + " =? AND "
                        + COLUMN_FUNCT_TYPE + " =? AND " + COLUMN_LEVEL + " =? " +
                        " ORDER BY " + COLUMN_ATTEMPT_COUNT + " DESC LIMIT 1 ) A WHERE A." + COLUMN_TIME_TAKEN + " <= ?  GROUP BY A." + COLUMN_LEVEL;

                cursor = db.rawQuery(countQuery, new String[]{String.valueOf(studId), function, String.valueOf(level), timeThreshold});
                if (cursor != null)
                    cursor.moveToFirst();
                score = Float.parseFloat(cursor.getString(0));
                //10% mistake
                float mediumMistake = (float)(.2 *total);
                float moreMistake = (float)(.60 *total);
                db.close();
                if (score == 0) {
                    return "promoteLevel";
                }else if((score) < mediumMistake) {
                    return "nextLevel";
                } else if((score) >= moreMistake) {
                    return "lowerLevel";
                }else{
                    return "continueSmeLevel";
                }
            }
            //If greater than time threshold and 75% mistake it will return lowerLevel, else return continue same Level
            countQuery = "SELECT " + COLUMN_NUM_WRONG + " FROM ( SELECT * FROM " + TABLE_PROGRESS_NAME
                    + " WHERE " + COLUMN_STUD_ID + " =? AND "
                    + COLUMN_FUNCT_TYPE + " =? AND " + COLUMN_LEVEL + " =? " +
                    " ORDER BY " + COLUMN_ATTEMPT_COUNT + " DESC LIMIT 1 ) WHERE " + COLUMN_TIME_TAKEN + " > ?  GROUP BY " + COLUMN_LEVEL;
            cursor = db.rawQuery(countQuery, new String[]{String.valueOf(studId), function, String.valueOf(level), timeThreshold});
            if (cursor.getCount() > 0) {
                if (cursor != null)
                    cursor.moveToFirst();
                score = Float.parseFloat(cursor.getString(0));
                float mediumMistake = (float)(.2 *total);
                float moreMistake = (float)(.60 *total);
                db.close();
                if(score < mediumMistake) {
                    return "nextLevel";
                } else if((score) > moreMistake) {
                    db.close();
                    return "lowerLevel";
                }else{
                    return "continueSmeLevel";
                }
            }
        }
        return "continueSmeLevel";
    }


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
        String countQuery = "SELECT * FROM " + TABLE_HEADING_NAME + " WHERE " + COLUMN_MAINQH_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    // Getting Data Count
    public static int getDataCount(int MainId) {
        String countQuery = "SELECT * FROM " + TABLE_HDATA_NAME + " WHERE " + COLUMN_MAIND_ID + " =? AND " + COLUMN_HEADINGD_ID + " =? ";
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(MainId),"0"});
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
        values.put(COLUMN_OPTION_TYPE, entry.getOptionType());
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
        values.put(COLUMN_MAINQH_ID, entry.getMqId());
        values.put(COLUMN_HEADING, entry.getHeading());

        // updating row
        int result = db.update(TABLE_HEADING_NAME, values, COLUMN_HEADING_AXIS + " = ?",
                new String[]{String.valueOf(entry.getMqId())});

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
        db.delete(TABLE_HEADING_NAME, COLUMN_MAINQH_ID + " = ? ", new String[]{String.valueOf(entry.getMqId())});
        db.delete(TABLE_HDATA_NAME, COLUMN_MAIND_ID + " = ? ", new String[]{String.valueOf(entry.getMqId())});
        db.close();
    }

    public void insertLevel2(SQLiteDatabase database){
        //Level 2/////////
        ////MainQ//////
        String MainQ4 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Han and Teresa hypothesize that the longer you are reading, the less pages you will read in total.  They believe that as you read, you become more tired and so slow down and read less pages.   They conduct an experiment where they measure how long they read for and the number of pages read.'," +
                "'Line'," +
                "'Create'," +
                "'2')";
        String MainQ5 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Yolanda and Kanye are debating if the amount of money something costs really does influence how much of a product is bought in the store that day.  They decide to test this by selling a simple chocolate heart, but to charge higher and higher prices for it.  Maybe if the price is really cheap people will buy more hearts or more people will be likely to buy them.  Maybe, though, if the price if really high people will think they are very exclusive and special and so more people will buy them thinking they are a luxury item." +
                "They conduct an experiment where they measure how many chocolate hearts are bought in the store in a day based on how much they charged for it.Yolanda and Kanye are debating if the amount of money something costs really does influence how much of a product is bought in the store that day.  They decide to test this by selling a simple chocolate heart, but to charge higher and higher prices for it.  Maybe if the price is really cheap people will buy more hearts or more people will be likely to buy them.  Maybe, though, if the price if really high people will think they are very exclusive and special and so more people will buy them thinking they are a luxury item." +
                "They conduct an experiment where they measure how many chocolate hearts are bought in the store in a day based on how much they charged for it.'," +
                "'Line'," +
                "'Create'," +
                "'2')";
        String MainQ6 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Tasha and Kevin wonder if the length of time you sleep at night affects how many cups of coffee you drink the next day. " +
                "They conduct an experiment where they measure the amount of sleep you’ve had in minutes the night before and how many cups of coffee you drink the next day.'," +
                "'Line'," +
                "'Create'," +
                "'2')";
        String MainQ7 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Burgundy and LaToya are wondering about the Traveller, a 30-day marathon-like, wilderness hike over desert, mountain ranges and tundra that pushes people to their limits.  Many people quit part way through it because it is so physically and mentally demanding." +
                "Burgundy and LaToya question if maybe the number of months you spend preparing for the Traveller affects if you finish or not, and if you don’t finish how many days you actually complete.They conduct an experiment to determine if there is any relationship.'," +
                "'Line'," +
                "'Create'," +
                "'2')";
        database.execSQL(MainQ4);
        database.execSQL(MainQ5);
        database.execSQL(MainQ6);
        database.execSQL(MainQ7);

        //option and explanation
        String Option411 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'1'," +
                "'Length of time reading'," +
                "'T'," +
                "'Correct!  Good job.')";
        database.execSQL(Option411);

        String Option412 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'1'," +
                "'Amount of pages read'," +
                "'F'," +
                "'Oops.  That’s incorrect.\n The length of time reading does not depend on the amount of pages read.It is the independent variable.  The number of pages read depends on the length of time you read, so it is the dependent variable. ')";
        database.execSQL(Option412);

        String Option421 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done!')";
        database.execSQL(Option421);

        String Option422 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.  The independent variable is placed on the x-axis.\nThe dependent variable is placed on the y.')";
        database.execSQL(Option422);

        String Option431 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was callen \nX Axis:" +
                "Length of Time Reading (min)\n " +
                "Y Axis:" +
                "Number of Pages Read.\n\n Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis?\n Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option431);

        String Option441 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'4'," +
                "'5'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option441);

        String Option442 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "' Oops.  That is incorrect. \n\n Look at your table and find the highest number for the independent variable/x-axis/Length of time reading.  The highest number is 30." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (30) by the number of available boxes (6).  30÷6=5.  This is your interval.')";
        database.execSQL(Option442);

        String Option451 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'5'," +
                "'2'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option451);

        String Option452 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "' Oops.  That is incorrect.\n\n  Look at your table and find the highest number for the dependent variable/y-axis/number of pages read.  The highest number is 12.Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (12) by the number of available boxes (6).  12÷6=2.  This is your interval.')";
        database.execSQL(Option452);

        String Option461 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.\nCheck your title against the one we wrote for this question:  \nGraph A: Number of Pages Read Depending on Length of Time Someone has Been Reading (min)\n" +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?\n\nIf you need to, go back and correct the title.')";
        database.execSQL(Option461);

        String Option471 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option471);

        String Option481 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'8'," +
                "'Based on the graph the longer you read the more you slow down and read less pages.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option481);

        String Option482 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'8'," +
                "'Based on the graph the length of time you read has no impact on the number of pages you read'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option482);

        String Option511 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'1'," +
                "'Cost of the chocolate heart'," +
                "'T'," +
                "'That’s correct!')";
        database.execSQL(Option511);

        String Option512 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'1'," +
                "'Number of chocolate hearts sold that day'," +
                "'F'," +
                "'Oops.  The number of chocolate hearts bought depends on the cost of the heart. \n Therefore the number of chocolate hearts bought is the dependent variable and the cost of the heart is the independent variable.')";
        database.execSQL(Option512);

        String Option521 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.\nThe dependent variable is placed on the y.')";
        database.execSQL(Option521);

        String Option522 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Yup! You are correct.')";
        database.execSQL(Option522);

        String Option531 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar:Cost of Heart ($); " +
                "Y Axis:" +
                "Exemplar:Number of Hearts Sold in 1 Day.Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option531);

        String Option541 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'4'," +
                "'2'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option541);

        String Option542 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/Cost of the heart ($).  The highest number is 12." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (12) by the number of available boxes (6).  12÷6=2.  This is your interval.')";
        database.execSQL(Option542);

        String Option551 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'5'," +
                "'10'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option551);

        String Option552 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/Number of hearts sold in 1 hour.The highest number is 60." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (60) by the number of available boxes (6).  60÷6=10.  This is your interval.')";
        database.execSQL(Option552);

        String Option561 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Number of Chocolate Hearts Purchased in One Day Depending on the Cost of the Heart ($)" +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option561);

        String Option571 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option571);

        String Option581 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'8'," +
                "'The cost has no impact on the number of hearts sold.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option581);

        String Option582 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'8'," +
                "'The cheaper the price the more hearts are sold.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option582);

        String Option583 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'8'," +
                "'It seems if the price is very low or very high people will buy lots of hearts, but if the price is moderate then little hearts are sold.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option583);

        String Option584 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'8'," +
                "'The higher the cost the more hearts are sold.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option584);

        String Option611 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'1'," +
                "'Amount of Sleep (min)'," +
                "'T'," +
                "'Great job! ')";
        database.execSQL(Option611);

        String Option612 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'1'," +
                "'Amount of Coffee Drank the Following Day (cups)'," +
                "'F'," +
                "'Oops.  The number of chocolate hearts bought depends on the cost of the heart.  Therefore the number of chocolate hearts bought is the dependent variable and the cost of the heart is the independent variable.')";
        database.execSQL(Option612);

        String Option621 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'2'," +
                "'a.The x-axis'," +
                "'F'," +
                "'Oops.  The independent variable is placed on the x-axis.The dependent variable is placed on the y.')";
        database.execSQL(Option621);

        String Option622 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'2'," +
                "'b.The y-axis'," +
                "'T'," +
                "'Correct!  Great job.')";
        database.execSQL(Option622);

        String Option631 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar:Amount of Sleep (min); " +
                "Y Axis:" +
                "Exemplar:Amount of Coffee Drank the Following Day (cups).  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option631);

        String Option641 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'4'," +
                "'100'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option641);

        String Option642 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/Amount of sleep (min).  The highest number is 600.')";
        database.execSQL(Option642);

        String Option651 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'5'," +
                "'2'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option651);

        String Option652 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/Amount of coffee drank the following day (cups).  The highest number is 12.  ')";
        database.execSQL(Option652);

        String Option661 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Amount of Coffee Drank in a Day (cups) Depending on the Amount of Sleep the Previous Night (min)." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option661);

        String Option671 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option671);

        String Option681 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'8'," +
                "'There seems to be no relationship between how much sleep someone has and how many cups of coffee they drink the next day.'," +
                "'F'," +
                "'Inorrect!')";
        database.execSQL(Option681);

        String Option682 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'8'," +
                "'The longer someone sleeps the less coffee they drink the next day'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option682);

        String Option683 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'6'," +
                "'8'," +
                "'is any sort of relationship between the amount of sleep and amount of coffee drunk the next day'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option683);

        String Option711 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'1'," +
                "'Number of days they participate'," +
                "'F'," +
                "'Oops.  The independent variable is actually the amount of pre-hike training (months).  The number of days they participate depends on the amount of pre-hike training (months), so it is the dependent variable. ')";
        database.execSQL(Option711);

        String Option712 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'1'," +
                "'Amount of pre-hike training (months)'," +
                "'T'," +
                "'Nicely done! You’re correct.')";
        database.execSQL(Option712);

        String Option721 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'2'," +
                "'a.The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option721);

        String Option722 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'2'," +
                "'b.The y-axis'," +
                "'T'," +
                "'Correct!  Great job.')";
        database.execSQL(Option722);

        String Option731 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar: Amount of Pre-Hike Training (months); " +
                "Y Axis:" +
                "Exemplar:Number of Days they Participate in the Race Before Quitting.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option731);

        String Option741 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'4'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option741);

        String Option742 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/Amount of Pre-Hike Training (months).  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval.The highest number is 600.')";
        database.execSQL(Option742);

        String Option751 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'5'," +
                "'5'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option751);

        String Option752 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/Number of Days they Participate in the Race Before Quitting.  The highest number is 30." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (30) by the number of available boxes (6).  30÷6=5.  This is your interval.')";
        database.execSQL(Option752);

        String Option761 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Effect of the Amount of Training (months) on the Number of Days which People Participate in the Traveller Before Quitting (or Finishing after 30 Days)." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option761);

        String Option771 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option771);

        String Option781 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'8'," +
                "'The amount of pre-race training in months has a direct effect on how many days someone stays in the race (the more months, the longer they stay in the race).'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option781);

        String Option782 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'8'," +
                "'There is no relationship between the amount of months training and the number of days participants stay in the race.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option782);

        String Option783 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'7'," +
                "'8'," +
                "'It’s impossible to tell if there is any relationship between the amount of pre-race training and the length of time people stay in the race.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option783);

        //table value//////////
        String heading41 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'4'," +
                "'x'," +
                "'Length of Time Reading (min)'" +
                ")";
        database.execSQL(heading41);

        String heading42 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'4'," +
                "'y'," +
                "'Number of Pages Read'" +
                ")";
        database.execSQL(heading42);

        String heading51 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'5'," +
                "'x'," +
                "'Cost of Chocolate Heart ($)'" +
                ")";
        database.execSQL(heading51);

        String heading52 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'5'," +
                "'y'," +
                "'Number of Chocolate Hearts Bought in 1 Day'" +
                ")";
        database.execSQL(heading52);

        String heading61 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'6'," +
                "'x'," +
                "'Amount of Sleep (min)'" +
                ")";
        database.execSQL(heading61);

        String heading62 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'6'," +
                "'y'," +
                "'Amount of Coffee Drank the Following Day (cups)'" +
                ")";
        database.execSQL(heading62);

        String heading71 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'7'," +
                "'x'," +
                "'Amount of Pre-Hike Training (months)'" +
                ")";
        database.execSQL(heading71);

        String heading72 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'7'," +
                "'y'," +
                "'Number of Days they Participate in the Race Before Quitting (N.B.: the race finishes after 30 days)'" +
                ")";
        database.execSQL(heading72);

        //Data
        String Data471 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'7'," +
                "'1'," +
                "'15'"+
                ")";
        database.execSQL(Data471);

        String Data472 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'7'," +
                "'2'," +
                "'20'"+
                ")";
        database.execSQL(Data472);

        String Data473 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'7'," +
                "'3'," +
                "'25'"+
                ")";
        database.execSQL(Data473);

        String Data474 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'7'," +
                "'4'," +
                "'30'"+
                ")";
        database.execSQL(Data474);

        String Data481 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'8'," +
                "'1'," +
                "'6'"+
                ")";
        database.execSQL(Data481);

        String Data482 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'8'," +
                "'2'," +
                "'8'"+
                ")";
        database.execSQL(Data482);

        String Data483 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'8'," +
                "'3'," +
                "'10'"+
                ")";
        database.execSQL(Data483);

        String Data484 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'4'," +
                "'8'," +
                "'4'," +
                "'12'"+
                ")";
        database.execSQL(Data484);

        String Data591 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'9'," +
                "'1'," +
                "'2'"+
                ")";
        database.execSQL(Data591);

        String Data592 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'9'," +
                "'2'," +
                "'4'"+
                ")";
        database.execSQL(Data592);

        String Data593 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'9'," +
                "'3'," +
                "'6'"+
                ")";
        database.execSQL(Data593);

        String Data594 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'9'," +
                "'4'," +
                "'8'"+
                ")";
        database.execSQL(Data594);

        String Data595 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'9'," +
                "'5'," +
                "'12'"+
                ")";
        database.execSQL(Data595);

        String Data5101 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'10'," +
                "'1'," +
                "'40'"+
                ")";
        database.execSQL(Data5101);

        String Data5102 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'10'," +
                "'2'," +
                "'20'"+
                ")";
        database.execSQL(Data5102);

        String Data5103 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'10'," +
                "'3'," +
                "'10'"+
                ")";
        database.execSQL(Data5103);

        String Data5104 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'10'," +
                "'4'," +
                "'20'"+
                ")";
        database.execSQL(Data5104);

        String Data5105 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'5'," +
                "'10'," +
                "'5'," +
                "'60'"+
                ")";
        database.execSQL(Data5105);

        String Data6111 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'11'," +
                "'1'," +
                "'100'"+
                ")";
        database.execSQL(Data6111);

        String Data6112 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'11'," +
                "'2'," +
                "'300'"+
                ")";
        database.execSQL(Data6112);

        String Data6113 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'11'," +
                "'3'," +
                "'400'"+
                ")";
        database.execSQL(Data6113);

        String Data6114 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'11'," +
                "'4'," +
                "'600'"+
                ")";
        database.execSQL(Data6114);

        String Data6121 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'12'," +
                "'1'," +
                "'10'"+
                ")";
        database.execSQL(Data6121);

        String Data6122 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'12'," +
                "'2'," +
                "'12'"+
                ")";
        database.execSQL(Data6122);

        String Data6123 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'12'," +
                "'3'," +
                "'4'"+
                ")";
        database.execSQL(Data6123);

        String Data6124 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'6'," +
                "'12'," +
                "'4'," +
                "'0'"+
                ")";
        database.execSQL(Data6124);

        String Data7131 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'13'," +
                "'1'," +
                "'0'"+
                ")";
        database.execSQL(Data7131);

        String Data7132 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'13'," +
                "'2'," +
                "'2'"+
                ")";
        database.execSQL(Data7132);

        String Data7133 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'13'," +
                "'3'," +
                "'5'"+
                ")";
        database.execSQL(Data7133);

        String Data7134 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'13'," +
                "'4'," +
                "'6'"+
                ")";
        database.execSQL(Data7134);

        String Data7141 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'14'," +
                "'1'," +
                "'5'"+
                ")";
        database.execSQL(Data7141);

        String Data7142 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'14'," +
                "'2'," +
                "'10'"+
                ")";
        database.execSQL(Data7142);

        String Data7143 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'14'," +
                "'3'," +
                "'25'"+
                ")";
        database.execSQL(Data7143);

        String Data7144 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'7'," +
                "'14'," +
                "'4'," +
                "'30'"+
                ")";
        database.execSQL(Data7144);

    }

    public void insertLevel1AndMarks(SQLiteDatabase database){
        //Level 1/////////
        ////MainQ//////
        String MainQ1 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'A group of students are testing how many snails can cross the finish line in one minute, depending on how many snails actually compete in the race.  They hypothesize that if there are more snails at the starting line of the race, then there will be less snails that finish by the end of a minute; they think the snails will bump into each other and get off track and the more snails there are the more likely they are to bump into each other'," +
                "'Line'," +
                "'Create'," +
                "'1')";
        String MainQ2 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Jack and Yousef are wondering if having more people in their study group means that more of the candy bars sitting in the middle of the table that the group is working at are eaten.  They use different sized groups and record how many candy bars are eaten by each group in an hour.'," +
                "'Line'," +
                "'Create'," +
                "'1')";
        String MainQ3 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Soorah and Daneshe are playing the game BrennerKale on their phones.  They wonder if the number of times that they have to repeat a level actually makes them that much faster at completing it.  They time how long it takes them to complete Level 12 the first time.  Then, when they are eventually defeated and have to start the whole game again, they time how long it takes them to complete Level 12 again. They do this for the third, fourth, fifth and sixth time that they complete Level 12.  Here is the data they recorded. '," +
                "'Line'," +
                "'Create'," +
                "'1')";
        database.execSQL(MainQ1);
        database.execSQL(MainQ2);
        database.execSQL(MainQ3);

        //subQ//////
        String SubQ1 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'For this question, which variable is the independent variable? '," +
                "'Radio')";
        String SubQ2 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Which axis does the dependent variable go on? '," +
                "'Radio')";
        String SubQ3 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Label the x and y axes. '," +
                "'LabelAxis')";
        String SubQ4 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Calculate the interval for the x-axis '," +
                "'TextBox')";
        String SubQ5 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Calculate the interval for the y-axis'," +
                "'TextBox')";
        String SubQ6 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Write your title below.'," +
                "'None')";
        String SubQ7 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Begin by numbering your axes and then place your points on the graph.'," +
                "'None')";
        String SubQ8 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'Based on the graph, choose which statement is correct '," +
                "'Radio')";
        database.execSQL(SubQ1);
        database.execSQL(SubQ2);
        database.execSQL(SubQ3);
        database.execSQL(SubQ4);
        database.execSQL(SubQ5);
        database.execSQL(SubQ6);
        database.execSQL(SubQ7);
        database.execSQL(SubQ8);

        //Marks for all subquestion
        String marks1 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'1','2')";
        String marks2 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'2','0.5')";
        String marks3 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'3','6')";
        String marks4 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'4','2')";
        String marks5 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'5','2')";
        String marks6 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'6','3')";
        String marks = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'7','6')";
        String marks7 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'7','0.5')";
        String marks8 = "INSERT INTO "+TABLE_NAME_MARK+ " ("+ COLUMN_SUBQ_ID_MARK+","+COLUMN_WEIGHT+") VALUES(" +
                "'8','2')";
        database.execSQL(marks1);
        database.execSQL(marks2);
        database.execSQL(marks3);
        database.execSQL(marks4);
        database.execSQL(marks5);
        database.execSQL(marks6);
        database.execSQL(marks7);
        database.execSQL(marks8);

        //option and explanation
        String Option111 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'1'," +
                "'Number of snails in the race'," +
                "'T'," +
                "'Correct!  Great job!')";
        database.execSQL(Option111);

        String Option112 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'1'," +
                "'Number of snails that cross the finish line in 1 minute'," +
                "'F'," +
                "'Oops.  That’s incorrect.  The number of snails that cross the finish line depends on the number of snails that start the race, so it is the dependent variable.')";
        database.execSQL(Option112);

        String Option121 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Incorrect. The x-axis is where the independent variable is located. \n The dependent variable goes on the y- axis.')";
        database.execSQL(Option121);

        String Option122 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Correct!  Great job.')";
        database.execSQL(Option122);

        String Option131 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar: Number of Snails Who Start the Race; " +
                "Y Axis:" +
                "Exemplar: Number of Snails Who Finish the Race in 1 Minute.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option131);

        String Option141 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'4'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option141);

        String Option142 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/number of snails who start the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option142);

        String Option151 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'5'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option151);

        String Option152 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of snails who finish the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option152);

        String Option161 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Number of Snails that Cross the Finish Line in 1 Minute Depending on How Many Snails Start the Race." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option161);

        String Option171 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option171);

        String Option181 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'8'," +
                "'a.The number of snails that start the race determines how many snails finish the race.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option181);

        String Option182 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'8'," +
                "'b.The number of snails that start the race does not seem to have an impact on how many snails finish the race.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option182);
        String Option211 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'1'," +
                "'a.Number of candy bars eaten by the group in 1 hour'," +
                "'F'," +
                "'Oops.  That’s incorrect.  The number of candy bars eaten by the group in 1 hour depends on how many people are in the group.  Because it depends, it is the dependent variable. ')";
        database.execSQL(Option211);

        String Option212 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'1'," +
                "'b.Number of people in the group'," +
                "'T'," +
                "'Great job!  ')";
        database.execSQL(Option212);

        String Option221 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'2'," +
                "'a.The x-axis'," +
                "'F'," +
                "'That’s incorrect. The independent variable goes on the x-axis.  The dependent variable goes on the y- axis.')";
        database.execSQL(Option221);

        String Option222 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'2'," +
                "'b.The y-axis'," +
                "'T'," +
                "'Correct!  Great job.')";
        database.execSQL(Option222);

        String Option231 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar: Number of People in the Study Group; " +
                "Y Axis:" +
                "Exemplar:Number of Candy Bars Eaten by the Group in 1 Hour.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option231);

        String Option241 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'4'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option241);

        String Option242 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/number of snails who start the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option242);

        String Option251 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'5'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option251);

        String Option252 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of snails who finish the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option252);

        String Option261 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Number of Candy Bars Eaten in 1 Hour Depending on Number of People in the Group." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option261);

        String Option271 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option271);

        String Option281 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'8'," +
                "'a.Based on the graph the number of people in the group determines how many candy bars are eaten.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option281);

        String Option282 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'2'," +
                "'8'," +
                "'b.Based on the graph the number of people in the group does not affect how many candy bars are eaten.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option282);

        String Option311 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'1'," +
                "'Number of times Level 12 was completed'," +
                "'T'," +
                "'Great job! ')";
        database.execSQL(Option311);

        String Option312 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'1'," +
                "'Length of Time Needed to Complete Level 12 (min)'," +
                "'F'," +
                "' Oops. That’s incorrect.  The length of time needed to complete level 12 depends on the number of times the level was completed. Because it depends, it is the dependent variable. ')";
        database.execSQL(Option312);

        String Option321 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'2'," +
                "'a.The x-axis'," +
                "'F'," +
                "'That’s incorrect. The independent variable goes on the x-axis.  The dependent variable goes on the y- axis.')";
        database.execSQL(Option321);

        String Option322 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'2'," +
                "'b.The y-axis'," +
                "'T'," +
                "'Correct!  Great job.')";
        database.execSQL(Option322);

        String Option331 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar:Number of Times Level 12 Was Completed; " +
                "Y Axis:" +
                "Exemplar:Length of Time (min) Needed to Complete Level 12.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option331);

        String Option341 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'4'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option341);

        String Option342 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/number of snails who start the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option342);

        String Option351 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'5'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option351);

        String Option352 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of snails who finish the race.  The highest number is 6." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (6) by the number of available boxes (6).  6÷6=1.  This is your interval. ')";
        database.execSQL(Option352);

        String Option361 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Length of Time Needed to Complete Level 12 in Brennerkale (min) Depending on the Number of Times Level 12 Has Been Completed." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option361);

        String Option371 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'7'," +
                "'None'," +
                "'T'," +
                "'Testing')";
        database.execSQL(Option371);

        String Option381 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'8'," +
                "'a.Based on the graph it appears that the more times Level 12 is completed, the faster Soorah and  Daneshe were at completing it.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option381);

        String Option382 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'3'," +
                "'8'," +
                "'b.Based on the graph it appears that there is no relationship between how many times the level was completed and how fast it took to complete it.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option382);

        //table value//////////
        String heading11 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'1'," +
                "'x'," +
                "'Number of snails in the race'" +
                ")";
        database.execSQL(heading11);

        String heading12 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'1'," +
                "'y'," +
                "'Number of snails that cross the finish line in 1 minute'" +
                ")";
        database.execSQL(heading12);

        String heading21 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'2'," +
                "'x'," +
                "'Number of people in the group'" +
                ")";
        database.execSQL(heading21);

        String heading22 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'2'," +
                "'y'," +
                "'Number of candy bars eaten by the group in 1 hour'" +
                ")";
        database.execSQL(heading22);

        String heading31 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'3'," +
                "'x'," +
                "'Number of times Level 12 was completed'" +
                ")";
        database.execSQL(heading31);

        String heading32 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'3'," +
                "'y'," +
                "'Length of Time Needed to Complete Level 12 (min)'" +
                ")";
        database.execSQL(heading32);

        //Data
        String Data111 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'1'," +
                "'1'"+
                ")";
        database.execSQL(Data111);

        String Data112 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'2'," +
                "'2'"+
                ")";
        database.execSQL(Data112);

        String Data113 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'3'," +
                "'3'"+
                ")";
        database.execSQL(Data113);

        String Data114 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'4'," +
                "'4'"+
                ")";
        database.execSQL(Data114);

        String Data115 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'5'," +
                "'5'"+
                ")";
        database.execSQL(Data115);

        String Data116 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'1'," +
                "'6'," +
                "'6'"+
                ")";
        database.execSQL(Data116);

        String Data121 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'1'," +
                "'1'"+
                ")";
        database.execSQL(Data121);

        String Data122 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'2'," +
                "'1'"+
                ")";
        database.execSQL(Data122);

        String Data123 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'3'," +
                "'2'"+
                ")";
        database.execSQL(Data123);

        String Data124 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'4'," +
                "'4'"+
                ")";
        database.execSQL(Data124);

        String Data125 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'5'," +
                "'3'"+
                ")";
        database.execSQL(Data125);

        String Data126 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'1'," +
                "'2'," +
                "'6'," +
                "'6'"+
                ")";
        database.execSQL(Data126);

        String Data231 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'3'," +
                "'1'," +
                "'2'"+
                ")";
        database.execSQL(Data231);

        String Data232 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'3'," +
                "'2'," +
                "'3'"+
                ")";
        database.execSQL(Data232);

        String Data233 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'3'," +
                "'3'," +
                "'4'"+
                ")";
        database.execSQL(Data233);

        String Data234 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'3'," +
                "'4'," +
                "'5'"+
                ")";
        database.execSQL(Data234);

        String Data235 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'3'," +
                "'5'," +
                "'6'"+
                ")";
        database.execSQL(Data235);

        String Data241 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'4'," +
                "'1'," +
                "'2'"+
                ")";
        database.execSQL(Data241);

        String Data242 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'4'," +
                "'2'," +
                "'3'"+
                ")";
        database.execSQL(Data242);

        String Data243 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'4'," +
                "'3'," +
                "'4'"+
                ")";
        database.execSQL(Data243);

        String Data244 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'4'," +
                "'4'," +
                "'6'"+
                ")";
        database.execSQL(Data244);

        String Data245 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'2'," +
                "'4'," +
                "'5'," +
                "'6'"+
                ")";
        database.execSQL(Data245);

        String Data351 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'1'," +
                "'1'"+
                ")";
        database.execSQL(Data351);

        String Data352 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'2'," +
                "'2'"+
                ")";
        database.execSQL(Data352);

        String Data353 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'3'," +
                "'3'"+
                ")";
        database.execSQL(Data353);

        String Data354 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'4'," +
                "'4'"+
                ")";
        database.execSQL(Data354);

        String Data355 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'5'," +
                "'5'"+
                ")";
        database.execSQL(Data355);

        String Data356 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'5'," +
                "'6'," +
                "'6'"+
                ")";
        database.execSQL(Data356);

        String Data361 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'1'," +
                "'6'"+
                ")";
        database.execSQL(Data361);

        String Data362 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'2'," +
                "'6'"+
                ")";
        database.execSQL(Data362);

        String Data363 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'3'," +
                "'4'"+
                ")";
        database.execSQL(Data363);

        String Data364 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'4'," +
                "'5'"+
                ")";
        database.execSQL(Data364);

        String Data365 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'5'," +
                "'4'"+
                ")";
        database.execSQL(Data365);

        String Data366 =  "INSERT INTO "+TABLE_HDATA_NAME+ " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'3'," +
                "'6'," +
                "'6'," +
                "'3'"+
                ")";
        database.execSQL(Data366);

    }
    public void insertLevel3(SQLiteDatabase database){
        ////MainQ//////
        String MainQ8 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Scientists wonder if the level of water in the local lake impacts how many elephants visit it to drink. '," +
                "'Line'," +
                "'Create'," +
                "'3')";
        String MainQ9 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Terry and Danny own a series of gyms.  Gym members are asked to wipe off the machine they use after they are done using it.  The next person doesn’t want to use a sweaty machine!  They’ve noticed, however, that lots of people don’t wipe off their machines.  They decide to put up signs around the gyms to try to encourage people to wipe off their machine.  They wonder, however, how many signs are enough to get people to actually act.  They decide to test this by putting up different amounts of signs in their gyms.  They ask their team members to track how many people wipe off their machines from 5:30 – 6:30pm.  This is the busiest time of day for the gym and almost every machine is in use, so they can assume there is a fairly even number of people in the gym at that time.'," +
                "'Line'," +
                "'Create'," +
                "'3')";
        String MainQ10 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Meghan and Hamilton are physiotherapists.  They specialize in dealing with injuries after auto-accidents, helping people to regain their mobility.  A significant number of their patients are on heavy pain medication after their accident.  The idea is to get patients off of the medication as quickly as possible, as the pain pills can be addictive and cause further problems down the road.  They want to determine if increasing the amount of time patients spend in the hot tub post physiotherapy can help decrease the amount of pain pills the patient chooses to take a day (patients take the pain pills as they feel they need them).  The test forty patients over the three days immediately following their accident. '," +
                "'Line'," +
                "'Create'," +
                "'3')";
        String MainQ11 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Students in Mr. Rose’s class are trying to build bridges using popsicle sticks.  The goal is to build the bridge that can hold the heaviest mass possible in the class. There is strong group competition to build the bridge that will hold the most!  Each group is given 38 popsicle sticks.  Mr. Rose keeps track of the number of popsicle sticks each group of students ends up using to build their final bridge and tracks how much mass that each bridge can ultimately carry, wondering if there is a relationship between the number of sticks and the mass.'," +
                "'Line'," +
                "'Create'," +
                "'3')";
        database.execSQL(MainQ8);
        database.execSQL(MainQ9);
        database.execSQL(MainQ10);
        database.execSQL(MainQ11);
        //options
        String Option811 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'1'," +
                "'Amount of Water in the Lake (cm)'," +
                "'T'," +
                "'Correct! Great job!')";
        database.execSQL(Option811);

        String Option812 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'1'," +
                "'Number of Elephants who Visit the Lake per day'," +
                "'F'," +
                "'Oops.  That’s incorrect. The amount of water in the lake does not depend on the number of elephants who visit it per day.  It is the independent variable.  The number of elephants who visit per day depends on the amount of water in the lake, so it is the dependent variable.')";
        database.execSQL(Option812);

        String Option821 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.  The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option821);

        String Option822 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option822);

        String Option831 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was:\nX-Axis:" +
                "Amount of Water in the Lake (cm)\n"+
                "Y-Axis:" +
                "Number of Elephants who Visit the Lake per Day\n" +
                ".Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option831);

        String Option841 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'4'," +
                "'3'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option841);

        String Option843 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  \nLook at your table and find the highest number for the independent variable/x-axis/Amount of water in the lake.  \nThe highest number is 18." +
                "\nNow count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "\nDivide your highest number (18) by the number of available boxes (6).  \n18÷6=3.')";
        database.execSQL(Option843);

        String Option851 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'5'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option851);

        String Option852 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  \nLook at your table and find the highest number for the dependent variable/y-axis/number of elephants who visit the lake per day.  The highest number is 6." +
                "\nNow count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "\nDivide your highest number (6) by the number of available boxes (6).  6÷6=1.')";
        database.execSQL(Option852);

        String Option861 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  \nCheck your title against the one we wrote for this question \nGraph A: Number of Elephants who Visit the Lake per Day by the Amount of Water in the Lake (cm)." +
                "\nDid your title begin “Graph A:”?" +
                "\nAre key words capitalized?  \nHave you included the units in brackets?" +
                "\nHave you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option861);

        String Option871 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'7'," +
                "'none'," +
                "'T'," +
                "'Oops!  It seems like your point doesn’t belong here.  \nRemember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the amount of water (cm) in the lake) and the y value from the dependent variable (in this case the number of elephants who visit the lake per day). \nSo your first point would be (6,4).')";
        database.execSQL(Option871);

        String Option881 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'8'," +
                "'Based on the graph the higher the water level the more elephants come to the lake'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option881);

        String Option882 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'8'," +
                "'Based on the graph the lower the water level the more elephants will come to the lake.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option882);

        String Option883 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'8'," +
                "'8'," +
                "'Based on the graph there is no relationship between how high the water level is in the lake and the number of elephants that will visit.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option883);

        //MainQ9
        String Option911 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'1'," +
                "'Number of People who Wipe off their Machine'," +
                "'F'," +
                "'Oops.  That’s incorrect. Number of signs does not depend on the number of people who wipe off their machine.  It is the independent variable.  The number of people who wipe off their machines depends on the number of signs, so it is the dependent variable.')";
        database.execSQL(Option911);

        String Option912 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'1'," +
                "'Number of Signs'," +
                "'T'," +
                "'Correct! Good job.')";
        database.execSQL(Option912);

        String Option921 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option921);

        String Option922 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option922);

        String Option931 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Number of Signs\n" +
                "Y-Axis:" +
                "Number of People who Wipe off their Machines\n.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option931);

        String Option941 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'4'," +
                "'3'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option941);

        String Option942 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/Number of Signs.  The highest number is 17.  \n" +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes.\n" +
                "Divide your highest number (17) by the number of available boxes (6).  17÷6=2.8.  Round this number up to 3.\n')";
        database.execSQL(Option942);

        String Option951 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'5'," +
                "'14'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option951);

        String Option952 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'5'," +
                "'15'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option952);

        String Option953 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of people who wipe off their machines.  The highest number is 80.  \n" +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes.\n" +
                "Divide your highest number (80) by the number of available boxes (6).  80÷6=13.33.  Round up.  You can choose either the first interval, 14, or an “easy” interval of 15. \n')";
        database.execSQL(Option953);

        String Option961 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Effect of Increased Number of Signs Asking People to Wipe off their Machines on the Number of People who Do Wipe their Machine off Measured Between 5:30-6:30pm, when Almost or All Machines are in Use." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?\n" +
                "Have you told the reader of your graph exactly what the graph is about?\n')";
        database.execSQL(Option961);

        String Option971 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'7'," +
                "'none'," +
                "'F'," +
                "'Oops!It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the number of signs) and the y value from the dependent variable (in this case the number of people who wipe off their machines).  So your first point would be (0,10).')";
        database.execSQL(Option971);

        String Option981 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'8'," +
                "'Terry and Danny should post 8 signs in the area.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option981);

        String Option982 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'8'," +
                "'Terry and Danny should post 13 signs in the area'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option982);

        String Option983 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'9'," +
                "'8'," +
                "'Terry and Danny should post 17 signs in the area'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option983);

        //MainQ10
        String Option1011 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'1'," +
                "'Amount of Pain Pills Taken per Day on Average'," +
                "'F'," +
                "'Oops.That’s incorrect. The amount of pain pills taken per day depends on the number of signs, so it is the dependent variable.')";
        database.execSQL(Option1011);

        String Option1012 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'1'," +
                "'Amount of Time in the Hot Tub After Physio (min)'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1012);

        String Option1021= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1021);

        String Option1022= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option1022);

        String Option1031= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Amount of Time in the Hot Tub After Physio (min)\n" +
                "Y-Axis:" +
                "Amount of Pain Pills Taken per Day on Average\n.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option1031);

        String Option1041= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'9'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1041);

        String Option1042 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'10'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1042);

        String Option1043 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/amount of time in the hot tub after physio (min).  The highest number is 50." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (50) by the number of available boxes (6).  50÷6=8.3.  Round this number up to 9. You could also round it up to the convenient number of 10.')";
        database.execSQL(Option1043);

        String Option1051 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'2.5'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1051);

        String Option1052 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'3'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1052);

        String Option1053 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of pain pills taken per day on average.  The highest number is 14." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (14) by the number of available boxes (6).  14÷6=2.3.  Round up.  You can choose either the first interval, 2.5, or an “easy” interval of 3.')";
        database.execSQL(Option1053);

        String Option1061= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Impact of Time Spent in Hot Tub (min) Post-Physiotherapy on the Average Number of Pain Pills Taken by Patients in the 3 Days Following an Auto-Accident as Tested on Forty Patients" +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?\n" +
                "Have you told the reader of your graph exactly what the graph is about?\n')";
        database.execSQL(Option1061);

        String Option1071 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'7'," +
                "'none'," +
                "'T'," +
                "'Oops!  It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the amount of time in the hot tub (min)) and the y value from the dependent variable (in this case the average number of pills taken by the patient in the 3 days post-accident).  So your first point would be (0,14).')";
        database.execSQL(Option1071);

        String Option1081= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'Sitting in a hot tub  after physiotherapy has no impact on the amount of pain medication a patient takes in the days following their accident'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1081);

        String Option1082 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'Meghan and Hamilton should recommend at least 40 minutes of sitting in a hot tub after physiotherapy in the three days following an accident'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option1082);

        String Option1083= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'Meghan and Hamilton should recommend sitting in a hot tub at least 20 minutes after physiotherapy in the three days following an accident'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1083);
        //MainQ11
        String Option1111 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'1'," +
                "'Mass the Bridge Holds (kg)'," +
                "'F'," +
                "'Oops.  That’s incorrect. The mass the bridge holds depends on the number of popsicle sticks used, so it is the dependent variable.')";
        database.execSQL(Option1111);

        String Option1112 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'1'," +
                "'Number of Popsicle Sticks Used'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1112);

        String Option1121= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1121);

        String Option1122= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option1122);

        String Option1131= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Number of Popsicle Sticks Used\n" +
                "Y-Axis:" +
                "Mass the Bridge Holds (kg)\n.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option1131);

        String Option1141= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'6.5'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1141);

        String Option1142 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'7'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1142);

        String Option1143 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/number of popsicles used.  The highest number is 38.  \n" +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes.\n" +
                "Divide your highest number (38) by the number of available boxes (6).  38÷6=6.3.  Round this number up to 6.5. You could also round it up to the convenient number of 7.\n')";
        database.execSQL(Option1143);

        String Option1151 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'5.5'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1151);

        String Option1152 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'6'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1152);

        String Option1153 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/mass the bridge holds.  The highest number is 31.  \n" +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes.\n" +
                "Divide your highest number (31) by the number of available boxes (6).  31÷6=5.17.  Round up.  You can choose either the first interval, 5.5, or an “easy” interval of 6.\n')";
        database.execSQL(Option1153);

        String Option1161= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Amount of Mass a Bridge Can Hold (kg) Based on the Number of Popsicle Sticks Used to Build It." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?\n" +
                "Have you told the reader of your graph exactly what the graph is about?\n')";
        database.execSQL(Option1161);

        String Option1171 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'7'," +
                "'none'," +
                "'T'," +
                "'Oops!  It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the amount of time in the hot tub (min)) and the y value from the dependent variable (in this case the average number of pills taken by the patient in the 3 days post-accident).  So your first point would be (27,22).')";
        database.execSQL(Option1171);

        String Option1181= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'There is no relationship between how many popsicle sticks are used to build the bridge and the amount of mass it can hold.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option1181);

        String Option1182 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'The more popsicle sticks used to build the bridge the more mass it can hold.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1182);

        String Option1183= "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'10'," +
                "'8'," +
                "'The less popsicle sticks used to build the bridge the more mass it can hold. '," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1183);


        //Heading
        String heading15 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'8'," +
                "'x'," +
                "'Amount of Water in Lake (cm)'" +
                ")";
        database.execSQL(heading15);

        String heading16 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'8'," +
                "'y'," +
                "'Number of Elephants who Visit the Lake per Day'" +
                ")";
        database.execSQL(heading16);

        String heading17 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'9'," +
                "'x'," +
                "'Number of Signs'" +
                ")";
        database.execSQL(heading17);

        String heading18 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'9'," +
                "'y'," +
                "'Number of People who Wipe off Their Machines'" +
                ")";
        database.execSQL(heading18);

        String heading19 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'10'," +
                "'x'," +
                "'Amount of Time in the Hot Tub After Physio (min)'" +
                ")";
        database.execSQL(heading19);

        String heading20 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'10'," +
                "'y'," +
                "'Amount of Pain Pills Taken per Day on Average'" +
                ")";
        database.execSQL(heading20);

        String heading21 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'11'," +
                "'x'," +
                "'Number of Popsicle Sticks Used'" +
                ")";
        database.execSQL(heading21);

        String heading22 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'11'," +
                "'y'," +
                "'Mass the Bridge Holds (kg)'" +
                ")";
        database.execSQL(heading22);


        //Data
        String Data8151 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'15'," +
                "'1'," +
                "'6'"+
                ")";
        database.execSQL(Data8151);

        String Data8152 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'15'," +
                "'2'," +
                "'10'"+
                ")";
        database.execSQL(Data8152);

        String Data8153 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'15'," +
                "'3'," +
                "'14'"+
                ")";
        database.execSQL(Data8153);

        String Data8154 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'15'," +
                "'4'," +
                "'18'"+
                ")";
        database.execSQL(Data8154);

        String Data8161 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'16'," +
                "'1'," +
                "'4'"+
                ")";
        database.execSQL(Data8161);

        String Data8162 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'16'," +
                "'2'," +
                "'6'"+
                ")";
        database.execSQL(Data8162);

        String Data8163 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'16'," +
                "'3'," +
                "'5'"+
                ")";
        database.execSQL(Data8163);

        String Data8164 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'8'," +
                "'16'," +
                "'4'," +
                "'6'"+
                ")";
        database.execSQL(Data8164);

        //table 2
        String Data9171 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'1'," +
                "'0'"+
                ")";
        database.execSQL(Data9171);

        String Data9172 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'2'," +
                "'2'"+
                ")";
        database.execSQL(Data9172);

        String Data9173 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'3'," +
                "'5'"+
                ")";
        database.execSQL(Data9173);

        String Data9174 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'4'," +
                "'8'"+
                ")";
        database.execSQL(Data9174);

        String Data9175 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'5'," +
                "'11'"+
                ")";
        database.execSQL(Data9175);

        String Data9176 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'6'," +
                "'13'"+
                ")";
        database.execSQL(Data9176);

        String Data9177 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'7'," +
                "'14'"+
                ")";
        database.execSQL(Data9177);

        String Data9178 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'17'," +
                "'8'," +
                "'17'"+
                ")";
        database.execSQL(Data9178);

        String Data9181 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'1'," +
                "'10'"+
                ")";
        database.execSQL(Data9181);

        String Data9182 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'2'," +
                "'12'"+
                ")";
        database.execSQL(Data9182);

        String Data9183 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'3'," +
                "'14'"+
                ")";
        database.execSQL(Data9183);

        String Data9184 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'4'," +
                "'30'"+
                ")";
        database.execSQL(Data9184);

        String Data9185 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'5'," +
                "'52'"+
                ")";
        database.execSQL(Data9185);

        String Data9186 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'6'," +
                "'77'"+
                ")";
        database.execSQL(Data9186);

        String Data9187=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'6'," +
                "'78'"+
                ")";
        database.execSQL(Data9187);

        String Data9188=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'9'," +
                "'18'," +
                "'8'," +
                "'80'"+
                ")";
        database.execSQL(Data9188);

        //table3
        String Data10191=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'1'," +
                "'0'"+
                ")";
        database.execSQL(Data10191);

        String Data10192 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'2'," +
                "'10'"+
                ")";
        database.execSQL(Data10192);

        String Data10193 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'3'," +
                "'20'"+
                ")";
        database.execSQL(Data10193);

        String Data10194 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'4'," +
                "'30'"+
                ")";
        database.execSQL(Data10194);

        String Data10195 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'5'," +
                "'40'"+
                ")";
        database.execSQL(Data10195);

        String Data10196=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'19'," +
                "'6'," +
                "'50'"+
                ")";
        database.execSQL(Data10196);

        String Data10201=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'1'," +
                "'14'"+
                ")";
        database.execSQL(Data10201);

        String Data10202=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'2'," +
                "'14'"+
                ")";
        database.execSQL(Data10202);

        String Data10203=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'3'," +
                "'8'"+
                ")";
        database.execSQL(Data10203);

        String Data10204=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'4'," +
                "'6'"+
                ")";
        database.execSQL(Data10204);

        String Data10205=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'5'," +
                "'4'"+
                ")";
        database.execSQL(Data10205);

        String Data10206=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'10'," +
                "'20'," +
                "'6'," +
                "'4'"+
                ")";
        database.execSQL(Data10206);

        //table4

        String Data11211=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'1'," +
                "'27'"+
                ")";
        database.execSQL(Data11211);

        String Data11212=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'2'," +
                "'22'"+
                ")";
        database.execSQL(Data11212);

        String Data11213=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'3'," +
                "'35'"+
                ")";
        database.execSQL(Data11213);

        String Data11214=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'4'," +
                "'30'"+
                ")";
        database.execSQL(Data11214);

        String Data11215=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'5'," +
                "'24'"+
                ")";
        database.execSQL(Data11215);

        String Data11216=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'21'," +
                "'6'," +
                "'38'"+
                ")";
        database.execSQL(Data11216);

        String Data11221=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'1'," +
                "'22'"+
                ")";
        database.execSQL(Data11221);

        String Data11222=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'2'," +
                "'31'"+
                ")";
        database.execSQL(Data11222);

        String Data11223=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'3'," +
                "'15'"+
                ")";
        database.execSQL(Data11223);

        String Data11224=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'4'," +
                "'26'"+
                ")";
        database.execSQL(Data11224);

        String Data11225=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'5'," +
                "'29'"+
                ")";
        database.execSQL(Data11225);

        String Data11226=  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'11'," +
                "'22'," +
                "'6'," +
                "'17'"+
                ")";
        database.execSQL(Data11226);

    }

    public void insertLevel4(SQLiteDatabase database){
        //MainQ
        String MainQ12 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Level 4 Amira and Echante are opening a brand new store and are trying to organize their shelves.  They want to place the most expensive products in locations where customers are most likely to see them and buy them.  They start off by placing their most expensive products right in the middle of the shelving, about 1.5 m off the ground.  Every two weeks they move the products up and down on the shelves and track how many sales of the product they have on average per day.'," +
                "'Line'," +
                "'Create'," +
                "'4')";
        String MainQ13 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Students are experimenting with building rockets in class.  Each group wants to build a rocket that will fly the highest.  In order to do this they have to make the rocket light and aerodynamic.  Their teacher, Mr. Daniel, keeps track of the mass of the rockets in grams and how high each rocket flies in meters, to demonstrate if there is a relationship between the two variables.'," +
                "'Line'," +
                "'Create'," +
                "'4')";
        String MainQ14 = "INSERT INTO "+TABLE_ENTRIES+ " ("+ COLUMN_QUESTION+","+COLUMN_TYPE+","+COLUMN_FUNCTION+","+COLUMN_GRADE +") VALUES(" +
                "'Bob and Mark are trying to set up a school store that sells fair trade chocolate candies to support a charity.  Their principal has asked them to create a business plan that explains their products and where the money will be going to.  They are trying to decide what the best price is to sell what they think will be their most popular seller, the chocolate star-shaped lollipops. They want to sell them at a price that will cause students to buy a lot of them. Realizing that they will have to do some market research they decide to spend six weeks selling them at different prices at the local hockey team’s Thursday night games, and to track how many are sold each evening and which price sold the most.  '," +
                "'Line'," +
                "'Create'," +
                "'4')";
        database.execSQL(MainQ12);
        database.execSQL(MainQ13);
        database.execSQL(MainQ14);

        //changed subquestion
        String SubQ9 = "INSERT INTO "+TABLE_NAME+ " ("+ COLUMN_SUB_FUNCTION+","+COLUMN_SUB_TYPE+","+COLUMN_SUBQUESTION+","+COLUMN_OPTION_TYPE +") VALUES(" +
                "'Create'," +
                "'Line'," +
                "'For this question, write which variable is the independent variable'," +
                "'TextBox')";
        database.execSQL(SubQ9);

        //OPTION
        String Option1291 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'9'," +
                "'none'," +
                "'T'," +
                "'The answer we had was Height of Shelves Product is Placed On (m).  Does your independent variable look similar?  If not, take a minute to review independent and dependent variables and correct your answer. ')";
        database.execSQL(Option1291);

        String Option1221 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option1221);

        String Option1222 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1222);

        String Option1231 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Height of Shelves Product is Placed On (m)\n" +
                "Y-Axis:" +
                "Number of Product Sold Per Day on Average\n.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option1231);

        String Option1241 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'4'," +
                "'0.6'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option1241);

        String Option1242 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'4'," +
                "'1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1242);

        String Option1243 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/height of shelves product is placed on (m). The highest number is 3.5." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (3.5) by the number of available boxes (6).  3.5÷6=0.583.  Round this number up to 0.6. You could also round it up to the convenient number of 1.')";
        database.execSQL(Option1243);

        String Option1251 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'5'," +
                "'88'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1251);

        String Option1252 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'5'," +
                "'90'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option1252);

        String Option1253 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of product sold per day on average.  The highest number is 523." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (523) by the number of available boxes (6).  523÷6=87.17.  Round up.  You can choose either the first interval, 88, or an “easy” interval of 90.')";
        database.execSQL(Option1253);

        String Option1261 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Number of Product Sold on Average as Measured over a Two Week Period Based on Height of Shelving (m) that the Product is Placed On." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option1261);

        String Option1271 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'7'," +
                "'none'," +
                "'F'," +
                "'Oops!  It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the height of the shelving (m)) and the y value from the dependent variable (in this case the number of product sold on average).  So your first point would be (1.5, 523).')";
        database.execSQL(Option1271);

        String Option1281 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'8'," +
                "'The height of the shelving has no impact on the number of product sold.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1281);

        String Option1282 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'8'," +
                "'Amira and Echante should put their product on shelving 3.5m high.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1282);

        String Option1283 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'8'," +
                "'Amira and Echante should put their product on shelving at 1.5 m high.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option1283);

        String Option1284 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'12'," +
                "'8'," +
                "'Amira and Echante should put their product on shelving 2 m high.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1284);

        String Option1391 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'9'," +
                "'none'," +
                "'T'," +
                "'The answer we had was Mass of the Rocket (g).  Does your independent variable look similar?  If not, take a minute to review independent and dependent variables and correct your answer. ')";
        database.execSQL(Option1391);

        String Option1321 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.  The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option1321);

        String Option1322 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done.')";
        database.execSQL(Option1322);

        String Option1331 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Mass of the Rocket (g):" +
                "Y-Axis:" +
                "Height of Rocket’s Flight (m).Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option1331);

        String Option1341 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'4'," +
                "'133'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1341);

        String Option1342 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'4'," +
                "'140'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1342);

        String Option1343 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'4'," +
                "'150'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1343);

        String Option1344 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/mass of the rocket (g). The highest number is 797." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (797) by the number of available boxes (6).  797÷6=132.83.  Round this number up to 133. You could also round it up to the convenient numbers of 140 or 150.')";
        database.execSQL(Option1344);

        String Option1351 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'5'," +
                "'5.1'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1351);

        String Option1352 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'5'," +
                "'6'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1352);

        String Option1353 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/height of rocket’s flight.  The highest number is 30.2." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (30.2) by the number of available boxes (6).  30.2÷6=5.03.  Round up.  You can choose either the first interval, 5.1, or an “easy” interval of 6.')";
        database.execSQL(Option1353);

        String Option1361 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Impact of Mass of a Rocket (g) on the Height of its Flight (m)." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option1361);

        String Option1371 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'7'," +
                "'none'," +
                "'F'," +
                "'Oops!  It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the height of the shelving (m)) and the y value from the dependent variable (in this case the number of product sold on average).  So your first point would be (520, 18.6).')";
        database.execSQL(Option1371);

        String Option1381 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'8'," +
                "'There seems to be no relationship between mass of the rocket and height of its flight'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option1381);

        String Option1382 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'13'," +
                "'8'," +
                "'The heavier the rocket the less height it attains during flight.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1382);

        //MainQ4
        String Option1491 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'9'," +
                "'none'," +
                "'T'," +
                "'The answer we had was Price of the Lollipop ($).  Does your independent variable look similar?  If not, take a minute to review independent and dependent variables and correct your answer. ')";
        database.execSQL(Option1491);

        String Option1421 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Great job!')";
        database.execSQL(Option1421);

        String Option1422 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option1422);

        String Option1431 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X-Axis:" +
                "Price of the Lollipop ($);" +
                "Y-Axis:" +
                "Number of Lollipops Sold.Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
        database.execSQL(Option1431);

        String Option1441 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'4'," +
                "'0.23'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1441);

        String Option1442 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'4'," +
                "'0.25'," +
                "'T'," +
                "'Great job! You’re correct.')";
        database.execSQL(Option1342);

        String Option1443 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'4'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/price of the lollipop. The highest number is 1.35." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (1.35) by the number of available boxes (6). 1.35÷6=0.225.  Round this number up to 0.23. You could also round it up to the convenient numbers of 0.25.')";
        database.execSQL(Option1443);

        String Option1451 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'5'," +
                "'18'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1451);

        String Option1452 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'5'," +
                "'20'," +
                "'T'," +
                "'Great job! You’re correct. ')";
        database.execSQL(Option1452);

        String Option1453 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'5'," +
                "'-1'," +
                "'F'," +
                "'Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of lollipops sold.  The highest number is 104." +
                "Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (104) by the number of available boxes (6).  104÷6=17.33.  Round up.  You can choose either the first interval, 18 (remember, you can’t have 17.33 of a lollipop, so round to the nearest whole number, or an “easy” interval of 20.')";
        database.execSQL(Option1453);

        String Option1461 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question Graph A: Number of Fair-Trade Chocolate Star-Shaped Lollipops Sold over a 6 Week Period at Local Hockey Games as Market Research to Determine the Best Price for the Lollipops ($)." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
        database.execSQL(Option1461);

        String Option1471 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'7'," +
                "'none'," +
                "'F'," +
                "'Oops!  It seems like your point doesn’t belong here.  Remember to use your table to create an (x,y) point.  The x value comes from the independent variable (in this case the price of the lollipop) and the y value from the dependent variable (in this case the number of lollipop’s sold).  So your first point would be (0.50, 27).')";
        database.execSQL(Option1471);

        String Option1481 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'8'," +
                "'There is no best price for the lollipops, so Bob and Mark should sell them at the highest price.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1481);

        String Option1482 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'8'," +
                "'The lollipop’s should cost $1.20.'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1482);

        String Option1483 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'8'," +
                "'The lollipop’s should cost $1.25.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option1483);

        String Option1484 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'14'," +
                "'8'," +
                "'The lollipop’s should cost $0.50'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option1484);

        //Heading
        String heading23 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'12'," +
                "'x'," +
                "'Height of Shelves Product is Placed On (m)'" +
                ")";
        database.execSQL(heading23);

        String heading24 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'12'," +
                "'y'," +
                "'Number of Products Sold Per Day on Average'" +
                ")";
        database.execSQL(heading24);

        String heading25 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'13'," +
                "'x'," +
                "'Mass of the Rocket (g)'" +
                ")";
        database.execSQL(heading25);

        String heading26 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'13'," +
                "'y'," +
                "'Height of Rocket’s Flight (m)'" +
                ")";
        database.execSQL(heading26);

        String heading27 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'14'," +
                "'x'," +
                "'Price of the Lollipop ($)'" +
                ")";
        database.execSQL(heading27);

        String heading28 =  "INSERT INTO "+TABLE_HEADING_NAME+ " ("+ COLUMN_MAINQH_ID+","+COLUMN_HEADING_AXIS+","+COLUMN_HEADING+") VALUES(" +
                "'14'," +
                "'y'," +
                "'Number of Lollipop’s Sold'" +
                ")";
        database.execSQL(heading28);
        //table1
        String Data12231 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'1'," +
                "'1.5'"+
                ")";
        database.execSQL(Data12231);

        String Data12232 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'2'," +
                "'3'"+
                ")";
        database.execSQL(Data12232);

        String Data12233 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'3'," +
                "'2'"+
                ")";
        database.execSQL(Data12233);

        String Data12234 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'4'," +
                "'0.5'"+
                ")";
        database.execSQL(Data12234);

        String Data12235 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'5'," +
                "'1'"+
                ")";
        database.execSQL(Data12235);

        String Data12236 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'23'," +
                "'6'," +
                "'3.5'"+
                ")";
        database.execSQL(Data12236);

        String Data12241 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'1'," +
                "'523'"+
                ")";
        database.execSQL(Data12241);

        String Data12242 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'2'," +
                "'178'"+
                ")";
        database.execSQL(Data12242);

        String Data12243 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'3'," +
                "'404'"+
                ")";
        database.execSQL(Data12243);

        String Data12244 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'4'," +
                "'97'"+
                ")";
        database.execSQL(Data12244);

        String Data12245 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'5'," +
                "'118'"+
                ")";
        database.execSQL(Data12245);

        String Data12246 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'12'," +
                "'24'," +
                "'6'," +
                "'15'"+
                ")";
        database.execSQL(Data12246);
        //table 2
        String Data13251 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'1'," +
                "'520'"+
                ")";
        database.execSQL(Data13251);

        String Data13252 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'2'," +
                "'660'"+
                ")";
        database.execSQL(Data13252);

        String Data13253 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'3'," +
                "'170'"+
                ")";
        database.execSQL(Data13253);

        String Data13254 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'4'," +
                "'293'"+
                ")";
        database.execSQL(Data13254);

        String Data13255 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'5'," +
                "'797'"+
                ")";
        database.execSQL(Data13255);

        String Data13256 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'25'," +
                "'6'," +
                "'435'"+
                ")";
        database.execSQL(Data13256);

        String Data13261 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'1'," +
                "'18.6'"+
                ")";
        database.execSQL(Data13261);

        String Data13262 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'2'," +
                "'19.2'"+
                ")";
        database.execSQL(Data13262);

        String Data13263 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'3'," +
                "'10.2'"+
                ")";
        database.execSQL(Data13263);

        String Data13264 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'4'," +
                "'30.2'"+
                ")";
        database.execSQL(Data13264);

        String Data13265 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'5'," +
                "'11.3'"+
                ")";
        database.execSQL(Data13265);

        String Data13266 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'13'," +
                "'26'," +
                "'6'," +
                "'25.2'"+
                ")";
        database.execSQL(Data13266);
        //table 3
        String Data14271 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'1'," +
                "'0.50'"+
                ")";
        database.execSQL(Data14271);

        String Data14272 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'2'," +
                "'0.65'"+
                ")";
        database.execSQL(Data14272);

        String Data14273 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'3'," +
                "'0.90'"+
                ")";
        database.execSQL(Data14273);

        String Data14274 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'4'," +
                "'1.20'"+
                ")";
        database.execSQL(Data14274);

        String Data14275 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'5'," +
                "'1.25'"+
                ")";
        database.execSQL(Data14275);

        String Data14276 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'27'," +
                "'6'," +
                "'1.35'"+
                ")";
        database.execSQL(Data14276);

        String Data14281 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'1'," +
                "'27'"+
                ")";
        database.execSQL(Data14281);

        String Data14282 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'2'," +
                "'42'"+
                ")";
        database.execSQL(Data14282);

        String Data14283 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'3'," +
                "'67'"+
                ")";
        database.execSQL(Data14283);

        String Data14284 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'4'," +
                "'92'"+
                ")";
        database.execSQL(Data14284);

        String Data14285 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'5'," +
                "'104'"+
                ")";
        database.execSQL(Data14285);

        String Data14286 =  "INSERT INTO "+TABLE_HDATA_NAME + " ("+ COLUMN_MAIND_ID+","+COLUMN_HEADINGD_ID+","+COLUMN_ORDER+","+COLUMN_DATA+") VALUES(" +
                "'14'," +
                "'28'," +
                "'6'," +
                "'63'"+
                ")";
        database.execSQL(Data14286);

    }


    public static Help getTextHelp(long mid, long sid){
        SQLiteDatabase db = databaseHandler.getReadableDatabase();


        String quert = "SELECT * FROM "+ TABLE_HELP +" WHERE "+COLUMN_MID + " =? AND "+COLUMN_SID + " =? AND "+ COLUMN_TY + " =?";
        Cursor cursor = db.rawQuery(quert,new String[]{String.valueOf(mid),String.valueOf(sid),"text"});

        if (cursor != null)
            cursor.moveToFirst();

        Help help = new Help(
                Long.parseLong(cursor.getString(0)),    //mid
                Long.parseLong(cursor.getString(1)),    //sid
                cursor.getString(2),                    //type
                cursor.getString(3),                    //value
                cursor.getBlob(4)
        );

        db.close();
        return help;
    }


    public static Help getImageHelp(long mid, long sid){
        SQLiteDatabase db = databaseHandler.getReadableDatabase();


        String quert = "SELECT * FROM "+ TABLE_HELP +" WHERE "+COLUMN_MID + " =? AND "+COLUMN_SID + " =? AND "+ COLUMN_TY + " =?";
        Cursor cursor = db.rawQuery(quert,new String[]{String.valueOf(mid),String.valueOf(sid),"image"});

        if (cursor != null)
            cursor.moveToFirst();

        Help help = new Help(
                Long.parseLong(cursor.getString(0)),    //mid
                Long.parseLong(cursor.getString(1)),    //sid
                cursor.getString(2),                    //type
                cursor.getString(3),                    //value
                cursor.getBlob(4)                       //blob
        );

        db.close();
        return help;
    }
    //get the video/picture/url from database
    public static Help getVideoHelp(long mid, long sid){
        SQLiteDatabase db = databaseHandler.getReadableDatabase();


        String quert = "SELECT * FROM "+ TABLE_HELP +" WHERE "+COLUMN_MID + " =? AND "+COLUMN_SID + " =? AND "+ COLUMN_TY + " =?";
        Cursor cursor = db.rawQuery(quert, new String[]{String.valueOf(mid), String.valueOf(sid), "video"});

        if (cursor != null)
            cursor.moveToFirst();

        Help help = new Help(
                Long.parseLong(cursor.getString(0)),    //mid
                Long.parseLong(cursor.getString(1)),    //sid
                cursor.getString(2),                    //type
                cursor.getString(3),                    //value
                cursor.getBlob(4)
        );

        db.close();
        return help;
    }
    public void insertHelp(SQLiteDatabase database) {
        String Video11 = "INSERT INTO " + TABLE_HELP + " (" + COLUMN_MID + " ," + COLUMN_SID + " ," + COLUMN_TY + " ," + COLUMN_VALUE + " ," + COLUMN_IMAGE + ") VALUES(" +
                "'1'," +
                "'1'," +
                "'video'," +
                "'https://youtu.be/liFLl3Eh_HU'," +
                "'none'" +
                ")";
        database.execSQL(Video11);

        String Text11 = "INSERT INTO " + TABLE_HELP + " (" + COLUMN_MID + " ," + COLUMN_SID + " ," + COLUMN_TY + " ," + COLUMN_VALUE + " ," + COLUMN_IMAGE + ") VALUES(" +
                "'1'," +
                "'1'," +
                "'text'," +
                "'To find your independent variable you " +
                "need to start by finding what two things " +
                "you are comparing in this question. " +
                "Here you are comparing how many snails " +
                "cross the finish line with how many snails " +
                "start. " +
                "Variable 1: Number of Snails Who Finish " +
                "the Race in 1 Minute " +
                "Variable 2: Number of Snails Who Start the " +
                "Race " +
                "Now ask yourself which variable depends " +
                "on the other. " +
                "Does Variable 1 depend on Variable 2 " +
                "OR" +
                "Does Variable 2 depend on Variable 1 " +
                "Here the number of snails who finish the race in 1 minute depends on the number of " +
                "snails who start the race. " +
                "So the number of snails who finish the race " +
                "in 1 minute is the dependent variable as it " +
                "depends on the other variable. " +
                "The other variable, the number of snails " +
                "who starts the race, is the independent " +
                "variable. '," +
                "'none'" +
                ")";
        database.execSQL(Text11);

        Bitmap image11 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.first);
        insertImage(1, 1, image11, database);

        //Help2

    }

    public void insertImage(long mid,long sid,Bitmap bitmap,SQLiteDatabase database){
        //change bitmap into byte[]
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bs);
        byte[] data = bs.toByteArray();
        //insert into the database
        //SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MID,mid);
        cv.put(COLUMN_SID,sid);
        cv.put(COLUMN_TY,"image");
        cv.put(COLUMN_VALUE,"none");
        cv.put(COLUMN_IMAGE, data);

        database.insert(TABLE_HELP, null, cv);

    }


}
