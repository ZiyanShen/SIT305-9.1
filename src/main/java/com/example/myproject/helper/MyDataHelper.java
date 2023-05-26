

package com.example.myproject.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataHelper extends SQLiteOpenHelper {

    private static final String CREATE_LOST_FOUND = "create table lost_found ("
            + "id integer primary key autoincrement, "
            + "type integer, "
            + "name text, "
            + "phone text, "
            + "description text, "
            + "date text, "
            + "latitude text, "
            + "longitude text, "
            + "location text)";

    public MyDataHelper(@Nullable Context context) {
        super(context, "myDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOST_FOUND);//COLUMN变为常量
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //增加
//    public String addOne(Integer id,String word_name,String translate){
//        ContentValues cv = new ContentValues();//存放数据的
//        cv.put(COLUMN_ID, String.valueOf(id.getClass()));
//        cv.put(COLUMN_WORD_NAME, String.valueOf(word_name.getClass()));
//        cv.put(COLUMN_TRANSLATE,String.valueOf(translate.getClass()));
//
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();//继承了SQLiteOpenHelper类
//        long insert =sqLiteDatabase.insert(TABLE_WORD,COLUMN_ID,cv);//COLUMN是空行，
//        if(insert == -1){//添加失败
//            return "fail";
//        }
//        sqLiteDatabase.close();
//        return "success";
//    }
    //删除
//    public String deleteOne(Integer id,String word_name,String translate){
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
//        int delete = sqLiteDatabase.delete(TABLE_WORD,COLUMN_ID + "=?",new String[]{String.valueOf(WordModel.getId())});
//        if (delete == 0){//删除失败
//            return  "fail";
//        }
//        sqLiteDatabase.close();
//
//        return "success";
//    }
    //修改
//    public String updateOne(Integer id,String word_name,String translate){
//        ContentValues cv = new ContentValues();//存放数据的
//        cv.put(COLUMN_ID, String.valueOf(id.getClass()));
//        cv.put(COLUMN_WORD_NAME, String.valueOf(word_name.getClass()));
//        cv.put(COLUMN_TRANSLATE,String.valueOf(translate.getClass()));
//
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();//继承了SQLiteOpenHelper类
//        int update = sqLiteDatabase.update(TABLE_WORD,cv,COLUMN_ID + "=?",new String[]{String.valueOf(WordModel.getId())});
//        if (update  == 0){//没有任何更新值
//            return  "fail";
//        }
//        sqLiteDatabase.close();
//        return "success";
//    }
    //查询
//    public List<WordModel> getAll(){
//        List<WordModel> list = new ArrayList<>();
//        String sql = "SELECT * FROM " + TABLE_WORD;
//        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();//继承了SQLiteOpenHelper类
//        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);//用rawQuery（）方法写查询语句，null——细节可以写在sql里面，并返回cursor值
//        int idIndex = cursor.getColumnIndex(COLUMN_ID);//获取id
//        int word_nameIndex = cursor.getColumnIndex(COLUMN_WORD_NAME);
//        int translateIndex = cursor.getColumnIndex(COLUMN_TRANSLATE);
//        while(cursor.moveToNext()){
//            //指向第一个，第二个等的循环，
//            WordModel wdl = new WordModel(cursor.getInt(idIndex),cursor.getString(word_nameIndex),cursor.getString(translateIndex));
//            list.add(wdl);
//        }
//
//        return list;
//    }
}
