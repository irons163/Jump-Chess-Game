package com.example.try_jumpchess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder surfaceHolder;
	boolean isGameRunning = true;
	// 螢幕右下角的座標值，即最大座標值
	private static int maxX;
	private static int maxY;
	// 第一点偏离左上角从像数，为了棋盘居中
	private static int yOffset;
	private static int xOffset;
	// 點大小
	private static int pointSize = 70;
	
	private static int lineDistance = pointSize/5*3;

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}
	
	private Point clickPoint;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			//若為已開局且為彈起事件，則執行以下程式
//			if(onProcessing()){ //是否正在處理中(對方尚未下棋)
//				return true;
//			}
			
			if(whoRun==1){
				clickPoint = newPoint(event.getX(), event.getY());
				if(redPoints.contains(clickPoint)){
				setOnProcessing();//思考中
				logic.startToDetected(clickPoint.x, clickPoint.y, 0, null);
				}
				
			}else{
				if(whoRun==-1)
					playerRun(event); //執行下棋步驟
			}		
		}		
		return true;
	}
	
	private synchronized void playerRun(MotionEvent event){
//		if(isPlayer1Run()){//玩家1下棋
//			player1Run(event);
//		}else if(isPlayer2Run()){//玩家2下棋
//			player2Run(event);
//		}
		
		player1Run(event);
	}
	
	private int whoRun = 1;
	
	private void setOnProcessing(){
		whoRun = -1;
	}
	
	private void player1Run(MotionEvent event){
		Point point = newPoint(event.getX(), event.getY());//取得觸摸的XY座標
		if(Logic.jumps.contains(point)){//此棋是否可下
			
//			player1.run(player2.getMyPoints(),point);
//			setPlayer2Run();
			
//			Logic.jumps.clear();
			
//			Logic.jumps = new ArrayList<Point>();
			
			Iterator<Point> iterator = Logic.jumps.iterator();
			while(iterator.hasNext()){
				iterator.next();
				iterator.remove();
			}
			
			redPoints.add(point);
			allFreePoints.remove(point);
			
			redPoints.remove(clickPoint);
			allFreePoints.add(clickPoint);
			
			whoRun=1;
			
			//playerOnePoints.add(point);
//			//刷新一下棋盘
//			refressCanvas();
//			//判断第一个玩家是否已经下了
//			if(!player1.hasWin()){//我还没有赢
//				if(player2==computer){//如果第二玩家是电脑
//					//10豪秒后才给玩家2下棋
//					refreshHandler.computerRunAfter(10);
//				}else{
//					setPlayer2Run();
//				}
//			}else{
//				//否则，提示游戏结束
//				setMode(PLAYER_TWO_LOST);
//			}
		}
	}
	
	private void move() {

	}

	private void draw() {
		Canvas canvas = surfaceHolder.lockCanvas();
		drawChssboardLines(canvas);

		drawPoints(canvas);
		drawAllFreePoints(canvas);
		
		
		
//		for(Point point : Logic.jumps)
//			drawPoint(canvas, point, WHITE);
		
		for(int i = 0; i<Logic.jumps.size(); i++){
			drawPoint(canvas, Logic.jumps.get(i), WHITE);
		}
		
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	private void drawPoints(Canvas canvas) {
		for (int i = 0; i < redPoints.size(); i++) { // 畫所有舊棋子
			// 畫點，傳入View的canvas與該舊棋子(point物件)與顏色種類(玩家1為綠色)
			drawPoint(canvas, redPoints.get(i), RED);
		}
	}
	
	private void drawAllFreePoints(Canvas canvas) {
		for (int i = 0; i < allFreePoints.size(); i++) { // 畫所有舊棋子
			// 畫點，傳入View的canvas與該舊棋子(point物件)與顏色種類(玩家1為綠色)
			drawPoint(canvas, allFreePoints.get(i), GREEN);
		}
	}
	
    //畫點(畫棋子)
    private void drawPoint(Canvas canvas,Point p,int color){
    	canvas.drawBitmap(pointArray[color],p.x*lineDistance - lineDistance / 2 +xOffset,p.y*lineDistance - lineDistance / 2 +yOffset, null);
    }

	Thread gameThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isGameRunning) {
				move();
				draw();
			}
		}
	});

	// 畫棋盤
	private List<Line> lines = new ArrayList<Line>();// 此Line集合在onSizeChange時已被初始化，內有數條線(EX:25)
	// 所有未下的空白点
	private final List<Point> allFreePoints = new ArrayList<Point>();

	// 產生棋盤上所有的線
	private void createLines() {
		for (int i = 0; i < maxX; i++) {// 豎線 0-24 共25條
			// (5+0-10) (240+20-10) (-5+480-10)
			lines.add(new Line(xOffset + (i + 1) * lineDistance - lineDistance / 2,
					yOffset + lineDistance / 2, xOffset + (i + 1) * lineDistance
							- lineDistance / 2, yOffset + maxY * lineDistance
							- lineDistance / 2));
		}
		for (int i = 0; i < maxY; i++) {// 橫線
			lines.add(new Line(xOffset + lineDistance / 2, yOffset + (i + 1)
					* lineDistance - lineDistance / 2, xOffset + maxX * lineDistance
					- lineDistance / 2, yOffset + (i + 1) * lineDistance - lineDistance
					/ 2));
		}
	}

	private void drawChssboardLines(Canvas canvas) {
		// 設置畫線時的顏色(棋盤的格子線)
		Paint paint = new Paint();
		paint.setColor(Color.LTGRAY);
		for (Line line : lines) {
			// 在View本身的畫布上畫線
			canvas.drawLine(line.xStart, line.yStart, line.xStop, line.yStop,
					paint);
		}
	}

	private final List<Point> allFreePoints2 = new ArrayList<Point>();
	private final List<Point> allLines = new ArrayList<Point>();
	
	private void go(){
		int firstX = 15;
		int startX = firstX;
		for(Point point : allFreePoints){
			if(point.x==startX){
				allFreePoints2.add(point);
				search(point);
			}
		}
		
	}
	private void search(Point point){
		
		int x = point.x;
		int y = point.y;
		
		int leftSearchX = x - 1;
		int leftSearchY = y - 1;
		
		Point point2 = new Point(x, y);
		if(allFreePoints.contains(point2)){
			search(point2);
		}else{
			allLines.add(point);
			return;
		}
		
		
		
//		if(point==null)
//			return   
		
	}
	
	 
	// 初始化空白點集合
	private void createPoints2() {
		allFreePoints2.clear(); // 所有空白點集合先清空(因為重新開始了)
		
		int firstX = 15;
		int startX = firstX;
		
			for (int j = 0; j < 17; j++) {				
				if(j<=3 ){					
					for(int k = 0; k<=j; k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
					}
					startX = startX - 1;
					if(j==3)
						startX = 3;					
				}else if(j>3 && j<=8){
					
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
					}					
					startX = startX + 1;
					if(j==8)
						startX = startX - 2;
				}
				else if(j>8 && j<=12){
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
					}					
					startX = startX - 1;
					if(j==12)
						startX = firstX - 3;
				}else if(j>12){
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
					}					
					startX = startX + 1;
					if(j==8)
						startX = 0;
				}
				
			}
		
	}
	
	// 初始化空白點集合
	private void createPoints() {
		allFreePoints.clear(); // 所有空白點集合先清空(因為重新開始了)
//		for (int i = 0; i < maxX; i++) { // 比線少1
//			for (int j = 0; j < maxY; j++) {
//				allFreePoints.add(new Point(i, j));// 空白點集合
//			}
//		}
		
		int firstX = 15;
		int startX = firstX;
		
			for (int j = 0; j < 17; j++) {

				
				if(j<=3 ){
					
					for(int k = 0; k<=j; k++){
//						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(startX-k, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(15, 0+j*2));// 空白點集合
						redPoints.add(new Point(startX + k*2, 0+j*2));
//						redPoints.add(new Point(15-j, 0+j*2));
					}
					startX = startX - 1;

					if(j==3)
						startX = 3;
					
				}else if(j>3 && j<=8){
					
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(startX-k, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(15, 0+j*2));// 空白點集合
//						redPoints.add(new Point(startX + k*2, 0+j*2));
//						redPoints.add(new Point(15-j, 0+j*2));
					}
					
					startX = startX + 1;

					if(j==8)
						startX = startX - 2;
				}
				else if(j>8 && j<=12){
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(startX-k, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(15, 0+j*2));// 空白點集合
//						redPoints.add(new Point(startX + k*2, 0+j*2));
//						redPoints.add(new Point(15-j, 0+j*2));
					}
					
					startX = startX - 1;

					if(j==12)
						startX = firstX - 3;
				}else if(j>12){
					for(int k = 0; k<(16-startX); k++){
						allFreePoints.add(new Point(startX + k*2, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(startX-k, 0+j*2));// 空白點集合
//						allFreePoints.add(new Point(15, 0+j*2));// 空白點集合
//						redPoints.add(new Point(startX + k*2, 0+j*2));
//						redPoints.add(new Point(15-j, 0+j*2));
					}
					
					startX = startX + 1;

					if(j==8)
						startX = 0;
				}
				
			}
		
	}

	// 線類別
	class Line {
		float xStart, yStart, xStop, yStop;

		// 建構子
		public Line(float xStart, float yStart, float xStop, float yStop) {
			// onSizeChange初始化時，把各個座標傳入(開始的xy座標到結束的xy座標)
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
	}

	private final List<Point> redPoints = new ArrayList<Point>();

	private void initChess() {
//		redPoints.add(new Point(10, 0));
//		redPoints.add(new Point(10 - 1, 0 + 2));
//		redPoints.add(new Point(10 + 1, 0 + 2));
//		redPoints.add(new Point(10 - 1 - 1, 0 + 2 + 2));
//		redPoints.add(new Point(10 , 0 + 2 + 2));
//		redPoints.add(new Point(10 + 1 + 1, 0 + 2 + 2));
//		redPoints.add(new Point(10 + 1 + 1, 0 + 2 + 2));
//		redPoints.add(new Point(10 + 1 + 1, 0 + 2 + 2));
		// redPoints.add(new Point(10-1, 0+2));
	}

	// 根據觸摸點座標找到對應點
	private Point newPoint(Float x, Float y) {
		Point p = new Point(0, 0);// 創建橫軸編號為0(橫軸的第一個點)，縱軸編號也為0(縱軸的第一個點)的點
		for (int i = 0; i < maxX; i++) {// 0-23 共24點
			// (0-5)<0 0<(20-5)
			if ((i * lineDistance + xOffset) <= x
					&& x < ((i + 2) * lineDistance + xOffset)) {
				// p.setX(i);//設定p的x為i，也就是橫軸第i+1個點
				p.x = i;
			}
		}
		for (int i = 0; i < maxY; i++) {// 跟上面橫軸差不多，這裡是處理縱軸
			if ((i * lineDistance + yOffset) <= y
					&& y < ((i + 2) * lineDistance + yOffset)) {
				// p.setY(i);
				p.y = i;
			}
		}
		return p; // 回傳 ponit p
	}

	// 點(4種類)的Bigmap陣列
	private Bitmap[] pointArray = new Bitmap[5];

	// 初始化好紅綠兩點
	public void fillPointArrays(int color, Drawable drawable) {
		// 新建一個bitmap，長寬20，使用ARGB_8888設定，此bitmap現在空白bitmap但非null。
		Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap); // 新建畫布，用空白bitmap當畫布
		drawable.setBounds(0, 0, pointSize, pointSize);// 設定drawable的邊界(原圖片有自己的長寬)
		drawable.draw(canvas); // 在畫布上畫上此drawable(此時bitmap已經被畫上東西，不是空白了)
		pointArray[color] = bitmap; // 將此bitmap存入點陣列中(共4種點)
	}

	private static final int GREEN = 0;// 綠色點(綠色棋子)
	private static final int NEW_GREEN = 1;// 最近下的綠色棋子
	private static final int RED = 2;// 紅色點(紅色棋子)
	private static final int NEW_RED = 3;// 最近下的紅色棋子
	private static final int WHITE = 4;
	Logic logic;
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		maxX = (int) Math.floor(width / lineDistance); // 取比此值大的最大整數(不大過自身)，也就是強制捨位
													// 480/20=24 470/20=23.5 >>
													// 23
		maxY = (int) Math.floor(width / lineDistance);

		// 设置X、Y座标微调值，目的整个框居中
		xOffset = ((width - (lineDistance * maxX)) / 2); // (480 - 20*24)/2=0
														// (470-20*23)/2=10/2=5
		yOffset = ((width - (lineDistance * maxY)) / 2);
		// 創建棋盤上的線條
		createLines();
		// 初始化棋盤上所有的空白點
		createPoints();

		// 把兩個顏色的點(四種類型)準備好，並放入陣列中
		Resources r = this.getContext().getResources();
		fillPointArrays(GREEN, r.getDrawable(R.drawable.green_point));
		 fillPointArrays(NEW_GREEN,r.getDrawable(R.drawable.green_point));
		fillPointArrays(RED, r.getDrawable(R.drawable.red_point));
		 fillPointArrays(NEW_RED,r.getDrawable(R.drawable.green_point));
		fillPointArrays(WHITE,r.getDrawable(R.drawable.white_point));
		initChess();

		logic = new Logic(allFreePoints);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
