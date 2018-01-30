### Spring Boot Batch Word Processing Sample App

This app demonstrates using Spring Batch as a simple word processor for a given input file.

The app consists of the following:

1. A Job definition as the runnable job to achieve the desired functionality
2. A Flow definition with a JobExecutionDecider to show conditional execution paths
3. A reader-processor-writer based Step to handle chunk-oriented processing
4. Tests to assert functionality and the control flow

#### Running the Service
````
gradle bootRun
````

Currently there is an example test file in src/test/resources - you can add a file to this directory and
add a test to run your file and assert results.
````
gradle -DfileName=lorem.txt test
````
