## This fork of Collide is not under development ##

The most active fork we know of is by James Nelson at https://github.com/WeTheInternet/collide
We suggest you contribute to his work.

Cheers, the original Collide team

### Collide is an open-source "collaborative IDE" demonstration ###

  * Pick a folder you want to edit on your local file system.
  * Run Collide from the command line in that folder.
  * You and your amigos browse to http://your_host_name:8080.
  * Collaboratively edit :).

**Requires**
  * Client: recent Chrome or Safari
  * Server: [Java 7 JRE](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u4-downloads-1591156.html)
  * Build: [Java 7 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u4-downloads-1591156.html), [Ant 1.8.4+](http://ant.apache.org/bindownload.cgi); all other build dependencies are bundled in.

See [README](http://collide.googlecode.com/git/README.md) for more details.


**Special thanks to the following open source projects we're using**
  * [vert.x](http://vertx.io/)
  * [Ant-Contrib](http://ant-contrib.sourceforge.net/)
  * [CodeMirror2](http://codemirror.net/)
  * [EasyMock](http://easymock.org/)
  * [Gson](http://code.google.com/p/google-gson/)
  * [Guava](http://code.google.com/p/guava-libraries/)
  * [Google Web Toolkit](http://code.google.com/p/google-web-toolkit/)
  * GWT Elemental
  * [JGit](http://eclipse.org/jgit/)
  * [JUnit](http://www.junit.org/)
  * [Wave](http://incubator.apache.org/wave/)


**Caution**
Collide does not have any proper auth, SSL support, or user account management just yet. Please consider that fact when running instances that expose important files on disk! As the project matures we will implement more security features to enable using this in a hosted fashion. **But we recommend that if you are running an instance now, to keep the server access secured to your local network!**