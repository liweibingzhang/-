package edu.hrbeu.ContentResolverDemo;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContentResolverDemoActivity extends Activity {
	
	private EditText nameText;
	private EditText ageText;
	private EditText heightText;
	private EditText idEntry;
	
	private TextView labelView;
	private TextView displayView;
	
	private ContentResolver resolver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        nameText = (EditText)findViewById(R.id.name);
        ageText = (EditText)findViewById(R.id.age);
        heightText = (EditText)findViewById(R.id.height);
        idEntry = (EditText)findViewById(R.id.id_entry);
        
        labelView = (TextView)findViewById(R.id.label);
        displayView = (TextView)findViewById(R.id.display);
        
        
        
        Button addButton = (Button)findViewById(R.id.add);
        Button queryAllButton = (Button)findViewById(R.id.query_all);      
        Button clearButton = (Button)findViewById(R.id.clear);
        Button deleteAllButton = (Button)findViewById(R.id.delete_all);
        
        Button queryButton = (Button)findViewById(R.id.query);
        Button deleteButton = (Button)findViewById(R.id.delete);
        Button updateButton = (Button)findViewById(R.id.update);
        
        
        addButton.setOnClickListener(addButtonListener); 
        queryAllButton.setOnClickListener(queryAllButtonListener);     
        clearButton.setOnClickListener(clearButtonListener);
        deleteAllButton.setOnClickListener(deleteAllButtonListener);      
        
        queryButton.setOnClickListener(queryButtonListener);
        deleteButton.setOnClickListener(deleteButtonListener);
        updateButton.setOnClickListener(updateButtonListener);
        
        resolver = this.getContentResolver();
           
    }
    
    
    OnClickListener addButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			ContentValues values = new ContentValues();
			  
			values.put(People.KEY_NAME, nameText.getText().toString());
			values.put(People.KEY_AGE, Integer.parseInt(ageText.getText().toString()));
			values.put(People.KEY_HEIGHT, Float.parseFloat(heightText.getText().toString()));
		    			
			Uri newUri = resolver.insert(People.CONTENT_URI, values);

			labelView.setText("��ӳɹ���URI:" + newUri);

		}	
    };
    
    OnClickListener queryAllButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Cursor cursor = resolver.query(People.CONTENT_URI,
					new String[] { People.KEY_ID, People.KEY_NAME, People.KEY_AGE, People.KEY_HEIGHT},
					null, null, null);
			if (cursor == null){
				labelView.setText("���ݿ���û������");
				return;
			}
			labelView.setText("���ݿ⣺" + String.valueOf(cursor.getCount()) + "����¼");
			
			String msg = "";
			if (cursor.moveToFirst()){
				do{
					msg += "ID��" + cursor.getInt(cursor.getColumnIndex(People.KEY_ID)) + "��";
					msg += "������" + cursor.getString(cursor.getColumnIndex(People.KEY_NAME))+ "��";
					msg += "���䣺" + cursor.getInt(cursor.getColumnIndex(People.KEY_AGE)) + "�� ";
					msg += "��ߣ�" + cursor.getFloat(cursor.getColumnIndex(People.KEY_HEIGHT)) + "\n";
				}while(cursor.moveToNext());
			  }

			displayView.setText(msg);
		}
    };
    
    
    
    OnClickListener clearButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			displayView.setText("");
		}	
    };
    
    OnClickListener deleteAllButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			resolver.delete(People.CONTENT_URI, null, null);
			String msg = "����ȫ��ɾ��" ;
			labelView.setText(msg);
		}	
    };
    
    OnClickListener queryButtonListener = new OnClickListener() {
		@Override 
		public void onClick(View v) {
			Uri uri = Uri.parse(People.CONTENT_URI_STRING + "/" + idEntry.getText().toString());
			Cursor cursor = resolver.query(uri,
					new String[] { People.KEY_ID, People.KEY_NAME, People.KEY_AGE, People.KEY_HEIGHT},
					null, null, null);
			if (cursor == null){
				labelView.setText("���ݿ���û������");
				return;
			}
			
			String msg = "";
			if (cursor.moveToFirst()){
				msg += "ID��" + cursor.getInt(cursor.getColumnIndex(People.KEY_ID)) + "��";
				msg += "������" + cursor.getString(cursor.getColumnIndex(People.KEY_NAME))+ "��";
				msg += "���䣺" + cursor.getInt(cursor.getColumnIndex(People.KEY_AGE)) + "�� ";
				msg += "��ߣ�" + cursor.getFloat(cursor.getColumnIndex(People.KEY_HEIGHT)) + "\n";
			  }

			labelView.setText("���ݿ⣺");
			displayView.setText(msg);
		}
    };
    
    OnClickListener deleteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Uri uri = Uri.parse(People.CONTENT_URI_STRING + "/" + idEntry.getText().toString());
			int result = resolver.delete(uri, null, null);
			String msg = "ɾ��IDΪ"+idEntry.getText().toString()+"������" + (result>0?"�ɹ�":"ʧ��");
			labelView.setText(msg);
		}	
    };
    
    OnClickListener updateButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ContentValues values = new ContentValues();
			  
			values.put(People.KEY_NAME, nameText.getText().toString());
			values.put(People.KEY_AGE, Integer.parseInt(ageText.getText().toString()));
			values.put(People.KEY_HEIGHT, Float.parseFloat(heightText.getText().toString()));
		    			
			Uri uri = Uri.parse(People.CONTENT_URI_STRING + "/" + idEntry.getText().toString());
			int result = resolver.update(uri, values, null, null);
			
			String msg = "����IDΪ"+idEntry.getText().toString()+"������" + (result>0?"�ɹ�":"ʧ��");
			labelView.setText(msg);
		}
    };
}