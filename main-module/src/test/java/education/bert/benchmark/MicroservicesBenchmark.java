package education.bert.benchmark;

import education.bert.MyApplication;
import education.bert.MyApplicationSmart;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static education.bert.benchmark.LoadTestScenarios.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class MicroservicesBenchmark {
    private final MyApplication myApplication = new MyApplication();
    private final MyApplicationSmart myApplicationSmart = new MyApplicationSmart();

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MicroservicesBenchmark.class.getSimpleName())
                .forks(1)
                .shouldFailOnError(true)
                .build();
        new Runner(options).run();
    }

    @Setup(Level.Iteration)
    public void setup() {
        myApplication.setup();
        myApplicationSmart.setup();
        addSomeInitialData(myApplicationSmart);
    }

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public void getAllPostsNotSmart(Blackhole blackhole) {
        blackhole.consume(getAllPosts(myApplication));
    }

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public void getAllPostsSmart(Blackhole blackhole) {
        blackhole.consume(getAllPosts(myApplicationSmart));
    }

    @Benchmark
    public void particularLoadTestScenarioNotSmart(Blackhole blackhole) {
        particularLoadTestScenario(myApplication, blackhole);
    }

    @Benchmark
    public void particularLoadTestScenarioSmart(Blackhole blackhole) {
        particularLoadTestScenario(myApplicationSmart, blackhole);
    }

    @Threads(100)
    @Benchmark
    public void randomLoadTestScenarioNotSmart(Blackhole blackhole) {
        randomLoadTestScenario(myApplication, blackhole);
    }

    @Threads(100)
    @Benchmark
    public void randomLoadTestScenarioSmart(Blackhole blackhole) {
        randomLoadTestScenario(myApplicationSmart, blackhole);
    }
}
