import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DegreeDistribution {
    public static void main(String[] args) {
        HashMap<String, Integer> degreeDistribution = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split("\\s+");

                String vertex1 = splitLine[0];
                String vertex2 = splitLine[1];

                incrementDegree(degreeDistribution, vertex1);
                incrementDegree(degreeDistribution, vertex2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Output the degree distribution
        for (String vertex : degreeDistribution.keySet()) {
            int degree = degreeDistribution.get(vertex);
            System.out.println("Vertex " + vertex + ": Degree " + degree);
        }
    }

    private static void incrementDegree(HashMap<String, Integer> degreeDistribution, String vertex) {
        degreeDistribution.put(vertex, degreeDistribution.getOrDefault(vertex, 0) + 1);
    }
}