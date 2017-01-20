package com.example.try_jumpchess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;

public class Logic {
	public static int[][][] position;
	public static Bobble[][] bobbles;
	private static final int CLEAR_BUBBLE_NUMBER = 3;
	private static final int MAX_NOT_FREE_POINT_DETECTED_COUNT = 2;
	private static final int NOT_FREE_POINT_COUNT_FOR_JUMP = 1;
	private int notFreePointDetectedCount = 0;
	private int detectedMoveOnceCount = 0;

	Bobble newAddBubbleAfterCollision;
	ArrayList<Bobble> removeBubbleAnimationArrayList = new ArrayList<Bobble>();

	private final List<Point> allFreePoints;

	public Logic(List<Point> allFreePoints) {
		this.allFreePoints = allFreePoints;
	}

	private void createNewBubbleAndClearDetectedAfterCollision(
			int newBobbleLine, int newBobblePosition, int type) {
		removeBubbleAnimationArrayList.clear();
		newAddBubbleAfterCollision = new Bobble(newBobbleBitmap(type), type,
				new Point(position[newBobbleLine][newBobblePosition][0],
						position[newBobbleLine][newBobblePosition][1]));
		bobbles[newBobbleLine][newBobblePosition] = newAddBubbleAfterCollision;

		HashSet<Bobble> bubbleClearHashSet = new HashSet<Bobble>();
		clearBubbleDetected(newBobbleLine, newBobblePosition, type,
				bubbleClearHashSet);
		if (bubbleClearHashSet.size() >= CLEAR_BUBBLE_NUMBER) {
			Iterator<Bobble> iterator = bubbleClearHashSet.iterator();
			while (iterator.hasNext()) {
				Bobble clearBubble = iterator.next();
				if (clearBubble.getPoint().x < newAddBubbleAfterCollision
						.getPoint().x) {
					clearBubble
							.setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_RIGHT);
				} else {
					clearBubble
							.setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_LEFT);
				}
				removeBubbleAnimationArrayList.add(clearBubble);
				clearBubble.clear();
			}

			for (int bubblePosition = 0; bubblePosition < bobbles[0].length; bubblePosition++) {
				if (bobbles[0][bubblePosition] != null) {
					detectBubbleWithLinkToTheOther(0, bubblePosition);
				}
			}

			for (int i = bobbles.length - 1; i >= 0; i--) {
				for (int j = bobbles[0].length - 1; j >= 0; j--) {
					if (bobbles[i][j] != null && !bobbles[i][j].isNeedClear()) {
						if (bobbles[i][j].isCheckedForLeft())
							bobbles[i][j].setIsCheckedForLeft(false);
						else {
							bobbles[i][j]
									.setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_DOWN);
							removeBubbleAnimationArrayList.add(bobbles[i][j]);
							bobbles[i][j] = null;
						}
					}
				}
			}
		}
	}

	private void clearBubbleDetected(int bubbleLineForDetected,
			int bubblePositionForDetected, int type,
			HashSet<Bobble> bubbleClearHashSet) {
		if ((bubbleLineForDetected >= 0 && bubbleLineForDetected < bobbles.length)
				&& (bubblePositionForDetected >= 0 && bubblePositionForDetected < 8)) {
			Bobble bubbleForDetected = bobbles[bubbleLineForDetected][bubblePositionForDetected];
			if (bubbleForDetected != null) {
				if (bubbleClearHashSet.size() == 0
						|| type == bubbleForDetected.getType()) {
					if (!isClearBubbleExistInHashSet(bubbleForDetected,
							bubbleClearHashSet)) {
						bubbleClearHashSet.add(bubbleForDetected);
						int lastBubbleLine = bubbleLineForDetected;
						int lastBubblePosition = bubblePositionForDetected;
						rightBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						rightTopBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftTopBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftBottomBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						rightBottomBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
					}
				}
			}
		}

	}

	int lastX = -1;
	int lastY = -1;
	Point point = new Point(lastX, lastY);

	public void startToDetected(int lastBubbleLine, int lastBubblePosition,
			int type, HashSet<Bobble> bubbleClearHashSet) {

		lastX = -1;
		lastY = -1;

		detectedMoveOnceCount = 0;

		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;

		rightBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
		detectedMoveOnceCount = 0;
		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		rightTopBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
		detectedMoveOnceCount = 0;
		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		leftTopBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
		detectedMoveOnceCount = 0;
		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		leftBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
		detectedMoveOnceCount = 0;
		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		leftBottomBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
		detectedMoveOnceCount = 0;
		point = new Point(lastX, lastY);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		rightBottomBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
	}

	public void startToDetected2(int lastBubbleLine, int lastBubblePosition,
			int type, HashSet<Bobble> bubbleClearHashSet) {

		int lastX = this.lastX;
		int lastY = this.lastY;

		point = new Point(lastX, lastY);
		lastX = this.lastX;
		lastY = this.lastY;

		rightBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);

		this.lastX = lastBubbleLine;
		this.lastY = lastBubblePosition;
		rightTopBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);

		this.lastX = lastBubbleLine;
		this.lastY = lastBubblePosition;
		leftTopBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);

		this.lastX = lastBubbleLine;
		this.lastY = lastBubblePosition;
		leftBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);

		this.lastX = lastBubbleLine;
		this.lastY = lastBubblePosition;
		leftBottomBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);

		this.lastX = lastBubbleLine;
		this.lastY = lastBubblePosition;
		rightBottomBubbleForDetected(lastBubbleLine, lastBubblePosition, type,
				bubbleClearHashSet);
	}

	public static ArrayList<Point> jumps = new ArrayList<Point>();

	private void clearBubbleDetected2(int bubbleLineForDetected,
			int bubblePositionForDetected, int type,
			HashSet<Bobble> bubbleClearHashSet) {

		if (notFreePointDetectedCount == MAX_NOT_FREE_POINT_DETECTED_COUNT) {
			notFreePointDetectedCount = 0;
			return;
		}

		Point point2 = new Point();
		point2.x = bubbleLineForDetected;
		point2.y = bubblePositionForDetected;

		if (!allFreePoints.contains(point2)) {
			notFreePointDetectedCount++;
			detectedMoveOnceCount++;
			int lastBubbleLine = bubbleLineForDetected
					- (lastX - bubbleLineForDetected);
			int lastBubblePosition = bubblePositionForDetected
					- (lastY - bubblePositionForDetected);

			lastX = bubbleLineForDetected;
			lastY = bubblePositionForDetected;

			clearBubbleDetected2(lastBubbleLine, lastBubblePosition, type,
					bubbleClearHashSet);

		} else {
			if (notFreePointDetectedCount == NOT_FREE_POINT_COUNT_FOR_JUMP) {
				notFreePointDetectedCount = 0;
				detectedMoveOnceCount++;
				if(!jumps.contains(point2)){
				point2.setJumpableChecked(true);
				jumps.listIterator().add(point2);
				startToDetected2(bubbleLineForDetected,
						bubblePositionForDetected, type, bubbleClearHashSet);
				}
			} else if (detectedMoveOnceCount == 0) {
				detectedMoveOnceCount++;
				jumps.listIterator().add(point2);
			}
		}
	}

	private boolean isClearBubbleExistInHashSet(Bobble bubbleForDetected,
			HashSet<Bobble> bubbleClearHashSet) {
		boolean isClearBubbleExistInHashSet = false;
		for (Bobble bobble : bubbleClearHashSet) {
			isClearBubbleExistInHashSet = bobble.equals(bubbleForDetected);
			if (isClearBubbleExistInHashSet)
				break;
		}
		return isClearBubbleExistInHashSet;
	}

	private void rightBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine + 2 && point.y == lastBubblePosition)
			return;
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine + 2, lastBubblePosition, type,
				bubbleClearHashSet);
	}

	private void rightTopBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine + 1 && point.y == lastBubblePosition - 2)
			return;
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine + 1, lastBubblePosition - 2, type,
				bubbleClearHashSet);
	}

	private void leftTopBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine - 1 && point.y == lastBubblePosition - 2)
			return;
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine - 1, lastBubblePosition - 2, type,
				bubbleClearHashSet);
	}

	private void leftBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine - 2 && point.y == lastBubblePosition)
			return;
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine - 2, lastBubblePosition, type,
				bubbleClearHashSet);
	}

	private void leftBottomBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine - 1 && point.y == lastBubblePosition + 2)
			return;
		// clearBubbleDetected2(lastBubbleLine + 1, lastBubblePosition - 1
		// + lastBubbleLine % 2, type, bubbleClearHashSet);
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine - 1, lastBubblePosition + 2, type,
				bubbleClearHashSet);
	}

	private void rightBottomBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		if (point.x == lastBubbleLine + 1 && point.y == lastBubblePosition + 2)
			return;
		lastX = lastBubbleLine;
		lastY = lastBubblePosition;
		clearBubbleDetected2(lastBubbleLine + 1, lastBubblePosition + 2, type,
				bubbleClearHashSet);
	}

	private void detectBubbleWithLinkToTheOther(int i, int j) {
		if (i < 0)
			return;
		else if (j < 0 || j > bobbles[i].length - 1)
			return;
		else if (bobbles[i][j] == null || bobbles[i][j].isNeedClear())
			return;
		else if (bobbles[i][j].isCheckedForLeft())
			return;
		else {
			bobbles[i][j].setIsCheckedForLeft(true);
			detectBubbleWithLinkToTheOther(i, j + 1);
			detectBubbleWithLinkToTheOther(i - 1, j - 1 + i % 2);
			detectBubbleWithLinkToTheOther(i - 1, j + i % 2);
			detectBubbleWithLinkToTheOther(i, j - 1);
			detectBubbleWithLinkToTheOther(i + 1, j - 1 + i % 2);
			detectBubbleWithLinkToTheOther(i + 1, j + i % 2);
		}
	}

	public Bitmap newBobbleBitmap(int type) {
		Bitmap newBobbleBitmap = null;
		// if (CommonUtil.isBlind == false) {
		// newBobbleBitmap = normal_bobble[type];
		// } else {
		// newBobbleBitmap = blind_bobble[type];
		// }
		return newBobbleBitmap;
	}
}
