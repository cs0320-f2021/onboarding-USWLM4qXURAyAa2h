package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StarBotTest {
  @Test
  public void testReadCsv() {
    // try reading in lots then few and make sure overwrites it
    StarBot sb = new StarBot();
    sb.readCsv("data/stars/stardata.csv");
    assertEquals("119617", sb.getNumStarsAsString());

    //make sure the old stars are overwritten each time by using same sb
    sb.readCsv("data/stars/one-star.csv");
    assertEquals("1", sb.getNumStarsAsString());
    assertEquals(true, sb.getStars().get(1).equals(new Star(1, "Lonely Star", 5.0,-2.24,10.04)));
    assertEquals(null, sb.getStars().get(0));

    sb.readCsv("data/stars/three-star.csv");
    assertEquals("3", sb.getNumStarsAsString());
    assertEquals(false, sb.getStars().get(1).equals(new Star(1, "Lonely Star", 5.0,-2.24,10.04)));
    assertEquals(true, sb.getStars().get(1).equals(new Star(1, "Star One", 1.0,0.0,0.0)));
    assertEquals(true, sb.getStars().get(2).equals(new Star(2, "Star Two", 2.0,0.0,0.0)));
    assertEquals(true, sb.getStars().get(3).equals(new Star(3, "Star Three", 3.0,0.0,0.0)));
    assertEquals(null, sb.getStars().get(4));

    sb.readCsv("data/stars/ten-star.csv");
    assertEquals("10", sb.getNumStarsAsString());
    assertEquals(false, sb.getStars().get(3).equals(new Star(3, "Star Three", 3.0,0.0,0.0)));
    assertEquals(true, sb.getStars().get(0).equals(new Star(0, "Sol", 0.0,0.0,0.0)));
    assertEquals(true, sb.getStars().get(118721).equals(new Star(118721, "", -2.28262, 0.64697, 0.29354)));
    assertEquals(true, sb.getStars().get(1).equals(new Star(1, "", 282.43485, 0.00449, 5.36884)));

    //read in one you already read in and test again, make sure no residue
    sb.readCsv("data/stars/one-star.csv");
    assertEquals("1", sb.getNumStarsAsString());
    assertEquals(true, sb.getStars().get(1).equals(new Star(1, "Lonely Star", 5.0,-2.24,10.04)));
    assertEquals(null, sb.getStars().get(0));
    assertEquals(null, sb.getStars().get(118721));
  }

  @Test
  public void testCalculateAndPrintDistances() {



  }

  @Test
  public void testNaiveNeighborsCoords() {

  }

  @Test
  public void testNaiveNeighborsName() {

  }



}
