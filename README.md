# Project Prerequisites and Build Instructions
This project requires the following tools to be built and run:

- Build tools:

  - GNU Make (version >= 4.3)
  - clang compiler (version >= 14.0.0)
  - gcc compiler (version >= 11.4.0)
    
- Ubuntu packages:

  - ```libbpf-dev``` (installable using ```sudo apt install libbpf-dev```)
  - ```linux-tools==6.5.0-26-generic``` (installable using ```sudo apt install linux-tools-6.5.0-26-generic```)
  - ```linux-cloud-tools==6.5.0-26-generic``` (installable using ```sudo apt install linux-cloud-tools-6.5.0-26-generic```)
 
## Building the project:

1. **Make sure you have the required tools and libraries installed.**
2. Open a terminal in your project directory.
3. Run the following command to build the project:
```bash
sudo make all
sudo make run
sudo make bpf-trace
```

## Observe the impact:

1. Open your web browser and visit a website.
2. Switch back to the terminal where the program is running. Analyze the captured trace data (from ```sudo make bpf-trace```) to see if the congestion control algorithm has changed.

