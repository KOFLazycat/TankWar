package com.delpy.deng;
/*
 * ��Ϸ��̹�˴�ս��������Ϸ
 * ������Delpy Deng
 * ��Ȩ���У���������ѧӦ�ÿ�ѧѧԺ
 * ��Ϸ����˵�����������ҷ���������ҷ�̹���˶����򣬿հ׼������ӵ����س�����ͣ/�ָ���Ϸ��Menu�������˵�
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TankWarActivity extends Activity {
	TankWarActivity context;
	GameView gv=null;
	View currentView;
	int guanKa;
	int allGuanKa;
	boolean isGameOver;
	boolean isPause;
	int keyCode;		//�û������������ң�19,20,21,22,�հף�62,�س���66,
	private static final int ITEM_RESTART=Menu.FIRST;
	private static final int ITEM_EXIT=Menu.FIRST+1;
	private static final int ITEM_PAUSE=Menu.FIRST+2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        guanKa=0;
        isPause=false;
        allGuanKa=Maps.maps.length;
        initGameView();
    }
    Handler myHandler=new Handler(){
    	public void handleMessage(Message msg) {
    		switch(msg.what){
    		case 0:									//�˳���Ϸ
    			finish();
    			break;
    		case 1:									//���¿�ʼ��Ϸ
    			initGameView();
    			break;
    		case 2:									//�ɹ�����һ��
    			guanKa++;
    			if(guanKa>=allGuanKa){
    				guanKa=0;
    			}
    			initGameView();
    			break;
    		case 100:								//Game Over
    			isGameOver=true;
    			if(gv!=null){
    				gv.gameOver=true;
    			}
    			break;
    		case 200:								//�ҷ�û��̹����
    			isGameOver=true;
    			if(gv!=null){
    				gv.noMoreTanks=true;
    				gv.gameOver=true;
    			}
    		}
    		super.handleMessage(msg);
    	}
    };
    public boolean onTouchEvent(MotionEvent event){		//��Ļ��������
		int X=(int)(event.getX());
		int Y=(int)(event.getY());
		Log.d("Event","Position:("+X+","+Y+")");
		Rect rUp,rDown,rLeft,rRight,rFire;
		rUp=new Rect(140,100,180,180);
		rDown=new Rect(140,240,180,320);
		rLeft=new Rect(0,190,80,230);
		rRight=new Rect(240,190,320,230);
		rFire=new Rect(140,360,200,400);
		if(currentView==gv && gv!=null)
		{
			if(rUp.contains(X,Y)){
				keyCode=19;
			}else if(rDown.contains(X,Y)){
				keyCode=20;
			}else if(rLeft.contains(X,Y)){
				keyCode=21;
			}else if(rRight.contains(X,Y)){
				keyCode=22;
			}
			if(rFire.contains(X,Y)){			//����
				keyCode=62;
			}
		}
		if(currentView==gv && gv!=null && gv.gameOver==true || gv==null){
			if(isGameOver){
				final AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("��ѡ��").setPositiveButton("������Ϸ", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myHandler.sendEmptyMessage(1);
					}
				}).setNegativeButton("�˳���Ϸ", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myHandler.sendEmptyMessage(0);
					}
				});
				AlertDialog ad=builder.create();
				ad.show();
			}
		}
		return super.onTouchEvent(event);
    }
    public void initGameView(){
    	long lScore=0;
    	int nMyTanks=gv.maxMyTanks;
    	boolean passACard=false;
    	if(gv!=null){
    		if(gv.nMyTanks>=gv.maxEnemyTanks){
    			nMyTanks=gv.nMyTanks;
    			lScore=gv.score;
    		}
    		passACard=true;
    	}
    	isGameOver=false;
        gv=new GameView(this,guanKa);
        if(passACard==true){
        	gv.nMyTanks=nMyTanks;
        	gv.score=lScore;
        }
        setContentView(gv);
        currentView=(View)gv;
        
    }
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	this.keyCode=keyCode;			//keyCode���û��İ�����19���ϣ�20���£�21����22���ң��հף�62,�س���66,
//    	Log.d("keyCode","keyCode="+keyCode);
    	if(keyCode==66){				//�س���Ϊ��ͣ��
    		this.isPause=!this.isPause;    		
    	}
    	return false;
    }
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0,ITEM_RESTART,0,"������Ϸ");
    	if(!isPause){
    		menu.add(0,ITEM_PAUSE,0,"��ͣ��Ϸ");
    	}else{
    		menu.add(0,ITEM_PAUSE,0,"�ָ���Ϸ");
    	}
    	menu.add(0,ITEM_EXIT,0,"�˳���Ϸ");
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case ITEM_RESTART:
    		myHandler.sendEmptyMessage(1);
    		break;
    	case ITEM_EXIT:
    		myHandler.sendEmptyMessage(0);
    		break;
    	case ITEM_PAUSE:
    		this.isPause=!this.isPause;
    		if(isPause)
    			item.setTitle("�ָ���Ϸ");
    		else
    			item.setTitle("��ͣ��Ϸ");
    	}
    	return true;
    }
}