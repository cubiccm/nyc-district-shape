package district_shape;

public class District {
  public int id;
  public Polygon shape;

  public District(int id, Polygon shape) {
    this.id = id;
    this.shape = shape;
  }
}
