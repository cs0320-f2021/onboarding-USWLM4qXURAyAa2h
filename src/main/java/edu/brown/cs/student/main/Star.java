package edu.brown.cs.student.main;

public class Star {
  private Integer id;
  private String name;
  private Double x;
  private Double y;
  private Double z;

  // Constructor for a Star
  public Star(Integer idi, String namei, Double xi, Double yi, Double zi) {
    id = idi;
    name = namei;
    x = xi;
    y = yi;
    z = zi;
  }

  //printing function for debugging
  public void debugPrint() {
    System.out.println(id);
    System.out.println("name: " + name);
  }




}
