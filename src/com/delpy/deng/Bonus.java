package com.delpy.deng;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * ��Ϸ�����࣬���ҷ�̹���������ߺ󣬲�����Ӧ��Ч��
 * ��������type��1����ʱ����2��ը����3�����ӣ�4��̹�ˣ�5��ʳ�����Ѫ����6���޵У�7���ִ���8�����ǣ�����������������ǿ��
 * ÿ���������ҷ�̹�˻��ٵз�̹�˻�ǽ������������ٵз�̹�˺�ļ���Ϊ20%������ǽ���
 * ����Ϊ10%����һ��ֻ��һ�����ߣ��õ�������Ļ�ϵ�������Ϊ120�룬�ڴ��ڼ䣬��Ļ��ֻ��һ��
 * ���ߣ����ҷ�̹���������ߺ󣬵��ߵ���Ч��ҲΪ120��
 */
public class Bonus {
	GameView gv;
	int type;
	int row,col;
	int status;		//����״̬��1��ʾ�Ѳ���������Ļ����ʾ��2��ʾ���ҷ�̹�˽��գ�3��ʾʱ�䵽�Զ���ʧ
	boolean flashStatus;	//��˸״̬
	public static final long MAX_EXIST_MIL_TIME=120*1000;		//���������120��
	long startTime;
	boolean flag=false;
	Resources r;
	Bitmap clock1,clock2,boom1,boom2,shovel1,shovel2,tank1,tank2,food1,food2,alive1,alive2,ship1,ship2,star1,star2;	
	public Bonus(GameView gv,int type,int row,int col){
		this.gv=gv;
		this.type=type;
		this.row=row;
		this.col=col;
		this.startTime=System.currentTimeMillis();
		this.status=1;
		this.flag=true;
		this.flashStatus=true;
		initBitmap();
		new Thread(){								//����ÿ������˸һ��
			public void run(){
				while(status==1){
					flashStatus=!flashStatus;
					try{
						Thread.sleep(1000);
					}catch(Exception e){
						
					}
				}
			}
		}.start();
		new Thread(){								//���Ƶ���������
			public void run(){
				while(status==1){
					long existTimes=System.currentTimeMillis()-startTime;
					if(existTimes>=MAX_EXIST_MIL_TIME){
						status=3;					//����״̬��3Ϊ�Զ���ʧ
					}
					try{
						Thread.sleep(200);			//200������һ��
					}catch(Exception e){
						
					}
				}
			}
		}.start();
	}
	public void initBitmap(){
		r=gv.getResources();
		clock1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.clock1);
		clock2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.clock2);
		boom1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.boom1);
		boom2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.boom2);
		shovel1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.shovel1);
		shovel2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.shovel2);
		tank1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.tank1);
		tank2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.tank2);
		food1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.food1);
		food2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.food2);
		alive1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.alive1);
		alive2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.alive2);
		ship1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.ship1);
		ship2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.ship2);
		star1=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.star1);
		star2=(Bitmap)BitmapFactory.decodeResource(r,R.drawable.star2);
	}
	public Bitmap getBitmap(int type,boolean flashStatus){
		Bitmap bmp=null;
		switch(type){
		case 1:
			if(flashStatus){
				bmp=clock1;
			}else{
				bmp=clock2;
			}
			break;
		case 2:
			if(flashStatus){
				bmp=boom1;
			}else{
				bmp=boom2;
			}
			break;
		case 3:
			if(flashStatus){
				bmp=shovel1;
			}else{
				bmp=shovel2;
			}
			break;
		case 4:
			if(flashStatus){
				bmp=tank1;
			}else{
				bmp=tank2;
			}
			break;
		case 5:
			if(flashStatus){
				bmp=food1;
			}else{
				bmp=food2;
			}
			break;
		case 6:
			if(flashStatus){
				bmp=alive1;
			}else{
				bmp=alive2;
			}
			break;
		case 7:
			if((flashStatus)){
				bmp=ship1;
			}else{
				bmp=ship2;
			}
			break;
		case 8:
			if(flashStatus){
				bmp=star1;
			}else{
				bmp=star2;
			}
			break; 
		}
		return bmp;
	}
	public void draw(Canvas canvas){
		if(status==1){
			Bitmap bmp=getBitmap(type,flashStatus);
			canvas.drawBitmap(bmp,col*10,row*10,null);
		}
	}
}
