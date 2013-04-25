package com.delpy.deng;

import android.util.Log;

/*
 * �߳��ࣺ���������ҷ�̹�˵��ƶ������ɡ���ײ��⼰������
 */
public class GameViewMyTankDoThread extends Thread {
	GameView gv;
	boolean flag=false;
	int sleepSpan=50;
	public GameViewMyTankDoThread(GameView father){
		this.gv=father;
		flag=true;
	}
	public void run(){
		while(flag){
			if(!gv.father.isPause){
				if(!gv.gameOver){		//�����û��gameOver
					if(!gv.hasGetAliveBonu && gv.myTank.crashWithOtherBullets()){	//�ڷ��޵�״̬�£�����Ƿ���з��ӵ�������ײ
						if(gv.myTank.blood<=0){
							gv.myTank=null;
							if(gv.nMyTanks<=0){					//���û̹���ˣ���gameOver
								gv.gameOver=true;	
								gv.noMoreTanks=true;
								Log.d("GameOver","GameOver "+gv.gameOver);
								gv.father.isGameOver=true;
								gv.father.myHandler.sendEmptyMessage(200);
								continue;
							}
						}
					}
					if((gv.myTank==null || (gv.myTank!=null && gv.myTank.blood<=0)) && gv.nMyTanks>0){	//����ҷ�̹��ѪΪ0,����̹�ˣ���newһ��
						gv.initMyTank(gv.nowGuanKa);
					}
					if(gv.hasBonu==true && gv.bonu!=null && gv.myTank.hasGetABonu()){			//����Ե���һ��Bonus�����д���
						synchronized (gv.bonu) {
							switch(gv.bonu.type){			//����Bonus�����ͽ�����Ӧ�Ĵ���
							case 1:							//ʱ�ӣ�����Ŀǰ��Ļ�����е��ӵ���ʧ��2�����ڲ����ƶ������ܷ���
								gv.hasGetClockBonu=true;
								synchronized (gv.bulletsList) {
									for(int i=0;i<gv.bulletsList.size();i++){
										Bullet oneBullet=gv.bulletsList.get(i);
										oneBullet=null;
									}
									gv.bulletsList.clear();
								}
								new Thread(){						//2���Ӻ󣬵��˿����ƶ�
									public void run(){
										try{
											Thread.sleep(1000*120);
										}catch(Exception e){
											e.printStackTrace();
										}
										gv.hasGetClockBonu=false;
									}
								}.start();
								break;
							case 2:							//ը����Ŀǰ��Ļ�����еĵ���̹�˾�����
								synchronized (gv.enemyTanks) {
									int nEnemyTanks=gv.enemyTanks.size();
									for(int i=0;i<gv.enemyTanks.size();i++){
										Tank oneTank=gv.enemyTanks.get(i);
										oneTank=null;										
									}
									gv.enemyTanks.clear();
									gv.screenEnemyTanks=0;
									gv.leftEnemyTanks-=nEnemyTanks;
								}
								break;								
							case 3:							//���ӣ����Լ����ϼ��ý��ʯ����������ֻ����2����
								synchronized (gv.maps) {
									gv.makeTargetWith(2);		//���ú��������Լ����ϼҵ�������2ȡ��
								}
								new Thread(){
									public void run(){
										try{
											Thread.sleep(1000*120);
										}catch(Exception e){
											e.printStackTrace();
										}
										gv.makeTargetWith(1);
									}
								}.start();
								break;
							case 4:								//̹�ˣ��ҷ�̹������һ
								gv.nMyTanks++;
								break;
							case 5:								//�ҷ�̹�˼���Ѫ
								gv.myTank.blood=750;
								break;
							case 6:								//�޵�״̬
								gv.hasGetAliveBonu=true;
								new Thread(){						//2���Ӻ��޵�״̬��ʧ
									public void run(){
										try{
											Thread.sleep(1000*120);
										}catch(Exception e){
											e.printStackTrace();
										}
										gv.hasGetAliveBonu=false;
									}
								}.start();
								break;
							case 7:									//�ִ�
								gv.hasGetShipBonu=true;
								new Thread(){						//2���Ӻ��޵�״̬��ʧ
									public void run(){
										try{
											Thread.sleep(1000*120);
										}catch(Exception e){
											e.printStackTrace();
										}
										gv.hasGetShipBonu=false;
										synchronized (gv.myTank) {
											gv.myTank.row=38;
											gv.myTank.col=11;
										}
									}
								}.start();
								break;
							case 8:									//���ǣ���������Ϊ1000
								gv.hasGetPowerBonu=true;
								new Thread(){						//2���Ӻ󣬹��������±�Ϊ500
									public void run(){
										try{
											Thread.sleep(1000*120);
										}catch(Exception e){
											e.printStackTrace();
										}
										gv.hasGetPowerBonu=false;
									}
								}.start();
								break;
							}
						}
						gv.bonu.status=2;					//״̬2��Bonus�Ѿ������ˣ������̸߳������һ���µ�Bonus
					}
					if(gv.myTank!=null){
						if(gv.father.keyCode==62){			//�û����Ŀհ׼��������ӵ���ֱ�ӷ���
							gv.father.keyCode=0;
							gv.myTank.manualFire();
						}else{								//�����
							boolean hasCrashOtherTank=false;
							synchronized (gv.enemyTanks) {	//����Ƿ���ĳ��̹�˷�������ײ
								for(int i=0;i<gv.enemyTanks.size();i++){
									if(gv.myTank.hasCrashWithOtherTank(gv.enemyTanks.get(i))){
										hasCrashOtherTank=true;
										break;
									}
								}
							}
							switch(gv.father.keyCode){
							case 19:						//��
								if(gv.myTank.dir!=1)		//ԭ���ķ��������ϣ���ֻת�䷽��Ϊ����
									gv.myTank.dir=1;
								else if(!hasCrashOtherTank)	//�������ǰ��û��̹�ˣ�����ͼ������
									gv.myTank.moveUp();
								gv.father.keyCode=0;
								break;
							case 20:						//��
								if(gv.myTank.dir!=2)
									gv.myTank.dir=2;
								else if(!hasCrashOtherTank)
									gv.myTank.moveDown();
								gv.father.keyCode=0;
								break;
							case 21:						//��
								if(gv.myTank.dir!=3)
									gv.myTank.dir=3;
								else if(!hasCrashOtherTank)
									gv.myTank.moveLeft();
								gv.father.keyCode=0;
								break;
							case 22:						//��
								if(gv.myTank.dir!=4)
									gv.myTank.dir=4;
								else if(!hasCrashOtherTank)
									gv.myTank.moveRight();
								gv.father.keyCode=0;
								break;
							}						
						}
					}
				}
			}
			try{
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				
			}
		}
	}	
}
