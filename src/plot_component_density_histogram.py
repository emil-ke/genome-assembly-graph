import matplotlib.pyplot as plt
import sys
import os

def plot_component_density_histogram(filename, base_output_file):
    component_densities = []
    with open(filename, 'r') as file:
        for line in file:
            density = float(line)
            component_densities.append(density)

    plt.style.use('dark_background')

    # Plot the histogram in linear scale
    plt.hist(component_densities, bins=70, edgecolor='black')
    plt.xlabel('Component Densities')
    plt.ylabel('Frequency')
    plt.title('Component Density Distribution (Linear Scale)')
    plt.savefig(f"{base_output_file}_linear.png", format='png', dpi=400)
    plt.clf()  # Clear the current figure

    # Plot the histogram in log scale
    plt.hist(component_densities, bins=70, edgecolor='black', log=True)
    plt.xlabel('Component Densities')
    plt.ylabel('Log Frequency')
    plt.title('Component Density Distribution (Log Scale)')
    plt.savefig(f"{base_output_file}_log.png", format='png', dpi=400)
    plt.clf()  # Clear the current figure

def main():
    if len(sys.argv) != 2:
        print("Error please try again.\nUsage: python component_density.py <file_path>")
        exit(1)

    input_file = sys.argv[1]
    base_name = os.path.splitext(input_file)[0]

    plot_component_density_histogram(input_file, base_name)

if __name__ == "__main__":
    main()
