import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification

import static Fixture.cleanBuildDir
import static Fixture.placePetstoreYaml

class DocGeneratorSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('doc-generator'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'generateSwaggerUI task should generate an Swagger UI'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('--stacktrace', 'generateSwaggerUI')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerUI').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateSwaggerUIPetstore').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/swagger-ui-petstore/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerUI').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateSwaggerUIPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

    def 'generateReDoc task should generate an ReDoc'() {
        given:
        placePetstoreYaml(runner, Fixture.PetstoreYaml.valid)
        runner.withArguments('--stacktrace', 'generateReDoc')

        when:
        def result = runner.build()

        then:
        result.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateReDocPetstore').outcome == TaskOutcome.SUCCESS
        new File("${runner.projectDir}/build/redoc-petstore/index.html").exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateReDoc').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateReDocPetstore').outcome == TaskOutcome.UP_TO_DATE
    }

}
