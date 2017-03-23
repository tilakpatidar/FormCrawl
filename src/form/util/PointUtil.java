package form.util;

import org.openqa.selenium.Point;

public final class PointUtil {

  public static int angleBetween2Lines(Point labelLocation, Point
      fieldLocation) {
    Point basePoint = new Point(fieldLocation.x + 10, fieldLocation.y);
    float angle1 = (float) Math.atan2(fieldLocation.y - labelLocation.y, labelLocation.x
        - fieldLocation.x);
    float angle2 = (float) Math.atan2(basePoint.y - fieldLocation.y, fieldLocation.x
        - basePoint.x);
    float calculatedAngle = (float) Math.toDegrees(angle1 - angle2);
    if (calculatedAngle < 0) calculatedAngle += 360;
    return (int) calculatedAngle;
  }
  public static boolean angleNear90Multiples(int angle) {
    int[] multiples = {0, 90, 180, 270, 360};
    double comparableWithinPercentage = 7.0 / 100.0;
    for (int multiple : multiples) {
      if (comparableNum(angle, multiple, comparableWithinPercentage)) {
        return true;
      }
    }
    return false;
  }
  public static boolean comparablePoint(Point labelPoint, Point fieldPoint) {
    double comparableWithinPercentage = 7.0 / 100.0;

    double x = labelPoint.x;
    double x1 = fieldPoint.x;
    double y = labelPoint.y;
    double y1 = fieldPoint.y;
    boolean comparableX = comparableNum(x, x1, comparableWithinPercentage);
    boolean comparableY = comparableNum(y, y1, comparableWithinPercentage);
    return comparableX || comparableY;
  }
  private static boolean comparableNum(double x, double y, double
      withinPercentage) {
    if(x == y){
      return true;
    }
    double xComparision = Math.abs(1.0 - (x / y));
    return xComparision <= withinPercentage;
  }
  public static int calculateDist(Point first, Point second) {
    int xDiff = second.x - first.x;
    int yDiff = second.y - first.y;
    int xDiffSq = (int) Math.pow(xDiff, 2);
    int yDiffSq = (int) Math.pow(yDiff, 2);
    int sum = xDiffSq + yDiffSq;
    return (int) Math.sqrt(sum);
  }
}
