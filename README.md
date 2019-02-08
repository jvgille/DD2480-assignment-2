# DD2480-assignment-2

### A basic CI (Continuous integration) server.
The server will clone your project and build it. It will then email you the result of that build.

### Structure
The code can be found in the __/src/main/java/group22__ folder.

### Requierments
- Your project need to use Gradle, since the CI-server uses it to build the project.
- The CI-server has to be run on a UNIX-system.
- You need to have the _sendmail_ package installed.
- Your project needs to be _public_

### How to use
#### Step 1:
Clone or download the repository.
#### Step 2:
Set up a webhook on the repository you want to test with the CI-server. See [GitHub Developer: Webhooks](https://developer.github.com/webhooks/) for more information.

The server will run on your localhost:8022, so in order to set up a webhook you need use http://__yourip__:8022 as the destination address.

#### Step 3:
Run the server via the command-line:
    ./gradlew run
This will start the webserver on your localhost. 
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
