import java.io.*;
import java.util.*;

public class GraphProcessor {
    private Map<String, Integer> identifierToInt = new HashMap<>();
    private Map<Integer, List<Integer>> graph = new HashMap<>();
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


    public void parseFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vertices = line.split("\\s+");
                int v1 = getOrCreateIntIdentifier(vertices[0]);
                int v2 = getOrCreateIntIdentifier(vertices[1]);

                graph.computeIfAbsent(v1, k -> new ArrayList<>()).add(v2);
                graph.computeIfAbsent(v2, k -> new ArrayList<>()).add(v1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper to remove the extension .* of files
    private String removeFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }

    public int getOrCreateIntIdentifier(String identifier) {
        return identifierToInt.computeIfAbsent(identifier, k -> intIdentifier++);
    }

    public void calculateDegreesToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename + "_degrees.txt"))) {
            for (int v : graph.keySet()) {
                int degree = graph.get(v).size();
                pw.println(degree);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int countComponents() {
        Set<Integer> visited = new HashSet<>();
        int componentCount = 0;

        for (int node : graph.keySet()) {
            if (!visited.contains(node)) {
                int verticesCount = dfs(node, visited);
                if (verticesCount >= 3) {
                    componentCount++;
                }
            }
        }

        return componentCount;
    }

    private int dfs(int node, Set<Integer> visited) {
        visited.add(node);
        int verticesCount = 1;

        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                verticesCount += dfs(neighbor, visited);
            }
        }

        return verticesCount;
    }

    public void calculateComponentDensityToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename + "_component_densities.txt"))) {
            Set<Integer> visited = new HashSet<>();
            for (int node : graph.keySet()) {
                if (!visited.contains(node)) {
                    Set<Integer> component = new HashSet<>();
                    dfs(node, visited, component);
                    if (component.size() >= 3) {
                        double density = calculateDensity(component);
                        pw.println(density);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateDensity(Set<Integer> component) {
        int edgesCount = 0;
        for (int node : component) {
            for (int neighbor : graph.get(node)) {
                if (component.contains(neighbor)) {
                    edgesCount++;
                }
            }
        }
        double nodesCount = component.size();
        return edgesCount / (2.0 * nodesCount * (nodesCount - 1));
    }

    private void dfs(int node, Set<Integer> visited, Set<Integer> component) {
        visited.add(node);
        component.add(node);

        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, component);
            }
        }
    }
}