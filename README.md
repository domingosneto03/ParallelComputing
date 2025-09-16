# CPD Projects

CPD Projects of group T14G12.

Group members:

1. Domingos Neto (up202108728@up.pt)
2. Eduardo Baltazar (up202206313@up.pt)
3. Joana Noites (up202206284@up.pt)
---

## Project Description

This project focuses on evaluating the performance impact of memory access patterns and parallelization techniques in matrix multiplication algorithms. The study is divided into two parts: **single-core** and **multi-core** performance analysis, using both **C++** and a second programming language.

---

## Part 1: Single-Core Performance

We implemented and evaluated the following algorithms:

1. **Standard Matrix Multiplication** (Row-by-Column)  
   - Implemented in **C++ (`-O2` optimization)** and Java  
   - Tested on matrices from `600×600` to `3000×3000` (incrementing by 400)

2. **Alternative Multiplication** (Element × Row)  
   - Implemented in both languages  
   - C++ version extended to `4096×4096` up to `10240×10240` (intervals of 2048)

3. **Block-Oriented Multiplication**  
   - Implemented in **C++**  
   - Benchmarked with varying block sizes (`128`, `256`, `512`)  
   - Matrix sizes: `4096×4096` to `10240×10240`

⏱ Performance metrics were collected using **PAPI** on native Linux systems.

---

## Part 2: Multi-Core Performance

Parallel implementations were developed using **OpenMP**, using:

- `#pragma omp parallel for`  
- Manual control with `#pragma omp for` inside nested loops

For each approach, we analyzed:
- **Execution time**
- **MFLOPs**
- **Speedup**
- **Efficiency**

---

## 📈 Report & Results

The final report includes:
- Algorithm descriptions
- Performance metric justification
- Comparative results across all versions
- Final conclusions and observations
