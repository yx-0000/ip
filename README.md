# tt.tt project template

This is a project template for a greenfield Java project named _tt_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/tt.tt.java` file, right-click it, and choose `Run tt.tt.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## JUnit testing

JUnit tests should cover the approximately 50% highest-value methods in the codebase, prioritising core and complex behavior. Update the relevant JUnit tests after every code change so that this coverage target remains accurate.

## Creating and running the executable JAR

This project uses the [Shadow Gradle plugin](https://github.com/GradleUp/shadow) to create a fat JAR: an executable JAR containing the application and its runtime dependencies.

1. Install and select JDK 25.
2. Open a terminal in the project root (`C:\Users\yx\ip`).
3. Run the following command:

   ```text
   .\gradlew.bat shadowJar
   ```

   On macOS or Linux, use `./gradlew shadowJar` instead.

4. Find the generated file at `build/libs/tt.jar`.
5. Copy `tt.jar` into an empty folder. Open a terminal in that folder and run:

   ```text
   java -jar "tt.jar"
   ```

The `shadowJar` task sets `tt.TT` as the entry point and packages all runtime dependencies, so no separate classpath or Gradle installation is needed to run the copied JAR. The generated JAR is ignored by Git through the existing `build/` rule and should be uploaded as a binary attachment to a GitHub release rather than committed.
