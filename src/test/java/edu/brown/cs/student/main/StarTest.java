package edu.brown.cs.student.main;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StarTest {
  @Test
  public void testGetDistanceFrom() {
    Star s1 = new Star(1, "Star1", 1.1, 2.2, 3.3);
    Star s2 = new Star(2, "Star2", 4.4, 5.5, 6.6);

    Star sol = new Star(0, "Sol", 0.0, 0.0, 0.0);

    assertEquals(4.11582312545, s1.getDistanceFrom(sol), 0.001);
    assertEquals(4.11582312545, sol.getDistanceFrom(s1), 0.001);
    assertEquals(s1.getDistanceFrom(sol), sol.getDistanceFrom(s1), 0.001);

    assertEquals(5.71576766498, s2.getDistanceFrom(s1), 0.001);
    assertEquals(5.71576766498, s1.getDistanceFrom(s2), 0.001);
  }

}
