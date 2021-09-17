package edu.brown.cs.student.main;

import java.util.HashMap;

public class Star {
  private Integer id;
  private String name;
  private Double x;
  private Double y;
  private Double z;
  private HashMap<Integer, Double> distances;

  /**
   * Constructor for a star
   *
   * @param idi   - star ID
   * @param namei - star name
   * @param xi    - coordinate x as Double
   * @param yi    - coordinate y as Double
   * @param zi    - coordinate z as Double
   */
  public Star(Integer idi, String namei, Double xi, Double yi, Double zi) {
    id = idi;
    name = namei;
    x = xi;
    y = yi;
    z = zi;
    distances = new HashMap<Integer, Double>();
  }

  /**
   * Getter for a star's coordinates
   *
   * @return x coordinate as Double
   */
  public Double getX() {
    return x;
  }

  /**
   * Getter for a star's coordinates
   *
   * @return y coordinate as Double
   */
  public Double getY() {
    return y;
  }

  /**
   * Getter for a star's coordinates
   *
   * @return z coordinate as Double
   */
  public Double getZ() {
    return z;
  }

  /**
   * Getter for a star's id
   *
   * @return the star's id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Setter to store the distance from this star to starId as a Double
   *
   * @param starId - id of the other star
   * @param d      - distance to the star with id starId
   * @throws if value previously stored in distance is different than d
   */
  public void setDistance(Integer starId, Double d) {
    Double prevValue = distances.get(starId);
    if (prevValue == null) {
      distances.put(starId, d);
    } else {
      assert (prevValue == d);
    }
  }

  /**
   * Getter to get the distance from this star to starId as a Double
   *
   * @param starId - star to return distance from
   * @return distance from this star to starId
   */
  public Double getDistance(Integer starId) {
    return distances.get(starId);
  }


}
