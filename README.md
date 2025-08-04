# genome assembly graph

This project processes a large overlapping dataset from the Norwegian spruce genome to extract information about an implicitly defined graph. The graph is implicitely defined in the data through edges which are described through long string identifiers in an `.m4` file (around 49 million lines), which first has to be filtered and cleaned. After preprocessing, a Java program constructs the graph using adjacency lists and assigns compact integer IDs to each unique node for space efficiency.

A degree distribution is calculated by iterating over the adjacency list and counting neighbors for each node; a DFS-based algorithm is used to count connected components with at least three nodes; and another DFS-based method calculates component density, defined as the ratio between actual and possible edges for each component. All core algorithms are written in Java using standard collections and file I/O.

The resulting data is written to text files and then visualized with Python using matplotlib and numpy. Histograms are generated for the degree distribution and component densities.

See [the full report.](./doc/report.md)
