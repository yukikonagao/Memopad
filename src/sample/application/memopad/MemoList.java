package sample.application.memopad;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.view.MenuInflater;//8/4追加
import android.widget.AdapterView.AdapterContextMenuInfo;//8/4追加
import android.app.AlertDialog;//8/4追加
import android.content.DialogInterface;//8/4追加

public class MemoList extends ListActivity {
	static final String[] cols = {"title","memo",android.provider.BaseColumns._ID,};
	MemoDBHelper memos;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO 自動生成されたメソッド・スタブ
		super.onListItemClick(l, v, position, id);
		this.memos = new MemoDBHelper(this);
		SQLiteDatabase db = memos.getWritableDatabase();
		Cursor cursor = db.query("memoDB",cols,"_ID="+String.valueOf(id),null,null,null,null);
		startManagingCursor(cursor);
		int idx = cursor.getColumnIndex("memo");
		cursor.moveToFirst();
		Intent i = new Intent();
		
		//メモが選択されたときの表示が上記。
		
		i.putExtra("text",cursor.getString(idx));
		setResult(RESULT_OK,i);
		memos.close();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.memolist);
		this.showMemos(this.getMemos());
		//下2行追加
		ListView lv = (ListView) this.findViewById(android.R.id.list);
		this.registerForContextMenu(lv);
		//これがないとプログラムが起動しない。
		
	}

	public void showMemos(Cursor cursor){
		if(cursor != null){
			String[] from = {"title"};
			int[] to = {android.R.id.text1};
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this,android.R.layout.simple_list_item_1,
					cursor, from, to
					);
			this.setListAdapter(adapter);
		}
		memos.close();
		
	}

	public Cursor getMemos() {
			this.memos = new MemoDBHelper(this);
			SQLiteDatabase db = memos.getReadableDatabase();
			Cursor cursor = db.query("memoDB",cols,null,null,null,null,null);
			startManagingCursor(cursor);
			return cursor;
	}

}
