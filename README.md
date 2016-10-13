# Sprint starter project

This project is meant to be a starting point for developing microservices with spring boot.
It has end to end tests set up using cucumber in groovy and unit tests using spock in groovy.
The mock servers are written in javascript.

## Contributing

Please DO NOT COMMIT DIRECTLY TO THE MASTER BRANCH. We follow a git-flow branching strategy. Please read through this carefully:

http://nvie.com/posts/a-successful-git-branching-model/

Branch names & purpose

* master - should reflect what's currently in production
* develop - should hold the latest changes that are TESTED and ready for release
* feature/xxxx - used for features under development and still being tested
* release/xxxx - created at point of code freeze for a release, contains a release candidate not yet in production
* hotfix/xxxx - a branch created from the master branch, which contains changes part of an emergency release

After releasing to production, the release branch should be merged into master and develop, and the HEAD revision should be tagged with the release version number.

## Prerequisites

You'll definitely need:

* [node (install the latest version, it is used for running a mock server in tests)](https://nodejs.org/en/download/)
* [java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Configuration

#####The application is configured using yml files. There are two levels of configuration:

* Environment independent in `application.yml`
* Environment dependent in `application-{environment name}.yml`

It is important to note that the environment specific configuration will overwrite the environment independent one if there are conflicts.

#####In addition there are parameters passed in on launch:

* `spring.profiles.active`: 
  * can be `local`, `dev`, `test`, `preprod` or `prod`. The first is for running end-to-end tests locally, while the others correspond to the configurations for the different environments
  * multiple profiles can be specified in a 'last wins' approach where the last property will overwrite any earlier ones. e.g. `spring.profiles.active=dev,prod`
  * application does not launch without this parameter
* `jsonlog`: true or false
  * enables or disables logs in json format. We use the json format so that logs can easily be ingested. For end-to-end tests we set it to false so that the logs are easier to read.
  * defaults to true if not passed in

### Running the application locally

From the root of the repository, use gradle to start the services:

* Mac or UNIX: `./gradlew bootRun -Dspring.profiles.active={env-name} -Djsonlog=false`

### Running the tests

Our approach to testing is based on a balanced [testing pyramid](https://www.mountaingoatsoftware.com/blog/the-forgotten-layer-of-the-test-automation-pyramid)

In addition, static analysis tools have been configured to flag design issues with code or potential bugs.

To run all the tests and static code analysis:

`./gradlew clean build`

To run the unit tests only:

`./gradlew clean test`

### Development

#### Lombok

Project lombok is included in this project so if you want to use an IDE we recommend intelliJ. You will have to do 2 things:

1. Enable annotation processing for the project after you import it 
2. Install the lombok plugin

### Running Hystrix Dashboard ###
There is a convenience subproject for running a Hystrix Dashboard application to view Hystrix metrics.

To run it:

`./gradlew runHystrixDashboard`

This will startup a local Hystrix Dashboard on http://localhost:7979/hystrix-dashboard/
