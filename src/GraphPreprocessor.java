import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GraphPreprocessor {
    private HashMap<String, Integer> vertexNameToId = new HashMap<>(); // Mapping from vertex names to integer identifiers
    private int nextVertexId = 0;

    public void preprocess(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split("\\s+");

                // Parse overlap lengths for both sequences
                int overlap1 = Integer.parseInt(splitLine[7]) - Integer.parseInt(splitLine[6]);
                int overlap2 = Integer.parseInt(splitLine[10]) - Integer.parseInt(splitLine[9]);

                // Only consider this pair if both overlaps are sufficiently large
                if (overlap1 >= 1000 && overlap2 >= 1000) {
                    addVertex(splitLine[0]);
                    addVertex(splitLine[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addVertex(String vertexName) {
        if (!vertexNameToId.containsKey(vertexName)) {
            vertexNameToId.put(vertexName, nextVertexId);
            nextVertexId++;
        }
    }

    public HashMap<String, Integer> getVertices() {
        return vertexNameToId;
    }

    // Your main function
    public static void main(String[] args) {
        GraphPreprocessor gp = new GraphPreprocessor();
        gp.preprocess(args[0]);
        HashMap<String, Integer> vertices = gp.getVertices();

        // Run test
        testPreprocess(vertices);
    }

    // A simple test function
    public static void testPreprocess(HashMap<String, Integer> vertices) {
        // The map should now contain the unique vertices
        int vertexCount = vertices.size();
        System.out.println("Number of vertices: " + vertexCount);

        System.out.println(vertices.keySet());

        // Each vertex should have a unique ID
        Integer id1 = vertices.get("fp.3.Luci_01A01.ctg.ctg7180000038386");
        Integer id2 = vertices.get("fp.3.Luci_02C06.ctg.ctg7180000060335");
        Integer id3 = vertices.get("fp.3.Luci_02C06.ctg.ctg7180000085546");
        Integer id4 = vertices.get("fp.3.Luci_02C06.ctg.ctg7180000085693");

        if (id1 == null || id2 == null || id3 == null || id4 == null ||
                id1 != 0 || id2 != 1 || id3 != 2 || id4 != 3) {
            System.out.println("Test failed: Wrong vertex IDs.");
        } else {
            System.out.println("Test passed.");
        }
    }
}