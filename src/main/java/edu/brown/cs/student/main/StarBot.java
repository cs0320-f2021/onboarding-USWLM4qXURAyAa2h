package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class StarBot {

  //Field that stores csv of star data as a hashtable
  //Where keys are star names (guaranteed unique) and values are xyz information
  private HashMap<Integer, LinkedList<Double>> stars;

  /**
   * Default constructor.
   */
  public StarBot() {
    stars = new HashMap<Integer, LinkedList<Double>>();
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
    stars = new HashMap<Integer, LinkedList<Double>>();
    try {
      File file = new File(filename);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String header = br.readLine();
      if (header != null) {
        String line;
        while ((line = br.readLine()) != null) {
          String[] a = line.split(",");
          if (a.length == 5) {
            LinkedList<Double> lst = new LinkedList<Double>();
            lst.add(Double.parseDouble(a[2]));
            lst.add(Double.parseDouble(a[3]));
            lst.add(Double.parseDouble(a[4]));

            stars.put(Integer.getInteger(a[0]), lst);
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

  public String nearestNeighborsCoords(Integer k, Double x, Double y, Double z) {
    //TODO check if k >= 0, if it is return error string
    return "";
  }

  public String nearestNeighborsName(Integer k, String name) {
    //TODO make sure that name is in quotes
    return "";
  }
}

