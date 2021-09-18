package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    //MathBot and StarBot for this REPL loop
    MathBot mb = new MathBot();
    StarBot sb = new StarBot();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");
          if (arguments[0].equals("add")) {
            System.out.println(
                mb.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2])));
          } else if (arguments[0].equals("subtract")) {
            System.out.println(
                mb.subtract(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2])));
          } else if (arguments[0].equals("stars")) {
            // first possible command: stars <filename>
            // takes in a filename to read a csv from. see function signature in StarBot class for details.

            try { // if there is a filename argument, use that. else complain
              String err = sb.readCsv(arguments[1]);
              if (err.equals("")) {
                System.out.println(
                    "Read " + sb.getNumStarsAsString() + " stars from " + arguments[1]);
              } else {
                System.out.println(err);
              }
            } catch (IndexOutOfBoundsException e) {
              System.out.println("ERROR: readcsv requires exactly one filename argument.");
            }
          } else if (arguments[0].equals("naive_neighbors")) {
            // Parse for strings in quotation marks with spaces
            List<String> naiveArgs = new ArrayList<String>();
            naiveArgs.add(arguments[0]);
            for (int i = 1; i < arguments.length; i++) {
              if (arguments[i].startsWith("\"") & !arguments[i].endsWith("\"")) {
                String newStr = arguments[i];
                for (int j = i + 1; j < arguments.length; j++) {
                  newStr += " " + arguments[j];
                  if (arguments[j].endsWith("\"")) {
                    naiveArgs.add(newStr);
                    i = j;
                    break;
                  }
                }
              } else {
                naiveArgs.add(arguments[i]);
              }
            }

            // Using the parsed inputs, respond appropriately
            if (naiveArgs.size() == 5) {
              Integer k = Integer.parseInt(naiveArgs.get(1));
              Double x = Double.parseDouble(naiveArgs.get(2));
              Double y = Double.parseDouble(naiveArgs.get(3));
              Double z = Double.parseDouble(naiveArgs.get(4));

              String err = sb.naiveNeighborsCoords(k, x, y, z);
              if (!err.equals("")) {
                System.out.println(err);
              }
            } else if (naiveArgs.size() == 3) {
              Integer k = Integer.parseInt(naiveArgs.get(1));
              String name = naiveArgs.get(2);

              String err = sb.naiveNeighborsName(k, name);
              if (!err.equals("")) {
                System.out.println(err);
              }
            } else {
              System.out.println(
                  "ERROR: naive_neighbors requires either two or four arguments: <k> <\"name\"> or <k> <x> <y> <z>");
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }


  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_0);

    // this is the directory where FreeMarker templates are placed
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");

    // when there's a server error, use ExceptionPrinter to display error on GUI
    Spark.exception(Exception.class, new ExceptionPrinter());

    // initialize FreeMarker template engine (converts .ftl templates to HTML)
    FreeMarkerEngine freeMarker = createEngine();

    // setup Spark Routes
    Spark.get("/", new MainHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      // status 500 generally means there was an internal server error
      res.status(500);

      // write stack trace to GUI
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * A handler to serve the site's main page.
   *
   * @return ModelAndView to render.
   * (main.ftl).
   */
  private static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // this is a map of variables that are used in the FreeMarker template
      Map<String, Object> variables = ImmutableMap.of("title",
          "Go go GUI");

      return new ModelAndView(variables, "main.ftl");
    }
  }
}
