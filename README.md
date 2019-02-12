# DD2480-assignment-2

### A basic CI (Continuous integration) server.
The server will clone your project and build it. It will then email you the result of that build.

### Structure
The code can be found in the __/src/main/java/group22__ folder.
The unit-tests for the server can be found in the __/src/test/java/group22__ folder.

### Documentation
By running `./gradlew javadoc` you can access the documentation in __/build/docs/javadoc/group22/index.html__

### Requierments
- Your project need to use Gradle, since the CI-server uses it to build the project.
- The CI-server has to be run on a UNIX-system.
- You need to have the _sendmail_ package installed.
- Your project needs to be _public_

### Access the server.
Go to http://__yourip__:8022 in your browser, where __yourip__ is the ip-address where the server is run in order to access the build history.

You can also shut down the server remotely by going to http://__yourip__:8022/stop

Note that if there are no past builds, the server will return a 404 error.
### How to use
#### Step 1:
Clone or download the repository.
#### Step 2:
Set up a webhook on the repository you want to test with the CI-server. See [GitHub Developer: Webhooks](https://developer.github.com/webhooks/) for more information.

The server will run on your localhost:8022, so in order to set up a webhook you need use http://__yourip__:8022 as the destination address.

#### Step 3:
Run the server via the command-line:
    `./gradlew run`
This will start the webserver on your localhost.

### How to run the unit-tests
Simply run `./gradlew test` to run the unit-tests.

## P+ criteria
We are going for P+, by doing criteria p6 and p8.

#### P6
Each build is given a URL which is the commit SHA, and all builds are saved in a local directory, so even if the server is rebooted the history will persist. As seen above you have one URL to list all of them (http://__yourip__:8022). The reason we have __yourip__ instead of an actual working address is because it depends on where the server is run.
#### P8
We have done all commits related to issues.
## Statement of contributions
* Arthur Carl Vignon
With Paul we implemented the handling of the http push payload from github in ContinuousIntegrationServer.java, we created the PushPayload class.
We also contributed in the notification system by sending an email to the user.
* Axel Larusson
I did the notification system and decided to do an e-mail that sends out the build result to a project member. I also attended all group meeting and helped with various issues.
* Joel Ekelöf
I have done the GitHandler class, using JGit in order for the server to clone the repository. I have also done the README and worked on some minor issues; such as adding a timestamp-tag.
* Joel Gille Vikström
I did the building and testing of the project, and the History feature. I also did most of the work of combining everyone's work.
* Paul Roland Jacques Griesser
I've done the PushPayload class with Arthur, we also handle the http payload from github in the ContinuousIntegrationServer class. I've helped in the notification system.
