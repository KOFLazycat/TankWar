package com.delpy.deng;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
 * �ӵ���
 */
public class Bullet {
	GameView gv;
	int ID;					//�ӵ���ID��
	int enemy;				//���ҵ�ʶ��1��2��ʾ���˷������ӵ���0��ʾ�Լ��������ӵ�
	int row,col;			//�ӵ�����ԭ�����꣬ע�ⲻ��������Ϊ��λ�������Կ�Ϊ��λ�������ӵ��������е��к����к�
							//ÿ�鶼��10*10���ش�С���к����к��Ǻ�����ת��Ϊ����������ģ�X=(col+1)*10��Y=(row+1)*10
	int R;					//�ӵ��İ뾶���ֱ�Ϊ2��3��5,Ĭ��Ϊ2������Ӧ�Ĺ������ֱ�Ϊ��300��500��1000
	int dir;				//�ӵ����˶�����1234�ֱ��ʾ�ϡ��¡�����
	int v;					//�ӵ����˶��ٶȣ��Կ�Ϊ��λ����ÿ����ʱ��������ǰ�ƶ����ٿ飨10���أ�
	int power;				//���������� �ӵ�����̹�˺�̹��һ�μ�����Ѫ
	Paint bulletPaint;		//�з��ӵ�Paint
	Paint myBulletPaint;	//�ҷ��ӵ�Paint
	public Bullet(GameView gv,int enemy,int row, int col, int dir, int v, int R){
		this.gv=gv;
		this.enemy=enemy;
		this.row=row;
		this.col=col;
		this.dir=dir;
		this.v=v;
		this.R=R;
		this.power=(R==2?200:R==3?500:1000);
		bulletPaint=new Paint();
		bulletPaint.setColor(Color.WHITE);
		myBulletPaint=new Paint();
		myBulletPaint.setColor(Color.BLACK);
	}
	public void draw(Canvas canvas){		//�����ӵ�
		canvas.drawCircle(col*10, row*10, R, bulletPaint);
	}
	public void drawMyBullet(Canvas canvas){
		canvas.drawCircle(col*10, row*10                                                      , R, myBulletPaint);
	}
	public void autoMove(){					//�ӵ��Զ��ƶ�
		switch(dir){
		case 1:
			row--;break;
		case 2:
			row++;break;
		case 3:
			col--;break;
		case 4:
			col++;break;
		}
	}
	public boolean hasFlyOutOfScreen(){
		boolean hasCrash=false;
		if(row<0 || row>39 || col<0 || col>31){	//�ɳ�����Ļ
			hasCrash=true;			
		}
		return hasCrash;
	}
	public boolean hasCrashWall(){	//��Ȿ�ӵ��Ƿ�������ǽ�ڡ����ʯ���Ƿ�ɳ�����Ļ���Ƿ������Ŀ����
		int i,j;
		boolean hasCrash=false;
		if(row<0 || row>39 || col<0 || col>31){
			return false;
		}
		if((row==38 && col==15) || (row==38 && col==16) ||
				(row==39 && col==15) || (row==39 && col==16)){	//������Ŀ���壬Game Over
			hasCrash=true;
			gv.maps[38][15]=0;
			gv.father.myHandler.sendEmptyMessage(100);			//����Activity����100����Ϣ
			gv.gameOver=true;
			return hasCrash;
		}
		if(gv.maps[row][col]==1){
			gv.maps[row][col]=0;
			hasCrash=true;
		}else if(gv.maps[row][col]==2){
			hasCrash=true;
		}		
		switch(dir){
		case 1:
		case 2:
			if(col-1>=0){
				if(gv.maps[row][col-1]==1){
					gv.maps[row][col-1]=0;
					hasCrash=true;
				}
				if(gv.maps[row][col-1]==2 ){
					hasCrash=true;
				}
			}
			break;
		case 3:
		case 4:	
			if(row-1>=0){
				if(gv.maps[row-1][col]==1){
					gv.maps[row-1][col]=0;
					hasCrash=true;
				}
				if(gv.maps[row-1][col]==2)
					hasCrash=true;
			}
			break;
		}
		return hasCrash;
	}
	public boolean hasCrashWithOtherBullet(){
		boolean has=false;
		if(enemy==0){			//�ҷ��ӵ��������Ƿ�����˵��ӵ�����
			synchronized(gv.bulletsList){
				for(int i=0;i<gv.bulletsList.size();i++){
					Bullet oneBullet=gv.bulletsList.get(i);
					if(row==oneBullet.row && col==oneBullet.col){	//������ӵ������ĳ�ӵ�����
						gv.bulletsList.remove(i);
						has=true;
						break;
					}else{
						switch(dir){		//����ӵ������෴�������һ��Ҳ��Ϊ�Ѿ������ˡ�
						case 1:
							if(row>0 && oneBullet.row==row-1 && oneBullet.col==col && oneBullet.dir==2){
								gv.bulletsList.remove(i);
								has=true;
								return has;
							}
							break;
						case 2:
							if(row<39 && oneBullet.row==row+1 && oneBullet.col==col && oneBullet.dir==1){
								gv.bulletsList.remove(i);
								has=true;
								return has;
							}
							break;
						case 3:
							if(col>0 && oneBullet.col==col-1 && oneBullet.row==row && oneBullet.dir==4){
								gv.bulletsList.remove(i);
								has=true;
								return has;
							}
							break;
						case 4:
							if(col<31 && oneBullet.col==col+1 && oneBullet.row==row && oneBullet.dir==3){
								gv.bulletsList.remove(i);
								has=true;
								return has;
							}
							break;
						}
					}
				}
			}
		}
		return has;
	}
}
