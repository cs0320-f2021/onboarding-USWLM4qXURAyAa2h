package edu.brown.cs.student.main;

public class Star {
  private Integer id;
  private String name;
  private Double x;
  private Double y;
  private Double z;

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


}
