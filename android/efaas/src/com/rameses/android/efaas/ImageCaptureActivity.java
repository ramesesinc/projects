package com.rameses.android.efaas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.ImageDB;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.util.DbBitmapUtility;

public class ImageCaptureActivity extends ControlActivity {
	
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    		
	private ImageView image;
	private EditText title;
	private Button  save;
	private String objid, examinationid;
	private Bitmap bitmap = null;
	private String data_title;
	private Activity activity;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		ApplicationUtil.changeTitle(this, "Capture Image");
		activity = this;
		
		objid = getIntent().getExtras().getString("objid");
		examinationid = getIntent().getExtras().getString("examinationid");
		
		if(objid == null) setContentView(R.layout.activity_image_empty);
		if(objid != null) setContentView(R.layout.activity_image);
		
		initComponents();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    if (fileUri != null) {
	        outState.putString("cameraImageUri", fileUri.toString());
	    }
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState.containsKey("cameraImageUri")) {
	        fileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
	    }
	}
	
	private void initComponents(){
		image = (ImageView) findViewById(R.id.image_view);
		title = (EditText) findViewById(R.id.image_title);
		save = (Button) findViewById(R.id.image_save);
		
		image.requestFocus();
		image.setClickable(true);
		image.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	captureImage();
		    }
		});
		
		if(objid != null){
			save.setText("Update");
			ImageDB db = new ImageDB();
			try{
				Properties prop = db.find(objid);
				String tle = prop.getProperty("title");
				String img = prop.getProperty("image");
				
				title.setText(tle);
				
				byte[] decodedBytes = Base64.decode(img, Base64.DEFAULT);
				bitmap = DbBitmapUtility.getImage(decodedBytes);
				image.setImageBitmap(bitmap);
			}catch(Throwable t){
				new ErrorDialog(this, t).show();
			}
		}
		
		save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(objid == null){
            		doCreate();
            	}else{
            		doUpdate();
            	}
            }
        });
	}
	
	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // if the result is capturing Image
	    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	setContentView(R.layout.activity_image);
	        	initComponents();
	            previewCapturedImage();
	        } else if (resultCode == RESULT_CANCELED) {
	        	setContentView(R.layout.activity_image_empty);
	        	initComponents();
	        } else {
	        	setContentView(R.layout.activity_image_empty);
	        	initComponents();
	            Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
	        }
	    }
	}
	
	private void captureImage() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ETRACS");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
            image.setImageBitmap(bitmap);
        } catch (Throwable e) {
        	e.printStackTrace();
        	new ErrorDialog(this,e).show();
        }
    }

	private void doCreate(){
		data_title = title.getText().toString();
		
		if(data_title.isEmpty()){
			new InfoDialog(this, "Title is required!").show();
			return;
		}
		
		if(bitmap == null){
			new InfoDialog(this, "Image is required!").show();
			return;
		}
		
		try{
			Map params = new HashMap();
			params.put("objid", UUID.randomUUID().toString());
			params.put("examinationid", examinationid);
			params.put("title", data_title);
			params.put("image", Base64.encodeToString(DbBitmapUtility.getBytes(bitmap),Base64.DEFAULT));
			
			ImageDB db = new ImageDB();
			db.create(params);
		}catch(Throwable e){
			new ErrorDialog(this,e).show();
			return;
		}
		disposeMe(); 
		ExaminationActivity.loadImageData();
	}
	
	private void doUpdate(){
		data_title = title.getText().toString();
		
		if(data_title.isEmpty()){
			new InfoDialog(this, "Title is required!").show();
			return;
		}
		
		if(bitmap == null){
			new InfoDialog(this, "Image is required!").show();
			return;
		}
		
		try{
			Map params = new HashMap();
			params.put("objid", objid);
			params.put("examinationid", examinationid);
			params.put("title", data_title);
			params.put("image", Base64.encodeToString(DbBitmapUtility.getBytes(bitmap),Base64.DEFAULT));
			
			ImageDB db = new ImageDB();
			db.update(params);
		}catch(Throwable e){
			new ErrorDialog(this,e).show();
			return;
		}
		disposeMe(); 
		ExaminationActivity.loadImageData();
	}

}
