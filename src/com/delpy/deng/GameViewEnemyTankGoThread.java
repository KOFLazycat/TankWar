package com.delpy.deng;

import java.util.Random;

import android.util.Log;

/*
 * ���ɵз�̹�ˣ�ÿ�������޸ĵз�̹�˵�λ�ã����������ƶ��������ӵ��Ȳ���
 */
public class GameViewEnemyTankGoThread extends Thread {
	GameView gv;
	boolean flag=false;
	int sleepSpan=400;				//400����һ������
	Random rand;
	int i;
	public GameViewEnemyTankGoThread(GameView father){
		this.gv=father;
		flag=true;
		rand=new Random(System.currentTimeMillis());
	}
	public void run(){
		while(flag){
			if(!gv.father.isPause){
				synchronized (gv.enemyTanks) {
					for(int i=0;i<gv.enemyTanks.size();i++){
						Tank enemyTank=gv.enemyTanks.get(i);
						if(enemyTank.blood<=0){				//���̹���Ѿ�û��Ѫ�ˣ�ɾ��֮
							int score=enemyTank.rawBlood;
							gv.score+=score;
							gv.leftEnemyTanks--;
							gv.screenEnemyTanks--;
							gv.enemyTanks.remove(i);
							Log.d("EnemyTanks","����̹��ʣ������"+gv.leftEnemyTanks);
							if(gv.hasBonu==false){				//�����û�г��ֵ��ߣ����������һ��1-8�ŵ���
								int getBonus=rand.nextInt(100);
								Log.d("Bonus","hasBonu="+gv.hasBonu+" getBonus="+getBonus);
								if(getBonus<20){
									int typeBonus=rand.nextInt(8)+1;
									gv.hasBonu=true;
									gv.bonu=new Bonus(gv,typeBonus,enemyTank.row,enemyTank.col);
								}
							}
						}											
					}
				}			
				if(gv.leftEnemyTanks<=0){		//�������û��̹���ˣ������һ��
					gv.father.myHandler.sendEmptyMessage(2);
				}	
				if(gv.enemyTanks.size()<6 && gv.leftEnemyTanks>=6){	//�����Ļ�ϵ�̹����С��6�һ��ез�̹�ˣ�����һ��̹��
					int nRand=rand.nextInt(15)+10;					//������ӵ�����
					float vFire=nRand/100.0f;
					//new��һ��̹�ˣ������ֱ��ʾ��GameView������̹�����ͣ������أ���ʼǰ�������ٶȣ������ӵ��ٶȣ�ɱ����
					Tank oneTank;
					oneTank=new Tank(gv,rand.nextInt(2)+1,rand.nextInt(3)+1,2,1,vFire,500+rand.nextInt(500));
					gv.enemyTanks.add(oneTank);
					gv.screenEnemyTanks++;
					Log.d("screenEnemyTanks","screenEnemyTanks:"+gv.screenEnemyTanks);
				}
				if(!gv.hasGetClockBonu){										//���û�нӵ�ʱ�ӵ��ߣ����ƶ�̹�ˣ�������
					synchronized (gv.enemyTanks) {
						for(i=0;i<gv.enemyTanks.size();i++){					//�ƶ�̹�ˣ�����̹�˷��ӵ�
							Tank oneTank=gv.enemyTanks.get(i);
							oneTank.autoFire();						
							boolean has=false;
							for(int j=0;j!=i && j<gv.enemyTanks.size();j++){	//��i��̹��û������̹�˷�����ײ
								if(oneTank.hasCrashWithOtherTank(gv.enemyTanks.get(j))){
									has=true;
									break;
								}									
							}
							if(oneTank.hasCrashWithOtherTank(gv.myTank)){
								has=true;
							}
							if(!has){
								oneTank.autoMoveTank();	
							}
						}
					}				
				}
			}
			try{				
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
}
