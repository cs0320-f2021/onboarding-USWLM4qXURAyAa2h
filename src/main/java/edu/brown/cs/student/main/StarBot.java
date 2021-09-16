package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class StarBot {

  //Field that stores csv of star data as a hashtable
  //Where keys are star IDs (guaranteed unique) and values are Star objects
  private HashMap<Integer, Star> stars;
  private HashMap<String, Integer> namesToIds;

  /**
   * Default constructor.
   */
  public StarBot() {
    stars = new HashMap<Integer, Star>();
    namesToIds = new HashMap<String, Integer>();
  }

  /**
   * Returns the number of stars this StarBot has.
   */
  public String getNumStarsAsString() {
    return Integer.toString(stars.size());
  }

  /**
   * Reads in a csv of star data, completely replacing any previously-read data.
   * Called with the `stars` command in the REPL loop.
   *
   * @param filename the name of the csv to be read in
   * @return empty string if successful
   * message if error occurs in reading the file
   * if csv is not well-formed as pdf specifies
   */
  public String readCsv(String filename) {
    //Refresh the stars that are in here right now
    stars = new HashMap<Integer, Star>();
    namesToIds = new HashMap<String, Integer>();
    int unnamedStarsCt = 0;
    try {
      File file = new File(filename);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String header = br.readLine();
      if (header != null) {
        String line;
        while ((line = br.readLine()) != null) {
          String[] a = line.split(",");
          if (a.length == 5) {
            int id = Integer.parseInt(a[0]);
            String name = a[1];
            Star s = new Star(id, name, Double.parseDouble(a[2]), Double.parseDouble(a[3]),
                Double.parseDouble(a[4]));

            //if a star does not have a name, give it a unique name
            //not in a project setting I would probably print some indication this happened
            if (name.equals("")) {
              name = "unnamedStar" + String.valueOf(unnamedStarsCt);
              unnamedStarsCt += 1;
            }

            //make sure these keys are not already in the hashmaps before putting
            assert (stars.get(id) == null);
            assert (namesToIds.get(name) == null);
            stars.put(id, s);
            namesToIds.put(name, id);
          } else {
            return "ERROR: not every row of csv has 5 columns";
          }
        }
      }
    } catch (FileNotFoundException e) {
      return "ERROR: File not found";
    } catch (IOException e) {
      return "ERROR: IOException occurred";
    }
    return "";
  }

  /**
   * Private function that calculates all of the Euclidean distances for all the stars from the given Star
   * And then prints out the IDs of the top k closest stars
   *
   * @param s - star to calculate all Euclidean distances from
   */
  private void calculateAndPrintDistances(Integer k, Star s) {
    System.out.println("BIIIG TODO!");
  }

  /**
   * Function that runs kNearestNeighbors searching from given xyz coordinations for k neighbors.
   *
   * @param k - number of neighbors to search for as a nonnegative integer
   * @param x - x coordinate, Double
   * @param y - y coordinate, Double
   * @param z - z coordinate, Double
   * @return - ERROR string if inputs not satisfied, else nothing
   */
  public String naiveNeighborsCoords(Integer k, Double x, Double y, Double z) {
    //Check if k>=0, if not then return an error string
    if (k < 0) {
      return "ERROR: k must be a nonnegative integer";
    }

    try {
      //make a new dummy Star object with the desired coordinates
      Star dummy = new Star(-1, "Naive_Dummy", x, y, z);

      //then pass to private calculateAndPrintDistances to do the rest of the work (getting all the
      //Euclidean distances and printing the top k closest stars in self.stars)
      calculateAndPrintDistances(k, dummy);
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR: an exception occurred";
    }

    return "";
  }


  public String naiveNeighborsName(Integer k, String name) {
    //Check if k>=0, if not then return an error string
    if (k < 0) {
      return "ERROR: k must be a nonnegative integer";
    }

    //Check if name is in quotes, if not then return an error string
    if (!(name.startsWith("\"") & name.endsWith("\""))) {
      return "ERROR: star name must be in double quotation marks";
    }

    try {
      //Try retrieving star id from the given name
      name = name.replaceAll("\"", "");
      Integer id = namesToIds.get(name);
      if (id == null) {
        return "ERROR: star id for " + name + " not found.";
      }

      //Retrieve the proper Star object and pass to calculateAndPrintDistances
      Star s = stars.get(id);
      calculateAndPrintDistances(k, s);
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR: an exception occurred";
    }

    return "";
  }
}

