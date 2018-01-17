/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package ga;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.codecs;
import org.jenetics.util.DoubleRange;

import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

public class OtimizacaoBuscador {

    // The fitness function.
    private static double fitness(final double[] x) {
        return x[0] + x[1] + x[2] + x[3];
    }

    public static void main(final String[] args) {
        final Engine<DoubleGene, Double> engine = Engine
                // Create a new builder with the given fitness function and chromosome.
                .builder(
                        OtimizacaoBuscador::fitness,
                        codecs.ofVector(DoubleRange.of(-1, 1), 4))
                .populationSize(500)
                .optimize(Optimize.MINIMUM)
                .alterers(
                        new Mutator<>(0.03),
                        new MeanAlterer<>(0.6))
                // Build an evolution engine with the defined parameters.
                .build();
        // Create evolution statistics consumer.
        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> best = engine.stream()
                // Truncate the evolution stream after 7 "steady" generations.
                .limit(bySteadyFitness(50))
                // The evolution will stop after maximal 100 generations.
                .limit(1000)
                // Update the evaluation statistics after each generation
                .peek(statistics)
                // Collect (reduce) the evolution stream to its best phenotype.
                .collect(toBestPhenotype());
        System.out.println(statistics);
        System.out.println(best);
    }
}