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
        Set<Integer> visited = new HashSet<>(); // We use a HashSet instead of array better for time complexity (contains operation)
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
     * Calculates the density of each component in the graph with at least three vertices and writes them to a file.
     * The file's name is the base filename followed by "_component_densities.txt".
     * Only components with densities between 0 and 1 (inclusive) are considered.
     *
     * @param filename The base filename to write to.
     */
    public void calculateComponentDensityToFile(String filename) {
        List<Double> densities = new ArrayList<>();

        Set<Integer> visited = new HashSet<>();

        // Iterate over each node in the graph
        for (int node : graph.keySet()) {
            // Check if the node has not been visited
            if (!visited.contains(node)) {
                Set<Integer> component = new HashSet<>();

                // Perform depth-first search to populate the component set
                dfsDensity(node, visited, component);

                if (component.size() >= 3) {
                    double density = calculateDensity(component);
                    densities.add(density);
                }
            }
        }

        // Write the densities to a file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename + "_component_densities.txt"))) {
            for (double density : densities) {
                // Check if the density is within the valid range (between 0 and 1 inclusive)
                // The graph is messy so I got an error where the were components that had density
                // greater than 1 which is impossible
                if (density >= 0 && density <= 1) {
                    // Write the density to the file
                    pw.println(density);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the density of a given component. 
     * For a undirected simple component C=(V,E) density is defined as (2|E|)/(|V|(|V|-1)).
     *
     * @param component The nodes of the component to calculate the density of.
     * @return The density of the component.
     */
    private double calculateDensity(Set<Integer> component) {
        int edgesCount = 0;

        // Iterate over each node in the component
        for (int node : component) {
            // Get the neighbors of the current node
            List<Integer> neighbors = graph.get(node);

            // Count the number of neighbors within the component
            for (int neighbor : neighbors) {
                if (component.contains(neighbor)) {
                    edgesCount++;
                }
            }
        }

        double nodesCount = component.size();
        double possibleEdges = (nodesCount * (nodesCount - 1)) / 2.0;
        return (double) edgesCount / possibleEdges; // cast to double to ensure floating-point division
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
