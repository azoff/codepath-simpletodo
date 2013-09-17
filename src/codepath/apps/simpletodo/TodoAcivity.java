package codepath.apps.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoAcivity extends Activity {

	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	EditText etNewItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_activity);
		items = new ArrayList<String>();
		lvItems = (ListView) findViewById(R.id.lvItems);
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		loadItems();
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		initDeleteItemListener();
	}

	private void initDeleteItemListener() {
		lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				itemsAdapter.remove(itemsAdapter.getItem(i));
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
	}

	private File getFile() {
		File filesDir = getFilesDir();
		return new File(filesDir, "todo.txt");
	}

	private void saveItems() {
		File todoFile = getFile();
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			Log.e(getLocalClassName(), "unable to save items", e);
		}
	}

	private void loadItems() {
		File todoFile = getFile();
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			Log.w(getLocalClassName(), "unable to load items", e);
		}
	}

	public void addItem(View view) {
		Editable text = etNewItem.getText();
		if (text.length() > 0) {
			itemsAdapter.add(text.toString());
			saveItems();
		}
		etNewItem.setText("");
	}

}