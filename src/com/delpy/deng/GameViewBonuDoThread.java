package com.delpy.deng;

import android.util.Log;

/*
 * �ࣺÿ50��������ߵ�״̬�������״̬�Ѿ���Ϊ2��3�ˣ��򽫸õ����ջ�
 */
public class GameViewBonuDoThread extends Thread {
	GameView gv;
	boolean flag;
	int sleepSpan=50;
	public GameViewBonuDoThread(GameView gv){
		this.gv=gv;
		flag=true;
	}
	public void run(){
		while(flag){
//			Log.d("hasBonus","hasBonus"+gv.hasBonu);
			if(!gv.father.isPause){
				if(gv.hasBonu==true && gv.bonu!=null && gv.bonu.status!=1){
					gv.hasBonu=false;
					gv.bonu=null;
				}
			}
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
		}
	}
}
