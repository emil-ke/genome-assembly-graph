import java.io.*;
import java.util.*;

public class GraphProcessor {
    // Maps node identifiers to ints
    private Map<String, Integer> identifierToInt = new HashMap<>();
    // The graph represented as an adjacency list
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    // Next available integer identifier
    private int intIdentifier = 0;

    public static void main(String[] args) {
        String filename = args[0];
        GraphProcessor gp = new GraphProcessor();
        gp.parseFile(filename);

        String baseFilename = gp.removeFileExtension(filename);
        gp.calculateDegreesToFile(baseFilename);

        int componentCount = gp.countComponents();
        System.out.println("Number of components with at least three vertices: " + componentCount);

        gp.calculateComponentDensityToFile(baseFilename);
    }

    // Parses the file and builds the graph
    public void parseFile(String filename) {
        // Open file and read each line
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vertices = line.split("\\s+");
                int v1 = getOrCreateIntIdentifier(vertices[0]);
                int v2 = getOrCreateIntIdentifier(vertices[1]);

                // Add vertices to the graph
                graph.computeIfAbsent(v1, k -> new ArrayList<>()).add(v2);
                graph.computeIfAbsent(v2, k -> new ArrayList<>()).add(v1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Removes file extension from a filename if present
    private String removeFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return (lastDotIndex > 0) ? filename.substring(0, lastDotIndex) : filename;
    }

    /**
     * Gets the integer identifier for a node. If the node does not yet have an identifier,
     * it assigns and returns a new one.
     *
     * @param identifier The node's identifier.
     * @return The node's integer identifier.
     */
    public int getOrCreateIntIdentifier(String identifier) {
        return identifierToInt.computeIfAbsent(identifier, k -> intIdentifier++);
    }

    /**
     * Writes the degree of each vertex to a file. The file's name is the base filename
     * followed by "_degrees.txt".
     *
     * @param filename The base filename to write to.
     */
    public void calculateDegreesToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename + "_degrees.txt"))) {
            for (int v : graph.keySet()) { // Iterate over each vertex in the graph.
                pw.println(graph.get(v).size()); // Write degree of node to file.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the number of connected components in the graph that have at least three vertices.
     * Uses depth-first search (DFS) to traverse the graph.
     *
     * @return The count of components with at least three vertices.
     *
     * Time Complexity: O(V + E) where V is the number of nodes and E is the number of edges in the graph.
     */
    public int countComponents() {
        Set<Integer> visited = new HashSet<>();
        int componentCount = 0;

        for (int node : graph.keySet()) {
            if (!visited.contains(node)) {
                // if vertex count >= 3 add 1 to componentCount else add 0 (ternary)
                componentCount += (dfsCountNodes(node, visited) >= 3) ? 1 : 0; 
            }
        }

        return componentCount;
    }

    // DFS helper for counting nodes, returns vertex count
    private int dfsCountNodes(int node, Set<Integer> visited) {
        visited.add(node);
        int verticesCount = 1;

        for (int neighbor : graph.get(node)) {
            verticesCount += (!visited.contains(neighbor)) ? dfsCountNodes(neighbor, visited) : 0;
        }

        return verticesCount;
    }

    /**
     * Writes the density of each component in the graph with at least three vertices to a file.
     * The file's name is the base filename followed by "_component_densities.txt".
     * The density of a component is defined as twice the number of edges divided by the number
     * of nodes times the number of nodes minus one.
     *
     * @param filename The base filename to write to.
     */
    public void calculateComponentDensityToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename + "_component_densities.txt"))) {
            Set<Integer> visited = new HashSet<>();
            for (int node : graph.keySet()) {
                if (!visited.contains(node)) {
                    Set<Integer> component = new HashSet<>();
                    dfsDensity(node, visited, component);
                    if (component.size() >= 3) {
                        pw.println(calculateDensity(component));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the density of a given component. 
     * Density is defined as twice the number of edges divided by the product of the number of nodes 
     * and the number of nodes minus one.
     *
     * @param component The nodes of the component to calculate the density of.
     * @return The density of the component.
     */
    private double calculateDensity(Set<Integer> component) {
        int edgesCount = 0;
        for (int node : component) {
            for (int neighbor : graph.get(node)) {
                edgesCount += (component.contains(neighbor)) ? 1 : 0;
            }
        }
        double nodesCount = component.size();
        return edgesCount / (2.0 * nodesCount * (nodesCount - 1));
    }

    /**
     * DFS helper for calculating the component's density. Populates the given 'component' set 
     * with the nodes of the component to which 'node' belongs.
     *
     * @param node The node to start the DFS from.
     * @param visited The set of nodes that have already been visited.
     * @param component The set of nodes in the component.
     */
    private void dfsDensity(int node, Set<Integer> visited, Set<Integer> component) {
        visited.add(node);
        component.add(node);

        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsDensity(neighbor, visited, component);
            }
        }
    }
}
