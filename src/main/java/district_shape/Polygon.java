package district_shape;

import java.util.ArrayList;

public class Polygon {
  private ArrayList<Double> x_coords = new ArrayList<>(), y_coords = new ArrayList<>();

  public void addPoint(double x, double y) {
    x_coords.add(x);
    y_coords.add(y);
  }
  
  // Checks if a given point is inside the polygon
  public boolean contains(double x, double y) {
    int n = x_coords.size();
    int i, j;
    boolean c = false;
    for (i = 0, j = n - 1; i < n; j = i++) {
      if (((y_coords.get(i) > y) != (y_coords.get(j) > y)) &&
          (x < (x_coords.get(j) - x_coords.get(i)) * (y - y_coords.get(i)) / (y_coords.get(j) - y_coords.get(i))
              + x_coords.get(i)))
        c = !c;
    }
    return c;
  }
}
