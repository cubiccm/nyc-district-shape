package district_shape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {
  private static final ArrayList<District> district_shapes = new ArrayList<>();

  private static JSONObject readJSONFromFile(String file_path) throws IOException {
    File json_file = new File(file_path);
    StringBuilder content = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(json_file))) {
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line);
      }
    }
    return new JSONObject(content.toString());
  }

  private static Polygon getPolygonFromJSON(JSONArray coordinates) {
    Polygon polygon = new Polygon();
    for (int i = 0; i < coordinates.length(); i++) {
      JSONArray coordinate = coordinates.getJSONArray(i);
      polygon.addPoint(coordinate.getDouble(0), coordinate.getDouble(1));
    }
    return polygon;
  }

  private static void readShapesFromFile(String file_path) throws IOException, Exception {
    JSONObject json = readJSONFromFile(file_path);
    JSONArray features = json.getJSONArray("features");
    for (int i = 0; i < features.length(); i++) {
      JSONObject feature = features.getJSONObject(i);
      int district_id = feature.getJSONObject("properties").getInt("ElectDist");
      JSONObject geometry = feature.getJSONObject("geometry");
      
      // There are two types of features in this GeoJSON, Polygon and MultiPolygon
      // MultiPolygon contains an array of multiple distinct polygons
      String type = geometry.getString("type");
      if (type.equals("Polygon")) {
        JSONArray coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);
        district_shapes.add(new District(district_id, getPolygonFromJSON(coordinates)));
      } else if (type.equals("MultiPolygon")) {
        JSONArray coordinates_array = geometry.getJSONArray("coordinates");
        for (int j = 0; j < coordinates_array.length(); j++) {
          JSONArray coordinates = coordinates_array.getJSONArray(j).getJSONArray(0);
          district_shapes.add(new District(district_id, getPolygonFromJSON(coordinates)));
        }
      } else {
        throw new Exception("Unknown feature type");
      }
    }
  }

  // Checks if a coordinate is included in any election district
  // Returns the five-digit district ID if found, throws an exception otherwise
  // x_coord should be longitude and y_coord should be latitude here
  public static Integer getDistrict(Double x_coord, Double y_coord) throws Exception {
    for (District district : district_shapes) {
      if (district.shape.contains(x_coord, y_coord)) {
        return district.id;
      }
    }
    throw new Exception("Unable to find a corresponding election district for the given coordinate");
  }

  public static void main(String[] args) {
    try {
      // args[0] is the path to GeoJSON file
      readShapesFromFile(args[0]);
      Double longitude = -73.99710171525513;
      Double latitude = 40.72975898745436;
      System.out.println("Your district: " + getDistrict(longitude, latitude));
      // Returns election district ID, which is a five-digit integer, for example 66041
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}