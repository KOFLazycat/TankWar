package com.delpy.deng;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/*
 * ̹���࣬���е�̹�˾��Ӹ���̳�������
 */
public class Tank {
	GameView gv;
	int enemy;				//���ҵ�ʶ��1��2��ʾ���ˣ�0��ʾ�Լ�
	int row,col;			//̹�����꣬ע�ⲻ��������Ϊ��λ�������Կ�Ϊ��λ������̹���ڷ���е��к����к�
							//ÿ�鶼��10*10���ش�С���к����к��Ǻ�����ת��Ϊ����������ģ�X=col*10��Y=row*10
	int birthLocation;		//����ʱ��λ�ñ�־��һ��������ط��ɳ���̹��12345�ֱ��ʾ���Ͻǡ��������롢���Ͻǡ��Ǳ��󡢳Ǳ���
	int dir;				//̹�˵��˶�����1234�ֱ��ʾ�ϡ��¡�����
	int v;					//̹�˵��˶��ٶȣ��Կ�Ϊ��λ����ÿ����ʱ��������ǰ�ƶ����ٿ飨10���أ�
	float vFire;			//̹�˴��ӵ���Ƶ�ʣ�ÿ����ʱ�����ڴ���ٸ��ӵ�����һ��С������0.25,���ʾÿ4�����ڴ�һ���ӵ�
	float fire;				//��ÿ������������vFireֵ������ֵ����1ʱ�㷢��һ���ӵ��������Լ���1,�Ӷ��ɿ��Ʒ����ӵ���Ƶ��
	Bitmap bmp;				//̹�˵�ͼƬ���������͡��˶�����ͬ����ͬ��ͼƬ��Դ�Ѿ���GameView���н����˶������ʼ��
	int blood;				//��̹�˵�Ѫ��ÿ����һ���ӵ���blood�������Ѫ����Ѫ��Ϊ0�󣬸�̹�˱�����
	int rawBlood;
	Random rand;			//������� ��
	public Tank(GameView gv,int enemy,int birthLocation,int dir,int v,float vFire,int blood){
		this.gv=gv;
		this.enemy=enemy;
		switch(birthLocation){
		case 1:						//���Ͻ�
			this.row=this.col=0;
			break;
		case 2:						//��������
			this.row=0;
			this.col=15;
			break;
		case 3:						//���ҽ�
			this.row=0;
			this.col=30;
			break;
		case 4:						//�Ǳ���
			this.row=38;
			this.col=11;
			break;
		case 5:						//�Ǳ���
			this.row=38;
			this.col=19;
			break;
		}
		this.dir=dir;
		this.v=v;
		this.vFire=vFire;
		this.fire=0;
		this.blood=blood;
		this.rawBlood=blood;
		bmp=getTankBitmap(enemy,dir);
		rand=new Random(System.currentTimeMillis());		//�õ�ǰʱ�������ӣ�new�����������
	}
	public Bitmap getTankBitmap(int enemy,int dir){
		Bitmap bmp=null;
		if(enemy==0){						//�ҷ�̹��
			if(gv.hasGetShipBonu){			//����õ��ִ�����
				switch(dir){
				case 1:
					bmp=gv.bmpMyTankRiverUp;break;
				case 2:
					bmp=gv.bmpMyTankRiverDown;break;
				case 3:
					bmp=gv.bmpMyTankRiverLeft;break;
				case 4:
					bmp=gv.bmpMyTankRiverRight;break;
				}
			}else if(gv.hasGetAliveBonu){
				switch(dir){
				case 1:
					bmp=gv.bmpMyTankAliveUp;break;
				case 2:
					bmp=gv.bmpMyTankAliveDown;break;
				case 3:
					bmp=gv.bmpMyTankAliveLeft;break;
				case 4:
					bmp=gv.bmpMyTankAliveRight;break;
				}
			}else{
				switch(dir){
				case 1:
					bmp=gv.bmpMyTankUp;break;
				case 2:
					bmp=gv.bmpMyTankDown;break;
				case 3:
					bmp=gv.bmpMyTankLeft;break;
				case 4:
					bmp=gv.bmpMyTankRight;break;
				}
			}
		}else if(enemy==1){					//�з�̹��1
			switch(dir){
			case 1:
				bmp=gv.bmpAnyTank1Up;break;
			case 2:
				bmp=gv.bmpAnyTank1Down;break;
			case 3:
				bmp=gv.bmpAnyTank1Left;break;
			case 4:
				bmp=gv.bmpAnyTank1Right;break;
			}
		}else if(enemy==2){					//�з�̹��2
			switch(dir){
			case 1:
				bmp=gv.bmpAnyTank2Up;break;
			case 2:
				bmp=gv.bmpAnyTank2Down;break;
			case 3:
				bmp=gv.bmpAnyTank2Left;break;
			case 4:
				bmp=gv.bmpAnyTank2Right;break;
			}
		}
		return bmp;
	}
	public void draw(Canvas canvas){	//����Ļ�Ͻ��Լ�������		
		bmp=getTankBitmap(enemy,dir);
		canvas.drawBitmap(bmp, col*10, row*10, null);
	}
	public void autoMoveTank(){				//ÿ�������Զ��ƶ��Լ�
		int newRand=rand.nextInt(100);		//����0-99֮�������
		if(newRand<85){						//80%�Ļ�������ԭ���ķ�����
			
		}else{								//�������Ϊ��һ����
			dir=rand.nextInt(4)+1;	//�õ��µķ���
		}
		switch(dir){						//�ж�ǰ���Ƿ���·���ߣ������·�����ķ�����֮
		case 1:								//������
			moveUp();
			break;
		case 2:
			moveDown();
			break;
		case 3:
			moveLeft();
			break;
		case 4:
			moveRight();
			break;           
		}
	}
	public void autoFire(){					//ÿ�������Զ������ӵ�
		this.fire+=this.vFire;				//�����Ƿ�Ҫ���ӵ��������ڵ���1ʱ���㷢���ӵ�
		if(fire>=1.0f){
			synchronized(gv.bulletsList){
				int nRand=rand.nextInt(3);
				int R=nRand==0?2:nRand==1?3:5;
				int bRow,bCol;
				if(dir==1 || dir==2){		//������ϻ����£��ӵ���row��col
					bRow=row;
					bCol=col+1;
				}else {
					bRow=row+1;
					bCol=col;
				}
				Bullet oneBullet=new Bullet(gv,enemy,bRow,bCol,dir,1,R);
				gv.bulletsList.add(oneBullet);		//�����ӵ����뵽�ӵ�������
				fire-=1.0;
			}			
		}
	}
	public void manualFire(){				//����̹���ֶ������ӵ�
		this.fire+=this.vFire;
		if(fire>=1.0f){
			synchronized (gv.myBulletsList) {
				Bullet oneBullet;
				int bRow,bCol;
				if(dir==1 || dir==2){		//������ϻ����£��ӵ���row��col
					bRow=row;
					bCol=col+1;
				}else {
					bRow=row+1;
					bCol=col;
				}
				if(!gv.hasGetPowerBonu){
					oneBullet=new Bullet(gv,0,bRow,bCol,dir,1,3);
				}else{
					oneBullet=new Bullet(gv,0,bRow,bCol,dir,1,5);
				}
				gv.myBulletsList.add(oneBullet);
				fire-=1.0;
			}			
		}
	}
	public void moveUp(){		//����������ߣ���������һ��
		if(enemy!=0 || (enemy==0 && !gv.hasGetShipBonu)){	//����̹�ˣ��������ҷ�̹�˵���û�нӵ��ִ�����
			if((row>0 && (gv.maps[row-1][col]==0 || gv.maps[row-1][col]==3))&&
					(col+1<=31 && (gv.maps[row-1][col+1]==0 || gv.maps[row-1][col+1]==3)) ){
				row--;
			}
		}		
		if(enemy==0 && gv.hasGetShipBonu){		//����ҷ�̹��ȡ�����ִ����ߣ������Թ�����Ϊ4��С��
			if((row>0 && (gv.maps[row-1][col]==0 || gv.maps[row-1][col]==3 || gv.maps[row-1][col]==4))&&
					(col+1<=31 && (gv.maps[row-1][col+1]==0 || gv.maps[row-1][col+1]==3 || gv.maps[row-1][col+1]==4)) ){
				row--;
			}
		}
	}
	public void moveDown(){
		if(enemy!=0 || (enemy==0 && !gv.hasGetShipBonu)){	//����̹�ˣ��������ҷ�̹�˵���û�нӵ��ִ�����
			if(row<38 && (gv.maps[row+2][col]==0 || gv.maps[row+2][col]==3) &&
					(col+1<=31 && (gv.maps[row+2][col+1]==0 || gv.maps[row+2][col+1]==3)) ){
				row++;
			}
		}
		if(enemy==0 && gv.hasGetShipBonu){		//����ҷ�̹��ȡ�����ִ����ߣ������Թ�����Ϊ4��С��
			if(row<38 && (gv.maps[row+2][col]==0 || gv.maps[row+2][col]==3 || gv.maps[row+2][col]==4) &&
					(col+1<=31 && (gv.maps[row+2][col+1]==0 || gv.maps[row+2][col+1]==3 || gv.maps[row+2][col+1]==4)) ){
				row++;
			}
		}
	}
	public void moveLeft(){
		if(enemy!=0 || (enemy==0 && !gv.hasGetShipBonu)){	//����̹�ˣ��������ҷ�̹�˵���û�нӵ��ִ�����
			if(col>0 && (gv.maps[row][col-1]==0 || gv.maps[row][col-1]==3) &&
					(row<39 && (gv.maps[row+1][col-1]==0 || gv.maps[row+1][col-1]==3))){
				col--;
			}
		}
		if(enemy==0 && gv.hasGetShipBonu){		//����ҷ�̹��ȡ�����ִ����ߣ������Թ�����Ϊ4��С��
			if(col>0 && (gv.maps[row][col-1]==0 || gv.maps[row][col-1]==3 || gv.maps[row][col-1]==4) &&
					(row<39 && (gv.maps[row+1][col-1]==0 || gv.maps[row+1][col-1]==3 || gv.maps[row+1][col-1]==4))){
				col--;
			}
		}
	}
	public void moveRight(){
		if(enemy!=0 || (enemy==0 && !gv.hasGetShipBonu)){	//����̹�ˣ��������ҷ�̹�˵���û�нӵ��ִ�����
			if(col<30 && (gv.maps[row][col+2]==0 || gv.maps[row][col+2]==3) &&
				(row<39 && (gv.maps[row+1][col+2]==0 || gv.maps[row+1][col+2]==3))){
				col++;
			}
		}
		if(enemy==0 && gv.hasGetShipBonu){		//����ҷ�̹��ȡ�����ִ����ߣ������Թ�����Ϊ4��С��
			if(col<30 && (gv.maps[row][col+2]==0 || gv.maps[row][col+2]==3 || gv.maps[row][col+2]==4) &&
					(row<39 && (gv.maps[row+1][col+2]==0 || gv.maps[row+1][col+2]==3 || gv.maps[row+1][col+2]==4))){
					col++;
				}
		}
		
	}
	public boolean crashWithOtherBullets(){			//����Ƿ���Է��ӵ���ײ��
		boolean hasCrash=false;
		if(enemy==0){								//�Լ�������̹��
			synchronized (gv.bulletsList) {
				for(int i=0;i<gv.bulletsList.size();i++){		//�����еĵз��ӵ����м��
					int rEnemyBullet=gv.bulletsList.get(i).row;
					int cEnemyBullet=gv.bulletsList.get(i).col;
//					if(row==rEnemyBullet && col==cEnemyBullet || 		//����Ľ������ӵ�
//							row==rEnemyBullet && col==cEnemyBullet+1 ||
//							row==rEnemyBullet+1 && col==cEnemyBullet ||
//							row==rEnemyBullet+1 && col==cEnemyBullet+1 ){
//						this.blood-=gv.bulletsList.get(i).power;		//Ѫ��power�����ӵ���ɱ������
//						Log.d("MyTankBlood","MyTankBlood="+this.blood);
//						gv.bulletsList.remove(i);						//�з��ӵ����
//						hasCrash=true;
//						break;
//					}
					int xEnemyBullet=cEnemyBullet*10;					//�ӵ���������
					int yEnemyBullet=rEnemyBullet*10;
					int xMyTank=(col+1)*10;								//�ҷ�̹���������꣬ע��λ��
					int yMyTank=(row+1)*10;
					int realDistance=(xMyTank-xEnemyBullet)*(xMyTank-xEnemyBullet)+
						(yMyTank-yEnemyBullet)*(yMyTank-yEnemyBullet);	//����ʵ�ʾ����ƽ��
					int minDistance=(10+gv.bulletsList.get(i).R)*(10+gv.bulletsList.get(i).R);	//������С�����ƽ����һ��С��������룬��˵���Ѿ���ײ
					if(realDistance<=minDistance){						//�����������ײ
						this.blood-=gv.bulletsList.get(i).power;		//Ѫ��power�����ӵ���ɱ������
						Log.d("MyTankBlood","MyTankBlood="+this.blood);
						gv.bulletsList.remove(i);						//�з��ӵ����
						hasCrash=true;
						break;
					}
				}
			}			
		}else{										//�з�̹��
			synchronized (gv.myBulletsList) {
				for(int i=0;i<gv.myBulletsList.size();i++){
					int rMyBullet=gv.myBulletsList.get(i).row;
					int cMyBullet=gv.myBulletsList.get(i).col;
					int xMyBullet=cMyBullet*10;
					int yMyBullet=rMyBullet*10;
					int xEnemyTank=(col+1)*10;
					int yEnemyTank=(row+1)*10;
					int realDistance=(xEnemyTank-xMyBullet)*(xEnemyTank-xMyBullet)+
						(yEnemyTank-yMyBullet)*(yEnemyTank-yMyBullet);	//����ʵϰ����
					int minDistance=(10+gv.myBulletsList.get(i).R)*(10+gv.myBulletsList.get(i).R);			//������С���룬һ��С��������룬��˵���Ѿ���ײ
					if(realDistance<minDistance){
						this.blood-=gv.myBulletsList.get(i).power;
						gv.myBulletsList.remove(i);
						hasCrash=true;
						break;
					}
				}
			}
//			synchronized (gv.myBulletsList) {
//				for(int i=0;i<gv.myBulletsList.size();i++){
//					Bullet myBullets=gv.myBulletsList.get(i);
//					int rMyBullet=myBullets.row;
//					int cMyBullet=myBullets.col;
//					if(row==rMyBullet && col==cMyBullet || 		//����Ľ������ӵ�
//							row==rMyBullet && col==cMyBullet+1 ||
//							row==rMyBullet+1 && col==cMyBullet ||
//							row==rMyBullet+1 && col==cMyBullet+1 ){
//						this.blood-=myBullets.power;		//Ѫ��power���ҷ��ӵ���ɱ������
//						Log.d("EnemyTankBlood","EnemyTankBlood="+this.blood);
//						gv.myBulletsList.remove(i);						//�ҷ��ӵ����
//						hasCrash=true;
//						break;
//					}
//				}
//			}			
		}
		return hasCrash;
	}
	public boolean hasCrashWithOtherTank(Tank oneTank){			//����Ƿ�����̹�˷�������ײ���Ծ����Ƿ���ǰ��
		boolean has=false;
		if(oneTank==null){
			return has;
		}
		switch(dir){
		case 1:
			if(row>1 && oneTank.row==row-2 && 
					((oneTank.col==col) || (col>0 && oneTank.col==col-1) || (col<31 && oneTank.col==col+1))){
				has=true;
				return has;
			}
			break;
		case 2:
			if(row<38 && oneTank.row==row+2 &&
					((oneTank.col==col) || (col>0 && oneTank.col==col-1) || (col<31 && oneTank.col==col+1))){
				has=true;
				return has;
			}
			break;
		case 3:
			if(col>1 && oneTank.col==col-2 &&
					((oneTank.row==row) || (row>0 && oneTank.row==row-1) || (row<39 && oneTank.row==row+1))){
				has=true;
				return has;
			}
			break;
		case 4:
			if(col<30 && oneTank.col==col+2 && 
					((oneTank.row==row) || (row>0 && oneTank.row==row-1) || (row<39 && oneTank.row==row+1))){
				has=true;
				return has;
			}
			break;
		}			
		return has;
	}
	public boolean hasGetABonu(){	//�жϱ�̹���Ƿ�ȡ����һ����Ʒ
		boolean has=false;
		synchronized (gv.bonu) {
			if(gv.hasBonu==true && gv.bonu!=null){
				if(row==gv.bonu.row && col==gv.bonu.col || row+1==gv.bonu.row && col==gv.bonu.col ||
						row==gv.bonu.row && col+1==gv.bonu.col || row+1==gv.bonu.row && col+1==gv.bonu.col)
					has=true;
			}
		}		
		return has;
	}
}
