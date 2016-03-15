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
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP);

        database.execSQL(DATABASE_MAIN_CREATE);
        database.execSQL(DATABASE_SUB_CREATE);
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
        insertLevel1(database);
        insertLevel2(database);
        insertHelp(database);
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
                "'To find your independent variable you \n" +
                "need to start by finding what two things \n" +
                "you are comparing in this question. \n" +
                "Here you are comparing how many snails \n" +
                "cross the finish line with how many snails \n" +
                "start. \n" +
                "Variable 1: Number of Snails Who Finish \n" +
                "the Race in 1 Minute \n" +
                "Variable 2: Number of Snails Who Start the \n" +
                "Race \n" +
                "Now ask yourself which variable depends \n" +
                "on the other. \n" +
                "Does Variable 1 depend on Variable 2 \n" +
                "OR\n" +
                "Does Variable 2 depend on Variable 1 \n" +
                "Here the number of snails who finish the race in 1 minute depends on the number of \n" +
                "snails who start the race. \n" +
                "So the number of snails who finish the race \n" +
                "in 1 minute is the dependent variable as it \n" +
                "depends on the other variable. \n" +
                "The other variable, the number of snails \n" +
                "who starts the race, is the independent \n" +
                "variable. '," +
                "'none'" +
                ")";
        database.execSQL(Text11);
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
                "'Correct! Good job.')";
        database.execSQL(Option411);

        String Option412 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'1'," +
                "'Amount of pages read'," +
                "'F'," +
                "'Oops.  That’s incorrect. The length of time reading does not depend on the amount of pages read.It is the independent variable.  The number of pages read depends on the length of time you read, so it is the dependent variable. ')";
        database.execSQL(Option412);

        String Option421 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'2'," +
                "'The y-axis'," +
                "'T'," +
                "'Well done')";
        database.execSQL(Option421);

        String Option422 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
        database.execSQL(Option422);

        String Option431 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'3'," +
                "'none'," +
                "'T'," +
                "'The answer we had was X Axis:" +
                "Exemplar:Length of Time Reading (min); " +
                "Y Axis:" +
                "Exemplar: Number of Pages Read.  Do your axes labels look similar?  Have you capitalized key words?  Have you placed units in brackets after the axes label?  Have you placed arrows at the end of each axis? Take a moment to correct your axes labels if you think changes need to be made.')";
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
                "' Oops.  That is incorrect.  Look at your table and find the highest number for the independent variable/x-axis/Length of time reading.  The highest number is 30." +
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
                "' Oops.  That is incorrect.  Look at your table and find the highest number for the dependent variable/y-axis/number of pages read.  The highest number is 12.Now count the number of boxes on your graph.  You have 8 boxes.  Subtract 2 from this number.  This is your number of available boxes." +
                "Divide your highest number (12) by the number of available boxes (6).  12÷6=2.  This is your interval.')";
        database.execSQL(Option452);

        String Option461 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'6'," +
                "'none'," +
                "'T'," +
                "'Titles can be tricky to write.  Check your title against the one we wrote for this question :Number of Pages Read Depending on Length of Time Someone has Been Reading (min)." +
                "Did your title begin “Graph A:”?" +
                "Are key words capitalized?  Have you included the units in brackets?" +
                "Have you told the reader of your graph exactly what the graph is about?')";
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
                "'a.Based on the graph the longer you read the more you slow down and read less pages.'," +
                "'T'," +
                "'Correct!')";
        database.execSQL(Option481);

        String Option482 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'4'," +
                "'8'," +
                "'b.Based on the graph the length of time you read has no impact on the number of pages you read'," +
                "'F'," +
                "'Incorrect!')";
        database.execSQL(Option482);

        String Option511 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'1'," +
                "'a.Cost of the chocolate heart'," +
                "'T'," +
                "'That’s correct!')";
        database.execSQL(Option511);

        String Option512 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'1'," +
                "'b.Number of chocolate hearts sold that day'," +
                "'F'," +
                "'Oops.  The number of chocolate hearts bought depends on the cost of the heart.  Therefore the number of chocolate hearts bought is the dependent variable and the cost of the heart is the independent variable.')";
        database.execSQL(Option512);

        String Option521 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'5'," +
                "'2'," +
                "'The x-axis'," +
                "'F'," +
                "'Oops.The independent variable is placed on the x-axis.  The dependent variable is placed on the y.')";
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

    public void insertLevel1(SQLiteDatabase database){
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
                "'Place your points on the graph.'," +
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

        //option and explanation
        String Option111 = "INSERT INTO "+TABLE_OPTION_NAME+ " ("+ COLUMN_MAINQ_ID+","+COLUMN_SUBQ_ID+","+COLUMN_OPTION_VALUE+","+COLUMN_ANSWER +","+COLUMN_EXPLAIN+") VALUES(" +
                "'1'," +
                "'1'," +
                "'Number of snails in the race'," +
                "'T'," +
                "'Correct!Great job!')";
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
                "'Incorrect. The x-axis is where the independent variable is located.  The dependent variable goes on the y- axis.')";
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
                cursor.getBlob(4)
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



}
