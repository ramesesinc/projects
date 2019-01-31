package com.rameses.android.main;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bsh.Interpreter;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.database.AccountDB;
import com.rameses.android.database.ReadingDB;
import com.rameses.android.database.RuleDB;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;
import com.rameses.util.ObjectDeserializer;

public class ReadingSheetSearchActivity extends UserInfoMenuActivity {
	    
	private AccountDB accountdb = new AccountDB();
	private ReadingDB readingdb = new ReadingDB();
	private RuleDB ruledb = new RuleDB();
	
	private MapProxy account = new MapProxy(new HashMap());
	private MapProxy reading = new MapProxy(new HashMap());
	private AutoCompleteTextView act_search;
	private TextView tv_reading1, tv_reading2, tv_reading3, tv_reading4, tv_reading5, tv_reading6, currentTextView;
	private Button btn_save, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_clear, btn_backspace;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			// TODO Auto-generated method stub
			currentTextView = (TextView) v;
			currentTextView.getParent().requestDisallowInterceptTouchEvent(true);
			return gestureDetector.onTouchEvent(ev);
		}
	};
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Reading Sheet");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_readingsheet_search, rl_container, true);	
		
//		ImageButton ib_search = (ImageButton) findViewById(R.id.ib_search);
//		ib_search.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				println("search");
//			}
//		});
		
//		btn_save = (Button) findViewById(R.id.btn_save);
		
		act_search = (AutoCompleteTextView) findViewById(R.id.act_search);
		act_search.setThreshold(1);
		act_search.setAdapter(new ReadingSheetSearchAdapter(this, R.layout.item_account_search));
		act_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int idx, long pos) {
				println("on item click");
				String objid = view.getTag(R.string.id_tag).toString();
				
				DBContext ctx = new DBContext("main.db");
				accountdb.setDBContext(ctx);
				
				try {
					account = new MapProxy(accountdb.findByObjid(objid));
					reading = new MapProxy(readingdb.findReadingByAcctid(objid));
				} catch (Exception e) {
					UIDialog.showMessage(e, ReadingSheetSearchActivity.this);
				}
				
//				act_search.setText(account.get("acctname").toString());
				
				setValue(R.id.act_search, account.getString("acctname"));
				setValue(R.id.tv_serialno, account.getString("serialno"));
				setValue(R.id.tv_name, account.getString("acctname"));
				setValue(R.id.tv_classification, account.getString("classificationid"));
				setValue(R.id.tv_prevreading, ApplicationUtil.readingFormat(account.getString("lastreading")));
								
				act_search.setText("");
				act_search.clearFocus();
				
				if (reading.isEmpty()) {
					setReading(account.getString("lastreading"));
				} else {
					setReading(reading.getString("reading"));
				}
//				setReading("0");
			}
		});
//		act_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int idx, long pos) {
//				println("on item selected " + idx);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				
//			}
//		});
		
		gestureDetector = new GestureDetector(this, new ReadingGestureDetector());
		
		
		tv_reading1 = (TextView) findViewById(R.id.tv_reading1);		
		tv_reading2 = (TextView) findViewById(R.id.tv_reading2);		
		tv_reading3 = (TextView) findViewById(R.id.tv_reading3);		
		tv_reading4 = (TextView) findViewById(R.id.tv_reading4);		
		tv_reading5 = (TextView) findViewById(R.id.tv_reading5);		
		tv_reading6 = (TextView) findViewById(R.id.tv_reading6);
		
		tv_reading1.setOnTouchListener(gestureListener);
		tv_reading2.setOnTouchListener(gestureListener);
		tv_reading3.setOnTouchListener(gestureListener);
		tv_reading4.setOnTouchListener(gestureListener);
		tv_reading5.setOnTouchListener(gestureListener);
		tv_reading6.setOnTouchListener(gestureListener);
		
		setButtonClickListener();
		
	}
	
	void clearValue() {
		setValue(R.id.act_search, "");
		setValue(R.id.tv_serialno, "");
		setValue(R.id.tv_name, "");
		setValue(R.id.tv_classification, "");
		setValue(R.id.tv_prevreading, "");
		setReading("0");
		account = new MapProxy(new HashMap());
	}
	
	
	private String supplyRemainingZeroes(String reading) {
		String str = "";
		int s = 6 - reading.trim().length();
//		println("size " + s);
		for (int i = 0; i < s; i++) {
			str += "0";
		}
		str += reading;
		
		return str;
	}
	
	void increment() {
		
		if (currentTextView != null && !account.isEmpty()) {
			String strval = currentTextView.getText().toString();
			int intval = Integer.parseInt(strval);
			intval++;
			if (intval > 9) {
				intval = 0;
			}
			currentTextView.setText(intval + "");
//			println("increment val " + strval);
		}
	} 
	
	void decrement() {
		if (currentTextView != null && !account.isEmpty()) {
			String strval = currentTextView.getText().toString();
			int intval = Integer.parseInt(strval);
			intval--;
			if (intval < 0) {
				intval = 9;
			}
			currentTextView.setText(intval + "");
//			println("decrement val " + strval);
//			println("decrement id " + currentTextView.getId());
		}
	}
	
	void setButtonClickListener() {

		new UIAction(this, R.id.btn_save) {
			
			public void onClick() {
				SQLTransaction maindb = new SQLTransaction("main.db");
				
				try {
					maindb.beginTransaction();
					boolean flag = onClickImpl(maindb);
					maindb.commit();
					
					if (flag == true) {
						ApplicationUtil.showShortMsg("Successfully saved reading!", ReadingSheetSearchActivity.this);
					}
				} catch (Exception e) {
					UIDialog.showMessage(e, ReadingSheetSearchActivity.this);
				} finally {
					maindb.endTransaction();
				}
			}
			
			private Boolean onClickImpl(SQLTransaction maindb) throws Exception {
				accountdb.setDBContext(maindb.getContext());
				accountdb.setAutoCloseConnection(false);
				
				readingdb.setDBContext(maindb.getContext());
				readingdb.setAutoCloseConnection(false);
				
				ruledb.setDBContext(maindb.getContext());
				ruledb.setAutoCloseConnection(false);
				
//				Map account = accountdb.findByObjid(acctid);
				String info = account.get("info").toString();
				
				Map map = (Map) ObjectDeserializer.getInstance().read(info);

//				String error = "";
				int consumption = getReading();//Integer.parseInt(getValueAsString(R.id.et_reading));
				
				int prevreading = Integer.parseInt(getValue(R.id.tv_prevreading).toString());
				if (prevreading > consumption) {
					ApplicationUtil.showShortMsg("The latest reading must be greater than the previous reading!", ReadingSheetSearchActivity.this);
					return false;
				}
				
				Interpreter interpreter = new Interpreter();
	            for(Object o : map.entrySet()){
	                Map.Entry me = (Map.Entry) o;
	                Object key  = me.getKey();
	                Object val = me.getValue();
//	                println("key " + key + " val " + val);
	                if (key == null) throw new RuntimeException("Rule Error : Info key could not be found!"); //println("Rule Error : Info key could not be found!");//error = "Rule Error : Info key could not be found!";
	                if (val == null) throw new RuntimeException("Rule Error : Info key could not be found!"); //println("Rule Error : Info key could not be found!");//error = "Rule Error : Info value could not be found!";
	                interpreter.set(key != null ? key.toString() : "", val != null ? val.toString() : "");
	            }
				
				List<Map> rules = ruledb.getRules();
				Map data = new HashMap();
				boolean bool;
				String cond;
				for (int i = 0; i < rules.size(); i++) {
					data = (Map) rules.get(i);
					cond = data.get("condition").toString();
					bool = Boolean.valueOf(interpreter.eval(cond).toString());
//					println("cond " + cond + " test " + bool);
					if (bool == true) break;
					data = null;
//					println("rule " + i + " " + rules.get(i));
				}
				
				if (data == null) {
					throw new RuntimeException("Rule cannot be found.");
				}
				
				interpreter.set(MapProxy.getString(data, "var"), consumption);
				Number charge = (Number) interpreter.eval(MapProxy.getString(data, "action"));
				
//				println("charge " + num.doubleValue());

				double xamt = 0.00;
				if (account.containsKey("items")) {
					String items = account.get("items").toString();
					List<Map> list = (List<Map>) ObjectDeserializer.getInstance().read(items);
					for (int i = 0; i < list.size(); i++) {
						data = (Map) list.get(i);
						if (data.containsKey("amount")) {
							xamt += Double.parseDouble(data.get("amount").toString());
						}
					}
				}
				
				double ttl = xamt + charge.doubleValue();
				
				BigDecimal totaldue = new BigDecimal(String.valueOf(ttl)).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal amtdue = new BigDecimal(String.valueOf(charge.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);

//		        String datetime = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "hh:mm");
		        String datetime = Platform.getApplication().getServerDate().toString(); 
				Map reading = readingdb.findReadingByAcctid(account.getString("objid"));
				if (reading != null) {
					reading.put("reading", consumption);
					reading.put("consumption", consumption);
					reading.put("amtdue", amtdue);
					reading.put("totaldue", totaldue);
//					println("reading " + reading);
					maindb.update("reading", "objid = $P{objid}", reading);
//					println("updating" + maindb.find("SELECT * FROM reading WHERE objid = ?", new Object[] {reading.get("objid").toString()}));
				} else {
					reading = new HashMap();
					reading.put("objid", "RDNG" + UUID.randomUUID().toString());
					reading.put("acctid", account.get("objid").toString());
					reading.put("reading", consumption);
					reading.put("consumption", consumption);
					reading.put("amtdue", amtdue);
					reading.put("totaldue", totaldue);
					reading.put("state", "OPEN");
					reading.put("dtreading", datetime);
					reading.put("batchid", account.get("batchid").toString());
					maindb.insert("reading", reading);
//					println("saving");
				}
				
//				Map params = new HashMap();
//				params.put("consumption", vol);
//				params.put("")
//				maindb.update
				
				
//				int prev = Integer.parseInt(getValue(R.id.tv_prevreading).toString());
//				int curr = Integer.parseInt(getValue(R.id.et_reading).toString());
//				
//				println("previous reading " + prev + " current reading " + curr);
				clearValue();
				return true;
			}
		};
		
		new UIAction(this, R.id.btn_0) {
			public void onClick() {
//				println("0");
				setReadingOnClick(0);
			}
		};
		
		new UIAction(this, R.id.btn_1) {
			public void onClick() {
//				println("1");
				setReadingOnClick(1);
			}
		};
		
		new UIAction(this, R.id.btn_2) {
			public void onClick() {
				setReadingOnClick(2);
			}
		};
		
		new UIAction(this, R.id.btn_3) {
			public void onClick() {
				setReadingOnClick(3);
			}
		};
		
		new UIAction(this, R.id.btn_4) {
			public void onClick() {
				setReadingOnClick(4);
			}
		};
		
		new UIAction(this, R.id.btn_5) {
			public void onClick() {
				setReadingOnClick(5);
			}
		};
		
		new UIAction(this, R.id.btn_6) {
			public void onClick() {
				setReadingOnClick(6);
			}
		};
		
		new UIAction(this, R.id.btn_7) {
			public void onClick() {
				setReadingOnClick(7);
			}
		};
		
		new UIAction(this, R.id.btn_8) {
			public void onClick() {
				setReadingOnClick(8);
			}
		};
		
		new UIAction(this, R.id.btn_9) {
			public void onClick() {
				setReadingOnClick(9);
			}
		};
		
		new UIAction(this, R.id.btn_clear) {
			public void onClick() {
				setReading("0");
			}
		};
		
		new UIAction(this, R.id.btn_backspace) {
			public void onClick() {
				String reading = String.valueOf(getReading());
				int end = 0;
				if (reading.length() > 1) end = reading.length() - 1; 
				reading = reading.substring(0, end);
				setReading(reading);
			}
		};
	}
	
	private int getReading() {
		String reading = "0";
		
		reading = String.valueOf(getValue(R.id.tv_reading1));
		reading += String.valueOf(getValue(R.id.tv_reading2));
		reading += String.valueOf(getValue(R.id.tv_reading3));
		reading += String.valueOf(getValue(R.id.tv_reading4));
		reading += String.valueOf(getValue(R.id.tv_reading5));
		reading += String.valueOf(getValue(R.id.tv_reading6));
		
		return Integer.parseInt(reading);
	}
	
	private void setReadingOnClick(int val) {
		String reading = String.valueOf(getReading());
		int s = reading.length();
		
		if (s > 5) {
			int start = s - 5;
			reading = reading.substring(start, reading.length());
		}
		
		reading += val;
		setReading(reading);
	}
	
	private void setReading(String reading) {
		if (!account.isEmpty()) {
			String r = supplyRemainingZeroes(reading);
			
			setValue(R.id.tv_reading1, r.charAt(0) + "");
			setValue(R.id.tv_reading2, r.charAt(1) + "");
			setValue(R.id.tv_reading3, r.charAt(2) + "");
			setValue(R.id.tv_reading4, r.charAt(3) + "");
			setValue(R.id.tv_reading5, r.charAt(4) + "");
			setValue(R.id.tv_reading6, r.charAt(5) + "");
		}

//		np1.setValue(Integer.parseInt(r.charAt(0) + ""));
//		np2.setValue(Integer.parseInt(r.charAt(1) + ""));
//		np3.setValue(Integer.parseInt(r.charAt(2) + ""));
//		np4.setValue(Integer.parseInt(r.charAt(3) + ""));
//		np5.setValue(Integer.parseInt(r.charAt(4) + ""));
//		np6.setValue(Integer.parseInt(r.charAt(5) + ""));
//		setValue(R.id.et_reading, reading);
	}

	void println(String msg) {
		Log.i("ReadingSheetActivity", msg);
	}

	class ReadingGestureDetector extends SimpleOnGestureListener {
		
		@Override
		public boolean onSingleTapUp(MotionEvent ev) {
//			println("on single tap up");
		 
			return true;
		} 
		
		@Override
		public void onShowPress(MotionEvent ev) {
//		    println("on show press");
		 
		}
		  
		@Override
		public void onLongPress(MotionEvent ev) {
//		    println("on long press");
		  
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//		    println("on scroll");
		 
			return true;
		}
		  
		@Override
		public boolean onDown(MotionEvent ev) {
//		    println("on down");
		  
			return true;
		}
		
		public boolean onFling (MotionEvent ev1, MotionEvent ev2, float velocityX, float velocityY) {
			String swipe = "";
			float sensitvity = 0;
			  
			   // TODO Auto-generated method stub
//			if((ev1.getX() - ev2.getX()) > sensitvity){
//				swipe += "Swipe Left\n";
//			} else if((ev2.getX() - ev1.getX()) > sensitvity){
//				swipe += "Swipe Right\n";
//			} else{
//				swipe += "\n";
//			}
			   
//			println("ev1 " + ev1.getY() + " ev2 " + ev2.getY());
//			println("(ev1 - ev2) -> " + (ev1.getY() - ev2.getY()) + " (ev2 - ev1) -> " + (ev2.getY() - ev1.getY()));
			if((ev1.getY() - ev2.getY()) > sensitvity){
				increment();
//				swipe += "Swipe Up\n";
			} else if((ev2.getY() - ev1.getY()) > sensitvity){
				decrement();
//				swipe += "Swipe Down\n";
			}
			
			
//			if (currentTextView != null) {
//				println(currentTextView.getText().toString());
//			}
			//tv.setText(swipe);
			
			return super.onFling(ev1, ev2, velocityX, velocityY);
		}
	}
}
