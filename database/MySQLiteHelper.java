package in.com.demo.myrecycleviewerapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

import in.com.demo.myrecycleviewerapplication.model.ArticleInfo;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String ARTICLE_INFO_TABLE_NAME = "article_info_table";
    public static final String ARTICLE_INFO_TABLE_ID = "article_info_table_id";
    public static final String ARTICLE_INFO_SOURCE_ID = "article_info_source_id";
    public static final String ARTICLE_INFO_SOURCE_NAME = "article_info_source_name";
    public static final String ARTICLE_INFO_AUTHOR = "article_info_author";
    public static final String ARTICLE_INFO_TITLE = "article_info_title";
    public static final String ARTICLE_INFO_DESC = "varticle_info_desc";
    public static final String ARTICLE_INFO_URL = "article_info_url";
    public static final String ARTICLE_INFO_URL_TO_IMAGE = "article_info_url_to_image";
    public static final String ARTICLE_INFO_PUBLISH_AT = "article_info_publish_at";
    public static final String ARTICLE_INFO_CONTENT = "article_info_content";

    private static final String DATABASE_NAME = "articles.db";
    private static final int CURRENT_DATABASE_VERSION = 1;
    private static final int UPGRADABLE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
    }



    private static final String ARTICLE_INFO_TABLE_NAME_CREATE_STATEMENT = "create table " + ARTICLE_INFO_TABLE_NAME
            + "(" + ARTICLE_INFO_TABLE_ID + " integer primary key autoincrement, "
            + ARTICLE_INFO_SOURCE_ID + " integer, "
            + ARTICLE_INFO_SOURCE_NAME + " text, "
            + ARTICLE_INFO_AUTHOR + " text, "
            + ARTICLE_INFO_TITLE + " text, "
            + ARTICLE_INFO_DESC + " text, "
            + ARTICLE_INFO_URL + " text, "
            + ARTICLE_INFO_URL_TO_IMAGE + " text, "
            + ARTICLE_INFO_PUBLISH_AT + " text, "
            + ARTICLE_INFO_CONTENT + " text);";

    private String[] table_columns = {
            ARTICLE_INFO_AUTHOR,
            ARTICLE_INFO_TITLE,
            ARTICLE_INFO_DESC,
            ARTICLE_INFO_URL
    };


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(ARTICLE_INFO_TABLE_NAME_CREATE_STATEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS '" + ARTICLE_INFO_TABLE_NAME + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  long insertArticleInfo(ArticleInfo aInfo) {
        long result = -1;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            if (null != aInfo) {
                values.put(ARTICLE_INFO_SOURCE_ID, aInfo.getSourceInfo().getId());
                values.put(ARTICLE_INFO_SOURCE_NAME, aInfo.getSourceInfo().getName());
                values.put(ARTICLE_INFO_AUTHOR, aInfo.getAuthor());
                values.put(ARTICLE_INFO_TITLE, aInfo.getTitle());
                values.put(ARTICLE_INFO_DESC, aInfo.getDesc());
                values.put(ARTICLE_INFO_URL, aInfo.getUrl());
                values.put(ARTICLE_INFO_URL_TO_IMAGE, aInfo.getUrlToImage());
                values.put(ARTICLE_INFO_PUBLISH_AT, aInfo.getPublishedAt());
                values.put(ARTICLE_INFO_CONTENT, aInfo.getContent());
                long insertId = database.insert(MySQLiteHelper.ARTICLE_INFO_TABLE_NAME, null, values);
                if (insertId != -1) {
                    result = insertId;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<ArticleInfo> getArticleInfo() {
        ArrayList<ArticleInfo> articleInfoList = null;

        SQLiteDatabase database = this.getWritableDatabase();

        try {
            Cursor cursor = database.query(MySQLiteHelper.ARTICLE_INFO_TABLE_NAME, table_columns, null,
                    null, null, null, null);
            if (!(cursor.getCount() < 1)) {
                cursor.moveToFirst();
                articleInfoList = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    ArticleInfo mdtInbox = cursorToArticleInfo(cursor);
                    articleInfoList.add(mdtInbox);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return articleInfoList;
    }

    private ArticleInfo cursorToArticleInfo(Cursor cursor) {
        ArticleInfo articleInfo = new ArticleInfo();
        try {
            articleInfo.setAuthor(cursor.getString(0));
            articleInfo.setTitle(cursor.getString(1));
            articleInfo.setDesc(cursor.getString(2));
            articleInfo.setUrl(cursor.getString(3));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return articleInfo;
    }

    public void removeArticleInfo(){
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("DROP TABLE IF EXISTS '" + ARTICLE_INFO_TABLE_NAME + "' ;");
    }
}
