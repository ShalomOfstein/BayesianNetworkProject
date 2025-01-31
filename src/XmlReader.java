package src;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * XmlReader.java
 * This class reads an XML file and creates a Bayesian Network from it
 * The XML file contains the list of variables and their relationships
 *
 */
public class XmlReader {


    /**
     * This method reads the list of variables from the XML file
     * @param filename the name of the XML file
     * @return the list of variables
     */
    public static ArrayList<Variable> readVariables(String filename) {

        // Create an empty ArrayList of Variables
        ArrayList<Variable> variables = new ArrayList<Variable>();

        try{
            // Create a new DocumentBuilderFactory and read the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            // Get the list of variables from the XML file
            NodeList VarList = doc.getElementsByTagName("VARIABLE");

            // Iterate through the list of variables
            for (int temp = 0; temp < VarList.getLength(); temp++) {
                Node nNode = VarList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variable = (Element) nNode;

                    // Get the name of the variable
                    String VarName = variable.getElementsByTagName("NAME").item(0).getTextContent();

                    // Get the list of outcomes for the variable
                    NodeList OutList = variable.getElementsByTagName("OUTCOME");
                    ArrayList<String> outcomes = new ArrayList<>();
                    for (int i = 0; i < OutList.getLength(); i++) {
                        outcomes.add(OutList.item(i).getTextContent());
                    }
                    // Create a new Variable object and add it to the list of variables
                    Variable v = new Variable(VarName);
                    v.setOutcomes(outcomes);
                    variables.add(v);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }

    /**
     * This method defines the relationships between the variables in the Bayesian Network
     * each variable has a list of parents and a conditional probability table
     * @param bn the Bayesian Network
     * @param filename the name of the XML file
     */
    public static void defineVariables(BayesianNetwork bn, String filename) {
        HashMap<String,Variable> variables = bn.getVariables();
        try {
            // Create a new DocumentBuilderFactory and read the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            // Get the list of relationships from the XML file
            NodeList RelList = doc.getElementsByTagName("DEFINITION");

            // Iterate through the list of relationships
            for (int temp = 0; temp < RelList.getLength(); temp++) {
                Node nNode = RelList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element relationship = (Element) nNode;

                    // Get the name of the variable
                    String VarName = relationship.getElementsByTagName("FOR").item(0).getTextContent();

                    // Get the list of parents for the variable
                    NodeList ParList = relationship.getElementsByTagName("GIVEN");
                    int numOfParents = ParList.getLength();
                    ArrayList<Variable> parentsList = new ArrayList<Variable>();

                    for (int i = 0; i < numOfParents; i++) {
                        String parent = ParList.item(i).getTextContent();
                        parentsList.add(variables.get(parent));
                    }

                    // Find the variable in the list of variables
                    Variable v = null;

                    v = variables.get(VarName);
                    // Add the parents to the variable
                    for (Variable parent : parentsList) {
                        v.addParent(variables.get(parent.getName()));
                    }

                    // Get the list of probabilities for the variable
                    NodeList list = relationship.getElementsByTagName("TABLE");

                    if(list.getLength() > 0){
                        String ProbList = list.item(0).getTextContent();
                        String[] probString = ProbList.split(" ");

                        //change the probabilities to double
                        double[] probabilities = new double[probString.length];
                        for (int i = 0; i < probString.length; i++) {
                            probabilities[i] = Double.parseDouble(probString[i]);
                        }

                        // Create a new CPT object
                        CPT cptObject = new CPT( v, parentsList, probabilities );

                        // Set the probabilities for the variable
                        v.setProbabilities(cptObject);
                    }


                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method creates a Bayesian Network from an XML file
     * @param bn the Bayesian Network
     * @param filename the name of the XML file
     */
    public static void createBayesianNetwork(BayesianNetwork bn, String filename) {
        // Read the list of variables from the XML file
        ArrayList<Variable> variables = XmlReader.readVariables(filename);
        // Add the variables to the Bayesian Network
        for (Variable v : variables) {
            bn.addVariable(v);
        }
        // Define the relationships between the variables
        XmlReader.defineVariables(bn, filename);
    }

}
