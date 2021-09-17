package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
   * Private function that calculates all of the Euclidean distances for all the stars from s
   * And then prints out the IDs of the top k closest stars
   * Euclidean distance is calculated by sqrt((x_1 - x_2)^2 + (y_1 - y_2)^2 + (z_1 - z_2)^2)
   *
   * @param s - star to calculate all Euclidean distances from
   */
  private void calculateAndPrintDistances(Integer k, Star s) {
    // get a map of all of the other stars by removing s, if it is there
    HashMap<Integer, Star> otherStars = new HashMap<>(stars);
    otherStars.remove(s.getId());

    // if k > numOtherStars, set equal to all those. otherwise, keep k.
    int numOtherStars = otherStars.size();
    k = Math.min(numOtherStars, k);

    //don't want it to do anything if k is 0. if nonzero, then look for neighbors.
    if (k > 0) {
      // make a little tuple class to store ID and distance. I feel like this is not a great way to solve this?
      final class MiniTuple {
        private Integer id;
        private Double dist;

        private MiniTuple(Integer i, Double d) {
          id = i;
          dist = d;
        }

        public Integer getId() {
          return id;
        }

        public Double getDist() {
          return dist;
        }
      }

      // set up some stuff before looping through the rest of stars
      List<MiniTuple> distances = new ArrayList<MiniTuple>();

      // loop through all the other stars and calculate distances
      for (Integer otherId : otherStars.keySet()) {
        if (otherId != s.getId()) { // should be unnecessary, but just in case
          Double dist = s.getDistanceFrom(otherStars.get(otherId));
          MiniTuple mt = new MiniTuple(otherId, dist);
          distances.add(mt);
        }
      }

      // Create comparator to compare stars by their distance
      // Returns 0 if same distance, negative if s1 < s2, positive if s1 > s2
      Comparator<MiniTuple> cs = new Comparator<MiniTuple>() {
        @Override
        public int compare(MiniTuple mt1, MiniTuple mt2) {
          return Double.valueOf(mt1.getDist() - mt2.getDist()).intValue();
        }
      };

      // Sort the list of distances
      Collections.sort(distances, cs);

      // Get the top k from this lst
      List<MiniTuple> topKStars = distances.subList(0, k);

      // Then go through and print all those ids
      for (MiniTuple mt : topKStars) {
        System.out.println(mt.getId());
      }
    }
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
      // make a new dummy Star object with the desired coordinates, and pass to helper
      Star dummy = new Star(-1, "Naive_Dummy", x, y, z);
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
      // Retrieve the proper Star object and calculate
      Star s = stars.get(id);
      calculateAndPrintDistances(k, s);
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR: an exception occurred";
    }
    return "";
  }
}

