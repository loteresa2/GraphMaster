package com.applicationcourse.mobile.graphmaster;

/**
 * Created by teresa on 16/02/16.

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "graphMaster";
    // tasks table name
    private static final String TABLE_CREATEG = "create_main";
    private static final String TABLE_VALUES= "values";
    private static final String TABLE_CREATE_SUBQUES= "subques";
    //private static final String TABLE_READG = "read_graph";
    //private static final String TABLE_OWNG = "own_graph";
    // tasks Table Columns names
    private static final String TYPE = "type";
    private static final String MAIN_QUES = "main_ques";
    private static final String MAIN_QUES_NO = "main_ques_no";
    private static final String SUB_QUES_NO = "sub_ques_no"; //correct option//option a
    private static final String SUB_QUES = "sub_ques";
    private static final String OPTIONS= "options";
    private static final String ANSWER= "answer";
    private static final String EXPLANATION = "explain";
   // private SQLiteDatabase dbase;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CREATEG + " ( "
                + MAIN_QUES_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TYPE
                + " TEXT, " + MAIN_QUES+ " TEXT, "+SUB_QUES_NO +" TEXT )";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CREATE_SUBQUES + " ( "
                + SUB_QUES_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MAIN_QUES_NO+ " INTEGER, "+SUB_QUES +" TEXT )";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_VALUES + " ( "
                + MAIN_QUES_NO + " INTEGER, "
                + SUB_QUES_NO+ " INTEGER, "+OPTIONS +" TEXT, "+ANSWER +" TEXT, "+EXPLANATION +" TEXT, FOREIGN KEY "+
                MAIN_QUES_NO+" REFERENCES "+TABLE_CREATEG+" ( "+MAIN_QUES_NO+" ) , FOREIGN KEY "
                +SUB_QUES_NO+" REFERENCES "+TABLE_CREATE_SUBQUES+" ( "+SUB_QUES_NO+" ))";
        db.execSQL(sql);

       // db.close();
    }
    public void addQuestions()
    {


    }
    // Adding new question
    public void addQuestion(CGQuestion quest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=0;i< quest.getSubQuestionList().size();i++) {
            values.put(MAIN_QUES, quest.getQuestion());
            values.put(TYPE, quest.getType());
            values.put(SUB_QUES_NO, i);
            values.put(KEY_OPTB, quest.getOptB());
            values.put(KEY_OPTC, quest.getOptC());
// Inserting Row
            db.insert(TABLE_QUEST, null, values);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
// Create tables again
        onCreate(db);
    }
    public List<CGQuestion> getAllQuestions() {
        List<CGQuestion> quesList = new ArrayList<CGQuestion>();
// Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CGQuestion quest = new CGQuestion();
                quest.setId(cursor.getInt(0));
                quest.setQuestion(cursor.getString(1));
                quest.setAnswer(cursor.getString(2));
                quest.setOptA(cursor.getString(3));
                quest.setOptB(cursor.getString(4));
                quest.setOptC(cursor.getString(5));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
// return quest list
        return quesList;
    }
    public int rowcount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }

} */
