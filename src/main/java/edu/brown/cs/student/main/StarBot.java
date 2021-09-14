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
  private HashMap<String, LinkedList<Double>> stars;

  /**
   * Default constructor.
   */
  public StarBot() {
    stars = new HashMap<String, LinkedList<Double>>();
  }

  /**
   * Reads in a csv of star data, completely replacing any previously-read data.
   *
   * @param filename the name of the csv to be read in
   *
   * @return empty string if successful
   *         message if error occurs in reading the file
   *                 if csv is not well-formed as pdf specifies
   */
  public String readCsv(String filename) {
    try {
      File file = new File(filename);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String header = br.readLine();
      if (header != null) {
        String line;
        while ((line = br.readLine()) != null) {
          String[] a = line.split("'");
          assert (a.length == 5);

          LinkedList<Double> lst = new LinkedList<Double>();
          lst.add(Double.parseDouble(a[2]));
          lst.add(Double.parseDouble(a[3]));
          lst.add(Double.parseDouble(a[4]));

          System.out.println(a[1]);
          System.out.println(lst);
          stars.put(a[1], lst);
        }
      }
    } catch (FileNotFoundException e) {
      return "File not found";
    } catch (IOException e) {
      return "An error occurred";
    }
    return "";
  }
}

