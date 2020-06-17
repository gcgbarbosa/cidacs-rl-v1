# Cidacs-RL



## How to run

After cloning this repository follow the steps:

1. Enter the repo folder and create a folder called `assets`.

```
git clone https://github.com/gcgbarbosa/cidacs-rl-v1.git
cd cidacs-rl-v1
mkdir assets
``` 

2. Inside the assets folder, download the datasets:

```
cd assets
wget https://github.com/cidacslab/atyimo/raw/master/atyimo_spark/datasets_sample/small/DATASET_1_5K_records.csv.gz
wget https://github.com/cidacslab/atyimo/raw/master/atyimo_spark/datasets_sample/small/DATASET_2_1M_records.csv.gz
```

3. Unpack the and rename files:

```
gunzip DATASET_1_5K_records.csv.gz
gunzip DATASET_2_1M_records.csv.gz
mv DATASET_1_5K_records.csv dsa.csv
mv DATASET_2_1M_records.csv dsb.csv
```

4. Replace the separator of `dsa.csv` and `dsb.csv` files from `;` to `,` (because Cidacs-RL uses comma as separator).

```
sed -i 's/;/,/g' dsa.csv
sed -i 's/;/,/g' dsb.csv
```

5. Go back to cidacs-rl-v1 folder and generate the jar file:

```
cd ..
mvn install
```

6. Finally, run the Cidacs-RL:

```
mvn exec:java
```

After the program finishes, a folder called `linkage-*date*-*time*` will be created with the files resulting from Spark run. There will be multiple files inside the folder. In order to generate a single csv file, execute the following command inside the linkage folder:

```
cat * > linkage.csv
```

# Cases:

1. Pescarini JM, Williamson E, Nery JS, et al. **Effect of a conditional cash transfer programme on leprosy treatment adherence and cure in patients from the nationwide 100 Million Brazilian Cohort: a quasi-experimental study**. Lancet Infect Dis. 2020;20(5):618-627. doi:10.1016/S1473-3099(19)30624-3 [[link]](https://pubmed.ncbi.nlm.nih.gov/32066527/)

2. de Andrade KVF, Silva Nery J, Moreira Pescarini J, et al. **Geographic and socioeconomic factors associated with leprosy treatment default: An analysis from the 100 Million Brazilian Cohort**. PLoS Negl Trop Dis. 2019;13(9):e0007714. Published 2019 Sep 6. doi:10.1371/journal.pntd.0007714 [[link]](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6750604/)


