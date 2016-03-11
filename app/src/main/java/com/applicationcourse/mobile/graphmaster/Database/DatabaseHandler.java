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
    public static final String COLUMN_GRADE = "level";

    ///////////////////////SubQuestion Table////////////////////////////////////
    public static final String TABLE_NAME = "Subquestion";
    public static final String COLUMN_SUB_ID = "subId";
    public static final String COLUMN_SUB_FUNCTION = "function";
    public static final String COLUMN_SUB_TYPE = "type";
    public static final String COLUMN_SUBQUESTION = "subquestion";
    public static final String COLUMN_OPTION_TYPE = "optionType";

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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HEADING_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HDATA_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_NAME);

        database.execSQL(DATABASE_MAIN_CREATE);
        database.execSQL(DATABASE_SUB_CREATE);
        database.execSQL(DATABASE_OPTION_CREATE);
        database.execSQL(DATABASE_HEADING_CREATE);
        database.execSQL(DATABASE_DATA_CREATE);
        database.execSQL(DATABASE_PROGRESS_CREATE);
        database.execSQL(DATABASE_STUDENT_CREATE);

        String insert1 = "INSERT INTO "+TABLE_PROGRESS_NAME+" ("+COLUMN_DATE+","+COLUMN_STUD_ID+","+COLUMN_FUNCT_TYPE+","+COLUMN_LEVEL+","+COLUMN_TIME_TAKEN+","+COLUMN_NUM_WRONG+") VALUES ('4/3/2016','1','create','1','00:02:00','0')";
        database.execSQL(insert1);
        database.execSQL(insert1);
        //String insert2 = "INSERT INTO "+TABLE_PROGRESS_NAME+" ("+COLUMN_DATE+","+COLUMN_STUD_ID+","+COLUMN_FUNCT_TYPE+","+COLUMN_LEVEL+","+COLUMN_TIME_TAKEN+","+COLUMN_NUM_WRONG+") VALUES ('4/3/2016','1','create','1','3','4')";
        //database.execSQL(insert2);
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
            p.x = Integer.parseInt(cursor.getString(0));
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
            p.y = Integer.parseInt(cursor.getString(0));
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

    //Get Progress Data row value from db
    public static String getProgressResult(int studId,String function, int level,String timeTaken,String timeThreshold) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        // Uses a cursor to query from the database.
        // Provides the strings we want from the query and the query parameters
        //IF student finishes fast and no mistake
        Cursor cursor;
        String countQuery;
        int noOfWrong;
        //TODO: count the number of attempt for each function: if count > 2 then only do below

        countQuery = "SELECT * FROM "+TABLE_PROGRESS_NAME+" WHERE "+COLUMN_LEVEL+" =?";
        cursor = db.rawQuery(countQuery,null);
        if(cursor.getCount() > 1) {
            if (timeTaken.compareTo(timeThreshold) < 0) {
                countQuery = "SELECT SUM( A." + COLUMN_NUM_WRONG + ") FROM ( SELECT * FROM " + TABLE_PROGRESS_NAME
                        + " WHERE " + COLUMN_STUD_ID + " =? AND "
                        + COLUMN_FUNCT_TYPE + " =? AND " + COLUMN_LEVEL + " =? " +
                        " ORDER BY " + COLUMN_ATTEMPT_COUNT + " DESC LIMIT 2 ) A WHERE A." + COLUMN_TIME_TAKEN + " <= ?  GROUP BY A." + COLUMN_LEVEL;
                cursor = db.rawQuery(countQuery, new String[]{String.valueOf(studId), function, String.valueOf(level), timeThreshold});
                if (cursor != null)
                    cursor.moveToFirst();
                noOfWrong = Integer.parseInt(cursor.getString(0));
                if (noOfWrong == 0) {
                    db.close();
                    return "promoteLevel";
                }
            }
            //If slow and no mistake it will return 0, else return no of mistake
            countQuery = "SELECT SUM(" + COLUMN_NUM_WRONG + ") FROM ( SELECT * FROM " + TABLE_PROGRESS_NAME
                    + " WHERE " + COLUMN_STUD_ID + " =? AND "
                    + COLUMN_FUNCT_TYPE + " =? AND " + COLUMN_LEVEL + " =? " +
                    " ORDER BY " + COLUMN_ATTEMPT_COUNT + " DESC LIMIT 2 ) WHERE " + COLUMN_TIME_TAKEN + " > ?  GROUP BY " + COLUMN_LEVEL;
            cursor = db.rawQuery(countQuery, new String[]{String.valueOf(studId), function, String.valueOf(level), timeThreshold});
            if (cursor.getCount() > 0) {
                if (cursor != null)
                    cursor.moveToFirst();
                noOfWrong = Integer.parseInt(cursor.getString(0));
                db.close();
                if (noOfWrong == 0) {
                    return "nextLevel";
                }
            }
        }
            return "repeatLevel";
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

}
