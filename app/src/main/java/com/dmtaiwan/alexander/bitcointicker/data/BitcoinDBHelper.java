package com.dmtaiwan.alexander.bitcointicker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;

import java.util.ArrayList;

/**
 * Created by Alexander on 8/20/2017.
 */

public class BitcoinDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bitcoin_ticker.db";
    private static final int DB_VERSION = 5;

    public BitcoinDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_BITCOIN_TABLE = "CREATE TABLE " + BitcoinDBContract.BitcoinEntry.TABLE_NAME + " ("
                + BitcoinDBContract.BitcoinEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_NAME + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_RANK + " INTEGER, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_EUR + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_EUR + " TEXT, "
                + BitcoinDBContract.BitcoinEntry.COLUMN_EXPANDED + " TEXT, "
                + "UNIQUE (" + BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + ") on conflict replace" + ")";

        String SQL_CREATE_POSITION_TABLE = "CREATE TABLE " + BitcoinDBContract.PositionEntry.TABLE_NAME + " ("
                + BitcoinDBContract.PositionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BitcoinDBContract.PositionEntry.COLUMN_SYMBOL + " TEXT, "
                + BitcoinDBContract.PositionEntry.COLUMN_POSITION + " TEXT, "
                + BitcoinDBContract.PositionEntry.COLUMN_PRICE + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_BITCOIN_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POSITION_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BitcoinDBContract.BitcoinEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BitcoinDBContract.PositionEntry.TABLE_NAME);
        onCreate(db);
    }


    public static void insertData(Context context, ContentValues contentValues) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(BitcoinDBContract.BitcoinEntry.TABLE_NAME, null, contentValues);
    }

    public static Cursor readDbPositions(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                BitcoinDBContract.PositionEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }


    public static void insertPositionData(Context context, String symbol, String position, String price) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BitcoinDBContract.PositionEntry.COLUMN_SYMBOL, symbol);
        contentValues.put(BitcoinDBContract.PositionEntry.COLUMN_POSITION, position);
        contentValues.put(BitcoinDBContract.PositionEntry.COLUMN_PRICE, price);
        db.insert(BitcoinDBContract.PositionEntry.TABLE_NAME, null, contentValues);
    }

    public static void removePositionData(Context context, int id) {
        String idString = String.valueOf(id);
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(BitcoinDBContract.PositionEntry.TABLE_NAME, BaseColumns._ID+ "=?", new String[]{idString});
    }

    public static void bulkInsertCoins(Context context, ArrayList<Coin> coins) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Coin coin : coins) {
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID, coin.getId());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_NAME, coin.getName());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL, coin.getSymbol());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_RANK, coin.getRank());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD, coin.getPrice_usd());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC, coin.getPrice_btc());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD, coin.getTwenty_four_hour_volume_usd());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD, coin.getMarket_cap_usd());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY, coin.getAvailable_supply());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY, coin.getTotal_supply());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H, coin.getPercent_change_one_hour());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H, coin.getPercent_change_twenty_four_hour());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D, coin.getPercent_change_seven_days());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED, coin.getLast_updated());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD, coin.getPrice_cad());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD, coin.getTwenty_four_hour_volume_cad());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD, coin.getMarket_cap_cad());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR, coin.getPrice_eur());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_EUR, coin.getTwenty_four_hour_volume_eur());
                values.put(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_EUR, coin.getMarket_cap_eur());
                db.insert(BitcoinDBContract.BitcoinEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Error inserting: ", e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public static Cursor readDbCoins(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                BitcoinDBContract.BitcoinEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public static Cursor querySimilarCoins(Context context, String term) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = term + '%';
        String query = "SELECT * FROM " + BitcoinDBContract.BitcoinEntry.TABLE_NAME
                + " WHERE " + BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + " LIKE ?";
        return db.rawQuery(query, args);
    }

    public static Cursor rawQueryCoins(Context context, String[] selectionArgs) {
        BitcoinDBHelper dbHelper = new BitcoinDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + BitcoinDBContract.BitcoinEntry.TABLE_NAME
                + " WHERE " + BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + " IN ( "
                + makePlaceholders(selectionArgs.length) + ")";


        return db.rawQuery(query, selectionArgs);
    }


    private static String makePlaceholders(int arrayLength) {
        if (arrayLength < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(arrayLength * 2 - 1);
            sb.append("?");
            for (int i = 1; i < arrayLength; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
