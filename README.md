# Coding a 2D Game Engine in Java

This is the repository for a YouTube series [here](https://www.youtube.com/watch?v=VyKE7vz65rY&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE). If you want to run the code yourself follow these instructions:

## To Run

1. You must have Gradle 6.3+ and Java 1.8+ installed. If you do not have these installed, you should install them and add them to your environment variables.
    * Once you have them installed you should be able to run:

        > ```gradle --version```

        and

        > ```java -version```

        And get no errors. The output should read similar to the following (it's ok if it doesn't read exactly the same way):
        > ```
        > ------------------------------------------------------------
        > Gradle 6.3
        > ------------------------------------------------------------
        >
        > Build time:   2020-03-24 19:52:07 UTC
        > Revision:     bacd40b727b0130eeac8855ae3f9fd9a0b207c60
        > 
        > Kotlin:       1.3.70
        > Groovy:       2.5.10
        > Ant:          Apache Ant(TM) version 1.10.7 compiled on September 1 2019
        > JVM:          1.8.0_231 (Oracle Corporation 25.231-b11)
        > OS:           Windows 10 10.0 amd64
        > 
        > java version "1.8.0_231"
        > Java(TM) SE Runtime Environment (build 1.8.0_231-b11)
        > Java HotSpot(TM) 64-Bit Server VM (build 25.231-b11, mixed mode)
        > ```
2. Open a Command Prompt in the projects root directory.
    * If you run ```ls``` (Mac, Linux) or ```dir``` (Windows) you should see ```libs```, ```src```, and ```assets``` listed as *part* of the output.
3. Run:
    >```gradle fatJar```
    * This will create a fat JAR containing all the dependencies for the project.
4. Run
    >```java -jar build/libs/mario-1.0-SNAPSHOT-all.jar```
    * This should open a new Window with the Mario game running.
    * NOTE: The ImGui Windows will probably be placed in very weird places. This is because I can't export default settings for ImGui, so you'll have to reposition the windows in the dock before it looks correct.
