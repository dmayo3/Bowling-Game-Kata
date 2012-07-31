# Bowling Game Kata in Java #

This is my attempt at writing a simple bowling game using TDD. Bowling is a very often used example in TDD discussions and code katas, for example:

* <http://xprogramming.com/index.php?s=bowling>
* <http://codingdojo.org/cgi-bin/wiki.pl?KataBowling>
* <http://programmingpraxis.com/2009/08/11/uncle-bobs-bowling-game-kata/>

It has very minimal dependencies to keep it lightweight and simple (no Spring!)

## Features ##

* A command line interface for entering player names and scores
* Input validation
* Game logic to determine the correct number of balls and frames to play
* Scoring logic that tallies scores for each player and applies bonus points
* At the end it displays the winner, each players score and the team's total score
* Ability to retrieve a player's scores for a particular frame
* Acceptance tests that drive the system end-to-end through the command line interface

## How to Run ##

You can build and run tests using Maven:

	$ mvn clean package

The Maven build will produce an executable Jar: `target/bowling-game-kata-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

Alternatively, import into your IDE of choice. The main class is `bowling.BowlingGame`.

