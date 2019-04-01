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

Cat header.csv to the rest of the code.
